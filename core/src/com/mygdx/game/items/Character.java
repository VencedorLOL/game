package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.ceil;

public class Character extends Entity{
	public Texture character;
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
	long cooldown;
	byte speed = 1;
	byte speedState = 0;

	public boolean overlapsWithStage(Stage stage, Entity entity){
		for(Wall b : stage.walls){
			if (entity.overlaps(b))
				return true;
		}
		for(Enemy e : stage.enemy) {
			if (entity.x == e.x && entity.y == e.y)
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

	public void checkerGetterAndUpdater (Stage stage){
		checkerGetter(stage);
		update();
	}

	public void checkerGetter(Stage stage){
		this.stage = stage;
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
				if (n.x == e.x && n.y == e.y && n != e) {
					System.out.println("Discrepancy with enemy: " + n + " :and enemy: " + e);
					System.out.println("In x: " + n.x + " :in y: " + e.y);
				}
			}

		previousPressKeySecondary = 'N';
		previousPressKey = 'N';
		previousPressLocation = 0;
		speedState++;
		if (speedState >= speed)
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

	public void update(){
		isItMyTurn();
		if (isOnTurn)
			movement();
		if (Gdx.input.isKeyPressed(Input.Keys.G))
			System.out.println(isOnTurn);
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
		}
		textureUpdater();
		if(Gdx.input.isKeyPressed(Input.Keys.V) && TimeUtils.millis() - cooldown > 300 && whatTurnIsIt()) {
			cooldown = TimeUtils.millis();
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
				character = new Texture("CharaLeft.png");
				break;
			}
			case 2: {
				character = new Texture("CharaRight.png");
				break;
			}
			case 3 : {
				character = new Texture("char.jpg");
				break;
			}
			case 4 :{
				character = new Texture("CharaUp.png");
				break;
			}
			case 5 :{
				character = new Texture("CharaDiagonalDownRight.png");
				break;
			}
			case 6 :{
				character = new Texture("CharaDiagonalDownLeft.png");
				break;
			}
			case 7 :{
				character = new Texture("CharaDiagonalUpRight.png");
				break;
			}
			case 8 :{
				character = new Texture("CharaDiagonalUpLeft.png");
				break;
			}
		}
	}
}
