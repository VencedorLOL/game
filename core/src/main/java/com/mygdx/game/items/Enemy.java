package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.click;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class Enemy extends Entity{
	PathFinder pathFindAlgorithm;
	int thisTurnVSM;
	public int speed = 3;
	Path path = new Path(x,y,speed,null);
	public float health = 20;
	public float defense = 5;
	Stage stage;
	public Entity testCollision = new Entity();
	int[] speedLeft = new int[2];
	boolean[] canDecide = {true,true};
	boolean allowedToMove = false;
	char[] availableSpaces = new char[]{'N','N','N','N','N','N','N','N'};
	char move;
	static boolean fastMode;
	boolean localFastMode;
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
		super("EvilGuy",x,y,globalSize(),globalSize());
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
				tester.y == stage.finalY + globalSize() ||
				tester.y == stage.startY - globalSize() ||
				tester.x == stage.finalX + globalSize() ||
				tester.x == stage.startX - globalSize();
	}

	public void permitToMove(){
		permittedToAct = true;
	}

	@Override
	public boolean isPermittedToAct() {
		return permittedToAct;
	}


	public boolean amIRendered(){
		return x - globalSize()*2 <= stage.camaraX + stage.camaraBase / 2 &&
				x + globalSize()*2 >= stage.camaraX - stage.camaraBase / 2 &&
				y - globalSize()*2 <= stage.camaraY + stage.camaraHeight / 2 &&
				y + globalSize()*2 >= stage.camaraY - stage.camaraHeight / 2;

	}
	public void fastModeSetter(){
		if (amIRendered())
			localFastMode = fastMode;
		else
			localFastMode = true;
	}

	public void spendTurn(){
		print("st 23");
		allowedToMove = false;
		permittedToAct = false;
	}

	// Movement version: 5.0

	// LETS DO THIS, MASSIVE DELETION TO MAKE SPACE FOR: 6.0, now it pathfinds.

	private void actionDecided(){
		Turns.enemyFinalizedChoosing(this);
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
		print("fin mov");
		spendTurn();
	}

	protected void movementInput(){
		pathFinding();
		if (path.pathCreate(x,y,speed,stage)) {
			canDecide = new boolean[] {false, false};
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
	}


	private void pathFinding(){
		if(pathFindAlgorithm == null)
			pathFindAlgorithm = new PathFinder(stage);
		path.pathReset();
		PathFinder.reset(stage);
		pathFindAlgorithm.setStart(x,y);
		pathFindAlgorithm.setPlayerAsEnd();
		pathFindAlgorithm.solve();
		if (pathFindAlgorithm.algorithm.getPath() != null){
			path.setPathTo(pathFindAlgorithm.getSolvedPath());
		} else
			print("no path found");
	}

	public void movement(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct()) {
			if(speedLeft[0] == 0 && speedLeft[1] == 0 && !path.pathEnded){
				print("speed x: " + speedLeft[0] + " speed y : " + speedLeft[1]);
				speedLeft = path.pathProcess(this);
			}

			if (speedLeft[0] != 0 || speedLeft[1] != 0) {
				print("speed x: " + speedLeft[0] + " speed y : " + speedLeft[1]);
				primaryMovement();
				print("alive1;");
			}
print("alixe 2;");
			print("pat " + path.pathEnded);
			if (speedLeft[0] == 0 && speedLeft[1] == 0 && path.pathEnded) {
				print("fin mov 2");
				finalizedMove();
			}
		}
		else if (canDecide() && isDecidingWhatToDo(this))
			movementInput();

		super.refresh(texture,x, y, base, height);
	}


	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && !isDead) {
			if (!(x % globalSize() == 0)) {
				System.out.println("Offset in x caused at: " + x + " :by: " + this + " :New x is: " + 128 * ceil(x / 128));
				x = (float) (globalSize() * ceil(x / globalSize()));
			}
			if (!(y % globalSize() == 0)) {
				System.out.println("Offset in y caused at: " + y + " :by: " + this + " :New y is: " + 128 * ceil(y / 128));
				y = (float) (globalSize() * ceil(y / globalSize()));
			}
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

	public void update(Stage stage, ParticleManager pm){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			gameScreenGetter(pm);
			this.stage = stage;
			if (pathFindAlgorithm == null)
				pathFindAlgorithm = new PathFinder(stage);
			path.getStats(x,y,speed,stage);
			onDeath();
			movement();
			if (Gdx.input.isKeyPressed(Input.Keys.E)) {
				System.out.println("canDecide: " + canDecide());
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
		pm.particleEmitter("BLOB",x + (float) globalSize() /2,y + (float) globalSize() /2,10);
		}
		print("remaining health is: " + health);

	}

	public boolean canDecide(){
		return canDecide[0] && canDecide[1];
	}

	public ParticleManager pm;
	public void gameScreenGetter(ParticleManager pm){ this.pm = pm; }
}
