package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.Entity;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Trapper;
import com.mygdx.game.items.characters.equipment.Weapons;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public abstract class TrapperWeapons extends Weapons{


	public abstract Trapper.Trap throwTrap(float x, float y, float damage,Entity character);



	public TrapperWeapons(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
	}

	public static class PrickyStones extends TrapperWeapons {


		@Override
		public Trapper.Trap throwTrap(float x, float y,float damage,Entity character) {
			return null;
		}

		public PrickyStones(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Pricky stones";
			weaponHealth = 0;
			weaponDamage = 5;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 1;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Trapper";
			aggro = 0;

		}


		public static class PStones extends Trapper.Trap {

			public PStones(float x, float y, float damage, Entity owner) {
				super(x, y, damage, owner);
				name = "PrickyStones";
				texture = "";
			}

			int pricked;
			public void update() {
				Actor victim = stepTrigger();
				if(canHazardAct){
					triggered = new ArrayList<>();
					finishedActing();
				}
				if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0 && !triggered.contains(victim)) {
					victim.damage(damage, AttackTextProcessor.DamageReasons.PIERCING, null);
					triggered.add(victim);
					pricked++;
				}
				if(pricked >= 2){
					deleteHazard(this);
				}
			}

		}



	}





}
