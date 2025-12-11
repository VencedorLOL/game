package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.GameScreen.chara;
//import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.GameScreen.particle;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.ParticleManager.particleEmitter;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static com.mygdx.game.items.Turns.isTurnRunning;

public class Imp extends CharacterClasses {

	public TargetProcessor targetProcessor;

	public OnVariousScenarios oVSce;

	public int turnsRitual = 6;
	public int turnsMark = 6;
	public float[] markCoords;
	public int markRange = 6;
	public boolean diedMark = false;

	public Imp() {
		super();
		name = "Imp";
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

		if(ClassStoredInformation.Imp.getWeapon() != null)
			weapon = ClassStoredInformation.Imp.getWeapon();
		if(ClassStoredInformation.Imp.getShield() != null)
			shield = ClassStoredInformation.Imp.getShield();

		abilities.add(new Ability("Ritual", "Ritual", 12, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				cancelDemonize();
				isItActive = true;
				character.actionDecided();
				targetProcessor.reset();
				print("activated rutial");
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
				targetProcessor.reset();
				character.movementLock = true;
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				markCoords = null;
				targetProcessor.reset();
				character.movementLock = false;
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
					abilities.get(0).cancelActivation();
				if (abilities.get(1).isItActive)
					abilities.get(1).cancelActivation();
			}
		};
		if(ClassStoredInformation.Imp.getCooldown().length >= abilities.size())
			for(int i = 0; i < abilities.size(); i++)
				abilities.get(i).cooldown = ClassStoredInformation.Imp.getCooldown()[i];

		reset();
		currentHealth = totalHealth;
		manaPool = mana;
		targetProcessor = new TargetProcessor(character,markRange,false,false,"marktarget");
	}

	public void resetClassesState() {
		targetProcessor.reset();
		abilities.get(0).cancelActivation();
		abilities.get(1).cancelActivation();
		markCoords = null;
	}


	public void updateOverridable() {
		targetProcessor.changeRadius(markRange);
		if(abilities.get(1).isItActive && isDecidingWhatToDo(character))
			demonizeInput();

		abilitiesProcessor();
		if(character.attackMode){
			softCancelDemonize();
			cancelRitual();
		}

		if(diedMark && !isTurnRunning() && isDecidingWhatToDo(character)){
			if(abilities.get(0).cooldownCounter < abilities.get(0).cooldown)
				abilities.get(0).cooldownCounter += abilities.get(0).cooldownCounter > 6 ? 1 : abilities.get(0).cooldownCounter > 3 ? 2 : 3;
			if(abilities.get(1).cooldownCounter < abilities.get(1).cooldown)
				abilities.get(1).cooldownCounter += abilities.get(1).cooldownCounter > 6 ? 1 : abilities.get(1).cooldownCounter > 3 ? 2 : 3;
			diedMark = false;
		}


		if(abilities.get(0).isItActive && character.isPermittedToAct()){
			abilities.get(0).finished();
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
			abilities.get(1).finished();
			boolean gotActor = false;
			for(Actor a : actors){
				if(a.getX() == markCoords[0] && a.getY() == markCoords[1]) {
					gotActor = true;
					a.conditions.status(Conditions.ConditionNames.DEMONIZED);
					a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED).setTurns(turnsMark);
					particleEmitter("DEMONIZE",(float) globalSize() /2,
							(float) globalSize() /2,1, 1,true,false,60,a);
					((Conditions.Demonized) a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED)).getBeneficiary(this);
					break;
				}
			}
			if(!gotActor){
				particleEmitter("DEMONIZE",markCoords[0] + (float) globalSize() /2,
						markCoords[1] + (float) globalSize() /2,1, 1,true,false,60,null);
			}
			character.spendTurn();
		}


	}

	public void resetAbilities(){
		for (Ability a : abilities) {
			a.cooldownCounter = 0;
			a.isItActive = false;
			character.movementLock = false;
		}
	}

	public void softCancelDemonize(){
		abilities.get(1).isItActive = false;
		markCoords = null;
		character.movementLock = false;
	}

	public void cancelDemonize(){
		abilities.get(1).cancelActivation();
//		getCamara().smoothZoom(1,30);

	}

	public void cancelRitual(){
		abilities.get(0).cancelActivation();
	}


	public void destroyOverridable(){
		ClassStoredInformation.Imp.setShield(shield);
		ClassStoredInformation.Imp.setWeapon(weapon);
		ClassStoredInformation.Imp.setCooldown(getAbilitiesCd());
		destroyListener(oVSce);
	}


	protected void demonizeInput() {
		targetProcessor.render();
		if(Gdx.input.justTouched()) {
	//		getCamara().smoothZoom(1,30);
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
				markCoords = new float[]{temporal.x,temporal.y};
				character.actionDecided();
			}
		}
		if(actionConfirmJustPressed()) {
	//		getCamara().smoothZoom(1,30);
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetY() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				markCoords = new float[]{targetProcessor.getTargetX(),targetProcessor.getTargetY()};
				character.actionDecided();
			}
		}
	}


}