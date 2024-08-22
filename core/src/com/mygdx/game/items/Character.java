package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
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
import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.*;

public class Character extends Entity implements Utils {
	public CharacterClasses character = new CharacterClasses();
	public String characterTexture;
	Stage stage;
	Entity testCollision = new Entity();
	int[] speedLeft = new int[2];
	char direction = 'N';
	char secondaryDirection = 'N';
	long animationSpeed = 1000000;
	int numberOfKeysPressed = 0;
	int visualSpeedMultiplier = 8;
	boolean canDecide = true;
	public boolean isOnTurn;
	byte speedState = 0;
	int distance;
	boolean isDead;
	// Used when the turn is being controlled by classes
	public boolean hasAttacked;

	// Turns overhaul
	public boolean permittedToMove;
	public Vector3 attacksCoordinate;
	// Turns overhaul



	public boolean overlapsWithStage(Stage stage, Entity entity){
		for(Wall b : stage.walls){
			if (entity.overlaps(b))
				return true;
		}
		for(Enemy e : stage.enemy) {
			if (entity.x == e.x && entity.y == e.y && !e.isDead)
				return true;
		}
		return false;
	}

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
		visualSpeedMultiplier = visualSpeedMultiplierGetter();
		animationSpeed = animationSpeedGetter();
		if(!(128 % visualSpeedMultiplier == 0)) {
			if(visualSpeedMultiplier > 128)
				visualSpeedMultiplier = 128;
			else if(visualSpeedMultiplier < 4 )
				visualSpeedMultiplier = 4;
			else if(visualSpeedMultiplier < 8 )
				visualSpeedMultiplier = 8;
			else if(visualSpeedMultiplier < 16 )
				visualSpeedMultiplier = 16;
			else if(visualSpeedMultiplier < 32 )
				visualSpeedMultiplier = 32;
			else if(visualSpeedMultiplier < 64 )
				visualSpeedMultiplier = 64;
			else if(visualSpeedMultiplier < 128)
				visualSpeedMultiplier = 128;
		}
	}

	public void spendTurn(){
		speedState = 0;
		isOnTurn = false;
		permittedToMove = false;
	}

	public void isItMyTurn(){
		isOnTurn = whatTurnIsIt();
		swapToCharacterTurn(stage);
	}

	private void actionDecided(){
		Turns.characterFinalizedToChooseAction();
	}

	public void permitToMove(){
		permittedToMove = true;
	}

	@Override
	public boolean isPermittedToMove() {
		return permittedToMove;
	}

	protected void primaryMovement(){
		if (speedLeft[0] > 0) {
			x += visualSpeedMultiplier;
			speedLeft[0] -= visualSpeedMultiplier;
		}
		else if (speedLeft[0] < 0) {
			x -= visualSpeedMultiplier;
			speedLeft[0] += visualSpeedMultiplier;
		}
		if (speedLeft[1] > 0) {
			y += visualSpeedMultiplier;
			speedLeft[1] -= visualSpeedMultiplier;
		}
		else if (speedLeft[1] < 0) {
			y -= visualSpeedMultiplier;
			speedLeft[1] += visualSpeedMultiplier;
		}
	}

	protected void movementOnFinalize(){
		for (Enemy e : stage.enemy)
			for (Enemy n : stage.enemy){
				if (n.x == e.x && n.y == e.y && n != e && !e.isDead) {
					System.out.println("Discrepancy with enemy: " + n + " :and enemy: " + e);
					System.out.println("In x: " + n.x + " :in y: " + e.y);
				}
			}
		secondaryDirection = 'N';
		direction = 'N';
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		speedState++;
		if (speedState >= round((float) character.speed / 2))
			spendTurn();
		canDecide = true;
		attacksCoordinate = null;
	}

	protected void movementInput(){
		if (Gdx.input.isKeyPressed(Input.Keys.A) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x -= 128;
			if (direction == 'N' || direction == 'A')
				direction = 'A';
			else
				secondaryDirection = 'A';
			speedLeft[0] = -128;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x += 128;
			if (direction == 'N' || direction == 'D')
				direction = 'D';
			else
				secondaryDirection = 'D';
			speedLeft[0] = 128;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y += 128;
			if (direction == 'N' || direction == 'W')
				direction = 'W';
			else
				secondaryDirection = 'W';
			speedLeft[1] = 128;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y -= 128;
			if (direction == 'N' || direction == 'S')
				direction = 'S';
			else
				secondaryDirection = 'S';
			speedLeft[1] = -128;
		}
		if (!overlapsWithStage(stage,testCollision) && direction != 'N'){
			canDecide = false;
			actionDecided();
		}
		else {
			secondaryDirection = 'N';
			direction = 'N';
			speedLeft[0] = 0;
			speedLeft[1] = 0;
			canDecide = true;
		}
	}

	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0) {
			if (!(x % 128 == 0)) {
				System.err.println("Offset in x. Coordinate was: " + x);
				x = (float) (128 * ceil(x / 128));
			}
			if (!(y % 128 == 0)) {
				System.err.println("Offset in y. Coordinate was: " + y);
				y = (float) (128 * ceil(y / 128));
			}
		}
	}

	public void movement(){
		testCollision.x = x;
		testCollision.y = y;
		if (permittedToMove) {
			if (speedLeft[0] != 0 || speedLeft[1] != 0 /*&& permittedToMove*/) {
				primaryMovement();
			}

			if (speedLeft[0] == 0 && speedLeft[1] == 0 && direction != 'N' /*&& permittedToMove*/)
				movementOnFinalize();
		}

		if (canDecide)
			movementInput();
		numberOfKeysPressed = 0;
		isOnTheGrid();
		super.refresh(characterTexture,x, y, base, height);
	}

	public void update(Stage stage, GameScreen cam){
		this.stage = stage;
		onDeath();
		isItMyTurn();
		character.update(this);
		if (isOnTurn){
			movement();
			attack();
			if(canDecide)
				attackDecider();
			changeTo();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canDecide)
			spendTurn();
		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			System.out.println("speedState on y: " + speedState);
			System.out.println("canDecide: " + canDecide);
			System.out.println("previousPressedKey: " + direction);
			System.out.println("previousPressedKeySecondary: " + secondaryDirection);
			System.out.println("speedLeft on x: " + speedLeft[0]);
			System.out.println("speedLeft on y: " + speedLeft[1]);
			System.out.println("onTurn: " + isOnTurn);
			System.out.println("Weapon" + character.weapon);
			System.out.println("Health: " + character.totalHealth);
			System.out.println("Damage: " + character.totalDamage);
			System.out.println("Current health: " + character.currentHealth);
		}
		textureUpdater();
		if(Gdx.input.isKeyJustPressed(Input.Keys.V) && whatTurnIsIt()) {
			Enemy.fastMode = !Enemy.fastMode;
			System.out.println("FastMode is now "+Enemy.fastMode);
			if (Enemy.fastMode)
				visualSpeedMultiplier = 32;
			else
				visualSpeedMultiplier = 8;

		}
		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+64,y+64,1, 0,true,false);
		}
		render();
	}


	public void attackDecider() {
		if (canDecide) {
			if (Gdx.input.isTouched()) {
				if (clickAndRayCasting(x, y,this, stage.enemy, stage.walls, character.range) != null) {
					attacksCoordinate = click();
					actionDecided();
				}
			}
		}
	}

	public void attack(){
		if(attacksCoordinate != null)
			if(rayCasting(x,y, attacksCoordinate.x,attacksCoordinate.y,this,stage.enemy,stage.walls, character.range) != null && character.range >= distance){
			Objects.requireNonNull(rayCasting(x, y, attacksCoordinate.x, attacksCoordinate.y, this, stage.enemy, stage.walls, character.range)).damage(character.outgoingDamage(), "Melee");
			if (!character.shouldTurnCompletionBeLeftToClass)
				spendTurn();
			else
				hasAttacked = true;

		}
	}


	int previousTexture = 3;
	public int texture() {
		switch(direction){
			case 'A': {
				previousTexture = 1;
				if(secondaryDirection == 'S'){
					previousTexture = 6;
				}
				if(secondaryDirection == 'W'){
					previousTexture = 8;
				}
				break;
			}
			case 'D': {
				previousTexture = 2;
				if(secondaryDirection == 'S'){
					previousTexture = 5;
				}
				if(secondaryDirection == 'W'){
					previousTexture = 7;
				}
				break;
			}
			case 'W': {
				previousTexture = 4;
				if(secondaryDirection == 'A'){
					previousTexture = 8;
				}
				if(secondaryDirection == 'D'){
					previousTexture = 7;
				}
				break;
			}
			case 'S': {
				previousTexture = 3;
				if(secondaryDirection == 'A'){
					previousTexture = 6;
				}
				if(secondaryDirection == 'D'){
					previousTexture = 5;
				}
				break;
			}
			default:{
				break;
			}
		}
		return previousTexture;
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
			isDead = true;
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
