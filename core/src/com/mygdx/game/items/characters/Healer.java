package com.mygdx.game.items.characters;

import java.lang.reflect.Field;
import java.util.Scanner;

public class Healer {
	// First character ever lets go
	public static String name = "Healer";
	public static double health = 40;
	public static double damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
	public static double defense = 0;
	public static int range = 4;

	//Specific stats
	public double healingFromAbility = 4;
	public static double weaponHealingAbilityBonus;
	public static double shieldHealingPerTurn;


	public double damageAffectingStatusEffectTotal;

	//weapon stats
	public static String weaponName;
	public static double weaponHealth;
	public static double weaponDamage;
	public static byte weaponSpeed;
	public static byte weaponAttackSpeed;
	public static double weaponDefense;
	public static int weaponRange;
	public static String shieldName;
	public static double shieldHealth;
	public static double shieldDamage;
	public static byte shieldSpeed;
	public static byte shieldAttackSpeed;
	public static double shieldDefense;
	public static int shieldRange;

	public double totalHealth, totalDamage, totalSpeed, totalAttackSpeed, totalDefense, totalRange;


	public static void EquippedWeaponryReceiver(String weaponName, double weaponHealth, double weaponDamage,
												byte weaponSpeed, byte weaponAttackSpeed, double weaponDefense,
												int weaponRange , double weaponHealingAbilityBonus)
	{
		Healer.weaponName = weaponName;
		Healer.weaponHealth = weaponHealth;
		Healer.weaponDamage = weaponDamage;
		Healer.weaponSpeed = weaponSpeed;
		Healer.weaponAttackSpeed = weaponAttackSpeed;
		Healer.weaponDefense = weaponDefense;
		Healer.weaponRange = weaponRange;
		Healer.weaponHealingAbilityBonus = weaponHealingAbilityBonus;
	}


	public static void EquippedShieldReceiver(String shieldName, double shieldHealth, double shieldDamage,
											  byte shieldSpeed, byte shieldAttackSpeed, double shieldDefense, int shieldRange , double shieldHealingAbilityBonus)
	{
		Healer.shieldName = shieldName;
		Healer.shieldHealth = shieldHealth;
		Healer.shieldDamage = shieldDamage;
		Healer.shieldSpeed = shieldSpeed;
		Healer.shieldAttackSpeed = shieldAttackSpeed;
		Healer.shieldDefense = shieldDefense;
		Healer.shieldRange = shieldRange;
		Healer.shieldHealingPerTurn = shieldHealingAbilityBonus;
	}

	public void TotalStatsCalculator(){
		totalHealth = health + shieldHealth + weaponHealth;
		totalDamage = damage + shieldDamage + weaponDamage;
		totalSpeed = (byte) (speed + shieldSpeed + weaponSpeed);
		totalAttackSpeed = (byte) (attackSpeed + shieldAttackSpeed + weaponAttackSpeed);
		totalDefense = defense + shieldDefense + weaponDefense;
		totalRange = range + shieldRange + weaponRange;
	}

	//Just initalize all weapons and shields
	public void HealerWeaponsInitalizer (){
		Weapons WeaponsInitalizer = new Weapons();
		WeaponsInitalizer.WeaponInitializer();
		Shields ShieldsInitalizer = new Shields();
		ShieldsInitalizer.ShieldInitializer();
	}

	public double OutgoingDamageCalculator (double otherMultiplicativeStatusEffects){
		double finalDamage = ((totalDamage * (otherMultiplicativeStatusEffects + 1) ) / 2);
		// The damage is halved cuz ability
		return finalDamage;

	}

	public double AbilityHealing(){
		if (weaponHealingAbilityBonus == 0)
			weaponHealingAbilityBonus = 1;
		return OutgoingDamageCalculator(damageAffectingStatusEffectTotal) * (healingFromAbility * weaponHealingAbilityBonus);
	}
	public double ShieldAbilityHealing(){
		return shieldHealingPerTurn;
	}

}

