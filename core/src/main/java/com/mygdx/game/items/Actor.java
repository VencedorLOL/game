package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.EARTHQUAKE;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.ELECTRIC;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.Enemy.enemies;
import static com.mygdx.game.items.FieldEffects.getAdditive;
import static com.mygdx.game.items.FieldEffects.getMultiplier;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnActorDeath;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnDamagedActor;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Actor extends Entity{
	public float maxHealth;
	public float health = 20;
	public float damage;
	public int speed;
	public int actingSpeed;
	public float defense;
	public int range;
	public float aggro = 1;
	public float sightRange = 10;
	public float followRange = 40;

	public float totalMaxHealth;
	public float totalDamage;
	public int totalSpeed;
	public int totalActingSpeed;
	public float totalDefense;
	public int totalRange;
	public float totalAggro = 1;
	public float totalSightRange = 10;
	public float totalFollowRange = 40;

	public boolean airborn = false;

	public float movedThisTurn;
	public Entity lastDamager;
	public byte team;
	// -1 = evil
	// 0 = neutral
	// 1 = good
	public boolean pierces;
	public int[] speedLeft = new int[2];
	public Path path;
	int thisTurnVSM = getVisualSpeedMultiplier();
	public PathFinder pathFindAlgorithm;
	public Entity testCollision = new Entity();
	public boolean permittedToAct;
	public boolean controlOfCamara;

	boolean isDead;

	public Actor targetActor;

	public static ArrayList<Actor> actors = new ArrayList<>();

	public float lastTimeTilLastMovement = 0;

	public byte textureOrientation;

	public boolean didItAct = false;

	public ConditionsManager conditions = new ConditionsManager(this);

	public boolean movementLock = false;

	public boolean didItAct(){return didItAct;}
	public void setDidItAct(boolean didItAct) {this.didItAct = didItAct;}

	public boolean lockClassTilAnimationFinishes = false;

	@SuppressWarnings("all")
	static OnVariousScenarios oVS = new OnVariousScenarios(){
		@Override
		public void onTickStart() {
			for (Actor a : actors)
				a.alreadyTextured = false;
		}

		@Override
		public void onStageChange() {
			for (Actor a : actors) {
				a.didItAct = false;
				a.permittedToAct = false;
			}
		}

		@Override
		public void onTurnPass() {
			for (Actor a : actors) {
				a.path.pathReset();
				a.movedThisTurn = 0;
				print("wan turn pass ofr "+ a );
				a.conditions.onTurnPass();
			}
		//	path = new Path(x,y,speed);
		}
	};

	public static Actor actorInPos(float x, float y){
		for(Actor a : actors)
			if(a.x == x && a.y == y)
				return a;
		return null;
	}

	public boolean getIsDead() { return isDead; }

	public Actor(String aChar, float x, float y, float base, float height) {
		super(aChar,x,y,base,height);
		pathFindAlgorithm = new PathFinder();
		actors.add(this);
	}

	public void statsUpdater(){
		totalMaxHealth = (maxHealth + conditions.getAdditive(0)+ getAdditive(0)) * conditions.getMultiplier(0) * getMultiplier(0);
		totalDamage = (damage + conditions.getAdditive(1)+ getAdditive(1)) * conditions.getMultiplier(1) * getMultiplier(1);
		totalSpeed = (int) ((speed + conditions.getAdditive(2)+ getAdditive(2)) * conditions.getMultiplier(2) * getMultiplier(2));
		totalActingSpeed = (int) ((actingSpeed + conditions.getAdditive(3)+ getAdditive(3)) * conditions.getMultiplier(3) * getMultiplier(3));
		totalDefense = (defense + conditions.getAdditive(4)+ getAdditive(4)) * conditions.getMultiplier(4) * getMultiplier(4);
		totalRange = (int) ((range + conditions.getAdditive(5)+ getAdditive(5)) * conditions.getMultiplier(5) * getMultiplier(5));
		totalAggro = (aggro + conditions.getAdditive(11)+ getAdditive(11)) * conditions.getMultiplier(11) * getMultiplier(11);
		totalSightRange = sightRange;
		totalFollowRange = followRange;
	}

	public void healThis(float heal){
		totalMaxHealth += heal;
		if(health > totalMaxHealth)
			health = totalMaxHealth;
		AttackTextProcessor.addAttackText(heal, AttackTextProcessor.DamageReasons.HEALING,this);
	}

	public float damageRecieved;
	public final void damage(float damage, AttackTextProcessor.DamageReasons damageReason, Entity lastDamager){
		this.lastDamager = lastDamager;
		damageRecieved = damage;
		conditions.onDamaged(damageReason);
		triggerOnDamagedActor(this,damageReason);
		damageOverridable(damageRecieved,damageReason);
	}

	public float getDamagedFor(float damage, AttackTextProcessor.DamageReasons damageReason) {
		float damagedFor;
		if(damageReason != ELECTRIC && damageReason !=  AttackTextProcessor.DamageReasons.BURNT
				&& damageReason !=  EARTHQUAKE && damageReason !=  AttackTextProcessor.DamageReasons.UNIVERSAL
				&& damageReason !=  AttackTextProcessor.DamageReasons.FROSTBITE)
			if(damageReason ==  AttackTextProcessor.DamageReasons.PIERCING)
				damagedFor = max(damage - (totalDefense/2),0);
			else
				damagedFor = max(damage - totalDefense,0);
		else
			damagedFor = damage;
		if((damageReason == ELECTRIC || damageReason == EARTHQUAKE) && airborn)
			damagedFor = 0;
		return damagedFor;
	}

	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){}

	public boolean isPermittedToAct(){return permittedToAct;}

	public void permitToAct(){permittedToAct = true;}

	public boolean overlapsWithStage(Stage stage, Entity tester){
		return overlapsWithStageWithException(stage,tester,null);
	}

	public boolean overlapsWithStageWithException(Stage stage, Entity tester, Entity ignore){
		for (Wall b : stage.walls){
			if (tester.overlaps(b))
				return true;
		}
		for (Actor e : actors) {
			if (ignore == e)
				continue;
			if (tester != e.testCollision && !e.isDead) {
				if (tester.overlaps(e))
					return true;
				if (tester.overlaps(e.testCollision))
					return true;

			}
		}
		if (tester.overlaps(chara) && tester != chara && ignore != chara && this != chara)
			return true;
		return  chara.x == tester.x && chara.y == tester.y && tester != chara ||
				stage.finalY >= 0 ? tester.y >= stage.finalY + 1 : tester.y <= stage.finalY - 1 ||
				stage.startY >= 0 ? tester.y <= stage.startY - 1 : tester.y >= stage.startY + 1 ||
				stage.finalX >= 0 ? tester.x >= stage.finalX + 1 : tester.x <= stage.finalX - 1 ||
				stage.startX >= 0 ? tester.x <= stage.startX - 1 : tester.x >= stage.startX + 1 ;
	}


	public void cancelDecision(){
		if(Turns.cancelDecision(this)){
			speedLeft[0] = 0; speedLeft[1] = 0;
			path.pathStart();
			attacks.clear();
		}
	}


	public void movement(){
		lastTimeTilLastMovement++;
		testCollision.x = x;
		testCollision.y = y;
		if (turnMode) {
			if (isPermittedToAct()) {
				lastTimeTilLastMovement = 0;
				if (speedLeft[0] == 0 && speedLeft[1] == 0 && !path.pathEnded)
					speedLeft = path.pathProcess();

				if (speedLeft[0] != 0 || speedLeft[1] != 0)
					turnSpeedActuator();

				if (speedLeft[0] == 0 && speedLeft[1] == 0 && path.pathEnded) {
					softlockOverridable(false);
					finalizedTurn();
					if (this instanceof Character)
						((Character) this).classes.runMove();
					else
						conditions.onMove();
				}

			} else if (isDecidingWhatToDo(this) && speedLeft[0] == 0 && speedLeft[1] == 0 && !movementLock)
				movementInputTurnMode();

		} else {
			speedActuator();
			if (this instanceof Enemy){
				enemyOnFreeMode();
			}
			if (this instanceof Character){
				movementInputManual();
				softlockOverridable(false);
			}
		}
	}
	ArrayList<Tile> dumpList;
	@SuppressWarnings("all")
	public void softlockOverridable(boolean type) {
		if (overlapsWithStageWithException(stage,this,this) && !betweenStages){
			print("SOFTLOCK SOFTLOCK at " + x  + " " + y);
			for (Tile t : stage.tileset)
				t.hasBeenChecked = false;
			dumpList = new ArrayList<>();
			ArrayList<Tile> currentTilesetChecking = new ArrayList<>();
			currentTilesetChecking.add(Tile.findATile(stage.tileset,(float) (globalSize() * round(this.x / globalSize())),(float) (globalSize() * round(this.y / globalSize()))));
			if(currentTilesetChecking.get(0) != null && currentAnalize(currentTilesetChecking.get(0),type)) {
				dumpList = null;
				return;
			}
			for (int i = 1; i <= stage.tileset.size(); i++) {
				currentTilesetChecking = (ArrayList<Tile>) dumpList.clone();
				dumpList.clear();
				for (Tile t : currentTilesetChecking)
					if (currentAnalize(t,type)) {
						dumpList = null;
						return;
					}
			}
		}
	}

	private boolean currentAnalize(Tile currentTile,boolean type){
		ArrayList<Tile> neighbours = currentTile.walkableOrthogonalTiles(stage.tileset);
		for (Tile t : neighbours)
			if (!t.hasBeenChecked) {
				t.hasBeenChecked = true;
				dumpList.add(t);
				dumpList.removeIf(tt -> t == tt);
				testCollision.x = t.x; testCollision.y = t.y;
				if (!overlapsWithStageWithException(stage,testCollision,this)) {
					if(type)
						glideAbsoluteCoords(t.x,t.y,20);
					else {
						x = t.x; y = t.y;
					}
					return true;
				}
			}
		return false;
	}

	protected void enemyOnFreeMode(){}

	protected void speedActuator(){
		if (speedLeft[0] > 0) {
			testCollision.x += thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				x += thisTurnVSM;
			speedLeft[0] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[0] < 0) {
			testCollision.x -= thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				x -= thisTurnVSM;
			speedLeft[0] += thisTurnVSM;
			movedThisTurn++;
		}
		testCollision.x = x;
		if (speedLeft[1] > 0) {
			testCollision.y += thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				y += thisTurnVSM;
			speedLeft[1] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[1] < 0) {
			testCollision.y -= thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				y -= thisTurnVSM;
			speedLeft[1] += thisTurnVSM;
			movedThisTurn++;
		}
	}

	protected void turnSpeedActuator(){
		if (speedLeft[0] > 0) {
			x += thisTurnVSM;
			speedLeft[0] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[0] < 0) {
			x -= thisTurnVSM;
			speedLeft[0] += thisTurnVSM;
			movedThisTurn++;
		}
		if (speedLeft[1] > 0) {
			y += thisTurnVSM;
			speedLeft[1] -= thisTurnVSM;
			movedThisTurn++;
		}
		else if (speedLeft[1] < 0) {
			y -= thisTurnVSM;
			speedLeft[1] += thisTurnVSM;
			movedThisTurn++;
		}
	}

	protected void movementInputTurnMode(){
		automatedMovement();
		if (path.pathCreate(x,y, totalSpeed,this))
			actionDecided();
	}


	protected void automatedMovement(){
		if(targetActor == null && turnMode)
			targetFinder();
		if (targetActor != null && totalFollowRange * globalSize() > dC(targetActor.getX(), targetActor.getY())) {
			path.pathReset();
			if (pathFindAlgorithm.quickSolve(x, y, targetActor.x, targetActor.y, getTakeEnemiesIntoConsideration()))
				path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
			return;
		}
		targetActor = null;
		actionDecided();
	}

	public void actionDecided(){
		thisTurnVSM = getVisualSpeedMultiplier();
		Turns.finalizedChoosing(this);
	}

	public void finalizedTurn(){
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		attacks.clear();
		spendTurn();
	}


	public void spendTurn(){
		printErr("Called spendTurn on + " + this);
		permittedToAct = false;
		path.pathStart();
		attacks.clear();
	}


	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && !isGliding) {
			float x, y;
			x = (float) (globalSize() * round(this.x / globalSize()));
			y = (float) (globalSize() * round(this.y / globalSize()));
			if (x !=0 || y != 0)
				glideAbsoluteCoords(x,y,20);
		}
	}

	protected void isOnTheGridForced(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && !isGliding) {
			if (!(this.x % globalSize() == 0))
				x = (float) (globalSize() * round(this.x / globalSize()));

			if (!(this.y % globalSize() == 0))
				y = (float) (globalSize() * round(this.y / globalSize()));

		}
	}

	public void movementInputManual(){}

	public void glideProcess(){
			if (glideTime <= 0) {
				glideXPerFrame = 0;
				glideYPerFrame = 0;
				glideZPerFrame = 0;
				isGliding = false;
				expectedX = null;
				expectedY = null;
				if (turnMode)
					isOnTheGridForced();
			}
			else {
				glideTime--;
				x += glideXPerFrame;
				y += glideYPerFrame;
				z += glideZPerFrame;
				if(glideTime == 0 && expectedX != null && expectedY != null){
					x = expectedX.aFloat;
					y = expectedY.aFloat;
				}
				textureCustomSpeed(abs(glideXPerFrame * glideTime) < 1 ? 0 : glideXPerFrame,
						abs(glideYPerFrame * glideTime) < 1 ? 0 : glideYPerFrame);
			}
	}

	boolean alreadyTextured;
	public void texture(){
		if (!alreadyTextured) {
			textureOrientation = 7;
			if (speedLeft[0] > 0) textureOrientation = 1;
			if (speedLeft[0] < 0) textureOrientation = 4;
			if (speedLeft[1] > 0) textureOrientation  ++;
			if (speedLeft[1] < 0) textureOrientation  --;
			alreadyTextured = true;
		}
	}

	public void textureCustomSpeed(float x, float y) {
		if (!alreadyTextured) {
			textureOrientation = 7;
			if (x > 0) textureOrientation = 1;
			if (x < 0) textureOrientation = 4;
			if (y > 0) textureOrientation++;
			if (y < 0) textureOrientation--;
//			alreadyTextured = true;
		}
	}


	public static void flushActorListButCharacter(){
		entityList.removeIf(e -> e instanceof Actor);
		actors.clear();
		friend.clear();
		enemies.clear();
		actors.add(chara);
	}


	public void targetFinder(){
		ArrayList<ActorAndDistance> targets = new ArrayList<>();
		for (Actor a : actors)
			if (!a.isDead && a.team == team*-1)
				targets.add(new ActorAndDistance(a,dC(a.x,a.y)*a.totalAggro));
		Collections.shuffle(targets);
		targets.sort((o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));
		Collections.reverse(targets);
		for (ActorAndDistance a : targets){
			if (pathFindAlgorithm.quickSolve(x, y, a.getActor().x, a.getActor().y, getTakeEnemiesIntoConsideration()) && dC(a.getActor().getX(), a.getActor().getY()) <= totalSightRange * globalSize()) {
				targetActor = a.actor;
				return;
			} else if (totalSightRange * globalSize() > dC(a.getActor().getX(), a.getActor().getY()))
				return;
		}
	}


	@SuppressWarnings("all")
	static class ActorAndDistance{
		private Actor actor;
		private double distance;

		ActorAndDistance(Actor actor, double distance){
			this.actor = actor;
			this.distance = distance;
		}

		Actor getActor(){return actor;}
		double getDistance() {return distance;}

		private void setActor(Actor actor){this.actor = actor;}
		private void setDistance(double distance){this.distance = distance;}
	}


	public static class Attack{
		Actor owner;
		float targetX, targetY;
		boolean render = true;
		boolean isBeingExecuted = false;
		public Attack(float x, float y,Actor owner){
			targetX = x; targetY = y; this.owner = owner;
		}
	}

	public void attack(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct())
			attackActuator();

		else if (isDecidingWhatToDo(this))
			attackInput();
	}


	public int elementOfAttack = 0;
	// explanation of da method:
	// the method checks if there's an attack to do and if the attack isnt locked.
	// then it locks the attack and plays an animation. when the animation finishes the attack will be calculated and done
	// then it advances in one elementOfAttack, so the animation queued will do the correct attack calculation when it finishes
	// after the animation finishes the method is then unlocked so it can proceed to the next animation/attack.
	// when the last attack of the list has to be queued up, it will queue itself with the instructions to finalize its turn.
	public void attackActuator(){
		if(!attacks.isEmpty() && !lockClassTilAnimationFinishes) {
			lockClassTilAnimationFinishes = true;
			attacks.get(elementOfAttack).isBeingExecuted = true;
			elementOfAttack++;
			if (elementOfAttack >= attacks.size())
				animations.add(new TextureManager.Animation("attack", x, y) {
					public void onFinish() {
						lockClassTilAnimationFinishes = false;
						attackDetector();
						finalizedTurn();
						elementOfAttack = 0;
					}});
			else
				animations.add(new TextureManager.Animation("attack", x, y) {
					public void onFinish() {
						lockClassTilAnimationFinishes = false;
						attackDetector();
					}});

		}
	}

	public void attackDetector(){
		ArrayList<Actor> actuallyEnemies = new ArrayList<>(enemies);
		actuallyEnemies.removeIf(e -> e.team != -1);
		ArrayList<Actor> list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY, actuallyEnemies, pierces, this);
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


	public ArrayList<Attack> attacks = new ArrayList<>();
	protected void attackInput() {
		if ((float) sqrt(pow(targetActor.x - x,2) + pow(targetActor.y - y,2)) / globalSize() <= totalRange) {
			attacks.add(new Attack(targetActor.x, targetActor.y,this));
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
	}


	public final void onDeath(){
		if(health <= 0) {
			conditions.onDeath();
			if (lastDamager != null && lastDamager instanceof Actor)
				((Actor) lastDamager).onKill();
			triggerOnActorDeath(this);
			onDeathOverridable();
		}
	}

	public final void onKill(){
		conditions.onKill();
		onKillOverridable();
	}


	public void onKillOverridable(){}

	public void onDeathOverridable(){}

}
