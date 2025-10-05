package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.allaies.Summon;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TextureManager.animations;
import static java.lang.Float.POSITIVE_INFINITY;

public class Summoner extends CharacterClasses {

	public float summonRange = 3;
	public float[] summonLocation = new float[2];
	public static ArrayList<Summon> summons = new ArrayList<>();
	public boolean summonDecided = false;
	OnVariousScenarios oVS;

	public Summoner() {
		super();
		name = "Summoner";
		health = 40;
		damage = 20;
		speed = 5;
		attackSpeed = 6;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("Summon", "Summon", 5, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				cancelControl();
				character.cancelAttackMode();
				character.actions = new Actor.Actions(false);
				summonRange = 3;
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelDecision();
				endSummonSelector();
			}


		});

		abilities.add(new Ability("Control", "Control", -1, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				summonRange = 40;
				character.cancelAttackMode();
				character.actions = new Actor.Actions(false);
				cancelSummon();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.actions = null;
			}
		});

		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				summons.clear();
			}
		};

		reset();
		currentHealth = totalHealth;
	}

	public void updateOverridable() {
		summons.removeIf(Actor::getIsDead);
		for (Ability a : abilities) {
			a.render();
			a.touchActivate();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.U))
			abilities.get(0).keybindActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.C))
			abilities.get(1).keybindActivate();
		if(character.attackMode){
			cancelSummon();
			cancelControl();
		}

		if(abilities.get(0).isItActive)
			summonInput();

		if (character.isPermittedToAct() && summonDecided) {
			if(summons.size() < 5)
				summons.add(new Summon(summonLocation[0],summonLocation[1],20));
			else{
				boolean checker = false;
				for (Summon s : summons)
					if (summonLocation[0] == s.getX() && summonLocation[1] == s.getY() && !checker) {
						s.health = s.totalMaxHealth;
						checker = true;
					}
				if (!checker) {
					float weakest = POSITIVE_INFINITY;
					Summon weakestSummon = null;
					for (Summon s : summons)
						if (s.health < weakest) {
							weakest = s.health;
							weakestSummon = s;
						}
					weakestSummon.setX(summonLocation[0]);
					weakestSummon.setY(summonLocation[1]);
					weakestSummon.health = weakestSummon.totalMaxHealth;
				}
			}
			endSummonSelector();
			character.spendTurn();
		}

		if(abilities.get(1).isItActive){
			controlInput();
		}

		if(summons.size() >= 5)
			abilities.get(0).textureIcon = "ReSummon";
		else
			abilities.get(0).textureIcon = "Summon";
		
	}

	public void cancelSummon(){
		abilities.get(0).cancelActivation();
	}

	public void cancelControl(){
		abilities.get(1).cancelActivation();
	}


	public void endSummonSelector(){
		circle = null;
		animations.remove(target);
		target = null;
		character.actions = null;
		summonLocation = new float[2];
		summonDecided = false;
	}

	void controlInput(){
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				for(Summon s : summons){
					s.cancelDecision();
					s.setTarget(temporal.x,temporal.y);
				}
				cancelControl();
				cancelSummon();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.getX(), targetsTarget.getY()) != null && !(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY())) {
				for(Summon s : summons){
					s.cancelDecision();
					s.setTarget(targetsTarget.getX(), targetsTarget.getY());
				}
				cancelControl();
				cancelSummon();
			}
		}
	}


	protected void summonInput() {
		targetProcesor();
		if(summons.size() >= 5) {
			float weakest = POSITIVE_INFINITY;
			Summon weakestSummon = null;
			for (Summon s : summons)
				if (s.health < weakest) {
					weakest = s.health;
					weakestSummon = s;
				}
			addToList("Ball",weakestSummon.getX(),weakestSummon.getY() + globalSize()/4,1,0,240,25,25);
		}

		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
					summonLocation[0] = temporal.x;
					summonLocation[1] = temporal.y;
					character.actionDecided();
					abilities.get(0).finished();
					summonDecided = true;
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.getX(), targetsTarget.getY()) != null && !(targetsTarget.getX() == character.getX() && targetsTarget.getY() == character.getY())) {
				summonLocation[0] = targetsTarget.getX();
				summonLocation[1] = targetsTarget.getY();
				character.actionDecided();
				abilities.get(0).finished();
				summonDecided = true;
			}
		}
	}



	Entity targetsTarget = new Entity(null,character.getX(),character.getY(),false);
	TextureManager.Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(character.getX(),character.getY()) || circle.tileset != stage.tileset || circle.radius != summonRange || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(character.getX(), character.getY()), stage.tileset, summonRange, true,false);

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
			target = new Animation(abilities.get(0).isItActive ? "summontarget" : "summoncontrol", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new Animation(abilities.get(0).isItActive ? "summontarget" : "summoncontrol", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
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
}