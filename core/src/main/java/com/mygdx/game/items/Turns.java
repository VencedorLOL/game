package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.FieldEffects.*;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTurnPass;

public class Turns implements Utils {
	private static boolean isTurnApproved;
	private static long turnCount;
	private static ArrayList<ActorAndBoolean> finalizedToChoose /*and ready to act!*/ = new ArrayList<>();
	private static boolean canTurnFinish;
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


	public static void turnStop(){
		willTurnRun = false;
	}

	private static int timer = 0;
	public static void turnStopTimer(int time){
		timer = time;
		willTurnRun = false;
	}

	public static void turnResume(){
		if(timer == 0)
			willTurnRun = true;
		else
			print("Can't resume turn as there's an active timer!");
	}

	public static void turnCancel(){
		willTurnRun = false;
		reset();
	}

	public static void turnRestart(){
		reset();
		willTurnRun = true;
	}

	public static boolean isTurnRunning(){
		return isTurnApproved;
	}

	private static ArrayList<ActorAndSpeed> finalList;


	public static void finalizedChoosing(Actor actor){
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

	public static boolean isDecidingWhatToDo(Actor entity){
		for (ActorAndBoolean f : finalizedToChoose)
			if(f.actor == entity)
				return !f.bool;
		finalizedToChoose.add(new ActorAndBoolean(false, entity));
		return true;
	}

	public static boolean cancelDecision(Actor actor){
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
		finalizedToChoose.removeIf(a -> a.actor.isDead);
		if(timer > 0){ timer--;
			if(timer == 0) willTurnRun = true;}
		if (willTurnRun) {
			if (isTurnApproved) {
				finalList = new ArrayList<>();
				for (Actor a : actors)
					if(!a.isDead)
						finalList.add(new ActorAndSpeed(a.totalActingSpeed * 100 + a.totalSpeed, a));
				Collections.shuffle(finalList);
				finalList.sort((o1, o2) -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
				act();
				if (canTurnFinish) {
					canTurnFinish = false;
					turnCount++;
					for (ActorAndBoolean f : finalizedToChoose)
						f.setBool(false);
					for (ActorAndSpeed l : finalList)
						l.getActor().setDidItAct(false);
					for(FieldEffects f : fieldEffects)
						f.didFieldAct = false;
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
		for (ActorAndSpeed a : finalList)
			if (a.getActor().didItAct() && a.getActor().isPermittedToAct())
				return;

		for (ActorAndSpeed a : finalList)
			if (a.getActor() != null && !a.getActor().didItAct() && !a.getActor().getIsDead()) {
				a.letAct();
				return;
			}
		canTurnFinish = true;
	}



	public static boolean nullPointerExceptionChecker(List<?> arrayList, int exceptionPossibleProvocator){
		try{
			arrayList.get(exceptionPossibleProvocator);
			return false;
		}
		catch (NullPointerException ignored){
			return true;
		}
	}

	private static ActorAndBoolean valueSearcher(ArrayList<ActorAndBoolean> theListInQuestion, Entity theValueInQuestion){
		for (ActorAndBoolean l : theListInQuestion){
			if(l.actor == theValueInQuestion)
				return l;
		}
		return null;
	}

	private static int valuePosition(ArrayList<ActorAndBoolean> theListInQuestion, Entity theValueInQuestion){
		for (int l = 0; l < theListInQuestion.size(); l++){
			if(theListInQuestion.get(l).actor == theValueInQuestion)
				return l;
		}
		return -1;
	}


	private static class ActorAndSpeed {
		Actor actor;
		int speed;

		public void setSpeed(int speed){this.speed = speed;}
		public void setActor(Actor actor) {this.actor = actor;}
		public int getSpeed() {return speed;}
		public Actor getActor() {return actor;}

		public ActorAndSpeed(int speed, Actor actor){
			setSpeed(speed);
			setActor(actor);
		}

		public void letAct(){
			if (!actor.didItAct()) {
				actor.permitToAct();
				actor.didItAct = true;
			}
		}
	}

	private static class ActorAndBoolean {
		boolean bool;
		Actor actor;

		public ActorAndBoolean(boolean bool, Actor actor){ this.bool = bool; this.actor = actor; }

		public boolean getBool() {return bool;}

		public void setBool (boolean bool) {this.bool = bool; }

	}
}
