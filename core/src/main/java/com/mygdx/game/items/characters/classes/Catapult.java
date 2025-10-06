package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.isDevMode;
import static com.mygdx.game.items.Actor.actorInPos;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;

public class Catapult extends CharacterClasses {

	public float chargeRange = 3;
	public float throwRange = 30;
	public ArrayList<Rock> rocks = new ArrayList<>();
	public float[] rocksCoords = new float[2];;
	public float[] chargeCoords = new float[2];;
	public boolean isCharged = false;
	public boolean willShoot = false;
	OnVariousScenarios oVS;

	public Catapult() {
		super();
		name = "Catapult";
		health = 30;
		damage = 20;
		speed = 3;
		attackSpeed = 1;
		defense = 0;
		range = 8;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("ChargeCatapult", "Charge the Catapult", 0, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				if(!isCharged) {
					isItActive = true;
					cancelRam();
					character.cancelAttackMode();
					character.actionDecided();
				}
				else
					text("Cannot charge the catapult as it's already charged!",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4 ,120, TextureManager.Fonts.ComicSans,32,200,200,40,1,30);

			}

			public void cancelActivation() {
				isItActive = false;
				character.cancelDecision();
				endTarget();
				chargeCoords = new float[2];
			}


		});

		abilities.add(new Ability("ChargeForwardCatapult", "Charge with Catapult", 5, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				character.cancelAttackMode();
				character.actions = new Actor.Actions(false);
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.actions = null;
				endTarget();
				chargeCoords = new float[2];
			}

			@Override
			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.actions = null;
			}
		});

		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				rocks.clear();
			}
		};

		reset();
		currentHealth = totalHealth;
	}

	public void onFinalizedTurn() {
		for(Rock r : rocks)
			r.advanceRock();
	}

	public void updateOverridable() {
		for (Ability a : abilities) {
			a.render();
			a.touchActivate();
		}
		rocks.removeIf(r -> r.finished);
		for(Rock r : rocks)
			r.update();
		if(chara.permittedToAct){
			if(willShoot && isCharged){
				isCharged = false;
				willShoot = false;
				rocks.add(new Rock(character.x,character.y,rocksCoords[0],rocksCoords[1],totalDamage*10));
				rocksCoords = new float[2];
				character.spendTurn();
			}
			if(abilities.get(0).isItActive) {
				isCharged = true;
				abilities.get(0).finished();
				character.spendTurn();
			}
			if(abilities.get(1).isItActive){
				ArrayList<Actor> chara = new ArrayList<>();
				chara.add(character);
				ArrayList<Actor> list = rayCasting(character.x, character.y, chargeCoords[0],chargeCoords[1],(chara),true,character);
				if(list != null)
					for(Actor l : list){
						l.conditions.status(Conditions.ConditionNames.SUBLIMATING);
					}
				character.glideAbsoluteCoords(chargeCoords[0],chargeCoords[1]);
				abilities.get(1).finished();
				character.spendTurn();
			}

		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.C))
			abilities.get(0).keybindActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.R))
			abilities.get(1).keybindActivate();


		if(character.attackMode && isCharged && isDecidingWhatToDo(character)) {
			rockThrowInput();
		}
		else if(character.attackMode){
			cancelRam();
		}

		if(abilities.get(1).isItActive && isDecidingWhatToDo(character)){
			chargeInput();
		}

		
	}

	public void cancelCharge(){
		abilities.get(0).cancelActivation();
	}

	public void cancelRam(){
		abilities.get(1).cancelActivation();
	}


	public void endTarget(){
		circle = null;
		animations.remove(target);
		target = null;
	}

	void chargeInput(){
		targetProcesor(true);
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				chargeCoords[0] = temporal.x;
				chargeCoords[1] = temporal.y;
				character.actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.getX(), targetsTarget.getY()) != null && !(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY())) {
				chargeCoords[0] = targetsTarget.getX();
				chargeCoords[1] = targetsTarget.getY();
				character.actionDecided();
			}
		}
	}


	protected void rockThrowInput() {
		targetProcesor(false);
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				rocksCoords[0] = temporal.x;
				rocksCoords[1] = temporal.y;
				willShoot = true;
				character.actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.getX(), targetsTarget.getY()) != null && !(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY())) {
				rocksCoords[0] = targetsTarget.getX();
				rocksCoords[1] = targetsTarget.getY();
				willShoot = true;
				character.actionDecided();
			}
		}
	}



	Entity targetsTarget = new Entity(null,character.getX(),character.getY(),false);
	Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(boolean charges){
		if (circle == null || circle.center != stage.findATile(character.getX(),character.getY()) || circle.tileset != stage.tileset || circle.radius != (charges ? chargeRange : throwRange) || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(character.getX(), character.getY()), stage.tileset, charges ? chargeRange : throwRange , true,false);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		mouseMoved = !(temporal.x == lastRecordedMousePos[0] && temporal.y == lastRecordedMousePos[1]);
		if (Gdx.input.justTouched())
			mouseMoved = true;
		lastRecordedMousePos[0] = temporal.x; lastRecordedMousePos[1] = temporal.y;
		if (circle.isInsideOfCircle(temporal.x, temporal.y)) {

			if (!mouseMoved)
				targetKeyboardMovement();

			if (!circle.isInsideOfCircle(targetsTarget.getX(), targetsTarget.getY()) || mouseMoved) {
				targetsTarget.setX(roundedClick().x);
				targetsTarget.setY(roundedClick().y);
			}

			targetRender();

		} else if (!mouseMoved){
			targetKeyboardMovement();
			if (!(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY()))
				targetRender();
		} else {
			animations.remove(target);
			target = null;
			targetsTarget.setX(character.getX());
			targetsTarget.setY(character.getY());
		}
	}


	private void targetRender(){
		if (target == null) {
			target = new Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new Animation("target" , targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
	}


	private void targetKeyboardMovement(){
		float x = targetsTarget.getX(); float y = targetsTarget.getY();
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			y += globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			x -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			y -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			x += globalSize();
		if(circle.isInsideOfCircle(x,y)) {
			targetsTarget.setX(x);
			targetsTarget.setY(y);
		} else if (circle.isInsideOfCircle(x, targetsTarget.getY()))
			targetsTarget.setX(x);
		else if (circle.isInsideOfCircle(targetsTarget.getX(),y))
			targetsTarget.setY(y);
	}


	@Override
	protected void destroyOverridable() {
		destroyListener(oVS);
	}


	public static class Rock extends Entity {
		float objectiveX, objectiveY,xPerTurn,yPerTurn;
		int turnsToFall;
		float damage;
		boolean finished;

		public Rock(float x,float y,float objectiveX, float objectiveY,float damage){
			super("Rock",x,y);
			this.damage = damage;
			this.objectiveX = objectiveX;
			this.objectiveY = objectiveY;
			double distance = dC(objectiveX,objectiveY)/globalSize();
			turnsToFall = distance <= 3 ? 4 : distance <= 6 ? 3 : distance <= 9  ? 2 : 1;
			xPerTurn = (objectiveX - x) / (1+turnsToFall);
			yPerTurn = (objectiveY - y) / (1+turnsToFall);
		}

		public void advanceRock(){
			glide(xPerTurn, yPerTurn);
			if(turnsToFall-- <= 0){
				if (actorInPos(objectiveX,objectiveY) != null)
					actorInPos(objectiveX,objectiveY).damage(damage, AttackTextProcessor.DamageReasons.RANGED,chara);
				finished  = true;
			}

		}

		public void update(){
			glideProcess();
			render();
		}

	}















}