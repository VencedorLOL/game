package com.mygdx.game.items;

import java.util.*;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.FieldEffects.*;
import static com.mygdx.game.items.Hazards.hazards;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTurnPass;

public class TurnManager {
	private static boolean isTurnApproved;
	private static long turnCount;
	private static ArrayList<ActorAndBoolean> finalizedToChoose /*and ready to act!*/ = new ArrayList<>();
	private static boolean canTurnFinish;
	@SuppressWarnings("all")
	private static final OnVariousScenarios oVS = new OnVariousScenarios(){
		@Override
		public void onStageChange() {
			reset();
		}
	};
	private static boolean willTurnRun = true;

	static void reset(){
		timer = 0;
		finalList = new ArrayList<>();
		canTurnFinish = false;
		turnCount = 0;
		isTurnApproved = false;
		finalizedToChoose = new ArrayList<>();
		for (Actor a : actors){
			a.setDidItAct(false);
			a.permittedToAct = false;
		}

	}


	@SuppressWarnings("all")
	public static void turnStop(){
		willTurnRun = false;
	}

	private static int timer = 0;
	public static void turnStopTimer(int time){
		timer = time;
		willTurnRun = false;
	}

	@SuppressWarnings("all")
	public static void turnResume(){
		if(timer == 0)
			willTurnRun = true;
		else
			print("Can't resume turn as there's an active timer!");
	}

	@SuppressWarnings("all")
	public static void turnCancel(){
		willTurnRun = false;
		reset();
	}

	@SuppressWarnings("all")
	public static void turnRestart(){
		reset();
		willTurnRun = true;
	}

	public static boolean isTurnRunning(){
		return isTurnApproved;
	}

	private static ArrayList<Turnable> finalList;


	public static void finalizedChoosing(Turnable actor){
		if(valueSearcher(finalizedToChoose,actor) != null)
			valueSearcher(finalizedToChoose,actor).setBool(true);
		else
			finalizedToChoose.add(new ActorAndBoolean(true,actor));

	}

	private static boolean checkForTurn(ArrayList<Actor> listOfActors){
		listOfActors.removeIf(a -> a.isDead);
		int numberOfTrueFinalizedChoosers = 0;
		for (ActorAndBoolean f : finalizedToChoose){
			if(f.getBool())
				numberOfTrueFinalizedChoosers++;
		}
		return listOfActors.size() == numberOfTrueFinalizedChoosers && !finalizedToChoose.isEmpty();
	}

	public static boolean isDecidingWhatToDo(Turnable entity){
		for (ActorAndBoolean f : finalizedToChoose)
			if(f.actor == entity)
				return !f.bool;
		finalizedToChoose.add(new ActorAndBoolean(false, entity));
		return true;
	}

	public static boolean cancelDecision(Turnable actor){
		if(!isTurnApproved){
			if(valueSearcher(finalizedToChoose,actor) != null)
				valueSearcher(finalizedToChoose,actor).setBool(false);
			else
				finalizedToChoose.add(new ActorAndBoolean(false,actor));
			return true;
		} return false;
	}


	public static long getTurnCount(){return turnCount;}

	public static void turnLogic() {
		finalizedToChoose.removeIf(a -> a.actor.getIsDead());
		if(timer > 0){ timer--;
			if(timer == 0) willTurnRun = true;}
		if (willTurnRun) {
			if (isTurnApproved) {
				finalList = new ArrayList<>();
				for (Turnable t : turnables)
					if(!t.getIsDead())
						finalList.add(t);
				Collections.shuffle(finalList);
				finalList.sort((o1, o2) -> Float.compare(o2.getSpeed(), o1.getSpeed()));
				act();
				if (canTurnFinish) {
					canTurnFinish = false;
					turnCount++;
					for (ActorAndBoolean f : finalizedToChoose)
						f.setBool(false);
					for (Turnable t : turnables)
						t.setDidItAct(false);
					for(FieldEffects f : fieldEffects)
						f.didFieldAct = false;
					for(Hazards h : hazards)
						h.didHazardAct = false;
					isTurnApproved = false;
					triggerOnTurnPass();
				}
			} else if (checkForTurn(actors)) {
				isTurnApproved = true;
			}
		}
	}


	private static void act(){
		for (FieldEffects f : fieldEffects)
			if (f != null && !f.didFieldAct) {
				f.canFieldAct = true;
				f.didFieldAct = true;
				return;
			}
		for (FieldEffects f : fieldEffects)
			if (f.canFieldAct) {
				return;
			}
		for (Turnable a : finalList)
			if (a.didItAct() && a.isPermittedToAct())
				return;

		for (Turnable a : finalList)
			if (a != null && !a.didItAct() && !a.getIsDead()) {
				a.letAct();
				return;
			}
		for (Hazards f : hazards)
			if (f != null && !f.didHazardAct) {
				f.canHazardAct = true;
				f.didHazardAct = true;
				return;
			}
		for (Hazards f : hazards)
			if (f.canHazardAct) {
				return;
			}
		canTurnFinish = true;
	}



	@SuppressWarnings("all")
	public static boolean nullPointerExceptionChecker(List<?> arrayList, int exceptionPossibleProvocator){
		try{
			arrayList.get(exceptionPossibleProvocator);
			return false;
		}
		catch (NullPointerException ignored){
			return true;
		}
	}

	private static ActorAndBoolean valueSearcher(ArrayList<ActorAndBoolean> theListInQuestion, Turnable theValueInQuestion){
		for (ActorAndBoolean l : theListInQuestion){
			if(l.actor == theValueInQuestion)
				return l;
		}
		return null;
	}

	@SuppressWarnings("all")
	private static int valuePosition(ArrayList<ActorAndBoolean> theListInQuestion, Turnable theValueInQuestion){
		for (int l = 0; l < theListInQuestion.size(); l++){
			if(theListInQuestion.get(l).actor == theValueInQuestion)
				return l;
		}
		return -1;
	}


	public static class ActorAndBoolean {
		boolean bool;
		Turnable actor;

		public ActorAndBoolean(boolean bool, Turnable actor){ this.bool = bool; this.actor = actor; }

		public boolean getBool() {return bool;}

		public void setBool (boolean bool) {this.bool = bool; }

	}
	public static ArrayList<Turnable> turnables = new ArrayList<>();
	public interface Turnable {
		float getSpeed();

		boolean didItAct();
		boolean getIsDead();
		boolean isPermittedToAct();

		void setDidItAct(boolean didItAct);
		void permitToAct();
		void letAct();


	}

}
