package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.equipment.Shields;

public class MeleeShields extends Shields {


	public MeleeShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
						byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
						float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
						float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
						float shieldMagicHealing, String equipableBy, float aggro) {

		super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
				shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
				shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, aggro);
	}


	public static class MeleeShield extends MeleeShields {
		public static String shieldName = "MeleeShield";
		public static float shieldHealth = 20;
		public static float shieldDamage = 0;
		public static byte shieldSpeed = 0;
		public static byte shieldAttackSpeed = 0;
		public static float shieldDefense = 5;
		public static int shieldRange = 0;
		public static float shieldTempDefense = 0;
		public static float shieldRainbowDefense = 0;
		public static float shieldMana = 0;
		public static float shieldMagicDefense = 0;
		public static float shieldMagicDamage = 0;
		public static float shieldManaPerTurn = 0;
		public static float shieldManaPerUse = 0;
		public static float shieldMagicHealing = 0;
		public static String equipableBy = "Melee";
		public static float aggro = 0;

		public MeleeShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, aggro);
		}

	}

}
