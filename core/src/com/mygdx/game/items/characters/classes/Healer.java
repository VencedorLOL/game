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

	public String weaponName;
	public float weaponHealth;
	public float weaponDamage;
	public byte weaponSpeed;
	public byte weaponAttackSpeed;
	public float weaponDefense;
	public int weaponRange;
	public String shieldName;
	public float shieldHealth;
	public float shieldDamage;
	public byte shieldSpeed;
	public byte shieldAttackSpeed;
	public float shieldDefense;
	public int shieldRange;

	public float healingFromAbility = 4;
	public float weaponHealingAbilityBonus;
	public float shieldHealingPerTurn;
	public double totalHealth, totalDamage, totalSpeed, totalAttackSpeed, totalDefense, totalRange;


	public Healer(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing);
	}

	public void equippedWeaponryGetter(String weaponName, float weaponHealth, float weaponDamage,
									   byte weaponSpeed, byte weaponAttackSpeed, float weaponDefense,
									   int weaponRange, float weaponTempDefense, float weaponRainbowDefense,
									   float weaponMagicDefense, float weaponHealingAbilityBonus) {
		this.weaponName = weaponName;
		this.weaponHealth = weaponHealth;
		this.weaponDamage = weaponDamage;
		this.weaponSpeed = weaponSpeed;
		this.weaponAttackSpeed = weaponAttackSpeed;
		this.weaponDefense = weaponDefense;
		this.weaponRange = weaponRange;
		this.weaponTempDefense = weaponTempDefense;
		this.weaponRainbowDefense = weaponRainbowDefense;
		this.weaponMagicDefense = weaponMagicDefense;
		this.weaponHealingAbilityBonus = weaponHealingAbilityBonus;
	}


	public void equippedShieldGetter(String shieldName, float shieldHealth, float shieldDamage,
									 byte shieldSpeed, byte shieldAttackSpeed, float shieldDefense,
									 int shieldRange, float shieldTempDefense, float shieldRainbowDefense,
									 float shieldMagicDefense, float shieldHealingPerTurn) {
		this.shieldName = shieldName;
		this.shieldHealth = shieldHealth;
		this.shieldDamage = shieldDamage;
		this.shieldSpeed = shieldSpeed;
		this.shieldAttackSpeed = shieldAttackSpeed;
		this.shieldDefense = shieldDefense;
		this.shieldRange = shieldRange;
		this.shieldTempDefense = shieldTempDefense;
		this.shieldRainbowDefense = shieldRainbowDefense;
		this.shieldMagicDefense = shieldMagicDefense;
		this.shieldHealingPerTurn = shieldHealingPerTurn;
	}



	public float outgoingDamageCalculator (float otherMultiplicativeStatusEffects){
		return (float) ((totalDamage * (otherMultiplicativeStatusEffects + 1) ) / 2);

	}

	public double abilityHealing(){
		if (weaponHealingAbilityBonus == 0)
			weaponHealingAbilityBonus = 1;
		return outgoingDamageCalculator(healingFromAbility * weaponHealingAbilityBonus);
	}
	public double ShieldAbilityHealing(){
		return shieldHealingPerTurn;
	}

}

