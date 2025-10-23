package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class SpeedsterWeapons extends Weapons{

	public SpeedsterWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class SpeedsterDagger extends SpeedsterWeapons {
		public SpeedsterDagger(CharacterClasses holder) {
			super(holder);
			weaponName = "SpeedsterDagger";
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
			equippableBy = "Speedster";
			aggro = 0;
		}

	}
}
