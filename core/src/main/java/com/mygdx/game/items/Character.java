package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Settings;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Healer;
import com.mygdx.game.items.characters.classes.Tank;
import com.mygdx.game.items.characters.classes.Vencedor;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.Turns.didTurnJustPass;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Character extends Entity implements Utils {
	PathFinder pathFindAlgorithm;
	Path path;
	int thisTurnVSM;
	public CharacterClasses character = new CharacterClasses();
	public String characterTexture;
	Stage stage;
	Entity testCollision = new Entity();
	int[] speedLeft = new int[2];
	public boolean[] canDecide = {true, true};
	boolean isDead;
	// Used when the turn is being controlled by classes
	public boolean hasAttacked;
	public boolean permittedToAct;
	public Vector3 attacksCoordinate;
	public boolean attackMode = false;
	public float lastClickX, lastClickY;
	Stage recordedStage;

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
		path = new Path(x,y,character.speed,stage);
	}

	public void spendTurn(){
		printErr("Called Spend Turn");
		permittedToAct = false;
		path.pathStart();
		isOnTheGrid();
	}


	private void actionDecided(){
		Turns.characterFinalizedToChooseAction(this);
	}

	public void permitToMove(){
		//print("permitted to move");
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
		attacksCoordinate = null;
		canDecide[0] = true;
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
		canDecide[0] = true;
		spendTurn();
		attacksCoordinate = null;
	}

	protected void movementInput(){
		automatedMovement();
		if (path.pathCreate(x,y,character.speed,stage)) {
			canDecide = new boolean[] {false, false};
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
	}

	private void automatedMovement(){
		if(Gdx.input.isTouched()){
			lastClickX = click().x;
			lastClickY = click().y;
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}

	private void pathFinding(){
		if(pathFindAlgorithm == null)
			pathFindAlgorithm = new PathFinder(stage);
		path.pathReset();
		PathFinder.reset(stage);
		pathFindAlgorithm.setStart(x,y);
		pathFindAlgorithm.setEnd(lastClickX,lastClickY);
		pathFindAlgorithm.solve();
		if (pathFindAlgorithm.algorithm.getPath() != null){
			path.setPathTo(pathFindAlgorithm.getSolvedPath());
		} else
			print("no path found");
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
			if(speedLeft[0] == 0 && speedLeft[1] == 0 && !path.pathEnded){
				speedLeft = path.pathProcess(this);
			}

			if (speedLeft[0] != 0 || speedLeft[1] != 0) {
				primaryMovement();
			}

			if (speedLeft[0] == 0 && speedLeft[1] == 0 && path.pathEnded)
				finalizedMove();
		}
		else if (canDecide() && isDecidingWhatToDo(this))
			movementInput();

		super.refresh(characterTexture,x, y, base, height);
	}

	private void stageChange(Stage stage){
		this.stage = stage;
		if (stage != recordedStage) {
			recordedStage = stage;
			stageChanged();
		}

	}

	private void stageChanged(){
		if(pathFindAlgorithm == null)
			pathFindAlgorithm = new PathFinder(stage);
		PathFinder.reset(stage);
	}

	public void update(Stage stage, GameScreen cam){
		if(didTurnJustPass)
			canDecide[1] = true;
		stageChange(stage);
		onDeath();
		character.update(this);
		path.getStats(x,y,character.speed,stage);
		movement();
		attack();
		if(canDecide())
			attackDecider();
		changeTo();
		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			print("canDecide: " + (canDecide[1] && canDecide[0]));
			print("speedLeft on x: " + speedLeft[0]);
			print("speedLeft on y: " + speedLeft[1]);
			print("permittedToAct: " + permittedToAct);
			print("Weapon" + character.weapon);
			print("Health: " + character.totalHealth);
			print("Damage: " + character.totalDamage);
			print("Current health: " + character.currentHealth);
		}
		textureUpdater();
		if(Gdx.input.isKeyJustPressed(Input.Keys.V) && canDecide[0] && canDecide[1]) {
			Enemy.fastMode = !Enemy.fastMode;
			print("FastMode is now "+Enemy.fastMode);
			if (Enemy.fastMode)
				Settings.setVisualSpeedMultiplier(32);
			else
				Settings.setVisualSpeedMultiplier(8);

		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.T) && canDecide()) {
			attackMode = !attackMode;
			print("AttackMode is now "+attackMode);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+ (float) globalSize() /2,
				y+ (float) globalSize() /2,1, 10,true,false);
		}

		path.render();
		render();
	}

// TODO kinda works. Kinda. Piercing attack is broken but not not piercing... have to do more tests
	public void attackDecider() {
		if (canDecide[0] && canDecide[1] && isDecidingWhatToDo(this) && attackMode) {
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

	boolean isAttacking = false;
	public void attack(){
		if(attacksCoordinate != null && isPermittedToAct() && !isAttacking) {
			isAttacking = true;
			print("Attacked previous turn");
			if (rayCasting(x, y, attacksCoordinate.x, attacksCoordinate.y, this, stage.enemy, stage.walls,
					false) != null /* uh oh && character.range <= distance */ ) {
				print("Attack Succeeded");
				for (Enemy e : (rayCasting(x, y, attacksCoordinate.x, attacksCoordinate.y,
						this, stage.enemy, stage.walls, false))) {
					printErr("attacked an enemy. Enemy was " + e + ". damage was: " + character.outgoingDamage());
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
			isAttacking = false;
		}
	}

	boolean didntRunMovementMethodYetEver = true;
	public byte texture(){
		didntRunMovementMethodYetEver = false;
		byte movement = 7;
		if (speedLeft[0] > 0)
			movement = 1;
		if (speedLeft[0] < 0)
			movement = 4;
		if (speedLeft[1] > 0)
			movement++;
		if (speedLeft[1] < 0)
			movement--;
		return movement;
	}




	public void textureUpdater(){
		switch (texture()){
			case 0 : characterTexture = "CharaDiagonalDownRight"; break;
			case 1 : characterTexture = "CharaRight";             break;
			case 2 : characterTexture = "CharaDiagonalUpRight";   break;
			case 3 : characterTexture = "CharaDiagonalDownLeft";  break;
			case 4 : characterTexture = "CharaLeft";              break;
			case 5 : characterTexture = "CharaDiagonalUpLeft";    break;
			case 6 : characterTexture = "char";                   break;
			case 8 : characterTexture = "CharaUp";                break;
		} if (didntRunMovementMethodYetEver)
			characterTexture = "char";
	}

	public boolean canDecide(){
		return canDecide[0] && canDecide[1];
	}


	public void onDeath(){
		if (character.currentHealth <= 0) {
			isDead = false;
			// shut up
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
			character = new Tank();
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
