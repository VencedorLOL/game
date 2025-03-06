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

import java.util.ArrayList;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.TextureManager.animationToList;
import static com.mygdx.game.items.Turns.didTurnJustPass;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Character extends Actor implements Utils {
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
	int[] attackDirection = new int[2];
	// haha now i have to code a basically new class in the same clas for when this is false
	boolean gridMode = true;

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
		print("AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Turns.actorsFinalizedChoosing(this);
	}

	public void permitToMove(){
		//print("permitted to move");
		permittedToAct = true;
	}

	public boolean isPermittedToAct() {
		return permittedToAct;
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

	protected void movementInput(){
		automatedMovement();
		if (path.pathCreate(x,y, (int) character.speed,stage, (byte) 1)) {
			canDecide = new boolean[] {false, false};
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
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

	private void automatedMovement(){
		if(touchDetect()){
			lastClickX = flooredClick().x;
			lastClickY = flooredClick().y;
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}

	public void finalizedMove(){
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		canDecide[0] = true;
		spendTurn();
		attacksCoordinate = null;
	}

	public void finalizedAttack(){
		if (numberOfHits == 0)
			print("Hit nothing!");
		else if (numberOfHits > 1)
			print("Hit " + numberOfHits + " times!");
		else
			print("Hit 1 time!");
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		canDecide[0] = true;
		spendTurn();
		attacksCoordinate = null;
		attackDirection[0] = 0;
		attackDirection[1] = 0;
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
		actingSpeed = character.attackSpeed;
		path.getStats(x,y,character.speed,stage);
		if (!attackMode)
			movement();
		else
			attack();
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
			path.pathReset();
			print("AttackMode is now "+attackMode);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+ (float) globalSize() /2,
				y+ (float) globalSize() /2,1, 10,true,false);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F9)){
			stage.enemy.add(new Enemy(x+256,y));
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
			animationToList("animation",x,y,1);
		}


		path.render();
		render();
	}

	int numberOfHits = 0;
	public void attack(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct()) {
			if (!path.pathEnded){
				attackDetector();
			}

		}
		else if (canDecide() && isDecidingWhatToDo(this))
			attackInput();

		super.refresh(characterTexture,x, y, base, height);
	}


	private void attackDetector(){
		attackDirection = path.pathProcess(this);
		for (Actor e : stage.enemy)
			if (path.getCurrentPathCoords()[0] == e.x && path.getCurrentPathCoords()[1] == e.y){
				e.damage(character.outgoingDamage(), "Melee");
				numberOfHits++;
				if (!character.pierces){
					path.pathEnded = true;
					finalizedAttack();
					return;
				}
			}
		if (path.pathEnded) {
			finalizedAttack();
			return;
		}
		attackDetector();
	}

	protected void attackInput(){
		ArrayList<Tile> circle = stage.findATile(x,y).circle(stage.tileset, character.range);
		for(Tile t : circle) {
			t.texture.setSecondaryTexture("SelectionWholeArea",.9f);
		}
		for (Tile.TileAndCirclePos t : stage.findATile(x,y).detectCornersOfCircle(circle)){
			switch (t.getTileCirclePos()){
				case 0: t.getTile().texture.setSecondaryTexture("SelU",.9f); break;
				case 1: t.getTile().texture.setSecondaryTexture("SelUR",.9f); break;
				case 2: t.getTile().texture.setSecondaryTexture("SelR",.9f); break;
				case 3: t.getTile().texture.setSecondaryTexture("SelRD",.9f); break;
				case 4: t.getTile().texture.setSecondaryTexture("SelD",.9f); break;
				case 5: t.getTile().texture.setSecondaryTexture("SelDL",.9f); break;
				case 6: t.getTile().texture.setSecondaryTexture("SelL",.9f); break;
				case 7: t.getTile().texture.setSecondaryTexture("SelLU",.9f); break;
				case 8: t.getTile().texture.setSecondaryTexture("SelInRU",.9f); break;
				case 9: t.getTile().texture.setSecondaryTexture("SelInRD",.9f); break;
				case 10: t.getTile().texture.setSecondaryTexture("SelInDL",.9f); break;
				case 11: t.getTile().texture.setSecondaryTexture("SelInLU",.9f); break;

			}
		}


	}

	private void automatedAttack(){
		if(touchDetect()){
			lastClickX = flooredClick().x;
			lastClickY = flooredClick().y;
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}

	private void attackProcess(){

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
