package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Stage.*;
import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class Enemy extends Entity{
	int thisTurnVSM;
	public int speed = 3;
	public float health = 20;
	public float defense = 5;
	Stage stage;
	public Entity testCollision = new Entity();
	int[] speedLeft = new int[2];
	boolean canDecide = true;
	boolean allowedToMove = false;
	char[] availableSpaces = new char[]{'N','N','N','N','N','N','N','N'};
	char move;
	static boolean fastMode;
	boolean localFastMode;
	boolean isRendered;
	boolean permittedToAct = false;
	public Enemy(float x, float y, String texture, float health) {
		super(texture, x, y, 128, 128);
		speed = random(1, 7);
		this.health = health;
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		this.texture = texture;
	}

	public Enemy(float x, float y) {
		super("EvilGuy",x,y,128,128);
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
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

	public void permitToMove(){
		permittedToAct = true;
	}

	@Override
	public boolean isPermittedToAct() {
		return permittedToAct;
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
		allowedToMove = false;
		permittedToAct = false;
	}

	// Movement version: 4.0

	// WIP: 5.0

	protected void freeSpaceDetector() {
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
		switch (move) {
			case 'U': {
				speedLeft[1] = 128;
				break;
			}
			case 'D': {
				speedLeft[1] = -128;
				break;
			}
			case 'R': {
				speedLeft[0] = 128;
				break;
			}
			case 'L': {
				speedLeft[0] = -128;
				break;
			}
			case 'E': {
				speedLeft[0] = 128;
				speedLeft[1] = 128;
				break;
			}
			case 'C': {
				speedLeft[1] = -128;
				speedLeft[0] = 128;
				break;
			}
			case 'Z': {
				speedLeft[1] = -128;
				speedLeft[0] = -128;
				break;
			}
			case 'Q': {
				speedLeft[1] = 128;
				speedLeft[0] = -128;
				break;
			}
			case 'S': {
				speedLeft[0] = 0;
				speedLeft[1] = 0;
				break;
			}
		}
		System.out.println(move);
	}

	protected void movementDirection(){
		testCollision.x = x;
		testCollision.y = y;
		freeSpaceDetector();
		canDecide = false;
	}

	protected void movementSlowMode(){
		System.out.println("Speed on x: " +speedLeft[0]);
		System.out.println("Speed on y: "+speedLeft[1]);
		System.out.println("VisualSpeedMultiplier is of: " +thisTurnVSM);

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


	protected void movementOnFinalize(){
		move = 'N';
		availableSpaces = new char[]{'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N'};
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		canDecide = true;
		spendTurn();
	}

	public void movement(){
		if (canDecide) {
			fastModeSetter();
			if (localFastMode) thisTurnVSM = 128;
			else thisTurnVSM = getVisualSpeedMultiplier();
			movementDirection();
		}
		if (!canDecide) {
			if (speedLeft[0] != 0 || speedLeft[1] != 0)
				movementSlowMode();

			if (speedLeft[0] == 0 && speedLeft[1] == 0 && move != 'N') {
				System.out.println("Finalized moving");
				movementOnFinalize();
			}
		}
		super.refresh(texture,x, y, base, height);
		isOnTheGrid();
	}

	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && !isDead) {
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
			if (isPermittedToAct())
				movement();
			if (Gdx.input.isKeyPressed(Input.Keys.E)) {
				System.out.println("canDecide: " + canDecide);
				System.out.println("move: " + move);
				System.out.println("availableSpaces UP: " + availableSpaces[0] + " :availableSpaces UP-RIGHT: " + availableSpaces[1]);
				System.out.println("availableSpaces RIGHT: " + availableSpaces[2] + " :availableSpaces DOWN-RIGHT: " + availableSpaces[3]);
				System.out.println("availableSpaces DOWN: " + availableSpaces[4] + " :availableSpaces DOWN-LEFT: " + availableSpaces[5]);
				System.out.println("availableSpaces LEFT: " + availableSpaces[6] + " :availableSpaces UP-LEFT: " + availableSpaces[7]);
				System.out.println("speed left on x : " + speedLeft[0]);
				System.out.println("speed left on y : " + speedLeft[1]);
				System.out.println("onTurn: " + allowedToMove);
				System.out.println("x: " + x);
				System.out.println("testX: " + testCollision.x);
				System.out.println("y: " + y);
				System.out.println("testY: " + testCollision.y);
				System.out.println("-");
			}
		}
	}

	public void onDeath(){
		if (health <= 0) {
			isDead = true;
			permittedToAct = false;
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
