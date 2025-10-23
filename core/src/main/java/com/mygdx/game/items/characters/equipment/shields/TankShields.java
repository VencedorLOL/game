package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class TankShields extends Shields {

	public TankShields(CharacterClasses holder) {
		super(holder);
	}



	public static class TankShield extends TankShields {
		public TankShield(CharacterClasses holder) {
			super(holder);
			shieldName = "TankShield";
			shieldHealth = 100;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 20;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Tank";
			aggro = 0;
		}

		@Override
		public void onHurt(AttackTextProcessor.DamageReasons source) {
			if (source != AttackTextProcessor.DamageReasons.ABSORBED)
				holder.character.damageRecieved *= 0.8f;
		}
	}




}
