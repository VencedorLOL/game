package com.mygdx.game.items.characters.equipment;

public class Shields{

	public String shieldName;
	public float shieldHealth;
	public float shieldTempDefense;
	public float shieldDamage;
	public byte shieldSpeed;
	public byte shieldAttackSpeed;
	public float shieldDefense;
	public float shieldRainbowDefense;
	public float shieldMagicDefense;
	public int shieldRange;
	public float shieldMana;
	public float shieldManaPerTurn;
	public float shieldManaPerUse;
	public float shieldMagicDamage;
	public float shieldMagicHealing;
	public String equipableBy;

	public Shields(String shieldName, float shieldHealth, float shieldDamage,
				   byte shieldSpeed, byte shieldAttackSpeed, float shieldDefense,
				   int shieldRange, float shieldTempDefense, float shieldRainbowDefense,
				   float shieldMana, float shieldMagicDefense, float shieldMagicDamage,
				   float shieldManaPerTurn, float shieldManaPerUse, float shieldMagicHealing, String equipableBy){
		this.shieldName = shieldName;
		this.shieldHealth = shieldHealth;
		this.shieldDamage = shieldDamage;
		this.shieldSpeed = shieldSpeed;
		this.shieldAttackSpeed = shieldAttackSpeed;
		this.shieldDefense = shieldDefense;
		this.shieldRange = shieldRange;
		this.shieldTempDefense = shieldTempDefense;
		this.shieldRainbowDefense = shieldRainbowDefense;
		this.shieldMana = shieldMana;
		this.shieldMagicDefense = shieldMagicDefense;
		this.shieldMagicDamage = shieldMagicDamage;
		this.shieldManaPerTurn = shieldManaPerTurn;
		this.shieldManaPerUse = shieldManaPerUse;
		this.shieldMagicHealing = shieldMagicHealing;
		this.equipableBy = equipableBy;
	}

	public void refresh(Shields shield){
		shieldName = shield.shieldName;
		shieldHealth = shield.shieldHealth;
		shieldDamage = shield.shieldDamage;
		shieldSpeed = shield.shieldSpeed;
		shieldAttackSpeed = shield.shieldAttackSpeed;
		shieldDefense = shield.shieldDefense;
		shieldRange = shield.shieldRange;
		shieldTempDefense = shield.shieldTempDefense;
		shieldRainbowDefense = shield.shieldRainbowDefense;
		shieldMana = shield.shieldMana;
		shieldMagicDefense = shield.shieldMagicDefense;
		shieldMagicDamage = shield.shieldMagicDamage;
		shieldManaPerTurn = shield.shieldManaPerTurn;
		shieldManaPerUse = shield.shieldManaPerUse;
		shieldMagicHealing = shield.shieldMagicHealing;
		equipableBy = shield.equipableBy;
	}

	public void update(){
		// Overridable method. Runs every tick. For shield-specific abilities.
	}



	public static class NoShield extends Shields {
		public static String shieldName = "NoShield";
		public static float shieldHealth = 0;
		public static float shieldDamage = 0;
		public static byte shieldSpeed = 0;
		public static byte shieldAttackSpeed = 0;
		public static float shieldDefense = 0;
		public static int shieldRange = 0;
		public static float shieldTempDefense = 0;
		public static float shieldRainbowDefense = 0;
		public static float shieldMana = 0;
		public static float shieldMagicDefense = 0;
		public static float shieldMagicDamage = 0;
		public static float shieldManaPerTurn = 0;
		public static float shieldManaPerUse = 0;
		public static float shieldMagicHealing = 0;
		public static String equipableBy;

		public NoShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
		}
	}

	// HEALER
	public static class HealerShields extends Shields{
		public float shieldHealingPerTurn;

		public HealerShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
							 byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
							 float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
							 float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
							 float shieldMagicHealing, String equipableBy, float shieldHealingPerTurn ) {

			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
			this.shieldHealingPerTurn = shieldHealingPerTurn;
		}
	}

	public static class BlessedShields extends HealerShields {
		public static String shieldName = "BlessedShields";
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

		public BlessedShields() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy, shieldHealingPerTurn);
		}

	}
	// MELEE
	public static class MeleeShields extends Shields{

		public MeleeShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
							byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
							float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
							float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
							float shieldMagicHealing, String equipableBy) {

			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
		}
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

		public MeleeShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
		}

	}


	// TANK
	public static class TankShields extends Shields{
		public float shieldHealingPerTurn;

		public TankShields(String shieldName, float shieldHealth, float shieldDamage, byte shieldSpeed,
							 byte shieldAttackSpeed, float shieldDefense, int shieldRange, float shieldTempDefense,
							 float shieldRainbowDefense, float shieldMana, float shieldMagicDefense,
							 float shieldMagicDamage, float shieldManaPerTurn, float shieldManaPerUse,
							 float shieldMagicHealing, String equipableBy ) {

			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
		}
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

		public TankShield() {
			super(shieldName, shieldHealth, shieldDamage, shieldSpeed, shieldAttackSpeed,
					shieldDefense, shieldRange, shieldTempDefense, shieldRainbowDefense, shieldMana, shieldMagicDefense,
					shieldMagicDamage, shieldManaPerTurn, shieldManaPerUse, shieldMagicHealing, equipableBy);
		}

		@Override
		public void update() {
			// This would make the Tank suffer 20% less damage, but character can't even take damage yet...
			super.update();
		}
	}
}
