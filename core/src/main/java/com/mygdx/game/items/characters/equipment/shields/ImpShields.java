package com.mygdx.game.items.characters.equipment.shields;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Imp;
import com.mygdx.game.items.characters.equipment.Shields;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.Conditions.ConditionNames.RITUAL;

public class ImpShields extends Shields {

	public ImpShields(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}


	public static class ImpDemonizeShield extends ImpShields {
		public ImpDemonizeShield(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Demonize shield";
			shieldHealth = 40;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Imp";
			aggro = 0;
		}

		@Override
		public void update() {
			int counter = 0;
			for (Actor a : actors){
				if(a.conditions.hasStatus(Conditions.ConditionNames.DEMONIZED))
					counter++;
			}
			shieldDefense = counter * 10;
			holder.refresh();
		}
	}

	public static class ImpRitualShield extends ImpShields {
		public ImpRitualShield(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Ritual shield";
			shieldHealth = 22;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Imp";
			aggro = 0;
		}

		@Override
		public void update() {
			if(holder.character.conditions.hasStatus(RITUAL))
				shieldDefense = 12;
			holder.refresh();
		}
	}

	public static class DarkWings extends ImpShields {
		public DarkWings(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Dark wings";
			shieldHealth = 66;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Imp";
			aggro = 0;
			if (effectiveInstantiation) {
				holder.character.airborn = true;
				holder.character.idleTexture = "animaWinged";
			}
		}

		public void update() {
			holder.character.airborn = true;
			for(Actor a : actors){
				if(a.totalTeam == holder.character.totalTeam && a.conditions.hasStatus(RITUAL)) {
					((Conditions.Ritual) a.conditions.getStatus(RITUAL)).wingedRitual = true;
					((Conditions.Ritual) a.conditions.getStatus(RITUAL)).speed = 3;
				}
			}
		}

		@Override
		public void destroyOverridable() {
			holder.character.idleTexture = "anima";
		}
	}

	public static class Daredevil extends ImpShields {
		boolean daredevilToggled = false;
		public Daredevil(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			shieldName = "Daredevil";
			shieldHealth = 333;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 66;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			equippableBy = "Imp";
			aggro = 0;
			if(effectiveInstantiation) {
				holder.abilities.add(new Ability("Daredevil", "Daredevil", 0, 83, 60, (float) globalSize() / 2) {
					@Override
					public void active() {
						isItActive = true;
						chara.cancelAttackMode();
						((Imp) holder).targetProcessor.reset();
						holder.character.actionDecided();
						holder.character.movementLock = true;
						holder.character.path.pathReset();
					}

					@Override
					public void cancelActivation() {
						isItActive = false;
						((Imp) holder).markCoords = null;
						((Imp) holder).targetProcessor.reset();
						holder.character.movementLock = false;
						holder.character.path.pathReset();
					}

					@Override
					public void finished() {
						isItActive = false;
						((Imp) holder).markCoords = null;
						((Imp) holder).targetProcessor.reset();
						holder.character.movementLock = false;
						holder.character.path.pathReset();
					}
				});
			}

		}

		public void update() {
			boolean checker = false;
			Ability daredevil = null;
			for(Ability a : holder.abilities)
				if (a.name.equals("Daredevil")) {
					checker = true;
					daredevil = a;
				}

			if(!checker){
				daredevil = new Ability("Daredevil", "Daredevil", 0, 83	,60, (float) globalSize() /2){
					@Override
					public void active() {
						isItActive = true;
						chara.cancelAttackMode();
						((Imp) holder).targetProcessor.reset();
						holder.character.actionDecided();
						holder.character.movementLock = true;
						holder.character.path.pathReset();
					}

					@Override
					public void cancelActivation() {
						isItActive = false;
						((Imp) holder).markCoords = null;
						((Imp) holder).targetProcessor.reset();
						holder.character.movementLock = false;
						holder.character.path.pathReset();
					}

					@Override
					public void finished() {
						isItActive = false;
						((Imp) holder).markCoords = null;
						((Imp) holder).targetProcessor.reset();
						holder.character.movementLock = false;
						holder.character.path.pathReset();
					}
				};
				holder.abilities.add(daredevil);

			}
			if(daredevil.isItActive && holder.character.isPermittedToAct()){
				daredevil.finished();
				if(!daredevilToggled) {
					daredevilToggled = true;
					for (Actor a : actors)
						if (a.totalTeam == holder.character.totalTeam) {
							if (a.conditions.hasStatus(RITUAL) && a.conditions.getStatus(RITUAL).getTurns() <= 1) {
								a.conditions.condition(RITUAL);
								a.conditions.getStatus(RITUAL).setTurns(1);
							} else if (!a.conditions.hasStatus(RITUAL)) {
								a.conditions.condition(RITUAL);
								a.conditions.getStatus(RITUAL).setTurns(1);
							}
						}
					holder.character.idleTexture = "animaMischievous2";
					float proportionalHealth = holder.currentHealth / holder.totalHealth;
					shieldHealth = 36.6f;
					shieldDefense = 6;
					holder.refresh();
					holder.currentHealth = proportionalHealth * holder.totalHealth;
				}
				else {
					holder.character.idleTexture = "anima";
					float proportionalHealth = holder.currentHealth / holder.totalHealth;
					daredevilToggled = false;
					shieldHealth = 333;
					shieldDefense = 66;
					holder.refresh();
					holder.currentHealth = proportionalHealth * holder.totalHealth;
				}
			}
			if(daredevilToggled){
				for(Actor a : actors)
					if(a.totalTeam == holder.character.totalTeam){
						if (a.conditions.hasStatus(RITUAL) && a.conditions.getStatus(RITUAL).getTurns() <= 1) {
							a.conditions.condition(RITUAL);
							a.conditions.getStatus(RITUAL).setTurns(1);
						} else if (!a.conditions.hasStatus(RITUAL)) {
							a.conditions.condition(RITUAL);
							a.conditions.getStatus(RITUAL).setTurns(1);
						}
					}
			}
		}

		@Override
		public void destroyOverridable() {
			holder.character.idleTexture = "anima";
			holder.abilities.removeIf(a -> a.name.equals("Daredevil"));
		}
	}




}
