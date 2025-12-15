package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class EarthquakerShields extends Shields {

	public EarthquakerShields(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class StablePlatform extends EarthquakerShields {

		OnVariousScenarios oVE;

		public StablePlatform(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "StablePlatform";
			shieldHealth = 20;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 25;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Earthquaker";
			aggro = 0;
			oVE = new OnVariousScenarios(){
				@Override
				public void onDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source) {
					if(holder.shield instanceof StablePlatform && damagedActor.team == holder.character.team && source == AttackTextProcessor.DamageReasons.EARTHQUAKE
							&&  damagedActor.dC(holder.character.x,holder.character.y) <= 7*globalSize()
							)
						damagedActor.damageRecieved = 0;
				}
			};
		}

		@Override
		public void destroyOverridable() {
			destroyListener(oVE);
		}
	}

}
