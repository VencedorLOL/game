package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ConditionsManager {

	Actor owner;
	public ArrayList<Conditions> conditions = new ArrayList<>();

	public ConditionsManager(Actor owner){
		this.owner = owner;
	}


	/**
	 * Adds an effect to a not-affected actor, but it doesn't refresh it.
	 **/
	public void condition(Conditions.ConditionNames condition){
		for (Conditions c : conditions){
			if(c.name.equals(condition.name))
				return;
		}
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

	public Conditions conditionBuilder(Conditions.ConditionNames conditionName){
		switch (conditionName){
			case BURNING: 		 return new Conditions.Burning		(owner);
			case BURNING_BRIGHT: return new Conditions.BurningBright(owner);
			case MELTING:  		 return new Conditions.Melting	    (owner);
			case SUBLIMATING: 	 return new Conditions.Sublimating  (owner);
			case FROZEN: 		 return new Conditions.Frozen	    (owner);
			case HYPERTHERMIA:   return new Conditions.Hyperthermia (owner);
			case HYPOTHERMIA:    return new Conditions.Hypothermia  (owner);
			case ONE_FOR_ALL: 	 return new Conditions.OneForAll    (owner);
			case FROSTBITE: 	 return new Conditions.Frostbite    (owner);
			case MANA_HIT:		 return new Conditions.ManaHit	    (owner);
			case EVEN_FASTER:    return new Conditions.EvenFaster   (owner);
			case PROTECTING:	 return new Conditions.Protected	(owner);
			case PROTECTED:    	 return new Conditions.Protecting   (owner);
			case RITUAL:		 return new Conditions.Ritual       (owner);
			case DEMONIZED:		 return new Conditions.Demonized    (owner);
			case CLOWDY: 		 return new Conditions.Clowdy       (owner);
			case RAINY:   		 return new Conditions.Rainy		(owner);
			case SNOWY: 		 return new Conditions.Snowy		(owner);
			case SUNNY:			 return new Conditions.Sunny		(owner);
		}
		return null;
	}

	public void render(){
		if(owner instanceof Character){
			int aid = 0; int aidY = 0;
			for(Conditions c : conditions)
				if (c.texture != null) {
				TextureManager.addToFixatedList(c.texture,150 + 150f*aid, 100 + 40f*aidY,1,0,3,3);
					if(++aid > 5) {
						aid = 0;
						aidY++;
					}
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

	public void onMove(){
		for (Conditions c : conditions)
			c.onMove();
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
				case 12: finalMultiplier *= c.getTempDefenseMultiplier(); break;
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
			case 12: finalSum += c.getTempDefenseAdditive(); break;
		}
		return finalSum;
	}



}
