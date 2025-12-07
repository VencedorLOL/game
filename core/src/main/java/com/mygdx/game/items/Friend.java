package com.mygdx.game.items;


import java.util.ArrayList;
import java.util.Collections;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackIconRenderer.actorsThatAttack;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Tile.findATile;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Friend extends Actor {
	public int[] color;


	@SuppressWarnings("all")
	public static OnVariousScenarios oVSc = new OnVariousScenarios(){
		@Override
		public void onStageChange() {
			for (Friend f : friend){
				f.x = chara.x; f.y = chara.y;
				f.softlockOverridable(false);
			}
		}
	};


	public static ArrayList<Friend> friend = new ArrayList<>();

	public static ArrayList<Tile> allaiesGrid;

	public float[] tileToReach = new float[2];

	public static void loop(){
		for (Friend e : friend) {
			if (isDecidingWhatToDo(e))
				break;
			if (allaiesGrid != null && findATile(allaiesGrid, e.x, e.y) != null)
				findATile(allaiesGrid, e.x, e.y).isWalkable = false;
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
			if (pathFindAlgorithm.quickSolve(x, y, gridSetter(targetActor.x), gridSetter(targetActor.y), allaiesGrid)) {
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

	public Friend(float x, float y, String texture, float health) {
		super(texture, x, y, globalSize(), globalSize());
		aggro = 1;
		pierces = false;
		team = 1;
		speed = 3;
		range = 2;
		damage = 20;
		actingSpeed = random(1, 7);
		print("acting speed of this friend is of " + totalActingSpeed);
		this.maxHealth = health;
		this.health = health;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		this.texture = texture;
		path = new Path(x,y,speed,this);
		permittedToAct = false;
		friend.add(this);
		actorsThatAttack.add(this);
		if(color == null)
			color = new int[]{random(0, 255), random(0, 255), random(0, 255)};
	}

	@SuppressWarnings("all")
	public Friend(float x, float y) {
		super("animaWithMustacheAndSurprisedWtfDidIJustDo",x,y,globalSize(),globalSize());
		aggro = 1;
		testCollision.x = x;
		testCollision.y = y;
		path = new Path(x,y,speed,this);
		team = 1;
		permittedToAct = false;
		friend.add(this);
		actorsThatAttack.add(this);
		if(color == null)
			color = new int[]{random(0, 255), random(0, 255), random(0, 255)};
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
			if (targetActor != null && !targetActor.isDead && ((targetActor.team == -team && (float) sqrt(pow(targetActor.x - x,2) + pow(targetActor.y - y,2)) / globalSize() <= totalRange && speedLeft[0] == 0 && speedLeft[1] == 0) || !attacks.isEmpty()) && (!attacks.isEmpty() || !permittedToAct))
				attack();
			else
				movement();
			conditions.render();
			glideProcess();
//*			if (!isDecidingWhatToDo(this) && !isTurnRunning() && !path.isListSizeOne())
//				path.renderLastStep();
		}
	}

	public void onDeathOverridable(){
		if (health <= 0) {
			animationToList("dying",x,y);
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			entityList.remove(this);
			actorsThatAttack.remove(this);
		}
	}

	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){
		float damagedFor = getDamagedFor(damage,damageReason);
		health -= damagedFor;
		if (damageReason == AttackTextProcessor.DamageReasons.MELEE && damagedFor != 0){
			particle.particleEmitter("BLOB",globalSize()/2f, globalSize()/2f,1,40,true,false,10,this);
		}
		AttackTextProcessor.addAttackText(damagedFor,damageReason,this);
		print("remaining health is: " + health);
		printErr("damaged for " + damagedFor + " damage");

	}

	public void attackDetector(){
		ArrayList<Actor> allMyFriends = new ArrayList<>(friend);
		allMyFriends.add(chara);
		allMyFriends.removeIf(e -> e.team != 1);
		ArrayList<Actor> list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY, allMyFriends, pierces, this);
		if (list != null) {
			for (Actor e : list)
				if ((float) sqrt(pow(e.x - x, 2) + pow(e.y - y, 2)) / globalSize() <= totalRange && e.team != team) {
					e.damage(totalDamage, AttackTextProcessor.DamageReasons.MELEE,this);
					if (!pierces)
						break;
				}
		}
		else
			text("Missed!", attacks.get(elementOfAttack -  1).targetX,attacks.get(elementOfAttack -  1).targetY + 240,60, TextureManager.Fonts.ComicSans,40,127,127,127,1,30);
	}



	public void targetFinder() {
		ArrayList<ActorAndDistance> targets = new ArrayList<>();
		for (Actor a : actors)
			if (!a.isDead && a.team == team * -1)
				targets.add(new ActorAndDistance(a, dC(a.x, a.y) * a.totalAggro));
		Collections.shuffle(targets);
		targets.sort((o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));
		Collections.reverse(targets);
		for (ActorAndDistance a : targets) {
			if (pathFindAlgorithm.quickSolve(x, y, a.getActor().x, a.getActor().y, getTakeEnemiesIntoConsideration()) && dC(a.getActor().getX(), a.getActor().getY()) <= totalSightRange * globalSize()) {
				targetActor = a.getActor();
				return;
			}
		}
		for (Actor a : actors)
			if (!a.isDead && a.team == team && a == chara)
				targets.add(new ActorAndDistance(a, dC(a.x, a.y)));
		Collections.shuffle(targets);
		targets.sort((o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));
		Collections.reverse(targets);
		for (ActorAndDistance a : targets) {
			if (pathFindAlgorithm.quickSolve(x, y, a.getActor().x, a.getActor().y, getTakeEnemiesIntoConsideration()) && dC(a.getActor().getX(), a.getActor().getY()) <= totalSightRange * globalSize()) {
				targetActor = a.getActor();
				return;
			}
		}
	}
/*	protected void turnSpeedActuator(){
*		if (speedLeft[0] > 0) {
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
