package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.allaies.Summon;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;
import static java.lang.Float.POSITIVE_INFINITY;

public class Summoner extends CharacterClasses {

	public float summonRange = 3;
	public float[] summonLocation = new float[2];
	public static ArrayList<Summon> summons = new ArrayList<>();
	public boolean summonDecided = false;
	OnVariousScenarios oVS;
	TargetProcessor targetProcessor;

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

		if(getClIns("Summoner").getWeapon(this) != null)
			equipWeapon(getClIns("Summoner").getWeapon(this));
		if(getClIns("Summoner").getShield(this) != null)
			equipShield(getClIns("Summoner").getShield(this));

		abilities.add(new Ability("Summon", "Summon", 5, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				cancelControl();
				character.cancelAttackMode();
				summonRange = 3;
				character.movementLock = true;
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.movementLock = false;
				endSummonSelector();
			}

			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
				targetProcessor.reset();
			}

		});

		abilities.add(new Ability("Control", "Control", -1, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				summonRange = 40;
				character.cancelAttackMode();
				cancelSummon();
				character.movementLock = true;
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.movementLock = false;
				getCamara().smoothZoom(1,30);
				endSummonSelector();
			}

			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
				endSummonSelector();
			}
		});

		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				summons.clear();
			}
		};

		if(getClIns("Summoner").getCooldown().length >= abilities.size())
			for(int i = 0; i < abilities.size(); i++)
				abilities.get(i).cooldown = getClIns("Summoner").getCooldown()[i];

		reset();
		currentHealth = totalHealth;
		targetProcessor = new TargetProcessor(character,summonRange,true,false,"summontarget");
	}

	public void resetClassesState() {
		targetProcessor.reset();
		abilities.get(0).cancelActivation();
		abilities.get(1).cancelActivation();
	}

	public void updateOverridable() {
		summons.removeIf(Actor::getIsDead);
		abilitiesProcessor();
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
		character.movementLock = false;
		summonLocation = new float[2];
		summonDecided = false;
		targetProcessor.reset();
	}

	void controlInput(){
		targetProcessor.changeAnimation("summoncontrol");
		targetProcessor.changeRadius(summonRange);
		targetProcessor.render();
		if(Gdx.input.justTouched()) {
			getCamara().smoothZoom(1,30);
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
				for(Summon s : summons){
					s.cancelDecision();
					s.setTarget(temporal.x,temporal.y);
				}
				cancelControl();
				cancelSummon();
			}
		}
		if(actionConfirmJustPressed()) {
			getCamara().smoothZoom(1,30);
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetY() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				for(Summon s : summons){
					s.cancelDecision();
					s.setTarget(targetProcessor.getTargetX(), targetProcessor.getTargetY());
				}
				cancelControl();
				cancelSummon();
			}
		}
	}


	protected void summonInput() {
		targetProcessor.changeAnimation("summontarget");
		targetProcessor.changeRadius(summonRange);
		targetProcessor.render();
		if(summons.size() >= 5) {
			float weakest = POSITIVE_INFINITY;
			Summon weakestSummon = null;
			for (Summon s : summons)
				if (s.health < weakest) {
					weakest = s.health;
					weakestSummon = s;
				}
			addToList("Ball",weakestSummon.getX(),weakestSummon.getY() + globalSize()/4f,1,0,240,25,25);
		}

		if(Gdx.input.justTouched()) {
			getCamara().smoothZoom(1,30);
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
					summonLocation[0] = temporal.x;
					summonLocation[1] = temporal.y;
					character.actionDecided();
					abilities.get(0).finished();
					summonDecided = true;
			}
		}
		if(actionConfirmJustPressed()) {
			getCamara().smoothZoom(1,30);
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetY() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				summonLocation[0] = targetProcessor.getTargetX();
				summonLocation[1] = targetProcessor.getTargetX();
				character.actionDecided();
				abilities.get(0).finished();
				summonDecided = true;
			}
		}
	}


	@Override
	protected void destroyOverridable() {
		getClIns("Summoner").setShield(shield);
		getClIns("Summoner").setWeapon(weapon);
		getClIns("Summoner").setCooldown(getAbilitiesCd());
		destroyListener(oVS);
	}
}