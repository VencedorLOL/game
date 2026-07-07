package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.DamageReceiver;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class Tank extends CharacterClasses {

	OnVariousScenarios oVE;

	public Tank(){
		super();
		name = "Tank";
		health = 80;
		damage = 10;
		speed = 2;
		attackSpeed = 2;
		defense = 0;
		range = 1;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;
		getEquipment();
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
		character.idleTexture="animaTankShoddy";
		oVE = new OnVariousScenarios() {
			public void onDamaged(DamageReceiver damagedActor, AttackTextProcessor.DamageReasons source) {
				if (damagedActor.getTotalHealth() == character.totalTeam && character.classes.name.equals("Tank") && damagedActor != character && damagedActor instanceof Actor) {
					((Actor)damagedActor).damageRecieved *= 0.2f;
					damage(((Actor)damagedActor).damageRecieved * 4, AttackTextProcessor.DamageReasons.ABSORBED); //this is exactly 80% of the damage
				}
			}
		};
	}

	@Override
	protected void destroyOverridable() {
		destroyListener(oVE);
	}
}

