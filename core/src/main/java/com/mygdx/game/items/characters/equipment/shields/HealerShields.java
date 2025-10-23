package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class HealerShields extends Shields{
	public float shieldHealingPerTurn;

	public HealerShields(CharacterClasses holder) {
		super(holder);
		this.shieldHealingPerTurn = shieldHealingPerTurn;
	}


	public static class BlessedShield extends HealerShields {


		public BlessedShield(CharacterClasses holder) {
			super(holder);
			shieldName = "BlessedShield";
			shieldHealth = 30;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 10;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			shieldHealingPerTurn = 5;
			equippableBy = "Healer";
			aggro = 0;

	}

		@Override
		public void turnHasPassed() {
			holder.currentHealth += shieldHealingPerTurn;
		}
	}
}
