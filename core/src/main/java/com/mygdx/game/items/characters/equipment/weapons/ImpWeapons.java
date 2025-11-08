package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Imp;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;


public class ImpWeapons extends Weapons{


	public ImpWeapons(CharacterClasses holder) {
		super(holder);
	}

	public static class ImpDagger extends ImpWeapons {
		public ImpDagger(CharacterClasses holder) {
			super(holder);
			weaponName = "ImpDagger";
			weaponHealth = 0;
			weaponDamage = 12;
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
			equippableBy = "Imp";
			aggro = 0;
		}

		OnVariousScenarios oVE = new OnVariousScenarios() {
			public void onDamagedActor(Actor damagedActor) {
				if (damagedActor.lastDamager == holder.character && damagedActor.conditions.hasStatus(Conditions.ConditionNames.DEMONIZED)) {
					damagedActor.damageRecieved += Math.max(damagedActor.totalDefense * 0.25f,damagedActor.totalDefense > 4 ? 5 : damagedActor.totalDefense);

				}
			}
		};

		public void destroyOverridable() {
			destroyListener(oVE);
		}
	}

	public static class ImpHastyDagger extends ImpWeapons {
		public ImpHastyDagger(CharacterClasses holder) {
			super(holder);
			weaponName = "ImpHastyDagger";
			weaponHealth = 0;
			weaponDamage = 12;
			weaponSpeed = 2;
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
			equippableBy = "Imp";
			aggro = 0;
		}

		public void update() {
			for (Actor a : actors)
				if(a.team == holder.character.team && a.conditions.hasStatus(Conditions.ConditionNames.RITUAL)) {
					((Conditions.Ritual) a.conditions.getStatus(Conditions.ConditionNames.RITUAL)).speed = 3;
					((Conditions.Ritual) a.conditions.getStatus(Conditions.ConditionNames.RITUAL)).actingSpeed = 3;
				}

		}
	}

	public static class MassDemonizeDagger extends ImpWeapons {
		public MassDemonizeDagger(CharacterClasses holder) {
			super(holder);
			weaponName = "MassDemonizeDagger";
			weaponHealth = 0;
			weaponDamage = 60;
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
			equippableBy = "Imp";
			aggro = 0;
			holder.abilities.clear();
			holder.abilities.add((new Ability("RitualCancelled", "RitualCancelled", 0, 75	,76, (float) globalSize() /2){
				@Override
				public void keybindActivate() {
					text("But it was blocked.",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f ,120, TextureManager.Fonts.ComicSans,32,240,20,40,1,30);
				}

				@Override
				public void cancelActivation() {
					isItActive = false;
					holder.character.cancelDecision();
				}

				@Override
				public void finished() {
					((Imp) holder).resetAbilities();
				}
			}));

			holder.abilities.add(new Ability("MassDemon", "Mass Demonize", 12, 87	,30, (float) globalSize() /2){
				@Override
				public void active() {
					isItActive = true;
					chara.cancelAttackMode();
					((Imp) holder).targetProcessor.reset();
				}

				@Override
				public void cancelActivation() {
					isItActive = false;
					((Imp) holder).markCoords = null;
					((Imp) holder).targetProcessor.reset();
				}

				@Override
				public void finished() {
					((Imp) holder).resetAbilities();
				}
			});

		}

		@Override
		public void update() {
			if(holder.abilities.get(1).isItActive && isDecidingWhatToDo(holder.character)){
				((Imp) holder).markCoords = new float[]{holder.character.x,holder.character.y};
				holder.character.actionDecided();
			}

			if(holder.abilities.get(1).isItActive && holder.character.isPermittedToAct()){
				for(Actor a : actors){
					if(a.team != holder.character.team) {
						a.conditions.status(Conditions.ConditionNames.DEMONIZED);
						a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED).setTurns(((Imp) holder).turnsMark);
						((Conditions.Demonized) a.conditions.getStatus(Conditions.ConditionNames.DEMONIZED)).getBeneficiary(holder);
					}
				}
				holder.abilities.get(1).finished();
				holder.character.spendTurn();
			}
		}

		@Override
		public void destroyOverridable() {
			if(holder instanceof Imp) {
				holder.abilities.add(new Ability("Ritual", "Ritual", 12, 75	,76, (float) globalSize() /2){
					@Override
					public void active() {
						((Imp) holder).cancelDemonize();
						isItActive = true;
						holder.character.actionDecided();
						((Imp) holder).targetProcessor.reset();
					}

					@Override
					public void cancelActivation() {
						isItActive = false;
						holder.character.cancelDecision();
					}

					@Override
					public void finished() {
						((Imp) holder).resetAbilities();
					}
				});

				holder.abilities.add(new Ability("Demon", "Demonize", 12, 87	,30, (float) globalSize() /2){
					@Override
					public void active() {
						((Imp) holder).cancelRitual();
						isItActive = true;
						chara.cancelAttackMode();
						((Imp) holder).targetProcessor.reset();
					}

					@Override
					public void cancelActivation() {
						isItActive = false;
						((Imp) holder).markCoords = null;
						((Imp) holder).targetProcessor.reset();
					}

					@Override
					public void finished() {
						((Imp) holder).resetAbilities();
					}
				});
			}
		}
	}

}
