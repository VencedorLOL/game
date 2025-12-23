package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Summoner;
import com.mygdx.game.items.characters.equipment.Shields;

public class SummonerShields extends Shields {

	public SummonerShields(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}



	public static class SummonerFlag extends SummonerShields {
		public SummonerFlag(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Flag of the leader";
			shieldHealth = 15;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 5;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Summoner";
			aggro = 0;
		}

		@Override
		public void update() {
			if(!Summoner.summons.isEmpty()){
				shieldDefense = 5 + Summoner.summons.size()*2;
			}
		}
	}




}
