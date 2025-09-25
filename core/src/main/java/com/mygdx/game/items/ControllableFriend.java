package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Character.controllableCharacters;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class ControllableFriend extends Friend {

	public boolean active = false;
	public boolean attackMode;


	public float lastClickX, lastClickY;
	protected void automatedMovement(){
		if(Gdx.input.justTouched()){
			lastClickX = roundedClick().x;
			lastClickY = roundedClick().y;
			print("last ckik x " + lastClickX + " y " + lastClickY);
			pathFinding();
		}
//		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
//			pathFinding();
//		}
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
	}



	public void update(){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			path.getStats(x,y,speed);
			onDeath();
			if (attackMode)
				attack();
			else
				movement();

			attackRenderer();
			path.render();

			if(Gdx.input.isKeyJustPressed(Input.Keys.T) && isDecidingWhatToDo(this)) {
				if (turnMode) {
					attackMode = !attackMode;
					path.pathReset();
					if (!attackMode)
						cancelAttackMode();
					mouseMoved = true;
				}
			}


			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				print("");
				print("Myself: " + this);
				print("Target: x" + tileToReach[0] + " y" + tileToReach[1]);
				print("Coords: x" + x + " y" + y);
				print("ASpeed: " + actingSpeed);
				print("Damage: " + damage);
				print("Defense: " + defense);
				print("am i dead? " + isDead);
				print("active?: " + active);
				print("");
			}
		}
	}

	public void movement(){
		lastTimeTilLastMovement++;
		testCollision.x = x;
		testCollision.y = y;
		glideProcess();
		if (turnMode) {
			if (isPermittedToAct()) {
				lastTimeTilLastMovement = 0;
				if (speedLeft[0] == 0 && speedLeft[1] == 0 && !path.pathEnded)
					speedLeft = path.pathProcess();

				if (speedLeft[0] != 0 || speedLeft[1] != 0)
					turnSpeedActuator();

				if (speedLeft[0] == 0 && speedLeft[1] == 0 && path.pathEnded) {
					softlockOverridable();
					finalizedTurn();
					conditions.onMove();
				}

			} else if (isDecidingWhatToDo(this) && speedLeft[0] == 0 && speedLeft[1] == 0 && actions == null && active)
				movementInputTurnMode();

		} else {
			speedActuator();
		}
	}

	public void actionDecided(){
		thisTurnVSM = getVisualSpeedMultiplier();
		Turns.finalizedChoosing(this);
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

	public void attackRenderer(){
		for (int i = 0; i < attacks.size(); i++){
			if(attacks.get(i).render) {
				byte counter = 0;
				for (int j = i; j < attacks.size(); j++) {
					if (attacks.get(j).targetX == attacks.get(i).targetX && attacks.get(j).targetY == attacks.get(i).targetY && attacks.get(j) != attacks.get(i))
						counter++;
				}
				addToList("attackIndicator", attacks.get(i).targetX - 5 * counter, attacks.get(i).targetY - 5 *counter, 1,
						0, 256, attacks.get(i).isBeingExecuted ? 20 : 256, attacks.get(i).isBeingExecuted ? 68 : 256);
			}
		}
	}

	protected void attackInput() {
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				attacks.add(new Attack(temporal.x, temporal.y));
//				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.x,targetsTarget.y) != null && !(targetsTarget.x == x && targetsTarget.y == y)) {
				attacks.add(new Attack(targetsTarget.x, targetsTarget.y));
//				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
	}

	Entity targetsTarget = new Entity("default",x,y,false);
	TextureManager.Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(x,y) || circle.tileset != stage.tileset || circle.radius != range || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 9; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(x, y), stage.tileset, range, true,false);

		}
		Vector3 temporal = roundedClick();
		mouseMoved = !(temporal.x == lastRecordedMousePos[0] && temporal.y == lastRecordedMousePos[1]);
		if (Gdx.input.justTouched())
			mouseMoved = true;
		lastRecordedMousePos[0] = temporal.x; lastRecordedMousePos[1] = temporal.y;
		if (circle.isInsideOfCircle(temporal.x, temporal.y)) {

			if (!mouseMoved)
				targetKeyboardMovement();

			if (!circle.isInsideOfCircle(targetsTarget.x, targetsTarget.y) || mouseMoved) {
				targetsTarget.x = roundedClick().x;
				targetsTarget.y = roundedClick().y;
			}

			targetRender();

		} else if (!mouseMoved){
			targetKeyboardMovement();
			if (!(targetsTarget.x == x && targetsTarget.y == y))
				targetRender();
		} else {
			animations.remove(target);
			target = null;
			targetsTarget.x = x;
			targetsTarget.y = y;
		}
	}

	private void targetRender(){
		if (target == null) {
			target = new TextureManager.Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new TextureManager.Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
	}


	private void targetKeyboardMovement(){
		float x = targetsTarget.x; float y = targetsTarget.y;
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			y += globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			x -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			y -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			x += globalSize();
		if(circle.isInsideOfCircle(x,y)) {
			targetsTarget.x = x;
			targetsTarget.y = y;
		} else if (circle.isInsideOfCircle(x,targetsTarget.y))
			targetsTarget.x = x;
		else if (circle.isInsideOfCircle(targetsTarget.x,y))
			targetsTarget.y = y;
	}


	public void cancelAttackMode(){
		attackMode = false;
		if (circle != null)
			for (Tile t : circle.circle)
				for (int i = 0; i < 9; i++)
					t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
		circle = null;
		animations.remove(target);
		target = null;
		attacks.clear();
	}

	public void attackDetector(){
		ArrayList<Actor> temp = new ArrayList<>(friend);
		temp.add(this);
		ArrayList<Actor> list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY,temp, pierces,this);
		if (list != null)
			for (Actor aa : list) {
				aa.damage(damage, AttackTextProcessor.DamageReasons.MELEE);
				if (!pierces)
					break;
			}
		else
			text("Missed!", attacks.get(elementOfAttack -  1).targetX,attacks.get(elementOfAttack -  1).targetY + 140,60, Fonts.ComicSans,40,127,127,127,1,30);
		attacks.get(elementOfAttack - 1).render = false;
	}

	public void onDeath(){
		if (health <= 0) {
			animationToList("dying",x,y);
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			entityList.remove(this);
			controllableCharacters.remove(this);
		}
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
