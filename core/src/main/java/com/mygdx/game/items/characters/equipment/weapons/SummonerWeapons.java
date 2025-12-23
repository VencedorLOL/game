package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.allaies.Summon;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Summoner;
import com.mygdx.game.items.characters.equipment.Weapons;

public class SummonerWeapons extends Weapons{


	public SummonerWeapons(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class SummonerInstrument extends SummonerWeapons {
		public SummonerInstrument(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Instrument";
			weaponHealth = 0;
			weaponDamage = 20;
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
			equippableBy = "Summoner";
			aggro = 0;
		}
	}

	public void update() {
		if(!Summoner.summons.isEmpty()){
			weaponDamage = 10;
			for (Summon s : Summoner.summons)
				s.conditions.status(Conditions.ConditionNames.POWERED);
		}
	}


	@Override
	public void destroyOverridable() {
		for (Summon s : Summoner.summons)
			s.conditions.remove(Conditions.ConditionNames.POWERED);
		weaponDamage = 20;
	}
}
