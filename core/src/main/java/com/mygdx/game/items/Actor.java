package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Actor extends Entity{

	public float maxHealth;
	byte speed;

	public byte team;
	// -1 = evil
	// 0 = neutral
	// 1 = good
	public int actingSpeed;
	int[] speedLeft = new int[2];
	Path path;
	int thisTurnVSM;
	PathFinder pathFindAlgorithm;
	public Entity testCollision = new Entity();
	public boolean[] canDecide = {true, true};
	public boolean permittedToAct;
	public boolean turnMode = true;
	public int range;
	public float aggro = 1;

	boolean isDead;

	public Actor target;

	public static ArrayList<Actor> actors = new ArrayList<>();

	public float lastTimeTilLastMovement = 0;

	public byte textureOrientation;

	public boolean didItAct = false;

	public boolean didItAct(){return didItAct;}
	public void setDidItAct(boolean didItAct) {this.didItAct = didItAct;}

	OnVariousScenarios oVS = new OnVariousScenarios(){
		@Override
		public void onTickStart() {
			alreadyTextured = false;
		}

		@Override
		public void onStageChange() {
			didItAct = false;
			permittedToAct = false;
		}
	};

	public boolean getIsDead() { return isDead; }

	public Actor(String aChar, float x, float y, float base, float height) {
		super(aChar,x,y,base,height);
		actors.add(this);
	}

	public Actor() {
		super();
		actors.add(this);
	}

	public void damage(float damage, String damageReason){}

	//Override
	public boolean isPermittedToAct(){return true;}
	//Override
	public void permitToMove(){}


	public boolean overlapsWithStage(Stage stage, Entity tester){
		for (Wall b : stage.walls){
			if (tester.overlaps(b))
				return true;
		}
		for (Enemy e : stage.enemy) {
			if (tester != e.testCollision && !e.isDead) {
				if (tester.overlaps(e))
					return true;
				if (tester.overlaps(e.testCollision))
					return true;

			}
		}
		return  chara.x == tester.x && chara.y == tester.y && tester != chara ||
				stage.finalY >= 0 ? tester.y >= stage.finalY + 1 : tester.y <= stage.finalY - 1 ||
				stage.startY >= 0 ? tester.y <= stage.startY - 1 : tester.y >= stage.startY + 1 ||
				stage.finalX >= 0 ? tester.x >= stage.finalX + 1 : tester.x <= stage.finalX - 1 ||
				stage.startX >= 0 ? tester.x <= stage.startX - 1 : tester.x >= stage.startX + 1 ;
	}


	//Movement!! now here cuz convenience

	public void movement(){
		lastTimeTilLastMovement++;
		testCollision.x = x;
		testCollision.y = y;
		glideProcess();
		if (turnMode) {
			if (isPermittedToAct()) {
				lastTimeTilLastMovement = 0;
				if (speedLeft[0] == 0 && speedLeft[1] == 0 && !path.pathEnded)
					speedLeft = path.pathProcess(this);


				if (speedLeft[0] != 0 || speedLeft[1] != 0)
					turnSpeedActuator();


				if (speedLeft[0] == 0 && speedLeft[1] == 0 && path.pathEnded)
					finalizedMove();


			} else if (canDecide() && isDecidingWhatToDo(this))
				movementInputTurnMode();

		} else {
			movementInputManual();
			speedActuator();
			if (this instanceof Enemy){
				enemyOnFreeMode();
			}
		}

		super.refresh(texture,x, y, base, height);
	}

	protected void enemyOnFreeMode(){}

	protected void speedActuator(){
		if (speedLeft[0] > 0) {
			testCollision.x += thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				x += thisTurnVSM;
			speedLeft[0] -= thisTurnVSM;
		}
		else if (speedLeft[0] < 0) {
			testCollision.x -= thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				x -= thisTurnVSM;
			speedLeft[0] += thisTurnVSM;
		}
		testCollision.x = x;
		if (speedLeft[1] > 0) {
			testCollision.y += thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				y += thisTurnVSM;
			speedLeft[1] -= thisTurnVSM;
		}
		else if (speedLeft[1] < 0) {
			testCollision.y -= thisTurnVSM;
			if (!overlapsWithStage(stage,testCollision))
				y -= thisTurnVSM;
			speedLeft[1] += thisTurnVSM;
		}


		//failsafe!!
		if(overlapsWithStage(stage,this))
			overrideSpawnIfFail();

	}
	// should any actor need any different overlaping behaviour override this!
	public void overrideSpawnIfFail(){
		x = stage.startX; y = stage.startY;
	}

	protected void turnSpeedActuator(){
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

	protected void movementInputTurnMode(){
		automatedMovement();
		if (path.pathCreate(x,y, speed, (byte) 1)) {
			canDecide = new boolean[] {false, false};
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
	}


	protected void automatedMovement(){
	//	if(pathFindAlgorithm == null)
	//		pathFindAlgorithm = new PathFinder(stage);
	//	path.pathReset();
	//	PathFinder.reset(stage);
	//	pathFindAlgorithm.setStart(x,y);
	//	pathFindAlgorithm.setPlayerAsEnd();
	//	pathFindAlgorithm.solve();
	//	if (pathFindAlgorithm.algorithm.getPath() != null){
	//		path.setPathTo(pathFindAlgorithm.getSolvedPath());
	//	} else
			print("no path found");
	}

	protected void actionDecided(){
		Turns.actorsFinalizedChoosing(this);
	}

	public void finalizedMove(){
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		canDecide[0] = true;
		spendTurn();
	}

	public void spendTurn(){
		printErr("Called spendTurn on + " + this);
		permittedToAct = false;
	}


	protected void isOnTheGrid(){
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && !isGliding) {
			float x = 0, y = 0;
			if (!(this.x % globalSize() == 0)) {
				print("Offset in x caused at: " + this.x + " :by: " + this + " :New x is: " + 128 * round(this.x / 128));
				x = (float) (globalSize() * round(this.x / globalSize())) - this.x;
			}
			if (!(this.y % globalSize() == 0)) {
				print("Offset in y caused at: " + this.y + " :by: " + this + " :New y is: " + 128 * round(this.y / 128));
				y = (float) (globalSize() * round(this.y / globalSize())) - this.y;
			}
			if (x !=0 || y != 0)
				glide(x,y);
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


	public boolean canDecide(){
		return canDecide[0] && canDecide[1];
	}

//FIXME: revisit when proper key handlin
	public void movementInputManual(){
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			speedLeft[1] += globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			speedLeft[0] -= globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			speedLeft[1] -= globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			speedLeft[0] += globalSize()/16;
		if (speedLeft[0] != 0 || speedLeft[1] != 0)
			lastTimeTilLastMovement = 0;

	}


	private float glideXPerFrame, glideYPerFrame;
	public boolean isGliding = false;
	public float glideTime;
	public void glide(float x, float y, float time){
		if (!isGliding){
			isGliding = true;
			glideTime = time;
			glideXPerFrame = x / time;
			glideYPerFrame = y / time;
		} else
			printErr("ERROR: ALREADY GLIDING");
	}

	public void glide(float x, float y){
		glide(x,y,(float)sqrt(pow(x,2)+pow(y,2))/2);
	}

	public void glideProcess(){
		if (isGliding)
			if (glideTime-- <= 0) {
				isGliding = false;
				if (turnMode)
					isOnTheGridForced();
			}
			else {
				x += glideXPerFrame;
				y += glideYPerFrame;
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
			alreadyTextured = true;
		}
	}


	public static void flushActorListButCharacter(){
		actors.clear();
		actors.add(chara);
	}


	public void targetFinder(){
		ArrayList<ActorAndDistance> targets = new ArrayList<>();
		for (Actor a : actors)
			if (!a.isDead && a.team == team*-1)
				targets.add(new ActorAndDistance(a,dC(a.x,a.y)*a.aggro));
		Collections.shuffle(targets);
		targets.sort((o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));
		//when pathfinding is implemented, add an if check to check if the entity can be reachable
		for (ActorAndDistance a : targets){
			//if (pathfinding.canReachThereOrSum())
			target = a.actor;
			return;
		}
	}

	public double dC(float x, float y){
		return sqrt(pow(abs(x)-abs(this.x),2)+pow(abs(y)-abs(this.y),2));
	}

	private static class ActorAndDistance{
		private Actor actor;
		private double distance;

		private ActorAndDistance(Actor actor, double distance){
			this.actor = actor;
			this.distance = distance;
		}

		private Actor getActor(){return actor;}
		private double getDistance() {return distance;}

		private void setActor(Actor actor){this.actor = actor;}
		private void setDistance(double distance){this.distance = distance;}
	}




}
