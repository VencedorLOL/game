package com.mygdx.game.items;

import java.util.ArrayList;

public class ConditionsManager {

	Actor owner;
	public ArrayList<Conditions> conditions = new ArrayList<>();
	public ArrayList<Conditions> queue = new ArrayList<>();


	public ConditionsManager(Actor owner){
		this.owner = owner;
	}

	public void queue(Conditions condition){
		queue.add(condition);
	}





	/**
	 * Adds an effect to a not-affected actor, but it doesn't refresh it.
	 **/
	public void condition(Conditions.ConditionNames condition){
		for (Conditions c : conditions){
			if(c.name.equals(condition.name))
				return;
		}
		if(queue == null)
			queue(conditionBuilder(condition));
		else
			conditions.add(conditionBuilder(condition));
	}

	/**
	 * Adds an effect or refreshes it.
	 * **/
	public void status(Conditions.ConditionNames condition){
		for (Conditions c : conditions){
			if(c.name.equals(condition.name)){
				c.setTurns(conditionBuilder(condition).turnsActive);
				return;
			}
		}
		conditions.add(conditionBuilder(condition));
	}

	/**
	 * Adds an effect and sums the effect length asked for to the one currently inflicted.
	 * **/
	@SuppressWarnings("all")
	public void addUp(Conditions.ConditionNames condition){
		for (Conditions c : conditions){
			if(c.name.equals(condition.name)){
				c.setTurns(conditionBuilder(condition).turnsActive + c.getTurns() > 0 ? c.getTurns() : 0);
				return;
			}
		}
		conditions.add(conditionBuilder(condition));
	}


	public boolean hasStatus(Conditions.ConditionNames condition){
		for (Conditions c : conditions)
			if(c.name.equals(condition.name))
				return true;
		return false;
	}

	public boolean hasStatus(Conditions condition){
		for (Conditions c : conditions)
			if(c.name.equals(condition.name))
				return true;
		return false;
	}

	public Conditions getStatus(Conditions.ConditionNames condition){
		for(Conditions c : conditions)
			if(c.name.equals(condition.name))
				return c;
		return null;
	}


	public void remove(Conditions.ConditionNames condition){
		for (Conditions c : conditions)
			c.destroyCondition();
		conditions.removeIf(c -> c.name.equals(condition.name));
	}

	public void render(){
		int aid = 0; int aidY = 0;
		for(Conditions c : conditions)
			if (c.texture != null) {
				c.render(150 + 150f*aid, 100 + 40f*aidY);
					if(++aid > 5) {
						aid = 0;
						aidY++;
					}
				}
	}

	public void onKill(){
		for(Conditions c : conditions)
			c.onKill();
	}

	public void onDeath(){
		for(Conditions c : conditions)
			c.onDeath();
	}

	public void onTurnPass(){
		for (Conditions c : conditions)
			c.onTurn();
		for (Conditions c : conditions) {
			if (c.tickDownOnTurn)
				--c.turnsActive;
			if(c.turnsActive == 0)
				c.destroyCondition();
		}
		conditions.removeIf(c -> c.turnsActive == 0);
	}

	public void onAttack(){
		for (Conditions c : conditions)
			c.onAttack();
	}

	public void onDamaged(AttackTextProcessor.DamageReasons reason){
		for (Conditions c : conditions)
			c.onDamaged(reason);
	}

	public boolean onAttackDecided(){
		boolean returnTrue = true;
		for (Conditions c : conditions)
			if(!c.onAttackDecided())
				returnTrue = false;
		return returnTrue;
	}

	@SuppressWarnings("all")
	public void onMove(){
		for (int i = 0; i < conditions.size(); i++)
			conditions.get(i).onMove();

	}


	public void onStageChange(){
		for (Conditions c : conditions)
			c.onStageChange();
		conditions.removeIf(c -> c.queuedForRemoval);
	}

	/**
	 * 0: maxHealth
	 * 1: damage
	 * 2: speed
	 * 3: actingSpeed
	 * 4: defense
	 * 5: range
	 * 6: mana
	 * 7: mana regenerated/turn
	 * 8: mana/use
	 * 9: magicDamage
	 * 11: aggro
	 * 12: temp. defense
	 **/
	public float getMultiplier(int type){
		float finalMultiplier = 1;
		for (Conditions c : conditions)
			switch (type) {
				case 0 : finalMultiplier *= c.getHealthMultiplier();      break;
				case 1 : finalMultiplier *= c.getDamageMultiplier();      break;
				case 2 : finalMultiplier *= c.getSpeedMultiplier();       break;
				case 3 : finalMultiplier *= c.getActingSpeedMultiplier(); break;
				case 4 : finalMultiplier *= c.getDefenseMultiplier();     break;
				case 5 : finalMultiplier *= c.getRangeMultiplier();       break;
				case 6 : finalMultiplier *= c.getManaMultiplier();        break;
				case 7 : finalMultiplier *= c.getManaPerTurnMultiplier(); break;
				case 8 : finalMultiplier *= c.getManaPerUseMultiplier();  break;
				case 9 : finalMultiplier *= c.getMagicDamageMultiplier(); break;

				case 11: finalMultiplier *= c.getAggroMultiplier();       break;

			}
		return finalMultiplier;
	}

	/**
	 * 0: maxHealth
	 * 1: damage
	 * 2: speed
	 * 3: actingSpeed
	 * 4: defense
	 * 5: range
	 * 6: mana
	 * 7: mana regenerated/turn
	 * 8: mana/use
	 * 9: magicDamage
	 * 11: aggro
	 * 12: temp. defense
	 **/
	public float getAdditive(int type){
		float finalSum = 0;
		for (Conditions c : conditions)
			switch (type) {
			case 0 : finalSum += c.getHealthAdditive();      break;
			case 1 : finalSum += c.getDamageAdditive();      break;
			case 2 : finalSum += c.getSpeedAdditive();       break;
			case 3 : finalSum += c.getActingSpeedAdditive(); break;
			case 4 : finalSum += c.getDefenseAdditive();     break;
			case 5 : finalSum += c.getRangeAdditive();       break;
			case 6 : finalSum += c.getManaAdditive();        break;
			case 7 : finalSum += c.getManaPerTurnAdditive(); break;
			case 8 : finalSum += c.getManaPerUseAdditive();  break;
			case 9 : finalSum += c.getMagicDamageAdditive(); break;

			case 11: finalSum += c.getAggroAdditive();       break;

		}
		return finalSum;
	}

	public Conditions conditionBuilder(Conditions.ConditionNames conditionName){
		switch (conditionName){
			case BURNING: 		 	return new Conditions.Burning			(owner);
			case BURNING_BRIGHT: 	return new Conditions.BurningBright		(owner);
			case MELTING:  		 	return new Conditions.Melting			(owner);
			case SUBLIMATING: 	 	return new Conditions.Sublimating		(owner);
			case FROZEN: 		 	return new Conditions.Frozen			(owner);
			case HYPERTHERMIA:   	return new Conditions.Hyperthermia		(owner);
			case HYPOTHERMIA:    	return new Conditions.Hypothermia		(owner);
			case ONE_FOR_ALL: 	 	return new Conditions.OneForAll			(owner);
			case FROSTBITE: 	 	return new Conditions.Frostbite			(owner);
			case MANA_HIT:		 	return new Conditions.ManaHit			(owner);
			case EVEN_FASTER:    	return new Conditions.EvenFaster		(owner);
			case PROTECTING:	 	return new Conditions.Protected			(owner);
			case PROTECTED:    	 	return new Conditions.Protecting		(owner);
			case RITUAL:		 	return new Conditions.Ritual    		(owner);
			case DEMONIZED:		 	return new Conditions.Demonized 		(owner);
			case CLOWDY: 		 	return new Conditions.Clowdy			(owner);
			case RAINY:   		 	return new Conditions.Rainy				(owner);
			case SNOWY: 		 	return new Conditions.Snowy				(owner);
			case SUNNY:			 	return new Conditions.Sunny				(owner);
			case STUNNED:		 	return new Conditions.Stunned			(owner);
			case COMING_THROUGH: 	return new Conditions.ComingThrough		(owner);
			case STELLAR_STORM:	 	return new Conditions.StellarStorm		(owner);
			case ELECTRIC_GROUND:	return new Conditions.ElectricGround	(owner);
			case AUGMENTED_GRAVITY:	return new Conditions.AugmentedGravity	(owner);
			case NUCLEAR_EVENT:		return new Conditions.NuclearEvent		(owner);
			case GLACIATION:		return new Conditions.Glaciation		(owner);
			case STELLAR_EXPLOSION:	return new Conditions.StellarExplosion	(owner);

		}
		return null;
	}

}
