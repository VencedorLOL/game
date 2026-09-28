package com.mygdx.game.items;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.Collections;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackIconRenderer.actorsThatAttack;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.InputHandler.attackModeJustPressed;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.Tile.findATile;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;
import static com.mygdx.game.items.TurnManager.turnables;
import static java.lang.Math.*;

public class Friend extends Actor {
	public int[] color;

	{
		aggro = 1;
		team = 1;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		path = new Path(x,y,speed,this);
		permittedToAct = false;
		friend.add(this);
		actorsThatAttack.add(this);
		if(color == null)
			color = new int[]{random(0, 255), random(0, 255), random(0, 255)};
		ArrayList<DamageReceiver> list = new ArrayList<>(friend);
		list.add(chara);
		exclusionList = list;
	}

	public Friend(float x, float y, String texture, float health) {
		super(texture, x, y, globalSize(), globalSize());
		pierces = false;
		speed = 3;
		range = 2;
		damage = 20;
		actingSpeed = random(1, 7);
		print("acting speed of this friend is of " + totalActingSpeed);
		this.maxHealth = health;
		this.health = health;
		this.texture = texture;
	}

	@SuppressWarnings("all")
	public Friend(float x, float y) {
		super("animaWithMustacheAndSurprisedWtfDidIJustDo",x,y,globalSize(),globalSize());
	}

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


	private float gridSetter(float coordinate){
		return (float) (globalSize() * round(coordinate / globalSize()));
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
			if(!isControllable){
				statsUpdater();
				path.getStats(x, y, totalSpeed);
				loop();
				onDeath();
				if (isDead)
					return;
				if ((targetActor == null || targetActor.getIsDead() || targetActor.totalTeam() != -totalTeam) && turnMode && isDecidingWhatToDo(this))
					targetFinder();
				if (targetActor != null && !targetActor.getIsDead() && ((targetActor.totalTeam() == -totalTeam && (float) sqrt(pow(targetActor.getX() - x, 2) + pow(targetActor.getY() - y, 2)) / globalSize() <= totalRange && speedLeft[0] == 0 && speedLeft[1] == 0) || !attacks.isEmpty()) && (!attacks.isEmpty() || !permittedToAct) && attackHitsTarget())
					attack();
				else
					movement();
				conditions.render();
				glideProcess();
				print("ran friend non controlalble");
//*			if (!isDecidingWhatToDo(this) && !isTurnRunning() && !path.isListSizeOne())
//				path.renderLastStep();}
			} else {
				controlOfCamara = active;
				statsUpdater();
				path.getStats(x,y,totalSpeed);
				onDeath();
				if (attackMode)
					attack();
				else
					movement();

				glideProcess();
				path.render(active);

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
	}

	public void renderBall(){
		if(active)
			addToList("Ball",x ,y  + height/2 + globalSize()/4f,1,0,color[0],color[1],color[2]);
	}


	public void onDeathOverridable(){
		if (health <= 0) {
			animationToList("dying",x,y);
			isDead = true;
			permittedToAct = false;
			actors.remove(this);
			entityList.remove(this);
			actorsThatAttack.remove(this);
			turnables.remove(this);
			damageReceivers.remove(this);
		}
	}

	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){
		float damagedFor = getDamagedFor(damage,damageReason);
		health -= damagedFor;
		if (damageReason == AttackTextProcessor.DamageReasons.MELEE && damagedFor != 0){
			ParticleManager.particleEmitter("BLOB",globalSize()/2f, globalSize()/2f,1,40,true,false,10,this);
		}
		AttackTextProcessor.addAttackText(damagedFor,damageReason,this);
		print("remaining health is: " + health);
		printErr("damaged for " + damagedFor + " damage");

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
