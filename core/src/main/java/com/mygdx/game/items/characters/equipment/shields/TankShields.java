package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.equipment.Shields;

public class TankShields extends Shields {

	public float shieldHealingPerTurn;

	public TankShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
					   byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
					   float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
					   float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
					   float shieldMagicHealing, String equipableBy, float aggro ) {

		super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
				shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
				shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, aggro);
	}





	public static class TankShield extends TankShields {
		public static String shieldName = "TankShield";
		public static float shieldHealth = 100;
		public static float shieldDamage = 0;
		public static byte shieldSpeed = 0;
		public static byte shieldAttackSpeed = 0;
		public static float shieldDefense = 20;
		public static int shieldRange = 0;
		public static float shieldTempDefense = 0;
		public static float shieldRainbowDefense = 0;
		public static float shieldMana = 0;
		public static float shieldMagicDefense = 0;
		public static float shieldMagicDamage = 0;
		public static float shieldManaPerTurn = 0;
		public static float shieldManaPerUse = 0;
		public static float shieldMagicHealing = 0;
		public static String equipableBy = "Tank";
		public static float aggro = 0;

		public TankShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, aggro);
		}

		@Override
		public void update() {
			// This would make the Tank suffer 20% less damage, but classes can't even take damage yet...

		}
	}




}
