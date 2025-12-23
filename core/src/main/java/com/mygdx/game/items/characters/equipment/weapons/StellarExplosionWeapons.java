package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

public class StellarExplosionWeapons extends Weapons{
	public StellarExplosionWeapons(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class EnergyCondensator extends StellarExplosionWeapons {
		public EnergyCondensator (CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Energy condensator";
			weaponHealth = 0;
			weaponDamage = 0;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 75;
			weaponManaPerTurn = 25;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "StellarExplosion";
			aggro = 0;
		}

		public void onAttack() {
			if (holder.damageReason == AttackTextProcessor.DamageReasons.MAGICAL){
				holder.healThis(5);
				if(holder.tempDefense + 5 > 50) {
					if(holder.tempDefense < 50)
						holder.tempDefense = 50;
				} else
					holder.tempDefense += 5;
			}
		}
	}





}
