package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Healer;
import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Turns.*;
import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.*;

public class Character extends Entity implements Utils {
	public CharacterClasses character = new CharacterClasses();
	public int range = 3;
	public Texture characterTexture;
	public float x, y, base, height;
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
	int distanceAux;
	boolean isDead;
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
		super(x,y,base,height);
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
		spendCharacterTurn();
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
				System.out.println("Offset in x");
				x = (float) (128 * ceil(x / 128));
			}
			if (!(y % 128 == 0)) {
				System.out.println("Offset in y");
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
		super.refresh(x, y, base, height);

	}

	public void update(Stage stage, GameScreen cam){
		this.stage = stage;
		onDeath();
		isItMyTurn();
		character.update(this);
		if (isOnTurn){
			movement();
			attack(cam);
			changeTo();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.B)) {
			isOnTurn = true;
			spendNotCharacterTurn();
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
	}



	public void attack(GameScreen cam) {
		if (!hasMovedBefore) {
			if (Gdx.input.isTouched()) {
				if (attackRayCasting(cam) != null && character.range >= distance) {
					attackRayCasting(cam).damage(character.outgoingDamage());
					System.out.println(distance);


					if(character instanceof Melee && ((Melee)character).FoA) {
						if (((Melee)character).attackState >= ((Melee) character).FoANumberOfExtraHits) {
							spendTurn();
							((Melee)character).FoA = false;
							((Melee)character).attackState = 0;
						}
						else
							((Melee)character).attackState++;
					}
					else
						spendTurn();
				}
			}
		}
	}


	public Enemy attackRayCasting(GameScreen cam){
		short timesRayTouchedWall = 0, touchedDL = 0, touchedDR = 0, touchedUL = 0, touchedUR = 0, touchedC = 0,timesRayTouchedOtherEnemy = 0,raysThroughWalls = 0,
				touchedEDL = 0, touchedEDR = 0, touchedEUL = 0, touchedEUR = 0, touchedEC = 0, raysThroughEnemies = 0;
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		// Just remove one '.camara' when transforming GameScreen to Camara.
		cam.camara.camara.unproject(touchedPosition);
		touchedPosition.x = (float) (128 * floor((touchedPosition.x)/ 128));
		touchedPosition.y = (float) (128 * floor((touchedPosition.y) / 128));
		for (Enemy e : stage.enemy) {
			if (touchedPosition.x == e.x && touchedPosition.y == e.y && !e.isDead) {
				distance = (int) round((sqrt(pow(x - e.x,2) + pow(y - e.y,2)))/128);
				System.out.println(distance);
				float rect = ((e.y + 64) - (y + 64)) / ((e.x + 64) - (x + 64));
				Entity rayCheckerCenter = new Entity(x + 64, y + 64, 4, 4);
				Entity rayCheckerDownLeft = new Entity(x, y, 4, 4);
				Entity rayCheckerDownRight = new Entity(x + 128, y, 4, 4);
				Entity rayCheckerUpLeft = new Entity(x, y + 128, 4, 4);
				Entity rayCheckerUpRight = new Entity(x + 128, y + 128, 4, 4);
				rayCheckerDownLeft.x += 1;
				rayCheckerDownRight.x -= 1;
				rayCheckerUpLeft.x += 1;
				rayCheckerUpRight.x -= 1;
				int sign = 1;
				if (e.x < x)
					sign = -1;
				// For some reason if I put 1/0 a warning pops up,
				// but if I put these constants, which are literally the same thing as I had
				// (1/0 and -1/0) it doesn't. Bruh.
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				for (int i = 0; i < pickValueAUnlessEqualsZeroThenPickB(abs(e.x-x),abs(e.y-y)); i++){
					rayCheckerCenter.x += sign;
					rayCheckerDownLeft.x += sign;
					rayCheckerDownRight.x += sign;
					rayCheckerUpLeft.x += sign;
					rayCheckerUpRight.x += sign;
					if (sign != 0) {
						rayCheckerCenter.y += rect * sign;
						rayCheckerDownLeft.y += rect * sign;
						rayCheckerDownRight.y += rect * sign;
						rayCheckerUpLeft.y += rect * sign;
						rayCheckerUpRight.y += rect * sign;
					}
					else if (rect == POSITIVE_INFINITY) {
						rayCheckerCenter.y++;
						rayCheckerDownLeft.y++;
						rayCheckerDownRight.y++;
						rayCheckerUpLeft.y++;
						rayCheckerUpRight.y++;
					}
					else if (rect == NEGATIVE_INFINITY) {
						rayCheckerCenter.y--;
						rayCheckerDownLeft.y--;
						rayCheckerDownRight.y--;
						rayCheckerUpLeft.y--;
						rayCheckerUpRight.y--;
					}
					cam.batch.draw(new Texture("FourByFour.png"), rayCheckerCenter.x,rayCheckerCenter.y);
					cam.batch.draw(new Texture("FourByFour.png"), rayCheckerUpLeft.x,rayCheckerUpLeft.y);
					cam.batch.draw(new Texture("FourByFour.png"), rayCheckerUpRight.x,rayCheckerUpRight.y);
					cam.batch.draw(new Texture("FourByFour.png"), rayCheckerDownLeft.x,rayCheckerDownLeft.y);
					cam.batch.draw(new Texture("FourByFour.png"), rayCheckerDownRight.x,rayCheckerDownRight.y);

					for (Wall w : stage.walls) {
						if(rayCheckerCenter.x < w.x + 128 && rayCheckerCenter.x + 1 > w.x && rayCheckerCenter.y < w.y + 128 && rayCheckerCenter.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedC++;
						}
						if (rayCheckerDownLeft.x < w.x + 128 && rayCheckerDownLeft.x + 1 > w.x && rayCheckerDownLeft.y < w.y + 128 && rayCheckerDownLeft.y + 1 > w.y){
							timesRayTouchedWall++;
							touchedDL++;
						}
						if (rayCheckerDownRight.x < w.x + 128 && rayCheckerDownRight.x + 1 > w.x && rayCheckerDownRight.y < w.y + 128 && rayCheckerDownRight.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedDR++;
						}
						if (rayCheckerUpLeft.x < w.x + 128 && rayCheckerUpLeft.x + 1 > w.x && rayCheckerUpLeft.y < w.y + 128 && rayCheckerUpLeft.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedUL++;
						}
						if (rayCheckerUpRight.x < w.x + 128 && rayCheckerUpRight.x + 1 > w.x && rayCheckerUpRight.y < w.y + 128 && rayCheckerUpRight.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedUR++;
						}
						if (touchedC >= 64) {
							raysThroughWalls++;
							touchedC = -256;
						}
						if (touchedDL >= 64) {
							raysThroughWalls++;
							touchedDL = -256;
						}
						if (touchedDR >= 64) {
							raysThroughWalls++;
							touchedDR = -256;
						}
						if (touchedUL >= 64) {
							raysThroughWalls++;
							touchedUL = -256;
						}
						if (touchedUR >= 64) {
							raysThroughWalls++;
							touchedUR = -256;
						}
						if (timesRayTouchedWall >= 325 || raysThroughWalls >= 3) {
							return null;
						}
					}
					for (Enemy en : stage.enemy) {
						if (!en.isDead){
							distanceAux = (int) round((sqrt(pow(x - en.x,2) + pow(y - en.y,2)))/128);
							if(rayCheckerCenter.x < en.x + 128 && rayCheckerCenter.x + 1 > en.x && rayCheckerCenter.y < en.y + 128 && rayCheckerCenter.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEC++;
							}
							if (rayCheckerDownLeft.x < en.x + 128 && rayCheckerDownLeft.x + 1 > en.x && rayCheckerDownLeft.y < en.y + 128 && rayCheckerDownLeft.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEDL++;
							}
							if (rayCheckerDownRight.x < en.x + 128 && rayCheckerDownRight.x + 1 > en.x && rayCheckerDownRight.y < en.y + 128 && rayCheckerDownRight.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEDR++;
							}
							if (rayCheckerUpLeft.x < en.x + 128 && rayCheckerUpLeft.x + 1 > en.x && rayCheckerUpLeft.y < en.y + 128 && rayCheckerUpLeft.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEUL++;
							}
							if (rayCheckerUpRight.x < en.x + 128 && rayCheckerUpRight.x + 1 > en.x && rayCheckerUpRight.y < en.y + 128 && rayCheckerUpRight.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEUR++;
							}
							if (touchedEC >= 64) {
								raysThroughEnemies++;
								touchedEC = -256;
							}
							if (touchedEDL >= 64) {
								raysThroughEnemies++;
								touchedEDL = -256;
							}
							if (touchedEDR >= 64) {
								raysThroughEnemies++;
								touchedEDR = -256;
							}
							if (touchedEUL >= 64) {
								raysThroughEnemies++;
								touchedEUL = -256;
							}
							if (touchedEUR >= 64) {
								raysThroughEnemies++;
								touchedEUR = -256;
							}
							if ((timesRayTouchedOtherEnemy >= 128 || raysThroughEnemies >= 3) && range >= distanceAux) {
								return en;
							}
						}
					}
				}
				return e;
			}
		}
		return null;
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
				characterTexture = new Texture("CharaLeft.png");
				break;
			}
			case 2: {
				characterTexture = new Texture("CharaRight.png");
				break;
			}
			case 3 : {
				characterTexture = new Texture("char.jpg");
				break;
			}
			case 4 :{
				characterTexture = new Texture("CharaUp.png");
				break;
			}
			case 5 :{
				characterTexture = new Texture("CharaDiagonalDownRight.png");
				break;
			}
			case 6 :{
				characterTexture = new Texture("CharaDiagonalDownLeft.png");
				break;
			}
			case 7 :{
				characterTexture = new Texture("CharaDiagonalUpRight.png");
				break;
			}
			case 8 :{
				characterTexture = new Texture("CharaDiagonalUpLeft.png");
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
		equipBestSword();
		equipBlessedShield();
		equipBlessedSword();
		equipMeleeShield();
		equipMeleeSword();
	}


	public void changeToHealer(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			character = new Healer();
		}
	}

	public void changeToMelee(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			character = new Melee();
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

}
