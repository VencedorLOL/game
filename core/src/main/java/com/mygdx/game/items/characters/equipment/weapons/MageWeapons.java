package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class MageWeapons extends Weapons{


	public MageWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class BasicWand extends MageWeapons {
		public BasicWand(CharacterClasses holder) {
			super(holder);
			weaponName = "BasicWand";
			weaponHealth = 0;
			weaponDamage = 5;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 30;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 150;
			weaponMagicHealing = 0;
			equippableBy = "Mage";
			aggro = 0;

		}
	}





}
