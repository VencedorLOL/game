package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class TankWeapons extends Weapons{


	public TankWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class TankSword extends TankWeapons{
		public TankSword(CharacterClasses holder) {
			super(holder);
			weaponName = "TankSword";
			weaponHealth = 10;
			weaponDamage = 15;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 10;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Tank";
			aggro = 0;
		}
	}





}
