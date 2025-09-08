package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class Tank extends CharacterClasses {


	public Tank(){
		super();
		name = "Tank";
		health = 80;
		damage = 5;
		speed = 2;
		attackSpeed = 3;
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
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
	}

	OnVariousScenarios oVE = new OnVariousScenarios() {
			public void onDamagedActor(Actor damagedActor) {
				if (damagedActor.team == character.team && character.classes.name == "Tank") {
					damagedActor.damageRecieved *= 0.2f;
					damage(damagedActor.damageRecieved * 4, AttackTextProcessor.DamageReasons.ABSORBED); //this is exactly 80% of the damage
				}
			}
		};

	@Override
	protected void destroyOverridable() {
		destroyListener(oVE);
	}
}

