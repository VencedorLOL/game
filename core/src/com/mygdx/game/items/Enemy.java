package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Settings.animationSpeedGetter;
import static com.mygdx.game.Settings.visualSpeedMultiplierGetter;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class Enemy extends Entity{
	public float health = 20;
	public float defense = 5;
	static float base, height = 128;
	Stage stage;
	public Entity testCollision = new Entity();
	long time = 0;
	int movementStage = 0;
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
	boolean isDead = false;

	public Enemy(float x, float y, String texture, float health){
		super(texture,x,y,base,height);
		this.health = health;
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		visualSpeedMultiplier = visualSpeedMultiplierGetter();
		animationSpeed = animationSpeedGetter();
		this.texture = texture;
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

	public Enemy(float x, float y) {
		super("EvilGuy",x,y,base,height);
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = Enemy.base;
		testCollision.height = Enemy.height;
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
			if (tester != e.testCollision && !e.isDead) {
				if (tester.x == e.x && tester.y == e.y)
					return true;
				if (tester.x == e.testCollision.x && tester.y == e.testCollision.y)
					return true;

			}
		}
		return stage.characterX == tester.x && stage.characterY == tester.y ||
				tester.y == stage.finalY + 128 ||
				tester.y == stage.startY - 128 ||
				tester.x == stage.finalX + 128 ||
				tester.x == stage.startX - 128;
	}

	public void isItMyTurn(){
		if (!isDead)
			whatEnemiesTurnIsIt(stage);
		else {
			hasHadItsTurn = true;
			isOnTurn = false;
		}
	}

	public void amIRendered(){
		isRendered = x - 128*2 <= stage.camaraX + stage.camaraBase / 2 &&
				x + 128*2 >= stage.camaraX - stage.camaraBase / 2 &&
				y - 128*2 <= stage.camaraY + stage.camaraHeight / 2 &&
				y + 128*2 >= stage.camaraY - stage.camaraHeight / 2;

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
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[0] = 'U';
		testCollision.x = x + 128;
		testCollision.y = y + 128;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[1] = 'E';
		testCollision.x = x + 128;
		testCollision.y = y;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[2] = 'R';
		testCollision.x = x + 128;
		testCollision.y = y - 128;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[3] = 'C';
		testCollision.x = x;
		testCollision.y = y - 128;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[4] = 'D';
		testCollision.x = x - 128;
		testCollision.y = y - 128;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[5] = 'Z';
		testCollision.x = x - 128;
		testCollision.y = y;
		if (!overlapsWithStage(stage, testCollision))
			availableSpaces[6] = 'L';
		testCollision.x = x - 128;
		testCollision.y = y + 128;
		if (!overlapsWithStage(stage, testCollision))
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
		movementStage -= visualSpeedMultiplier;
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
		movementStage = 0;
		canMove = true;
		spendTurn();
	}

	public void movement(){
		if(movementStage < 128 && !localFastMode)
			movementSlowMode();
		if (canMove)
			movementDirection();
		if(movementStage >= 128)
			movementOnFinalize();
		if (movementStage < 128 && localFastMode)
			movementFastMode();
		if(hasMovedBefore && !localFastMode)
			movementStage += visualSpeedMultiplier;
		hasMovedBefore = false;
		super.refresh(texture,x, y, base, height);
		isOnTheGrid();
	}

	protected void isOnTheGrid(){
		if (movementStage == 0 && !isDead) {
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


	public void update(Stage stage, ParticleManager pm){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			gameScreenGetter(pm);
			this.stage = stage;
			onDeath();
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
				System.out.println("previousPressedKeyState: " + movementStage);
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

	public void onDeath(){
		if (health <= 0) {
			isDead = true;
		}
	}

	public void damage(float damage, String damageReason){
		health = health - max(damage - defense,0);
		if (Objects.equals(damageReason, "Melee")){
			pm.particleEmitter("BLOB",x + 64,y + 64,10);
		}

	}

	public ParticleManager pm;
	public void gameScreenGetter(ParticleManager pm){ this.pm = pm; }
}
