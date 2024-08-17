package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Healer;
import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.classes.Vencedor;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.*;

public class Character extends Entity implements Utils {
	public CharacterClasses character = new CharacterClasses();
	public String characterTexture;
	Stage stage;
	Entity testCollision = new Entity();
	long time = 0;
	int previousPressLocation = 0;
	char previousPressKey = 'N';
	char previousPressKeySecondary = 'N';
	long animationSpeed = 1000000;
	int numberOfKeysPressed = 0;
	int visualSpeedMultiplier = 8;
	boolean isDivisibleBy128 = false;
	boolean hasMovedBefore;
	boolean canMove = true;
	public boolean isOnTurn;
	byte speedState = 0;
	int distance;
	boolean isDead;
	// Used for when the turn is being controlled by classes
	public boolean hasAttacked;
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
			if(visualSpeedMultiplier > 128 && !isDivisibleBy128) {
				visualSpeedMultiplier = 128;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 4 && !isDivisibleBy128) {
				visualSpeedMultiplier = 4;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 8 && !isDivisibleBy128) {
				visualSpeedMultiplier = 8;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 16 && !isDivisibleBy128) {
				visualSpeedMultiplier = 16;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 32 && !isDivisibleBy128) {
				visualSpeedMultiplier = 32;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 64 && !isDivisibleBy128) {
				visualSpeedMultiplier = 64;
				isDivisibleBy128 = true;
			}
			if(visualSpeedMultiplier < 128 && !isDivisibleBy128) {
				visualSpeedMultiplier = 128;
				isDivisibleBy128 = true;
			}
		}
	}

	public void spendTurn(){
		speedState = 0;
		isOnTurn = false;
		Turns.spendTurn();
	}

	public void isItMyTurn(){
		isOnTurn = whatTurnIsIt();
		swapToCharacterTurn(stage);
	}


	public void secondaryMovement(){
		switch (previousPressKeySecondary) {
			case 'A': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						x -= visualSpeedMultiplier;
						if (previousPressKey == 'N')
							previousPressKey = 'A';
						else
							previousPressKeySecondary = 'A';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'D': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						x += visualSpeedMultiplier;
						if (previousPressKey == 'N')
							previousPressKey = 'D';
						else
							previousPressKeySecondary = 'D';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'S': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y -= visualSpeedMultiplier;
						if (previousPressKey == 'N')
							previousPressKey = 'S';
						else
							previousPressKeySecondary = 'S';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'W': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y += visualSpeedMultiplier;
						if (previousPressKey == 'N')
							previousPressKey = 'W';
						else
							previousPressKeySecondary = 'W';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
		}
	}

	protected void primaryMovement(){
		switch (previousPressKey) {
			case 'A': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						x -= visualSpeedMultiplier;
						if (previousPressKey == 'N' || previousPressKey == 'A')
							previousPressKey = 'A';
						else
							previousPressKeySecondary = 'A';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'D': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						x += visualSpeedMultiplier;
						if (previousPressKey == 'N' || previousPressKey == 'D')
							previousPressKey = 'D';
						else
							previousPressKeySecondary = 'D';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'S': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y -= visualSpeedMultiplier;
						if (previousPressKey == 'N' || previousPressKey == 'S')
							previousPressKey = 'S';
						else
							previousPressKeySecondary = 'S';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'W': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y += visualSpeedMultiplier;
						if (previousPressKey == 'N' || previousPressKey == 'W')
							previousPressKey = 'W';
						else
							previousPressKeySecondary = 'W';
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
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

		previousPressKeySecondary = 'N';
		previousPressKey = 'N';
		previousPressLocation = 0;
		speedState++;
		if (speedState >= round((float) character.speed / 2))
			spendTurn();
		canMove = true;
	}

	protected void movementInput(){
		if (Gdx.input.isKeyPressed(Input.Keys.A) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x -= 128;
			if (previousPressKey == 'N' || previousPressKey == 'A')
				previousPressKey = 'A';
			else
				previousPressKeySecondary = 'A';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x += 128;
			if (previousPressKey == 'N' || previousPressKey == 'D')
				previousPressKey = 'D';
			else
				previousPressKeySecondary = 'D';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y += 128;
			if (previousPressKey == 'N' || previousPressKey == 'W')
				previousPressKey = 'W';
			else
				previousPressKeySecondary = 'W';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y -= 128;
			if (previousPressKey == 'N' || previousPressKey == 'S')
				previousPressKey = 'S';
			else
				previousPressKeySecondary = 'S';
		}
		if (!overlapsWithStage(stage,testCollision) && previousPressKey != 'N'){
			hasMovedBefore = true;
			canMove = false;
			previousPressLocation -= visualSpeedMultiplier;
		}
		else {
			previousPressKeySecondary = 'N';
			previousPressKey = 'N';
			previousPressLocation = 0;
			canMove = true;
		}
	}

	protected void isOnTheGrid(){
		if (previousPressLocation == 0) {
			if (!(x % 128 == 0)) {
				System.out.println("Offset in x. Coordinate was: " + x);
				x = (float) (128 * ceil(x / 128));
			}
			if (!(y % 128 == 0)) {
				System.out.println("Offset in y. Coordinate was: " + y);
				y = (float) (128 * ceil(y / 128));
			}
		}
	}

	public void movement(){
		testCollision.x = x;
		testCollision.y = y;
		if (previousPressLocation < 128) {
			secondaryMovement();
			primaryMovement();
		}
		if (canMove)
			movementInput();
		if (previousPressLocation >= 128)
			movementOnFinalize();
		if (hasMovedBefore)
			previousPressLocation += visualSpeedMultiplier;
		numberOfKeysPressed = 0;
		hasMovedBefore = false;
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
			changeTo();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canMove)
			spendTurn();
		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			System.out.println("speedState: " + speedState);
			System.out.println("canMove: " + canMove);
			System.out.println("hasMovedBefore: " + hasMovedBefore);
			System.out.println("previousPressedKey: " + previousPressKey);
			System.out.println("previousPressedKeySecondary: " + previousPressKeySecondary);
			System.out.println("previousPressedKeyState: " + previousPressLocation);
			System.out.println("onTurn: " + isOnTurn);
			System.out.println("Weapon" + character.weapon);
			System.out.println("Health: " + character.totalHealth);
			System.out.println("Damage: " + character.totalDamage);
			System.out.println("Current health: " + character.currentHealth);
		}
		textureUpdater();
		if(Gdx.input.isKeyJustPressed(Input.Keys.V) && whatTurnIsIt()) {
			Enemy.fastMode = !Enemy.fastMode;
			System.out.println(Enemy.fastMode);
			if (Enemy.fastMode){
				animationSpeed = 12500;
				visualSpeedMultiplier = 32;
			}
			else {
				animationSpeed = 1000000;
				visualSpeedMultiplier = 8;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+64,y+64,1, 0,true,false);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.N))
			camaraShake(50,55,cam);

		camaraShake(50,0,cam);
		render();
	}

	float frames;
	byte state = 0;
	public void camaraShake(float intensity,int frameDuration,GameScreen gs){

		frames += frameDuration;

		if(frames > 0) {
			if (state == 0) {
				gs.camara.camara.rotate(2 * intensity);
				state++;
			} else if (state == 1){
				gs.camara.camara.rotate(-4 * intensity);
				state++;
			} else if (state == 2){
				gs.camara.camara.rotate(4 * intensity);
				state--;
			}

			frames--;
			if(frames <= 0) {
				gs.camara.camara.rotate(-2*intensity);
				state = 0;
			}

		}

	}




	public void attack() {
		if (!hasMovedBefore) {
			if (Gdx.input.isTouched()) {
				if (clickAndRayCasting(x, y,this, stage.enemy, stage.walls, character.range) != null && character.range >= distance) {
					clickAndRayCasting(x, y,this, stage.enemy, stage.walls, character.range).damage(character.outgoingDamage(), "Melee");
					if (!character.shouldTurnCompletionBeLeftToClass)
						spendTurn();
					else {
						hasAttacked = true;
					}
				}
			}
		}
	}



	int previousTexture = 3;
	public int texture() {
		switch(previousPressKey){
			case 'A': {
				previousTexture = 1;
				if(previousPressKeySecondary == 'S'){
					previousTexture = 6;
				}
				if(previousPressKeySecondary == 'W'){
					previousTexture = 8;
				}
				break;
			}
			case 'D': {
				previousTexture = 2;
				if(previousPressKeySecondary == 'S'){
					previousTexture = 5;
				}
				if(previousPressKeySecondary == 'W'){
					previousTexture = 7;
				}
				break;
			}
			case 'W': {
				previousTexture = 4;
				if(previousPressKeySecondary == 'A'){
					previousTexture = 8;
				}
				if(previousPressKeySecondary == 'D'){
					previousTexture = 7;
				}
				break;
			}
			case 'S': {
				previousTexture = 3;
				if(previousPressKeySecondary == 'A'){
					previousTexture = 6;
				}
				if(previousPressKeySecondary == 'D'){
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
