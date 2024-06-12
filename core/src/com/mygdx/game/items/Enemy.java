package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Settings.animationSpeedGetter;
import static com.mygdx.game.Settings.visualSpeedMultiplierGetter;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.ceil;

public class Enemy extends Entity{
	Texture enemyTexture = new Texture("EvilGuy.jpg");
	float x, y;
	float base, height = 128;
	Stage checker;
	public Entity testCollision = new Entity();
	long time = 0;
	int previousPressLocation = 0;
	int animationSpeed;
	int visualSpeedMultiplier;
	boolean isDivisibleBy128 = false;
	boolean hasMovedBefore;
	boolean canMove = true;
	boolean isOnTurn = false;
	boolean hasHadItsTurn = false;
	char[] availableSpaces = new char[]{'N','N','N','N','N','N','N','N'};
	char move;
	static boolean fastMode;
	boolean localFastMode;
	boolean isRendered;
	public Enemy(float x, float y, float base, float height, Texture texture){
		super(x,y,base,height);
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = this.base;
		testCollision.height = this.height;
		visualSpeedMultiplier = visualSpeedMultiplierGetter();
		animationSpeed = animationSpeedGetter();
		enemyTexture = texture;
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

	public Enemy(float x, float y, float base, float height) {
		super(x,y,base,height);
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = this.base;
		testCollision.height = this.height;
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
	// Movement

	public boolean overlapsWithStage(Stage stage, Entity tester){
		for (Wall b : stage.walls){
			if (tester.x == b.x && tester.y == b.y)
				return true;
		}
		for (ScreenWarp s : stage.screenWarp){
			if (tester.x == s.x && tester.y == s.y)
				return true;
		}
		for (Enemy e : stage.enemy) {
			if (tester != e.testCollision) {
				if (tester.x == e.x && tester.y == e.y)
					return true;
				if (tester.x == e.testCollision.x && tester.y == e.testCollision.y)
					return true;

			}
		}
		return stage.characterX == tester.x && stage.characterY == tester.y || tester.y == stage.finalY + 128 ||
				tester.y == stage.startY - 128 || tester.x == stage.finalX + 128 || tester.x == stage.startX - 128;
	}

	public void checkerGetterAndUpdater (Stage stage){
		checkerGetter(stage);
		update();
	}

	public void checkerGetter(Stage stage){
		checker = stage;
	}

	public void isItMyTurn(){
		whatEnemiesTurnIsIt(checker);
	}

	public void amIRendered(){
		isRendered = x - 128*2<= checker.camaraX + checker.camaraBase / 2 &&
				x + 128*2>= checker.camaraX - checker.camaraBase / 2 &&
				y - 128*2<= checker.camaraY + checker.camaraHeight / 2 &&
				y + 128*2>= checker.camaraY - checker.camaraHeight / 2;

	}
	public void fastModeSetter(){
		if (isRendered)
			localFastMode = fastMode;
		else
			localFastMode = true;
	}

	public void spendTurn(){
		isOnTurn = false;
		nextEnemyTurnCaller();
	}

	// Movement version: 4.0

	protected void freeSpaceDetector(){
		testCollision.x = x;
		testCollision.y = y + 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[0] = 'U';
		testCollision.x = x + 128;
		testCollision.y = y + 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[1] = 'E';
		testCollision.x = x + 128;
		testCollision.y = y;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[2] = 'R';
		testCollision.x = x + 128;
		testCollision.y = y - 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[3] = 'C';
		testCollision.x = x;
		testCollision.y = y - 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[4] = 'D';
		testCollision.x = x - 128;
		testCollision.y = y - 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[5] = 'Z';
		testCollision.x = x - 128;
		testCollision.y = y;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[6] = 'L';
		testCollision.x = x - 128;
		testCollision.y = y + 128;
		if (!overlapsWithStage(checker, testCollision))
			availableSpaces[7] = 'Q';

		if (availableSpaces[0] == 'N' && availableSpaces[1] == 'N' && availableSpaces[2] == 'N' &&
				availableSpaces[3] == 'N' && availableSpaces[4] == 'N' && availableSpaces[5] == 'N' &&
				availableSpaces[6] == 'N' && availableSpaces[7] == 'N')
			move = 'S';
		if (move != 'S') {
			do {
				move = availableSpaces[random(0, 7)];
			}
			while (move == 'N');
		}
	}

	protected void movementDirection(){
		fastModeSetter();
		testCollision.x = x;
		testCollision.y = y;
		freeSpaceDetector();
		hasMovedBefore = true;
		canMove = false;
		previousPressLocation -= visualSpeedMultiplier;
	}

	protected void movementSlowMode(){
		switch (move){
			case 'U': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y += visualSpeedMultiplier;
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
						y -= visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'R': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed){
						x += visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'L': {
				time = TimeUtils.nanoTime();
				while(true)
					if (TimeUtils.nanoTime() - time > animationSpeed){
						x -= visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'E': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y += visualSpeedMultiplier;
						x += visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'C': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed) {
						y -= visualSpeedMultiplier;
						x += visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'Z': {
				time = TimeUtils.nanoTime();
				while (true)
					if (TimeUtils.nanoTime() - time > animationSpeed){
						y -= visualSpeedMultiplier;
						x -= visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'Q': {
				time = TimeUtils.nanoTime();
				while(true)
					if (TimeUtils.nanoTime() - time > animationSpeed){
						y += visualSpeedMultiplier;
						x -= visualSpeedMultiplier;
						hasMovedBefore = true;
						break;
					}
				canMove = false;
				break;
			}
			case 'S': {
				movementOnFinalize();
			}
		}
	}

	protected void movementFastMode(){
		switch (move){
			case 'U': {
				y += 128;
				break;
				}
			case 'D': {
				y -= 128;
				break;
			}
			case 'R': {
				x += 128;
				break;
			}
			case 'L': {
				x -= 128;
				break;
			}
			case 'E': {
				y += 128;
				x += 128;
				break;
			}
			case 'C': {
				y -= 128;
				x += 128;
				break;
			}
			case 'Z': {
				y -= 128;
				x -= 128;
				break;
			}
			case 'Q': {
				y += 128;
				x -= 128;
				break;
			}
			case 'S': {
				movementOnFinalize();
			}
		}
		movementOnFinalize();
	}

	protected void movementOnFinalize(){
		move = 'N';
		availableSpaces = new char[]{'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N'};
		previousPressLocation = 0;
		canMove = true;
		spendTurn();
	}

	public void movement(){
		if(previousPressLocation < 128 && !localFastMode)
			movementSlowMode();
		if (canMove)
			movementDirection();
		if(previousPressLocation >= 128)
			movementOnFinalize();
		if (previousPressLocation < 128 && localFastMode)
			movementFastMode();
		if(hasMovedBefore && !localFastMode)
			previousPressLocation += visualSpeedMultiplier;
		hasMovedBefore = false;
		super.refresh(x, y, base, height);
		isOnTheGrid();
	}

	protected void isOnTheGrid(){
		if (previousPressLocation == 0) {
			if (!(x % 128 == 0)) {
				System.out.println("Offset in x caused at: " + x + " :by: " + this + " :New x is: " + 128 * ceil(x / 128));
				x = (float) (128 * ceil(x / 128));
			}
			if (!(y % 128 == 0)) {
				System.out.println("Offset in y caused at: " + y + " :by: " + this + " :New y is: " + 128 * ceil(y / 128));
				y = (float) (128 * ceil(y / 128));
			}
		}
	}


	public void update(){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && haveGrassBeenRendered && haveScreenWarpsBeenRendered) {
			amIRendered();
			isItMyTurn();
			if (isOnTurn)
				movement();
			if (Gdx.input.isKeyPressed(Input.Keys.E)) {
				System.out.println("canMove: " + canMove);
				System.out.println("hasMovedBefore: " + hasMovedBefore);
				System.out.println("move: " + move);
				System.out.println("availableSpaces UP: " + availableSpaces[0] + " :availableSpaces UP-RIGHT: " + availableSpaces[1]);
				System.out.println("availableSpaces RIGHT: " + availableSpaces[2] + " :availableSpaces DOWN-RIGHT: " + availableSpaces[3]);
				System.out.println("availableSpaces DOWN: " + availableSpaces[4] + " :availableSpaces DOWN-LEFT: " + availableSpaces[5]);
				System.out.println("availableSpaces LEFT: " + availableSpaces[6] + " :availableSpaces UP-LEFT: " + availableSpaces[7]);
				System.out.println("previousPressedKeyState: " + previousPressLocation);
				System.out.println("onTurn: " + isOnTurn);
				System.out.println("x: " + x);
				System.out.println("testX: " + testCollision.x);
				System.out.println("y: " + y);
				System.out.println("testY: " + testCollision.y);
				System.out.println("-");
			}
			if (localFastMode)
				visualSpeedMultiplier = 128;
			else
				visualSpeedMultiplier = 8;
		}
	}
}
