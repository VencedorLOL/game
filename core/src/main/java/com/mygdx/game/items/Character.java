package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Settings;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Healer;
import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.classes.Vencedor;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import java.util.Objects;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Turns.isCharacterDecidingWhatToDo;
import static java.lang.Math.*;

public class Character extends Entity implements Utils {
	CharacterPath characterPath;
	float startOfTurnX, startOfTurnY;
	int thisTurnVSM;
	public CharacterClasses character = new CharacterClasses();
	public String characterTexture;
	Stage stage;
	Entity testCollision = new Entity();
	int[] speedLeft = new int[2];
	int numberOfKeysPressed = 0;
	public boolean canDecide = true;
	int distance;
	boolean isDead;
	// Used when the turn is being controlled by classes
	public boolean hasAttacked;

	// Turns overhaul
	public boolean permittedToAct;
	public Vector3 attacksCoordinate;
	// Turns overhaul





	public Character(){}

	public Character(float x, float y, float base, float height) {
		super("char",x,y,base,height);
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		characterPath = new CharacterPath(x,y,character.speed,stage);
	}

	public void spendTurn(){
		printErr("Called Spend Turn");
		permittedToAct = false;
		characterPath.pathStarter();
		isOnTheGrid();
	}


	private void actionDecided(){
		Turns.characterFinalizedToChooseAction();
	}

	public void permitToMove(){
		print("permitted to move");
		permittedToAct = true;
	}

	public boolean isPermittedToAct() {
		return permittedToAct;
	}

	protected void primaryMovement(){
		if (speedLeft[0] > 0) {
			x += thisTurnVSM;
			speedLeft[0] -= thisTurnVSM;
		}
		else if (speedLeft[0] < 0) {
			x -= thisTurnVSM;
			speedLeft[0] += thisTurnVSM;
		}
		if (speedLeft[1] > 0) {
			y += thisTurnVSM;
			speedLeft[1] -= thisTurnVSM;
		}
		else if (speedLeft[1] < 0) {
			y -= thisTurnVSM;
			speedLeft[1] += thisTurnVSM;
		}
	}

	public void finalizedAttack(){
		spendTurn();
		canDecide = true;
		attacksCoordinate = null;
		speedLeft[0] = 0;
		speedLeft[1] = 0;
	}

	public void finalizedMove(){
		for (Enemy e : stage.enemy)
			for (Enemy n : stage.enemy){
				if (n.x == e.x && n.y == e.y && n != e && !e.isDead) {
					print("Discrepancy with enemy: " + n + " :and enemy: " + e);
					print("In x: " + n.x + " :in y: " + e.y);
				}
			}
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		spendTurn();
		canDecide = true;
		attacksCoordinate = null;
	}

	protected void movementInput(){
//		if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A)
//		|| Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)
//		|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			if (characterPath.pathBegin()) {
				printErr("True");
				canDecide = false;
				thisTurnVSM = getVisualSpeedMultiplier();
				startOfTurnX = x;
				startOfTurnY = y;
				actionDecided();
			}
	}

	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0) {
			if (!(x % globalSize() == 0)) {
				printErr("Offset in x. Coordinate was: " + x);
				x = (float) (globalSize() * ceil(x / globalSize()));
			}
			if (!(y % globalSize() == 0)) {
				printErr("Offset in y. Coordinate was: " + y);
				y = (float) (globalSize() * ceil(y / globalSize()));
			}
		}
	}

	public void movement(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct()) {
			if(speedLeft[0] == 0 && speedLeft[1] == 0 && !characterPath.pathEnded){
				speedLeft = characterPath.pathProcess();
			}

			if (speedLeft[0] != 0 || speedLeft[1] != 0) {
				primaryMovement();
			}

			if (speedLeft[0] == 0 && speedLeft[1] == 0 && characterPath.pathEnded)
				finalizedMove();
		}
		if (canDecide)
			movementInput();
		numberOfKeysPressed = 0;

		super.refresh(characterTexture,x, y, base, height);
	}

	public void update(Stage stage, GameScreen cam){
		this.stage = stage;
		onDeath();
		character.update(this);
		characterPath.getStats(x,y,character.speed,stage);
		movement();
		attack();
		if(canDecide)
			attackDecider();
		changeTo();
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canDecide)
			spendTurn();
		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			print("canDecide: " + canDecide);
			print("speedLeft on x: " + speedLeft[0]);
			print("speedLeft on y: " + speedLeft[1]);
			print("permittedToAct: " + permittedToAct);
			print("Weapon" + character.weapon);
			print("Health: " + character.totalHealth);
			print("Damage: " + character.totalDamage);
			print("Current health: " + character.currentHealth);
		}
		textureUpdater();
		if(Gdx.input.isKeyJustPressed(Input.Keys.V) && canDecide) {
			Enemy.fastMode = !Enemy.fastMode;
			print("FastMode is now "+Enemy.fastMode);
			if (Enemy.fastMode)
				Settings.setVisualSpeedMultiplier(32);
			else
				Settings.setVisualSpeedMultiplier(8);

		}
		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+ (float) globalSize() /2,
				y+ (float) globalSize() /2,1, 0,true,false);
		}
		render();
	}

// TODO fix attack
	public void attackDecider() {
		if (canDecide && isCharacterDecidingWhatToDo()) {
			if (Gdx.input.isTouched()) {
				if (clickDistance(x,y) <= character.range &&
						clickAndRayCastingButOnlyForWallsAndNowReturnsBoolean(x,y,stage.walls)) {
					print("Check passed");
					attacksCoordinate = click();
					actionDecided();
				}
			}
		}
	}

	public void attack(){
		if(attacksCoordinate != null && isPermittedToAct()) {
			printErr("Attacked previous turn");
			if (rayCasting(x, y, attacksCoordinate.x, attacksCoordinate.y, this, stage.enemy, stage.walls,
					character.pierces) != null && character.range >= distance) {
				printErr("Attack Succeeded");
				for (Enemy e : Objects.requireNonNull(rayCasting(x, y, attacksCoordinate.x, attacksCoordinate.y,
						this, stage.enemy, stage.walls, character.pierces))) {
					e.damage(character.outgoingDamage(), "Melee");
				}

			}
			else
				printErr("Attack failed");

			if (!character.shouldTurnCompletionBeLeftToClass) {
				print("finalized attack");
				finalizedAttack();
			} else {
				printErr("Attack finalization left to classes");
				hasAttacked = true;
			}
		}
	}


	int previousTexture = 3;
	public int texture() {
		/*switch(characterPath.path.get(characterPath.currentPath).direction){
			case 'A': {
				previousTexture = 1;
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'S'){
					previousTexture = 6;
				}
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'W'){
					previousTexture = 8;
				}
				break;
			}
			case 'D': {
				previousTexture = 2;
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'S'){
					previousTexture = 5;
				}
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'W'){
					previousTexture = 7;
				}
				break;
			}
			case 'W': {
				previousTexture = 4;
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'A'){
					previousTexture = 8;
				}
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'D'){
					previousTexture = 7;
				}
				break;
			}
			case 'S': {
				previousTexture = 3;
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'A'){
					previousTexture = 6;
				}
				if(characterPath.path.get(characterPath.currentPath).secondaryDirection == 'D'){
					previousTexture = 5;
				}
				break;
			}
			default:{
				break;
			}
		} */
		return 1;
	}

	public void textureUpdater(){
		switch (texture()){
			case 1 : {
				characterTexture ="CharaLeft";
				break;
			}
			case 2: {
				characterTexture = "CharaRight";
				break;
			}
			case 3 : {
				characterTexture = "char";
				break;
			}
			case 4 :{
				characterTexture = "CharaUp";
				break;
			}
			case 5 :{
				characterTexture = "CharaDiagonalDownRight";
				break;
			}
			case 6 :{
				characterTexture = "CharaDiagonalDownLeft";
				break;
			}
			case 7 :{
				characterTexture = "CharaDiagonalUpRight";
				break;
			}
			case 8 :{
				characterTexture = "CharaDiagonalUpLeft";
				break;
			}
		}
	}


	public void onDeath(){
		if (character.currentHealth <= 0) {
			isDead = false;
			// shut the fuck up
		}
	}

	public void changeTo(){
		changeToHealer();
		changeToMelee();
		changeToVencedor();
		equipBestSword();
		equipBlessedShield();
		equipBlessedSword();
		equipMeleeShield();
		equipMeleeSword();
		equipVencedorSword();
	}


	public void changeToHealer(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
			character = new Healer();
		}
	}

	public void changeToMelee(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
			character = new Melee();
		}
	}

	public void changeToVencedor(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
			character = new Vencedor();
		}
	}


	public void equipBlessedSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
			character.equipWeapon(new Weapons.BlessedSword());
		}
	}

	public void equipBestSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
			character.equipWeapon(new Weapons.BestHealerSword());
		}
	}

	public void equipBlessedShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
			character.equipShield(new Shields.BlessedShields());
		}
	}

	public void equipMeleeSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
			character.equipWeapon(new Weapons.MeleeSword());
		}
	}

	public void equipMeleeShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
			character.equipShield(new Shields.MeleeShield());
		}
	}

	public void equipVencedorSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
			character.equipWeapon(new Weapons.VencedorSword());
		}
	}

}
