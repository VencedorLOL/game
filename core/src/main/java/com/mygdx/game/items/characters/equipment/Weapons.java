package com.mygdx.game.items.characters.equipment;

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
	public String equipableBy;
	public float aggro;

	public Weapons(String weaponName, float weaponHealth, float weaponDamage,
				   byte weaponSpeed, byte weaponAttackSpeed, float weaponDefense,
				   int weaponRange, float weaponTempDefense, float weaponRainbowDefense,
				   float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
				   float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy, float aggro){
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
		this.equipableBy = equipableBy;
		this.aggro = aggro;
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
		equipableBy = weapon.equipableBy;
		aggro = weapon.aggro;
	}

	public void update(){
		// Overridable method. Runs every tick. For weapon-specific abilities.
	}

	// New weapons can be nested here (note to self: please dont) or on other different classes, if and only if they are children of "Weapons"

	public static class NoWeapon extends Weapons{
		public static String weaponName = "NoWeapon";
		public static float weaponHealth = 0;
		public static float weaponDamage = 0;
		public static byte weaponSpeed = 0;
		public static byte weaponAttackSpeed = 0;
		public static float weaponDefense = 0;
		public static int weaponRange = 0;
		public static float weaponTempDefense = 0;
		public static float weaponRainbowDefense = 0;
		public static float weaponMana = 0;
		public static float weaponMagicDefense = 0;
		public static float weaponMagicDamage = 0;
		public static float weaponManaPerTurn = 0;
		public static float weaponManaPerUse = 0;
		public static float weaponMagicHealing = 0;
		public static String equipableBy;
		public static float aggro = 0;

		public NoWeapon() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, aggro);
		}
	}

	// VENCEDOR
	public static class VencedorSword extends Weapons {
		public static String weaponName = "Vencedor's Sword";
		public static float weaponHealth = 0;
		public static float weaponDamage = 295;
		public static byte weaponSpeed = 0;
		public static byte weaponAttackSpeed = 0;
		public static float weaponDefense = POSITIVE_INFINITY;
		public static int weaponRange = 0;
		public static float weaponTempDefense = 0;
		public static float weaponRainbowDefense = 0;
		public static float weaponMana = 0;
		public static float weaponMagicDefense = POSITIVE_INFINITY;
		public static float weaponMagicDamage = 0;
		public static float weaponManaPerTurn = 0;
		public static float weaponManaPerUse = 0;
		public static float weaponMagicHealing = 0;
		public static String equipableBy = "Vencedor";
		public static float aggro = 2;

		public VencedorSword() {

			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, aggro);
		}
	}
	
}
