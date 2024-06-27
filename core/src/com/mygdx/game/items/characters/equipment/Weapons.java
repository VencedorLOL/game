package com.mygdx.game.items.characters.equipment;

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

	public Weapons(String weaponName, float weaponHealth, float weaponDamage,
				   byte weaponSpeed, byte weaponAttackSpeed, float weaponDefense,
				   int weaponRange, float weaponTempDefense, float weaponRainbowDefense,
				   float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
				   float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy){
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
	}

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

		public NoWeapon() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}



	public static class BlessedSword extends Weapons{
		public static String weaponName = "BlessedSword";
		public static float weaponHealth = 0;
		public static float weaponDamage = 10;
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
		public static float weaponHealingAbilityBonus = 2;
		public static String equipableBy = "Healer";

		public BlessedSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}

	public static class BestSword extends Weapons{
		public static String weaponName = "BestSword";
		public static float weaponHealth = 0;
		public static float weaponDamage = 100;
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
		public double weaponHealingAbilityBonus = 6;

		public static String equipableBy;

		public BestSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}
}
