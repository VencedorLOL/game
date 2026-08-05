package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.weapons.TrapperWeapons;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Hazards.hazards;
import static com.mygdx.game.items.OnVariousScenarios.*;
import static java.lang.Math.max;

public class Trapper extends CharacterClasses {

	public boolean willTrap;

	public Trapper() {
		super();
		name = "Trapper";
		health = 25;
		damage = 5;
		speed = 6;
		attackSpeed = 7;
		defense = 0;
		range = 3;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		getEquipment();
		reset();
		currentHealth = totalHealth;
	}


	public boolean onAttackDecided() {
		willTrap = weapon == null || !(weapon instanceof TrapperWeapons) || ((TrapperWeapons) weapon).willTrap();
		return true;
	}

	public void updateOverridable() {
		if(willTrap && character.permittedToAct && !character.attacks.isEmpty()){
			if(weapon != null && weapon instanceof TrapperWeapons) {
				Trap trap = ((TrapperWeapons) weapon).throwTrap(character.attacks.get(0).targetX, character.attacks.get(0).targetY, totalDamage, character);
				if(trap != null) {
					hazards.add(trap);
					character.finalizedTurn();
				}
			}
			else {
				hazards.add(new Trap(character.attacks.get(0).targetX, character.attacks.get(0).targetY, max(totalDamage - 15, 1), character));
				character.finalizedTurn();
			}
			willTrap = false;
		}


		
	}



	public static class Trap extends Hazards {
		public float damage;
		public ArrayList<Actor> triggered = new ArrayList<>();
		Entity owner;

		public Trap(float x, float y){
			super(x,y,globalSize(),globalSize());
			name = "DamagableTrap";
			texture = "Spikes";
		}


		public Trap(float x, float y, float damage, Entity owner){
			super(x,y,globalSize(),globalSize());
			name = "DamagableTrap";
			texture = "Spikes";
			this.damage = damage;
			this.owner = owner;
		}

		public void update() {
			Actor victim = stepTrigger();
			if(canHazardAct){
				triggered = new ArrayList<>();
				finishedActing();
			}
			if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0 && !triggered.contains(victim) && actorVerification(victim)) {
				victim.damage(damage, AttackTextProcessor.DamageReasons.PIERCING, null);
				triggered.add(victim);
			}
		}



	}



	public static class DamagableTrap extends Trap implements DamageReceiver {
		public float maxHp;
		public float hp;
		public byte team = 1;
		public AttackTextProcessor.DamageReasons[] immunities;

		{
			damageReceivers.add(this);
		}


		public DamagableTrap(float x, float y){
			super(x,y);
			name = "DamagableTrap";
			texture = "Spikes";
			immunities = new AttackTextProcessor.DamageReasons[]{};
		}

		public DamagableTrap(float x, float y, float damage, Entity owner,float health){
			super(x,y);
			maxHp = health;
			hp = health;
			name = "DamagableTrap";
			texture = "Spikes";
			this.owner = owner;
			this.damage = damage;
			immunities = new AttackTextProcessor.DamageReasons[]{};
		}

		// All checks are neccesary: we don't want the trap ticking if it's dead, thus we check if it is at the beggining of the method.
		// The trap may die inside updateOverridable(), thus we need to check if it's still alive once it has finished.
		public final void update(){
			if(hp <= 0){
				destroyHazard();
				queuedForDeletion = true;
				return;
			}
			updateOverridable();
			if (hp <= 0) {
				destroyHazard();
				queuedForDeletion = true;
			}
		}

		public void updateOverridable(){super.update();}

		@Override
		public void destroyHazard() {damageReceivers.remove(this);}

		@Override
		public void damage(float damage, AttackTextProcessor.DamageReasons damageReason, Entity lastDamager) {
			triggerOnDamaged(this,damageReason);
			if(damage > 0 && !DamageReceiver.checkImmunities(damageReason,immunities))
				hp -= damage;

		}
		@Override
		public float getTotalHealth(){return maxHp;}
		@Override
		public byte totalTeam(){return team;}

		@Override
		public boolean getIsDead() {
			return hp <= 0;
		}

		@Override
		public float getHealth(){return hp;}


	}













}