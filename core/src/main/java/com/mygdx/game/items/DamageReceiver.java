package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.EARTHQUAKE;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.ELECTRIC;

public interface DamageReceiver {

	ArrayList<DamageReceiver> damageReceivers = new ArrayList<>();

	static DamageReceiver getDRInPos(float x, float y){
		ArrayList<DamageReceiver> temp = new ArrayList<>();
		for(DamageReceiver d : damageReceivers){
			if (d instanceof Actor)
				temp.add(0,d);
			else
				temp.add(d);
		}
		for(DamageReceiver d : temp){
			if(d.getX() == x && d.getY() == y)
				return d;
		}
		return null;
	}

	/**
	 * Returns true if damage is:
	 * Electric
	 * Burn
	 * Universal
	 * Frostbite
	 * Pressure
	 * Radiation
	 */
	static boolean damageIgnoresDef(AttackTextProcessor.DamageReasons reason){
		return !(reason != ELECTRIC && reason !=  AttackTextProcessor.DamageReasons.BURNT
				&& reason != EARTHQUAKE && reason != AttackTextProcessor.DamageReasons.UNIVERSAL
				&& reason != AttackTextProcessor.DamageReasons.FROSTBITE && reason != AttackTextProcessor.DamageReasons.PRESSURE
				&& reason != AttackTextProcessor.DamageReasons.RADIATION);
	}
	/**
	 * Returns true if damage is:
	 * Piercing
	 */
	static boolean damageHalvesDef(AttackTextProcessor.DamageReasons reason){
		return reason == AttackTextProcessor.DamageReasons.PIERCING;
	}


	void damage(float damage, AttackTextProcessor.DamageReasons reason, Entity lastDamager);
	float getX();
	float getY();
	float[] getPos();
	float getBase();
	float getHeight();
	float[] getSize();

	float getTotalHealth();
	float getHealth();
	byte totalTeam();
	boolean getIsDead();

	/**
	 * @return: Returns true if there's an immunity on the given list that coincides with the damage reason given.
	 */
	static boolean checkImmunities(AttackTextProcessor.DamageReasons reason, AttackTextProcessor.DamageReasons... immunities){
		if(immunities == null)
			return false;
		for(AttackTextProcessor.DamageReasons i : immunities)
			if (i == reason)
				return true;
		return false;
	}

	
	
}
