package com.mygdx.game.items.characters.equipment;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.print;
import static java.lang.Float.POSITIVE_INFINITY;

public class Weapons {

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
	public String equippableBy;
	public float aggro;

	public CharacterClasses holder;

	public Weapons(CharacterClasses holder){
		this.holder = holder;
	}

	public Weapons(Weapons weapon){
		refresh(weapon);
	}

	public void refresh(Weapons weapon){
		weaponName = weapon.weaponName;
		weaponHealth = weapon.weaponHealth;
		weaponDamage = weapon.weaponDamage;
		weaponSpeed = weapon.weaponSpeed;
		weaponAttackSpeed = weapon.weaponAttackSpeed;
		weaponDefense = weapon.weaponDefense;
		weaponRange = weapon.weaponRange;
		weaponTempDefense = weapon.weaponTempDefense;
		weaponRainbowDefense = weapon.weaponRainbowDefense;
		weaponMana = weapon.weaponMana;
		weaponMagicDefense = weapon.weaponMagicDefense;
		weaponMagicDamage = weapon.weaponMagicDamage;
		weaponManaPerTurn = weapon.weaponManaPerTurn;
		weaponManaPerUse = weapon.weaponManaPerUse;
		weaponMagicHealing = weapon.weaponMagicHealing;
		equippableBy = weapon.equippableBy;
		aggro = weapon.aggro;
	}

	public void update(){
		// Overridable method. Runs every tick. For weapon-specific abilities.
	}

	public void onHurt(AttackTextProcessor.DamageReasons source){}

	public void onAttack(){}

	public void onMove(){}

	public boolean onAttackDecided(){return true;}

	public void turnHasPassed(){}

	// New weapons can be nested here (note to self: please dont) or on other different classes, if and only if they are children of "Weapons"

	public static class NoWeapon extends Weapons{
		public NoWeapon(CharacterClasses holder) {
			super(holder);
			weaponName = "NoWeapon";
			weaponHealth = 0;
			weaponDamage = 0;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponTempDefense = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			aggro = 0;
		}
	}

	// VENCEDOR
	public static class VencedorSword extends Weapons {
		public VencedorSword(CharacterClasses holder) {
			super(holder);
			weaponName = "Vencedor's Sword";
			weaponHealth = 0;
			weaponDamage = 295;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = POSITIVE_INFINITY;
			weaponRange = 0;
			weaponTempDefense = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = POSITIVE_INFINITY;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Vencedor";
			aggro = 2;
		}
	}
	
}
