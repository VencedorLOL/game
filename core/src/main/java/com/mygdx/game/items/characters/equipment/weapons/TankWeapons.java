package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.equipment.Weapons;

public class TankWeapons extends Weapons{


	public TankWeapons(String weaponName, float weaponHealth, float weaponDamage, byte weaponSpeed,
					  byte weaponAttackSpeed, float weaponDefense, int weaponRange, float weaponTempDefense,
					  float weaponRainbowDefense, float weaponMana, float weaponMagicDefense, float weaponMagicDamage,
					  float weaponManaPerTurn, float weaponManaPerUse, float weaponMagicHealing, String equipableBy, float aggro) {

		super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
				weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
				weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, aggro);
	}

	public static class TankSword extends TankWeapons{
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
		public static float aggro = 0;

		public TankSword() {
			super(weaponName, weaponHealth, weaponDamage, weaponSpeed, weaponAttackSpeed,
				weaponDefense, weaponRange, weaponTempDefense, weaponRainbowDefense, weaponMana, weaponMagicDefense,
				weaponMagicDamage, weaponManaPerTurn, weaponManaPerUse, weaponMagicHealing, equipableBy, aggro);
		}
	}





}
