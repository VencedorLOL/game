package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;

public class Melee extends CharacterClasses {

	public byte attackState;
	public final byte FoANumberOfExtraHits = 4;
	public byte OfAMultiplier = 6;
	public OnVariousScenarios oVSce;

	public Melee() {
		super();
		name = "Melee";
		health = 40;
		damage = 40;
		speed = 3;
		attackSpeed = 2;
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

		abilities.add(new Ability("flurryofhits", "Flurry Of Attacks", 4, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				cancelOfA();
				character.attackMode = true;
				isItActive = true;
				character.path.pathReset();

			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelAttackMode();
				character.conditions.remove(Conditions.ConditionNames.ONE_FOR_ALL);
				character.path.pathReset();
			}
		});
		abilities.add(new Ability("oneforall", "One For All", 4, 87f, 30f, (float) globalSize() /2){
			@Override
			public void active() {
				cancelFoA();
				character.attackMode = true;
				isItActive = true;
				character.conditions.status(Conditions.ConditionNames.ONE_FOR_ALL);
				character.path.pathReset();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelAttackMode();
				character.conditions.remove(Conditions.ConditionNames.ONE_FOR_ALL);
				character.path.pathReset();
			}

			public void finished(){
				resetAbilities();
			}


		});

		oVSce = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				if (abilities.get(0).isItActive || abilities.get(1).isItActive)
					resetAbilities();
			}
		};

		getEquipment();
		reset();

		if(getClIns("Melee").getCooldown().length >= abilities.size())
			for(int i = 0; i < abilities.size(); i++)
				abilities.get(i).cooldownCounter = getClIns("Melee").getCooldown()[i];

		currentHealth = totalHealth;
		manaPool = mana;
	}

	public void resetClassesState() {
		abilities.get(0).cancelActivation();
		abilities.get(1).cancelActivation();
		attackState = 0;
	}

	public void updateOverridable() {
		abilitiesProcessor();

		if (!character.attackMode && (abilities.get(0).isItActive || abilities.get(1).isItActive))
			for (Ability a : abilities)
				a.cancelActivation();

	}

	public void cancelFoA(){
		abilities.get(0).cancelActivation();
		attackState = 0;
		character.attacks.clear();
		character.conditions.remove(Conditions.ConditionNames.ONE_FOR_ALL);
	}

	public void cancelOfA(){
		abilities.get(1).cancelActivation();
		attackState = 0;
		character.attacks.clear();
		character.conditions.remove(Conditions.ConditionNames.ONE_FOR_ALL);
	}


	public void resetAbilities(){
		for (Ability a : abilities) {
			a.cooldownCounter = 0;
			a.isItActive = false;
		}
		attackState = 0;
	}


	@Override
	public boolean onAttackDecided() {
		if (abilities.get(0).isItActive){
			attackState++;
			return attackState >= FoANumberOfExtraHits;
		}
		return true;
	}


	public void destroyOverridable(){
		destroyListener(oVSce);
	}


}