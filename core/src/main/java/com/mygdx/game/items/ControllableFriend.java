package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackIconRenderer.actorsThatAttack;
import static com.mygdx.game.items.Character.controllableCharacters;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;
import static com.mygdx.game.items.TurnManager.turnables;

public class ControllableFriend extends Friend {

	public boolean active = false;
	public boolean attackMode;
	public static OnVariousScenarios oVSc;
	public TargetProcessor targetProcessor;
	static {
		oVSc = new OnVariousScenarios(){
			public void onStageChange() {
				controllableCharacters.clear();
			}
		};
	}

	public float lastClickX, lastClickY;
	protected void automatedMovement(){
		if(leftClickReleased()){
			lastClickX = roundedClick().x;
			lastClickY = roundedClick().y;
			print("last ckik x " + lastClickX + " y " + lastClickY); 
			pathFinding();
		}
	}

	private void pathFinding(){
		path.pathReset();
		if (pathFindAlgorithm.quickSolve(x,y,lastClickX,lastClickY, getTakeEnemiesIntoConsideration()))
			path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
		else
			print("no path found");
	}



	public ControllableFriend(float x, float y, String texture, float health) {
		super(x, y,texture,health);
		controllableCharacters.add(this);
		targetProcessor = new TargetProcessor(this,totalRange,true,false,"target","notarget");
	}



	public void update(){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			controlOfCamara = active;
			statsUpdater();
			path.getStats(x,y,totalSpeed);
			onDeath();
			if (attackMode)
				attack();
			else
				movement();

			glideProcess();
			path.render();

			if(attackModeJustPressed() && active && isDecidingWhatToDo(this)) {
				if (turnMode) {
					targetProcessor.reset();
					attackMode = !attackMode;
					path.pathReset();
					if (!attackMode)
						cancelAttackMode();
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.C))
				print("color: r: " + color[0] + ", g: " + color[1] + ", b: " + color[2] );
			renderBall();
			conditions.render();
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
					conditions.onMove();
				}

			} else if (isDecidingWhatToDo(this) && speedLeft[0] == 0 && speedLeft[1] == 0 && !movementLock && active)
				movementInputTurnMode();

		} else {
			speedActuator();
		}
	}

	public void renderBall(){
		if(active)
			addToList("Ball",x ,y  + height/2 + globalSize()/4f,1,0,color[0],color[1],color[2]);
	}

	public void actionDecided(){
		thisTurnVSM = getVisualSpeedMultiplier();
		TurnManager.finalizedChoosing(this);
		active = false;
	}

	public void attack(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct())
			attackActuator();

		else if (isDecidingWhatToDo(this) && active)
			attackInput();
	}


	protected void attackInput() {
		targetProcessor.changeRadius(totalRange);
		targetProcessor.render();
		if(actionConfirmJustPressed() || leftClickReleased()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(),targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetX() == x && targetProcessor.getTargetY() == y)) {
				attacks.add(new Attack(targetProcessor.getTargetX(), targetProcessor.getTargetY(),this));
//				if (classes.runOnAttackDecided())
					actionDecided();
			} else if (targetProcessor.getTargetX() == x && targetProcessor.getTargetY() == y)
				cancelAttackMode();
		}
	}


	public void cancelAttackMode(){
		attackMode = false;
		if (targetProcessor.circle != null)
			targetProcessor.deleteTexture();
		targetProcessor.reset();
		attacks.clear();
	}

	public void attackDetector(){
		ArrayList<Actor> temp = new ArrayList<>(friend);
		temp.add(this);
		ArrayList<Actor> list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY,temp, pierces,this);
		if (list != null)
			for (Actor aa : list) {
				aa.damage(totalDamage, AttackTextProcessor.DamageReasons.MELEE,this);
				if (!pierces)
					break;
			}
		else
			text("Missed!", attacks.get(elementOfAttack -  1).targetX,attacks.get(elementOfAttack -  1).targetY + 240,60, Fonts.ComicSans,40,127,127,127,1,30);
		attacks.get(elementOfAttack - 1).render = false;
	}

	public void onDeathOverridable(){
		if (health <= 0) {
			animationToList("dying",x,y);
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			entityList.remove(this);
			controllableCharacters.remove(this);
			actorsThatAttack.remove(this);
			turnables.remove(this);
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
