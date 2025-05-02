package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Settings;

import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Stage.*;
import static com.mygdx.game.items.Turns.didTurnJustPass;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class Enemy extends Actor{
	public byte speed = 3;
	public float health = 20;
	public float defense = 5;

	boolean allowedToMove = false;
	char[] availableSpaces = new char[]{'N','N','N','N','N','N','N','N'};
	char move;

	boolean localFastMode;

	public Enemy(float x, float y, String texture, float health) {
		super(texture, x, y, 128, 128);
		speed = 3;
		actingSpeed = random(1, 7);
		this.health = health;
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		this.texture = texture;
		path = new Path(x,y,speed,null);
	}

	public Enemy(float x, float y) {
		super("EvilGuy",x,y,globalSize(),globalSize());
		this.x = x;
		this.y = y;
		testCollision.x = x;
		testCollision.y = y;
		path = new Path(x,y,speed,null);
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

	public void spendTurn(){
		print("st 23");
		allowedToMove = false;
		permittedToAct = false;
	}

	// Movement version: 5.0

	// LETS DO THIS, MASSIVE DELETION TO MAKE SPACE FOR: 6.0, now it pathfinds.
	// update, it doesnt pathfind cuz idk how to code ig


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



	public void update(Stage stage, ParticleManager pm){
		if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
			if(didTurnJustPass)
				canDecide[1] = true;
			gameScreenGetter(pm);
			super.speed = this.speed;
			if (pathFindAlgorithm == null)
				pathFindAlgorithm = new PathFinder(stage);
			path.getStats(x,y,speed);
			onDeath();
			movement();
			if (Gdx.input.isKeyPressed(Input.Keys.E)) {
				System.out.println("canDecide: " + canDecide());
				System.out.println("move: " + move);
				System.out.println("availableSpaces UP: " + availableSpaces[0] + " :availableSpaces UP-RIGHT: " + availableSpaces[1]);
				System.out.println("availableSpaces RIGHT: " + availableSpaces[2] + " :availableSpaces DOWN-RIGHT: " + availableSpaces[3]);
				System.out.println("availableSpaces DOWN: " + availableSpaces[4] + " :availableSpaces DOWN-LEFT: " + availableSpaces[5]);
				System.out.println("availableSpaces LEFT: " + availableSpaces[6] + " :availableSpaces UP-LEFT: " + availableSpaces[7]);
				System.out.println("actingSpeed left on x : " + speedLeft[0]);
				System.out.println("actingSpeed left on y : " + speedLeft[1]);
				System.out.println("onTurn: " + allowedToMove);
				System.out.println("x: " + x);
				System.out.println("testX: " + testCollision.x);
				System.out.println("y: " + y);
				System.out.println("testY: " + testCollision.y);
				System.out.println("-");
			}
		}
	}

	public void onDeath(){
		if (health <= 0) {
			isDead = true;
			permittedToAct = false;
		}
	}

	public void damage(float damage, String damageReason){
		health = health - max(damage - defense,0);
		if (Objects.equals(damageReason, "Melee")){
		pm.particleEmitter("BLOB",x + (float) globalSize() /2,y + (float) globalSize() /2,10);
		}
		print("remaining health is: " + health);

	}

	public boolean canDecide(){
		return canDecide[0] && canDecide[1];
	}

	public ParticleManager pm;
	public void gameScreenGetter(ParticleManager pm){ this.pm = pm; }
}
