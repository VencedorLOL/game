package com.mygdx.game.items.characters;

public class CharacterClasses {
	public String name;
	public float health;
	public float tempDefense;
	public float damage;
	public byte speed;
	public byte attackSpeed;
	public float defense;
	public float rainbowDefense;
	public float magicDefense;
	public int range;
	public float mana;
	public float manaPerTurn;
	public float manaPerUse;
	public float magicDamage;
	public float magicHealing;

	public String weaponName;
	public float weaponHealth;
	public float weaponTempDefense;
	public float weaponDamage;
	public byte weaponSpeed;
	public byte weaponAttackSpeed;
	public float weaponDefense;
	public float weaponRainbowDefense;
	public float weaponMagicDefense;
	public int weaponRange;
	public float weaponMana;
	public float weaponManaPerTurn;
	public float weaponManaPerUse;
	public float weaponMagicDamage;
	public float weaponMagicHealing;

	public String shieldName;
	public float shieldHealth;
	public float shieldTempDefense;
	public float shieldDamage;
	public byte shieldSpeed;
	public byte shieldAttackSpeed;
	public float shieldDefense;
	public float shieldRainbowDefense;
	public float shieldMagicDefense;
	public int shieldRange;
	public float shieldMana;
	public float shieldManaPerTurn;
	public float shieldManaPerUse;
	public float shieldMagicDamage;
	public float shieldMagicHealing;

	public float totalHealth;
	public float totalDamage;
	public byte totalSpeed;
	public byte totalAttackSpeed;
	public float totalMana;
	public float totalManaPerTurn;
	public float totalManaPerUse;
	public float totalMagicDamage;
	public float totalMagicHealing;
	public float totalDefense;
	public float totalTempDefense;
	public float totalRainbowDefense;
	public float totalMagicDefense;
	public int totalRange;

	public CharacterClasses(String name, float health, float damage,
							byte speed, byte attackSpeed, float defense,
							int range, float tempDefense, float rainbowDefense,
							float mana, float magicDefense, float magicDamage,
							float manaPerTurn, float manaPerUse, float magicHealing){
		this.name = name;
		this.health = health;
		this.damage = damage;
		this.speed = speed;
		this.attackSpeed = attackSpeed;
		this.defense = defense;
		this.range = range;
		this.tempDefense = tempDefense;
		this.rainbowDefense = rainbowDefense;
		this.mana = mana;
		this.magicDefense = magicDefense;
		this.magicDamage = magicDamage;
		this.manaPerTurn = manaPerTurn;
		this.manaPerUse = manaPerUse;
		this.magicHealing = magicHealing;
	}

	public void equippedWeaponryGetter(String weaponName, float weaponHealth, float weaponDamage,
											  byte weaponSpeed, byte weaponAttackSpeed, float weaponDefense,
											  int weaponRange, float weaponTempDefense, float weaponRainbowDefense,
									   float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
									   float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing) {
		this.weaponName = weaponName;
		this.weaponHealth = weaponHealth;
		this.weaponDamage = weaponDamage;
		this.weaponSpeed = weaponSpeed;
		this.weaponAttackSpeed = weaponAttackSpeed;
		this.weaponDefense = weaponDefense;
		this.weaponRange = weaponRange;
		this.weaponTempDefense = weaponTempDefense;
		this.weaponRainbowDefense = weaponRainbowDefense;
		this.weaponMana = weaponMana;
		this.weaponMagicDefense = weaponMagicDefense;
		this.weaponMagicDamage = weaponMagicDamage;
		this.weaponManaPerTurn = weaponManaPerTurn;
		this.weaponManaPerUse = weaponManaPerUse;
		this.weaponMagicHealing = weaponMagicHealing;
	}

	public void equippedWeaponryGetter(String weaponName, float weaponHealth, float weaponDamage,
									   byte weaponSpeed, byte weaponAttackSpeed, float weaponDefense,
									   int weaponRange, float weaponTempDefense, float weaponRainbowDefense,
									   float weaponMagicDefense) {
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
	}

	public void equippedShieldGetter(String shieldName, float shieldHealth, float shieldDamage,
									   byte shieldSpeed, byte shieldAttackSpeed, float shieldDefense,
									   int shieldRange, float shieldTempDefense, float shieldRainbowDefense,
									   float shieldMana, float shieldMagicDefense, float shieldMagicDamage,
									   float shieldManaPerTurn, float shieldManaPerUse, float shieldMagicHealing) {
		this.shieldName = shieldName;
		this.shieldHealth = shieldHealth;
		this.shieldDamage = shieldDamage;
		this.shieldSpeed = shieldSpeed;
		this.shieldAttackSpeed = shieldAttackSpeed;
		this.shieldDefense = shieldDefense;
		this.shieldRange = shieldRange;
		this.shieldTempDefense = shieldTempDefense;
		this.shieldRainbowDefense = shieldRainbowDefense;
		this.shieldMana = shieldMana;
		this.shieldMagicDefense = shieldMagicDefense;
		this.shieldMagicDamage = shieldMagicDamage;
		this.shieldManaPerTurn = shieldManaPerTurn;
		this.shieldManaPerUse = shieldManaPerUse;
		this.shieldMagicHealing = shieldMagicHealing;
	}

	public void equippedShieldGetter(String shieldName, float shieldHealth, float shieldDamage,
									 byte shieldSpeed, byte shieldAttackSpeed, float shieldDefense,
									 int shieldRange, float shieldTempDefense, float shieldRainbowDefense,
									 float shieldMagicDefense) {
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
	}

	public void totalStatsCalculator(){
		totalHealth = health + shieldHealth + weaponHealth;
		totalDamage = damage + shieldDamage + weaponDamage;
		totalSpeed = (byte) (speed + shieldSpeed + weaponSpeed);
		totalAttackSpeed = (byte) (attackSpeed + shieldAttackSpeed + weaponAttackSpeed);
		totalDefense = defense + shieldDefense + weaponDefense;
		totalRange = range + shieldRange + weaponRange;
		totalMana = mana + weaponMana + shieldMana;
		totalManaPerTurn = manaPerTurn + weaponMana + shieldMana;
		totalManaPerUse = manaPerUse + weaponManaPerUse + shieldManaPerUse;
		totalMagicDamage = magicDamage + weaponMagicDamage + shieldMagicDamage;
		totalMagicHealing = magicHealing + weaponMagicHealing + shieldMagicHealing;
		totalTempDefense = tempDefense + weaponTempDefense + shieldTempDefense;
		totalRainbowDefense = rainbowDefense + weaponRainbowDefense + shieldRainbowDefense;
		totalMagicDefense = magicDefense + weaponMagicDefense + shieldMagicDefense;
	}

	public float outgoingDamageCalculator (float otherMultiplicativeStatusEffects){
		return totalDamage * (otherMultiplicativeStatusEffects + 1);
	}

}
