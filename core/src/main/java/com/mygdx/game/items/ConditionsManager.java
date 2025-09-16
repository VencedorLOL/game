package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.print;

public class ConditionsManager {

	Actor owner;
	public ArrayList<Conditions> conditions = new ArrayList<>();

	public ConditionsManager(Actor owner){
		this.owner = owner;
	}


	/**
	 * Only adds an effect to a not-affected actor, it doesn't refresh it.
	 **/
	public void condition(Conditions.ConditionNames condition){
		for (Conditions c : conditions){
			if(c.name.equals(condition.name))
				return;
		}
		conditions.add(conditionBuilder(condition));
	}

	/**
	 * Adds an effect and refreshes it.
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



	public void remove(Conditions.ConditionNames condition){
		conditions.removeIf(c -> c.name.equals(condition.name));
	}

	private Conditions conditionBuilder(Conditions.ConditionNames conditionName){
		switch (conditionName){
			case BURNING: 		 return new Conditions.Burning(owner);
			case BURNING_BRIGHT: return new Conditions.BurningBright(owner);
			case MELTING:  		 return new Conditions.Melting(owner);
			case SUBLIMATING: 	 return new Conditions.Sublimating(owner);
			case FROZEN: 		 return new Conditions.Frozen(owner);
			case HYPERTHERMIA:   return new Conditions.Hyperthermia(owner);
			case HYPOTHERMIA:    return new Conditions.Hypothermia(owner);
			case ONE_FOR_ALL: 	 return new Conditions.OneForAll(owner);
			case FROSTBITE: 	 return new Conditions.Frostbite(owner);
		}
		return null;
	}

	public void render(){
		if(owner instanceof Character){
			int aid = 0; int aidY = 0;
			for(Conditions c : conditions)
				if (c.texture != null) {
				TextureManager.addToFixatedList(c.texture,150 + 40f*aid, 100 + 40f*aidY,1,0,6,6);
					if(++aid > 5) {
						aid = 0;
						aidY++;
					}
				}
		}
	}



	public void onTurnPass(){
		for (Conditions c : conditions)
			c.onTurn();
		for (Conditions c : conditions)
			if (c.tickDownOnTurn)
				c.turnsActive--;
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

	public void onAttackDecided(){
		for (Conditions c : conditions)
			c.onAttackDecided();
	}

	public void onMove(){
		for (Conditions c : conditions)
			c.onMove();
	}


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
