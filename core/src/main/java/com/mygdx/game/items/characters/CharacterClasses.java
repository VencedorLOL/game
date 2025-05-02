package com.mygdx.game.items.characters;

import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.Character;

import java.util.ArrayList;

import static com.mygdx.game.items.Turns.*;
import static java.lang.Math.max;

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

	// Any character might have more than one ability.
	public ArrayList<Ability> abilityButton;

	public boolean pierces;
	// If true, turn completion will be handled by classes instead of normal procedure
	public boolean shouldTurnCompletionBeLeftToClass;

	OnVariousScenarios cooldownHelper;
	public int defaultCooldown;

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
		this.shouldTurnCompletionBeLeftToClass = false;
		reset();
		cooldownHelper = new OnVariousScenarios(){
			@Override
			public void onTurnPass(){
				turnHasPassed();
			}
		};
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

	public float outgoingDamage(){
		return totalDamage;
	}

	public void damage(float damage){
		currentHealth = currentHealth - max(damage - totalDefense, 0);
	}

	public void equipWeapon(Weapons targetWeapon) {
		System.out.println(targetWeapon.weaponName);
		if (targetWeapon.equipableBy == name || targetWeapon.equipableBy == null) {
			weapon = targetWeapon;
			reset();
		}
	}

	public void equipShield(Shields targetShield) {
		System.out.println(targetShield.shieldName);
		if (name == targetShield.equipableBy || targetShield.equipableBy == null) {
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

	public void refresh(CharacterClasses characterClasses){
		health = characterClasses.health;
		damage = characterClasses.damage;
		speed = characterClasses.speed;
		attackSpeed = characterClasses.attackSpeed;
		defense = characterClasses.defense;
		range = characterClasses.range;
		tempDefense = characterClasses.tempDefense;
		rainbowDefense = characterClasses.rainbowDefense;
		mana = characterClasses.mana;
		magicDefense = characterClasses.magicDefense;
		magicDamage = characterClasses.magicDamage;
		manaPerTurn = characterClasses.manaPerTurn;
		manaPerUse = characterClasses.manaPerUse;
		magicHealing = characterClasses.magicHealing;
		currentHealth = characterClasses.currentHealth;
		if(currentHealth > health)
			currentHealth = health;
	}

	public void update(Character character){
		updateOverridable(character);
		refresh(this);
	}


	public void updateOverridable(Character character) {}


	public void turnHasPassed() {
		defaultCooldown--;
	}

	public CharacterClasses(String name, float health, float damage,
							byte speed, byte attackSpeed, float defense,
							int range, float tempDefense, float rainbowDefense,
							float mana, float magicDefense, float magicDamage,
							float manaPerTurn, float manaPerUse, float magicHealing,boolean shouldTurnCompletionBeLeftToClass){
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
		this.shouldTurnCompletionBeLeftToClass = shouldTurnCompletionBeLeftToClass;
		reset();
	}

	//Convenience methods for the future
	public void onHurt(String source){}

	public void onAttack(){}

	public void onMove(){}




}
