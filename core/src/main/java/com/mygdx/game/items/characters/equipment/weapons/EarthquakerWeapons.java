package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Earthquaker;
import com.mygdx.game.items.characters.equipment.Weapons;

public class EarthquakerWeapons extends Weapons{
	public EarthquakerWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class EnergyCondensator extends EarthquakerWeapons {
		public EnergyCondensator (CharacterClasses holder) {
			super(holder);
			weaponName = "GroundStomper";
			weaponHealth = 0;
			weaponDamage = 5;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 50;
			weaponMagicDefense = 0;
			weaponMagicDamage = 15;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Earthquaker";
			aggro = 0;
		}

		public void onAttack() {
			if (holder instanceof Earthquaker && ((Earthquaker) holder).earthquakeProcessor.circle.center.x() == holder.character.x && ((Earthquaker) holder).earthquakeProcessor.circle.center.y() == holder.character.y){
				weaponMagicDamage = 45;
			}
			else
				weaponMagicDamage = 15;
		}
	}





}
