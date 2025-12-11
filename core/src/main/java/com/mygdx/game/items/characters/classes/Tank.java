package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class Tank extends CharacterClasses {


	public Tank(){
		super();
		name = "Tank";
		health = 80;
		damage = 10;
		speed = 2;
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
		if(ClassStoredInformation.Tank.getWeapon() != null)
			weapon = ClassStoredInformation.Tank.getWeapon();
		if(ClassStoredInformation.Tank.getShield() != null)
			shield = ClassStoredInformation.Tank.getShield();

		reset();
		currentHealth = totalHealth;
		manaPool = mana;
	}

	OnVariousScenarios oVE = new OnVariousScenarios() {
			public void onDamagedActor(Actor damagedActor) {
				if (damagedActor.team == character.team && character.classes.name.equals("Tank")) {
					damagedActor.damageRecieved *= 0.2f;
					damage(damagedActor.damageRecieved * 4, AttackTextProcessor.DamageReasons.ABSORBED); //this is exactly 80% of the damage
				}
			}
		};

	@Override
	protected void destroyOverridable() {
		ClassStoredInformation.Tank.setShield(shield);
		ClassStoredInformation.Tank.setWeapon(weapon);
		destroyListener(oVE);
	}
}

