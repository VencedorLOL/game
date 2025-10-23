package com.mygdx.game.items.characters.classes;

import com.mygdx.game.Utils;
import com.mygdx.game.items.Actor;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.shields.HealerShields;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;


public class Healer extends CharacterClasses implements Utils {

	public float healingFromAbility = 1.2f;
	public Actor healTarget;


	public Healer(){
		super();
		name = "Healer";
		health = 40;
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
		healTarget = character;
	}


	public float outgoingDamageOverridable(){
		float damage = totalDamage / 2;
		currentHealth += damage * healingFromAbility * Utils.pickValueAUnlessEqualsZeroThenPickB(weaponHealingAbilityBonus(),1);
		return damage;
	}


	public float weaponHealingAbilityBonus(){
		if (weapon instanceof HealerWeapons)
			return ((HealerWeapons) weapon).weaponHealingAbilityBonus;
		return 0;
	}


}

