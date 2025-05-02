package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.ceil;

public class Actor extends Entity{

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

	boolean isDead;

	public boolean getIsDead() { return isDead; }

	public Actor(String aChar, float x, float y, float base, float height) {
		super(aChar,x,y,base,height);
	}

	public Actor() {
		super();
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
		testCollision.x = x;
		testCollision.y = y;
		if (turnMode) {
			if (isPermittedToAct()) {
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
		}

		super.refresh(texture,x, y, base, height);
	}

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
		//failsafe!! Should any actor need any different behaviour override this!
		if(overlapsWithStage(stage,this))
			overrideSpawnIfFail();

	}

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
		if (path.pathCreate(x,y, speed,stage, (byte) 1)) {
			canDecide = new boolean[] {false, false};
			thisTurnVSM = getVisualSpeedMultiplier();
			actionDecided();
		}
	}


	protected void automatedMovement(){
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

	private void actionDecided(){
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
		if (speedLeft[0] == 0 && speedLeft[1] == 0) {
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
	}



}
