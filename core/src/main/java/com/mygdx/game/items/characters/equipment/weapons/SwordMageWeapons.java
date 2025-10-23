package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class SwordMageWeapons extends Weapons{

	public float damageMultiplier;
	public float manaCost;
	public SwordMageWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class SwordWand extends SwordMageWeapons {
		public SwordWand(CharacterClasses holder) {
			super(holder);
			weaponName = "SwordWand";
			weaponHealth = 0;
			weaponDamage = 30;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 10;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "SwordMage";
			aggro = 0;
			damageMultiplier = .5f;
		}
	}





}
