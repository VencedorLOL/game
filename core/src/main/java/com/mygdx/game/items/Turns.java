package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actors;
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
	static List<ActorAndSpeed> listOfSpeeds = new ArrayList<>();
	private static boolean willTurnRun = true;

	static void reset(){
		zero     = new ArrayList<>();
		one      = new ArrayList<>();
		two      = new ArrayList<>();
		three    = new ArrayList<>();
		four     = new ArrayList<>();
		five     = new ArrayList<>();
		six      = new ArrayList<>();
		seven    = new ArrayList<>();
		eight    = new ArrayList<>();
		nine     = new ArrayList<>();
		ten      = new ArrayList<>();
		tenPlus  = new ArrayList<>();
		canTurnFinish = false;
		turnCount = 0;
		finalizedToChoose = new ArrayList<>();
		for (Actor a : actors){
			a.setDidItAct(false);
			a.permittedToAct = false;
		}

	}


	public static void turnStop(){
		willTurnRun = false;
		reset();
	}

	public static void turnResume(){
		reset();
		willTurnRun = true;
	}

	// this is the best way of doing it, trust.
	private static ArrayList<ActorAndSpeed> zero     = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> one      = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> two      = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> three    = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> four     = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> five     = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> six      = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> seven    = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> eight    = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> nine     = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> ten      = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> tenPlus  = new ArrayList<>();
	private static ArrayList<ActorAndSpeed> finalList;


	public static void actorsFinalizedChoosing(Actor actor){
		if(valueSearcher(finalizedToChoose,actor) != null)
			valueSearcher(finalizedToChoose,actor).setBool(true);
		else
			finalizedToChoose.add(new ActorAndBoolean(true,actor));

	}

	private static boolean checkForTurn(ArrayList<Actor> listOfActors){
		int numberOfTrueFinalizedChoosers = 0;
		for (ActorAndBoolean f : finalizedToChoose){
			if(f.getBool())
				numberOfTrueFinalizedChoosers++;
		}
		if (listOfActors.size() == numberOfTrueFinalizedChoosers)
			print("aproveddadw. turn can start");

		return listOfActors.size() == numberOfTrueFinalizedChoosers;
	}

	// cant believe how i overcomplicated this in the past lmfao and it didnt even work lmfaoo
	public static boolean isDecidingWhatToDo(Actor entity){
		for (ActorAndBoolean f : finalizedToChoose)
			if(f.actor == entity)
				return !f.bool;
		finalizedToChoose.add(new ActorAndBoolean(false, entity));
		return false;
	}

	public static long getTurnCount(){return turnCount;}

	public static void turnLogic() {
		if (willTurnRun) {
			if (isTurnApproved) {
				listOfSpeeds = new ArrayList<>();
				for (Actor e : actors) {
					if (e instanceof Enemy)
						listOfSpeeds.add(new ActorAndSpeed(e.actingSpeed, e));
					if (e instanceof Character)
						listOfSpeeds.add(new ActorAndSpeed(((Character) e).character.attackSpeed, e));
				}
				refreshSpeedAddAndSort(listOfSpeeds);
				act();
				if (canTurnFinish) {
					canTurnFinish = false;
					turnCount++;
					for (ActorAndBoolean f : finalizedToChoose)
						f.setBool(false);
					for (ActorAndSpeed l : listOfSpeeds)
						l.getActor().setDidItAct(false);
					isTurnApproved = false;
					triggerOnTurnPass();
				}
			} else if (checkForTurn(actors)) {
				isTurnApproved = true;
			}
		}
	}


	private static void act(){
		for (ActorAndSpeed a : finalList)
			if (a.getActor().didItAct() && a.getActor().isPermittedToAct()) {
				return;
			}
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
				actor.permitToMove();
				actor.didItAct = true;
			}
		}
	}

	private static class ActorAndBoolean {
		boolean bool;
		Actor actor;

		public ActorAndBoolean(){}

		public ActorAndBoolean(boolean bool, Actor actor){ this.bool = bool; this.actor = actor; }

		public boolean getBool() {return bool;}

		public void setBool (boolean bool) {this.bool = bool; }

	}



	private static void refreshSpeedAddAndSort(List<ActorAndSpeed> listOfSpeeds){

		ArrayList<ArrayList<ActorAndSpeed>> listOfLists = new ArrayList<>();
		listOfLists.add(tenPlus);
		listOfLists.add(ten);
		listOfLists.add(nine);
		listOfLists.add(eight);
		listOfLists.add(seven);
		listOfLists.add(six);
		listOfLists.add(five);
		listOfLists.add(four);
		listOfLists.add(three);
		listOfLists.add(two);
		listOfLists.add(one);
		listOfLists.add(zero);
		//empty the lists
		for (ArrayList<ActorAndSpeed> l : listOfLists)
			l.clear();
		for (ActorAndSpeed e : listOfSpeeds)
			switch (e.actor.actingSpeed) {
				case 0:  zero.add  (new ActorAndSpeed(e.speed, e.actor)); break;
				case 1:  one.add   (new ActorAndSpeed(e.speed, e.actor)); break;
				case 2:  two.add   (new ActorAndSpeed(e.speed, e.actor)); break;
				case 3:  three.add (new ActorAndSpeed(e.speed, e.actor)); break;
				case 4:  four.add  (new ActorAndSpeed(e.speed, e.actor)); break;
				case 5:  five.add  (new ActorAndSpeed(e.speed, e.actor)); break;
				case 6:  six.add   (new ActorAndSpeed(e.speed, e.actor)); break;
				case 7:  seven.add (new ActorAndSpeed(e.speed, e.actor)); break;
				case 8:  eight.add (new ActorAndSpeed(e.speed, e.actor)); break;
				case 9:  nine.add  (new ActorAndSpeed(e.speed, e.actor)); break;
				case 10: ten.add   (new ActorAndSpeed(e.speed, e.actor)); break;
				default: if (e.actor.actingSpeed > 10) tenPlus.add(new ActorAndSpeed(e.speed, e.actor)); break;
			}
		//first shuffling to make the double speed ties random, then ordering. merging them in an easy-to-use arrlist
		for (ArrayList<ActorAndSpeed>  l : listOfLists) {
			Collections.shuffle(l);
			l.sort((o1, o2) -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		}
		finalList = new ArrayList<>(tenPlus);
		for (ArrayList<ActorAndSpeed>  l : listOfLists)
			finalList.addAll(l);
	} //because of how i added all lists top to bottom to listOfLists, all lists' contents will pour in order of, first, actingSpeed, then speed, to finalList

}
