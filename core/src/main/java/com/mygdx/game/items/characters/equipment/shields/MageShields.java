package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class MageShields extends Shields {

	public MageShields(CharacterClasses holder) {
		super(holder);
	}



	public static class RoughCrystal extends MageShields {

		public RoughCrystal(CharacterClasses holder) {
			super(holder);
			shieldName = "RoughCrystal";
			shieldHealth = 0;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 200;
			shieldMagicDefense = 0;
			shieldMagicDamage = 20;
			shieldManaPerTurn = 15;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Mage";
			aggro = 0;
		}
	}




}
