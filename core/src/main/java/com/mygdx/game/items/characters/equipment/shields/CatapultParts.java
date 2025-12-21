package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

public class CatapultParts extends Shields {

	public CatapultParts(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}



	public static class MetalBucket extends CatapultParts {

		public MetalBucket(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Metal Bucket";
			shieldHealth = 10;
			shieldDamage = 5;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 25;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Catapult";
			aggro = 0;
		}
	}




}
