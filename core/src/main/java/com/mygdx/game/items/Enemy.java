package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Tile.findATile;
import static java.lang.Math.*;

public class Enemy extends Actor {
	public float health = 20;
	public float defense = 5;;

	boolean localFastMode;


	public static ArrayList<Enemy> enemies = new ArrayList<>();

	public static ArrayList<Tile> enemyGrid;

	public float[] tileToReach = new float[2];

	static {
	//	generation();
		OnVariousScenarios oVE2 = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				enemies.clear();
			}

			public void onTurnPass() {
			//	generation();
			}


		};
	}

/*	public static void generation(){
		Collections.shuffle(enemies);
		//TODO: make it so it also takes speed in consideration
		enemies.sort((o1, o2) -> Integer.compare(o2.actingSpeed, o1.actingSpeed));
		generateGrids();
		enemyGrid = new ArrayList<>();
		for (Tile t : grid)
			enemyGrid.add(t.clone());
		for (Tile t : enemyGrid)
			for (Actor a : actors)
				if (!(a instanceof Enemy) && a.x == t.x && a.y == t.y) {
					t.isWalkable = false;
					break;
				}

	}*/

	public static void loop(){
		for (Enemy e : enemies) {
			if (e.canDecide[1])
				break;
			if (enemyGrid != null && findATile(enemyGrid, e.x, e.y) != null) {
				findATile(enemyGrid, e.x, e.y).isWalkable = false;
			}
		}

	}

	public void getObjectiveTitle(){
		Tile objective;
		if (pathFindAlgorithm.solution != null && !pathFindAlgorithm.solution.isEmpty()) {
			try {objective = pathFindAlgorithm.solution.get(speed / 2 - 1);}
			catch (IndexOutOfBoundsException ignored) {objective = pathFindAlgorithm.solution.get(pathFindAlgorithm.solution.size() - 1);}
			tileToReach[0] = objective.x;tileToReach[1] = objective.y;
		} else {
			tileToReach[0] = x; tileToReach[1] = y;
		}
		print("Tile to reach is " + tileToReach[0] + " " + tileToReach[1]);

	}

	protected void automatedMovement(){
		if(targetActor == null)
			targetFinder();
		if (targetActor != null) {
			path.pathReset();
			if (pathFindAlgorithm.quickSolve(x, y, targetActor.x, targetActor.y, enemyGrid)) {
				path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
				getObjectiveTitle();
			}
			else
				actionDecided();
		} else actionDecided();

	}

	public Enemy(float x, float y, String texture, float health) {
		super(texture, x, y, 128, 128);
		pierces = false;
		team = -1;
		speed = 3;
		range = 2;
		damage = 20;
		actingSpeed = random(1, 7);
		print("acting speed of this enemy is of " + actingSpeed);
		this.health = health;
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		this.texture = texture;
		path = new Path(x,y,speed);
		team = -1;
		oVS = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				canDecide[1] = true;
			}
		};
		enemies.add(this);
	}

	public Enemy(float x, float y) {
		super("EvilGuy",x,y,globalSize(),globalSize());
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		path = new Path(x,y,speed);
		team = -1;
		oVS = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				canDecide[1] = true;
			}
		};
		enemies.add(this);
	}
	// Movement



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
			localFastMode = Settings.getFastMode();
		else
			localFastMode = true;
	}


	// Movement version: 6.0

	//AKA: it doesnt work



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



	public void update(ParticleManager pm){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			gameScreenGetter(pm);
			path.getStats(x,y,speed);
			loop();
			onDeath();
			if (targetActor == null)
				targetFinder();
			if (targetActor != null && (float) sqrt(pow(targetActor.x - x,2) + pow(targetActor.y - y,2)) / globalSize() <= range && speedLeft[0] == 0 && speedLeft[1] == 0)
				attack();
			else
				movement();
			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				print("");
				print("Myself: " + this);
				print("Target: x" + tileToReach[0] + " y" + tileToReach[1]);
				print("Coords: x" + x + " y" + y);
				print("ASpeed: " + actingSpeed);
				print("");
			}
		}
	}

	public void onDeath(){
		if (health <= 0) {
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			enemies.remove(this);
		}
	}

	public void damage(float damage, String damageReason){
		float damagedFor = max(damage - defense,0);
		health -= damagedFor;
		if (Objects.equals(damageReason, "Melee") && damagedFor != 0){
			pm.particleEmitter("BLOB",x + (float) globalSize() /2,y + (float) globalSize() /2,10);
		}
		int fontSize = 40;
		text(""+(damagedFor > 0 ? damagedFor : "0"),getX()
						+(fontSize-(float) (damagedFor > 0 ? (damagedFor + "").toCharArray().length - 1 : 1)/(fontSize*2*globalSize()))
				// original: +(16-(float) ((damagedFor + "").toCharArray().length))/32*globalSize()
				, (float) (getY()+(globalSize()*1.3*min(max(fontSize/25,1),2))),200, TextureManager.Fonts.ComicSans,fontSize, damagedFor == 0 ? 125 : 255, damagedFor == 0 ? 125 : 0, damagedFor == 0 ? 125 : 0,1,50);
		print("remaining health is: " + health);
		printErr("damaged for " + damagedFor + " damage");

	}

	public ParticleManager pm;
	public void gameScreenGetter(ParticleManager pm){ this.pm = pm; }
}
