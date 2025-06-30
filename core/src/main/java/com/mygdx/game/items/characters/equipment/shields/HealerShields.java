package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.equipment.Shields;

public class HealerShields extends Shields{
	public float shieldHealingPerTurn;

	public HealerShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
						 byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
						 float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
						 float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
						 float shieldMagicHealing, String equipableBy, float shieldHealingPerTurn, float aggro ) {

		super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
				shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
				shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, aggro);
		this.shieldHealingPerTurn = shieldHealingPerTurn;
	}


	public static class BlessedShield extends HealerShields {
		public static String shieldName = "BlessedShield";
		public static float shieldHealth = 30;
		public static float shieldDamage = 0;
		public static byte shieldSpeed = 0;
		public static byte shieldAttackSpeed = 0;
		public static float shieldDefense = 10;
		public static int shieldRange = 0;
		public static float shieldTempDefense = 0;
		public static float shieldRainbowDefense = 0;
		public static float shieldMana = 0;
		public static float shieldMagicDefense = 0;
		public static float shieldMagicDamage = 0;
		public static float shieldManaPerTurn = 0;
		public static float shieldManaPerUse = 0;
		public static float shieldMagicHealing = 0;
		public static float shieldHealingPerTurn = 5;
		public static String equipableBy = "Healer";
		public static float aggro = 0;

		public BlessedShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, shieldHealingPerTurn, aggro);
	}

}
}
