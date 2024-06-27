package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.characters.CharacterClasses;

public class Healer extends CharacterClasses {
	public static String name = "Healer";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
	public static float defense = 0;
	public static int range = 4;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;

	public float healingFromAbility = 4;
	public float weaponHealingAbilityBonus;
	public float shieldHealingPerTurn;


	public Healer(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing);
	}


	public float outgoingDamage(float otherMultiplicativeStatusEffects){
		return (totalDamage * (otherMultiplicativeStatusEffects + 1) ) / 2;

	}

	public double abilityHealing(){
		if (weaponHealingAbilityBonus == 0)
			weaponHealingAbilityBonus = 1;
		return outgoingDamage(healingFromAbility * weaponHealingAbilityBonus);
	}
	public double shieldAbilityHealing(){
		return shieldHealingPerTurn;
	}

}

