package com.mygdx.game.items.allaies;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.Friend;
import com.mygdx.game.items.Tile;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.Stage.haveScreenWarpsBeenRendered;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Summon extends Friend {

	boolean isThereAnAliveSetTarget = false;
	Tile targetTile;

	public Summon(float x, float y, float health) {
		super(x, y, "ally", health);
		damage = 15;
		speed = 7;
		actingSpeed = 7;
		defense = 3;
	}


	protected void automatedMovement(){
		if(targetActor != null && targetActor.getIsDead()) {
			isThereAnAliveSetTarget = false;
			targetActor = null;
		}
		if(!isThereAnAliveSetTarget)
			targetFinder();
		if (targetActor != null && totalFollowRange * globalSize() > dC(targetActor.getX(), targetActor.getY())) {
			path.pathReset();
			if (pathFindAlgorithm.quickSolve(getX(), getY(), targetActor.getX(), targetActor.getY(), getTakeEnemiesIntoConsideration()))
				path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
			return;
		}
		if (targetTile != null) {
			path.pathReset();
			if (pathFindAlgorithm.quickSolve(getX(), getY(), targetTile.x(), targetTile.y(), getTakeEnemiesIntoConsideration()))
				path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
			return;
		}

		actionDecided();
	}

	public void update(){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !this.getIsDead()) {
			statsUpdater();
			path.getStats(this.getX(),this.getY(),totalSpeed);
			loop();
			onDeath();
			if (targetActor != null && !targetActor.getIsDead() && ((targetActor.totalTeam == -totalTeam && (float) sqrt(pow(targetActor.getX() - this.getX(),2) + pow(targetActor.getY() - this.getY(),2)) / globalSize() <= totalRange && speedLeft[0] == 0 && speedLeft[1] == 0) || !attacks.isEmpty()) && (!attacks.isEmpty() || !permittedToAct))
				attack();
			else
				movement();
			glideProcess();

//	*		if (!isDecidingWhatToDo(this) && !isTurnRunning() && !path.isListSizeOne())
//				path.renderLastStep();
		}
	}

	public void setTarget(float x, float y){
		for (Actor a: actors){
			if(a.getX() == x && a.getY() == y && dC(a.getX(),a.getY()) <= totalSightRange * globalSize()){
				if(a == chara) {
					targetTile = null;
					targetActor = null;
					isThereAnAliveSetTarget = false;
					return;
				}
				targetActor = a;
				targetTile = null;
				isThereAnAliveSetTarget = true;
				return;
			}
		}
		for(Tile t : stage.tileset){
			if(t.x() == x && t.y() == y){
				targetTile = t;
				targetActor = null;
				isThereAnAliveSetTarget = true;
				return;
			}
		}
	}

}
