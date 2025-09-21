package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class SwordMageShields extends Shields {

	public SwordMageShields(CharacterClasses holder) {
		super(holder);
	}

	public float damageMultiplier;
	public float manaCost;

	public static class CrystalizedShield extends SwordMageShields {

		public CrystalizedShield(CharacterClasses holder) {
			super(holder);
			shieldName = "CrystalizedShield";
			shieldHealth = 30;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 5;
			shieldRange = 0;
			shieldTempDefense = 0;
			shieldRainbowDefense = 0;
			shieldMana = 140;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "SwordMage";
			aggro = 0;
			manaCost = -.75f;
		}
	}




}
