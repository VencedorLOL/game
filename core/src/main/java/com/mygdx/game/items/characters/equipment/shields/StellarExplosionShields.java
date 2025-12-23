package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class StellarExplosionShields extends Shields {

	public StellarExplosionShields(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class EnergyAccelerator extends StellarExplosionShields {

		public EnergyAccelerator(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Energy accelerator";
			shieldHealth = 0;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 5;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 50;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 25;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "StellarExplosion";
			aggro = 0;
		}
	}




}
