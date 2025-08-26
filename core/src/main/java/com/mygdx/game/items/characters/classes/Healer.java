package com.mygdx.game.items.characters.classes;

import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.shields.HealerShields;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;


public class Healer extends CharacterClasses implements Utils {
	public static String name = "Healer";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
	public static float defense = 0;
	public static int range = 2;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;
	public static float aggro = 1;

	public float healingFromAbility = 1.2f;


	public Healer(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing,aggro);
	}


	public float outgoingDamageOverridable(){
		float damage = totalDamage / 2;
		currentHealth += damage * healingFromAbility * Utils.pickValueAUnlessEqualsZeroThenPickB(
				weaponHealingAbilityBonus(),1);
		return damage;
	}


	public float weaponHealingAbilityBonus(){
		if (weapon instanceof HealerWeapons)
			return ((HealerWeapons) weapon).weaponHealingAbilityBonus;
		return 0;
	}


}

