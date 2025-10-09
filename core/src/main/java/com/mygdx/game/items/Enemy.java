package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.particle;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.animationToList;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Tile.findATile;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Enemy extends Actor {
	public static ArrayList<Enemy> enemies = new ArrayList<>();

	public static ArrayList<Tile> enemyGrid;

	public float[] tileToReach = new float[2];

	public static void loop(){
		for (Enemy e : enemies) {
			if (isDecidingWhatToDo(e))
				break;
			if (enemyGrid != null && findATile(enemyGrid, e.x, e.y) != null) {
				findATile(enemyGrid, e.x, e.y).isWalkable = false;
			}
		}

	}

	public void getObjectiveTitle(){
		Tile objective;
		if (pathFindAlgorithm.solution != null && !pathFindAlgorithm.solution.isEmpty()) {
			try {objective = pathFindAlgorithm.solution.get(totalSpeed / 2 - 1);}
			catch (IndexOutOfBoundsException ignored) {objective = pathFindAlgorithm.solution.get(pathFindAlgorithm.solution.size() - 1);}
			tileToReach[0] = objective.x;tileToReach[1] = objective.y;
		} else {
			tileToReach[0] = x; tileToReach[1] = y;
		}
		print("Tile to reach is " + tileToReach[0] + " " + tileToReach[1]);

	}

	protected void automatedMovement(){
		if(targetActor == null && turnMode)
			targetFinder();
		if (targetActor != null && totalFollowRange * globalSize() > dC(targetActor.getX(), targetActor.getY())) {
			path.pathReset();
			if (pathFindAlgorithm.quickSolve(x, y, gridSetter(targetActor.x), gridSetter(targetActor.y), enemyGrid)) {
				path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
				getObjectiveTitle();
			} return;
		}
		targetActor = null;
		actionDecided();

	}

	private float gridSetter(float coordinate){
		return (float) (globalSize() * round(coordinate / globalSize()));
	}

	public Enemy(float x, float y, String texture, float health) {
		super(texture, x, y, globalSize(), globalSize());
		aggro = 1;
		pierces = false;
		team = -1;
		speed = 3;
		range = 2;
		damage = 20;
		defense = 5;
		actingSpeed = random(1, 7);
		print("acting speed of this enemy is of " + totalActingSpeed);
		maxHealth = health;
		this.health = health;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		this.texture = texture;
		path = new Path(x,y,speed,this);
		team = -1;
		permittedToAct = false;
		enemies.add(this);
	}

	public Enemy(float x, float y) {
		super("EvilGuy",x,y,globalSize(),globalSize());
		aggro = 1;
		testCollision.x = x;
		testCollision.y = y;
		path = new Path(x,y,speed,this);
		team = -1;
		permittedToAct = false;
		enemies.add(this);
		health = 20;
	}
	// Movement


	public boolean amIRendered(){
		return x - globalSize()*2 <= stage.camaraX + stage.camaraBase / 2 &&
				x + globalSize()*2 >= stage.camaraX - stage.camaraBase / 2 &&
				y - globalSize()*2 <= stage.camaraY + stage.camaraHeight / 2 &&
				y + globalSize()*2 >= stage.camaraY - stage.camaraHeight / 2;

	}



	protected void overlappingCheck() {
		for (Enemy e : stage.enemy)
			for (Enemy n : stage.enemy){
				if (n.x == e.x && n.y == e.y && n != e && !e.isDead) {
					print("Discrepancy with enemy: " + n + " :and enemy: " + e);
					print("In x: " + n.x + " :in y: " + e.y);
				}
			}
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



	public void update(){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			statsUpdater();
			path.getStats(x,y,totalSpeed);
			loop();
			onDeath();
			if ((targetActor == null || targetActor.isDead || targetActor.team != -team) && turnMode && isDecidingWhatToDo(this))
				targetFinder();
			if (targetActor != null && !targetActor.isDead && (((float) sqrt(pow(targetActor.x - x,2) + pow(targetActor.y - y,2)) / globalSize() <= totalRange && speedLeft[0] == 0 && speedLeft[1] == 0) || !attacks.isEmpty()) && (!attacks.isEmpty() || !permittedToAct))
				attack();
			else
				movement();
			glideProcess();
			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				print("");
				print("Myself: " + this);
				print("Target: x" + tileToReach[0] + " y" + tileToReach[1]);
				print("Coords: x" + x + " y" + y);
				print("ASpeed: " + totalActingSpeed);
				print("Damage: " + totalDamage);
				print("Defense: " + totalDefense);
				print("am i dead? " + isDead);
				print("health " + health);
				print("total health " + totalMaxHealth);
				print("");
			}
		}
	}

	public void onDeathOverridable(){
		if (health <= 0) {
			animationToList("dying",x,y);
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			enemies.remove(this);
			entityList.remove(this);
		}
	}

	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){
		float damagedFor;
		if(damageReason != AttackTextProcessor.DamageReasons.LIGHTNING)
			damagedFor = max(damage - totalDefense,0);
		else
			damagedFor = damage;

		health -= damagedFor;

		if (damageReason == AttackTextProcessor.DamageReasons.MELEE && damagedFor != 0){
			particle.particleEmitter("BLOB",x + (float) globalSize() /2,y + (float) globalSize() /2,10);
		}
		AttackTextProcessor.addAttackText(damagedFor,damageReason,this);
		print("remaining health is: " + health);
		printErr("damaged for " + damagedFor + " damage");

	}

/*	protected void turnSpeedActuator(){
		if (speedLeft[0] > 0) {
			testCollision.x += thisTurnVSM;
			if (!overlapsWithStageWithException(stage,testCollision,this))
				x += thisTurnVSM;
			speedLeft[0] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[0] < 0) {
			testCollision.x -= thisTurnVSM;
			if (!overlapsWithStageWithException(stage,testCollision,this))
				x -= thisTurnVSM;
			speedLeft[0] += thisTurnVSM;
			movedThisTurn++;
		}
		testCollision.x = x;
		if (speedLeft[1] > 0) {
			testCollision.y += thisTurnVSM;
			if (!overlapsWithStageWithException(stage,testCollision,this))
				y += thisTurnVSM;
			speedLeft[1] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[1] < 0) {
			testCollision.y -= thisTurnVSM;
			if (!overlapsWithStageWithException(stage,testCollision,this))
				y -= thisTurnVSM;
			speedLeft[1] += thisTurnVSM;
			movedThisTurn++;
		}
	}*/

}
