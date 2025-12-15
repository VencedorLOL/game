package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class HealerWeapons extends Weapons {
	public float weaponHealingAbilityBonus;

	public HealerWeapons(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class BlessedSword extends HealerWeapons {
		public BlessedSword(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "BlessedSword";
			weaponHealth = 0;
			weaponDamage = 10;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			weaponHealingAbilityBonus = (float) 10/9;
			equippableBy = "Healer";
			aggro = 0;

		}

	}

	public static class BestHealerSword extends HealerWeapons {
		public BestHealerSword(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "BestSword";
			weaponHealth = 0;
			weaponDamage = 100;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			weaponHealingAbilityBonus = 6;
			equippableBy = "Healer";
			aggro = 0;

		}
	}
}
