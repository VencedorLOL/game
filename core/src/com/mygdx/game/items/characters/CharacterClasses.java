package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import static java.lang.Math.max;

public class CharacterClasses {
	public CharacterClasses chara;
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

	public Weapons weapon;

	public Shields shield;

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

	public float currentHealth;

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

	public CharacterClasses(){}

	public void totalStatsCalculator(){
		totalHealth = health + shield.shieldHealth + weapon.weaponHealth;
		totalDamage = damage + shield.shieldDamage + weapon.weaponDamage;
		totalSpeed = (byte) (speed + shield.shieldSpeed + weapon.weaponSpeed);
		totalAttackSpeed = (byte) (attackSpeed + shield.shieldAttackSpeed + weapon.weaponAttackSpeed);
		totalDefense = defense + shield.shieldDefense + weapon.weaponDefense;
		totalRange = range + shield.shieldRange + weapon.weaponRange;
		totalMana = mana + weapon.weaponMana + shield.shieldMana;
		totalManaPerTurn = manaPerTurn + weapon.weaponMana + shield.shieldMana;
		totalManaPerUse = manaPerUse + weapon.weaponManaPerUse + shield.shieldManaPerUse;
		totalMagicDamage = magicDamage + weapon.weaponMagicDamage + shield.shieldMagicDamage;
		totalMagicHealing = magicHealing + weapon.weaponMagicHealing + shield.shieldMagicHealing;
		totalTempDefense = tempDefense + weapon.weaponTempDefense + shield.shieldTempDefense;
		totalRainbowDefense = rainbowDefense + weapon.weaponRainbowDefense + shield.shieldRainbowDefense;
		totalMagicDefense = magicDefense + weapon.weaponMagicDefense + shield.shieldMagicDefense;
	}

	public float outgoingDamage(float otherMultiplicativeStatusEffects){
		return totalDamage * (otherMultiplicativeStatusEffects + 1);
	}

	public void damage(float damage){
		currentHealth = currentHealth - max(damage - totalDefense, 0);
	}

	public void equipWeapon(Weapons targetWeapon) {
		System.out.println(targetWeapon.weaponName);
		System.out.println("a");
		if (targetWeapon.equipableBy == name || targetWeapon.equipableBy == null) {
			weapon = targetWeapon;
			System.out.println("New Weapon Equipped");
			reset();
		}
	}

	public void equipShield(Shields targetShield) {
		if (name.equals(targetShield.equipableBy) || targetShield.equipableBy == null) {
			shield = targetShield;
			reset();
		}
	}

	public void reset(){
		if (shield == null)
			shield = new Shields.NoShield();
		if(weapon == null)
			weapon = new Weapons.NoWeapon();
		totalStatsCalculator();
		currentHealth = totalHealth;
	}

}
