package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.allaies.Summon;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static com.mygdx.game.items.Turns.isTurnRunning;
import static java.lang.Float.POSITIVE_INFINITY;

public class Imp extends CharacterClasses {

	public OnVariousScenarios oVSce;

	public int turnsRitual = 6;
	public int turnsMark = 6;
	public float[] markCoords;
	public int markRange = 6;
	public boolean diedMark = false;

	public Imp() {
		super();
		name = "Ritual";
		health = 30;
		damage = 40;
		speed = 3;
		attackSpeed = 7;
		defense = 0;
		range = 3;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("Ritual", "Ritual", 12, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				cancelDemonize();
				isItActive = true;
				character.actionDecided();
				deleteTarget();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelDecision();
			}

			@Override
			public void finished() {
				resetAbilities();
			}
		});

		abilities.add(new Ability("Demon", "Demonize", 12, 87	,30, (float) globalSize() /2){
			@Override
			public void active() {
				cancelRitual();
				isItActive = true;
				chara.cancelAttackMode();
				deleteTarget();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				markCoords = null;
				deleteTarget();
			}

			@Override
			public void finished() {
				resetAbilities();
			}
		});


		oVSce = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				if (abilities.get(0).isItActive)
					abilities.get(0).finished();
				if (abilities.get(1).isItActive)
					abilities.get(1).finished();
			}
		};
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
	}


	public void updateOverridable() {
		for (Ability a : abilities) {
			a.render();
			a.touchActivate();
		}
		if(character.attackMode){
			cancelDemonize();
			cancelRitual();
		}

		if(diedMark && !isTurnRunning() && isDecidingWhatToDo(character)){
			if(abilities.get(0).cooldownCounter < abilities.get(0).cooldown)
				abilities.get(0).cooldownCounter += abilities.get(0).cooldownCounter > 6 ? 1 : abilities.get(0).cooldownCounter > 3 ? 2 : 3;
			if(abilities.get(1).cooldownCounter < abilities.get(1).cooldown)
				abilities.get(1).cooldownCounter += abilities.get(1).cooldownCounter > 6 ? 1 : abilities.get(1).cooldownCounter > 3 ? 2 : 3;
			diedMark = false;
		}

		if(abilities.get(1).isItActive && isDecidingWhatToDo(character)){
			demonizeInput();
		}

		if(abilities.get(0).isItActive && character.isPermittedToAct()){
			character.conditions.status(Conditions.ConditionNames.RITUAL);
			character.conditions.getStatus(Conditions.ConditionNames.RITUAL).setTurns(turnsRitual);
			((Conditions.Ritual) character.conditions.getStatus(Conditions.ConditionNames.RITUAL)).setExtraTurnsLimit(6);
			for(Friend f : friend){
				f.conditions.status(Conditions.ConditionNames.RITUAL);
				f.conditions.getStatus(Conditions.ConditionNames.RITUAL).setTurns(turnsRitual);
				((Conditions.Ritual) f.conditions.getStatus(Conditions.ConditionNames.RITUAL)).setExtraTurnsLimit(6);
			}
			character.spendTurn();
		}

		if(abilities.get(1).isItActive && character.isPermittedToAct() && markCoords != null){
			for(Actor a : actors){
				if(a.getX() == markCoords[0] && a.getY() == markCoords[1]) {
					a.conditions.status(Conditions.ConditionNames.DEMONIZED);
					a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED).setTurns(turnsMark);
					((Conditions.Demonized) a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED)).getBeneficiary(this);
					break;
				}
			}
			character.spendTurn();
		}

		if (actionResetJustPressed() && isDecidingWhatToDo(character)) {
			abilities.get(0).keybindActivate();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.M) && isDecidingWhatToDo(character)) {
			abilities.get(1).keybindActivate();
		}
	}

	public void resetAbilities(){
		for (Ability a : abilities) {
			a.cooldownCounter = 0;
			a.isItActive = false;
		}
	}

	public void cancelDemonize(){
		abilities.get(1).cancelActivation();

	}

	public void cancelRitual(){
		abilities.get(0).cancelActivation();
	}


	public void destroyOverridable(){
		destroyListener(oVSce);
	}


	protected void demonizeInput() {
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				markCoords = new float[]{temporal.x,temporal.y};
				character.actionDecided();
			}
		}
		if(actionConfirmJustPressed()) {
			if (circle.findATile(targetsTarget.getX(), targetsTarget.getY()) != null && !(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY())) {
				markCoords = new float[]{targetsTarget.getX(),targetsTarget.getY()};
				character.actionDecided();
			}
		}
	}

	public void deleteTarget(){
		circle = null;
		animations.remove(target);
		target = null;
	}


	Entity targetsTarget = new Entity(null,character.getX(),character.getY(),false);
	TextureManager.Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(character.getX(),character.getY()) || circle.tileset != stage.tileset || circle.radius != markRange || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(character.getX(), character.getY()), stage.tileset, markRange, true,false);

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
			target = new TextureManager.Animation("marktarget" , targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || actionConfirmJustPressed()) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new TextureManager.Animation("marktarget" , targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || actionConfirmJustPressed()) this.stop();}};
			animations.add(target);
		}
	}


	private void targetKeyboardMovement(){
		float x = targetsTarget.getX(); float y = targetsTarget.getY();
		byte counter = directionalBuffer();
		if (counter % 2 != 0)
			x += globalSize();
		if(counter - 8 >= 0)
			x -= globalSize();
		if((counter & (1<<2)) != 0)
			y += globalSize();
		if((counter & (1<<1)) != 0)
			y -= globalSize();
		if(circle.isInsideOfCircle(x,y)) {
			targetsTarget.setX(x);
			targetsTarget.setY(y);
		} else if (circle.isInsideOfCircle(x, targetsTarget.getY()))
			targetsTarget.setX(x);
		else if (circle.isInsideOfCircle(targetsTarget.getX(),y))
			targetsTarget.setY(y);
	}


}