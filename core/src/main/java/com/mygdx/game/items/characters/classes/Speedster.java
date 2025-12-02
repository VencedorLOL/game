package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;

public class Speedster extends CharacterClasses {

	public byte attackState;
	public final byte EvenFasterNumberOfExtraHits = 7;
	public OnVariousScenarios oVSce;

	public Speedster() {
		super();
		name = "Speedster";
		health = 40;
		damage = 10;
		speed = 7;
		attackSpeed = 8;
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

		abilities.add(new Ability("evenFaster", "Even Faster", 2, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				character.conditions.condition(Conditions.ConditionNames.EVEN_FASTER);
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelAttackMode();
				character.conditions.remove(Conditions.ConditionNames.EVEN_FASTER);
			}
		});

		oVSce = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				if (abilities.get(0).isItActive)
					abilities.get(0).finished();
			}
		};
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
	}

	public void resetClassesState() {
		abilities.get(0).cancelActivation();
		attackState = 0;
	}

	public void updateOverridable() {
		abilitiesProcessor();

	}



	public void finishAbilities(){
		abilities.get(0).finished();
		attackState = 0;
	}

	@Override
	public boolean onAttackDecided() {
		if (abilities.get(0).isItActive){
			attackState++;
			print("EvenFaster was registered correctly. AttackState is " + attackState);
			if (attackState < EvenFasterNumberOfExtraHits)
				return false;
			finishAbilities();
		}
		return true;
	}

/*	public float outgoingDamageOverridable(){
		if (abilities.get(1).isItActive) {
			print("OFA was registered correctly");
			finishAbilities();
			return totalDamage * OfAMultiplier;
		}
		return totalDamage;
	}
*/
	public void destroyOverridable(){
		destroyListener(oVSce);
	}


}