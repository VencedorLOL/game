package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.equipment.Weapons;

public class HealerWeapons extends Weapons {
	public float weaponHealingAbilityBonus;

	public HealerWeapons(String weaponName, float weaponHealth, float weaponDamage, byte weaponSpeed,
						byte weaponAttackSpeed, float weaponDefense, int weaponRange, float weaponTempDefense,
						float weaponRainbowDefense, float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
						float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy,
						float weaponHealingAbilityBonus, float aggro) {

		super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
				weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
				weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, aggro);
		this.weaponHealingAbilityBonus = weaponHealingAbilityBonus;
	}

	public HealerWeapons(Weapons weapon,float weaponHealingAbilityBonus){
		super(weapon);
		this.weaponHealingAbilityBonus = weaponMagicHealing;
	}



	public static class BlessedSword extends HealerWeapons {
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
		public static float weaponHealingAbilityBonus = (float) 10/9;
		public static String equipableBy = "Healer";
		public static float aggro = 0;

		public BlessedSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
					weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
					weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, weaponHealingAbilityBonus, aggro);
		}

	}

	public static class BestHealerSword extends HealerWeapons {
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
		public static float aggro = 0;

		public BestHealerSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
				weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
				weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy,
				weaponHealingAbilityBonus, aggro);
		}
	}
}
