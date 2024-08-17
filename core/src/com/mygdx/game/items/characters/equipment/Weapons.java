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

	public void update(){
		// Overridable method. Runs every tick. For weapon-specific abilities.
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
	// HEALER
	public static class HealerSwords extends Weapons {
		public float weaponHealingAbilityBonus;

		public HealerSwords(String weaponName, float weaponHealth, float weaponDamage, byte weaponSpeed,
							byte weaponAttackSpeed, float weaponDefense, int weaponRange, float weaponTempDefense,
							float weaponRainbowDefense, float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
							float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy,
					float weaponHealingAbilityBonus) {

			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
			this.weaponHealingAbilityBonus = weaponHealingAbilityBonus;
		}
	}




	public static class BlessedSword extends HealerSwords{
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
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, weaponHealingAbilityBonus);
		}

	}

	public static class BestHealerSword extends HealerSwords{
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
		public static float weaponHealingAbilityBonus = 6;
		public static String equipableBy = "Healer";

		public BestHealerSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy,
					weaponHealingAbilityBonus);
		}
	}
	// MELEE
	public static class MeleeSwords extends Weapons {

		public MeleeSwords(String weaponName, float weaponHealth, float weaponDamage, byte weaponSpeed,
							byte weaponAttackSpeed, float weaponDefense, int weaponRange, float weaponTempDefense,
							float weaponRainbowDefense, float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
							float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy) {

			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}

	public static class MeleeSword extends MeleeSwords{
		public static String weaponName = "MeleeSword";
		public static float weaponHealth = 0;
		public static float weaponDamage = 40;
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
		public static String equipableBy = "Melee";

		public MeleeSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
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

		public VencedorSword() {

			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}

	// TANK
	public static class TankSwords extends Weapons {
		public float weaponHealingAbilityBonus;

		public TankSwords(String weaponName, float weaponHealth, float weaponDamage, byte weaponSpeed,
							byte weaponAttackSpeed, float weaponDefense, int weaponRange, float weaponTempDefense,
							float weaponRainbowDefense, float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
							float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy) {

			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}
	}




	public static class TankSword extends TankSwords{
		public static String weaponName = "TankSword";
		public static float weaponHealth = 10;
		public static float weaponDamage = 15;
		public static byte weaponSpeed = 0;
		public static byte weaponAttackSpeed = 0;
		public static float weaponDefense = 10;
		public static int weaponRange = 0;
		public static float weaponTempDefense = 0;
		public static float weaponRainbowDefense = 0;
		public static float weaponMana = 0;
		public static float weaponMagicDefense = 0;
		public static float weaponMagicDamage = 0;
		public static float weaponManaPerTurn = 0;
		public static float weaponManaPerUse = 0;
		public static float weaponMagicHealing = 0;
		public static String equipableBy = "Tank";

		public TankSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy);
		}

	}


}
