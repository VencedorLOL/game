package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class MeleeShields extends Shields {


	public MeleeShields(CharacterClasses holder) {

		super(holder);
	}


	public static class MeleeShield extends MeleeShields {

		public MeleeShield(CharacterClasses holder) {
			super(holder);
			shieldName = "MeleeShield";
			shieldHealth = 20;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 5;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Melee";
			aggro = 0;

		}

	}

}
