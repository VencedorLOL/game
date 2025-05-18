package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.OnVariousScenarios.onTurnPass;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTurnPass;
import static com.mygdx.game.items.Stage.betweenStages;

//TODO: Redid, but it still needs this:  method that returns true if a turn passed
// (takes nº of  turn, compares it to this nº of turn, if not the same returns true)
public class Turns implements Utils {
	static boolean didTurnJustPass;
	static long turnCount;
	static ArrayList<ActorAndBoolean> finalizedToChoose = new ArrayList<>();
	static int currentTurn;

	// this is the best way of doing it, trust.
	static ArrayList<ActorAndSpeed> zero     = new ArrayList<>();
	static ArrayList<ActorAndSpeed> one      = new ArrayList<>();
	static ArrayList<ActorAndSpeed> two      = new ArrayList<>();
	static ArrayList<ActorAndSpeed> three    = new ArrayList<>();
	static ArrayList<ActorAndSpeed> four     = new ArrayList<>();
	static ArrayList<ActorAndSpeed> five     = new ArrayList<>();
	static ArrayList<ActorAndSpeed> six      = new ArrayList<>();
	static ArrayList<ActorAndSpeed> seven    = new ArrayList<>();
	static ArrayList<ActorAndSpeed> eight    = new ArrayList<>();
	static ArrayList<ActorAndSpeed> nine     = new ArrayList<>();
	static ArrayList<ActorAndSpeed> ten      = new ArrayList<>();
	static ArrayList<ActorAndSpeed> tenPlus  = new ArrayList<>();
	static ArrayList<ActorAndSpeed> finalList;


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
			print("aproveddadw");

		return listOfActors.size() == numberOfTrueFinalizedChoosers;
	}

	public static boolean isDecidingWhatToDo(Actor entity){
		boolean foundItself = false;
		for (ActorAndBoolean f : finalizedToChoose){
			if (!f.bool)
				return true;
			if(f.actor == entity)
				foundItself = true;
		}
		if (!foundItself)
			finalizedToChoose.add(new ActorAndBoolean(false,entity));
		return false;
	}

	public static long getTurnCount(){return turnCount;}
	public static boolean getDidTurnJustPass(){return didTurnJustPass;}

	public static void turnLogic2(ArrayList<Actor> listOfActors) {
		if (betweenStages)
			currentTurn = 0;
		else if (checkForTurn(listOfActors)) {
			print("stated tun");
			didTurnJustPass = false;
			List<ActorAndSpeed> listOfSpeeds = new ArrayList<>();
			for (Actor e : listOfActors) {
				if (e instanceof Enemy)
					listOfSpeeds.add(new ActorAndSpeed(e.actingSpeed, e));
				if (e instanceof Character)
					listOfSpeeds.add(new ActorAndSpeed(((Character) e).character.attackSpeed, e));
			}
			refreshSpeedAddAndSort(listOfSpeeds);
			act();
			if (currentTurn >= listOfSpeeds.size() && !didTurnJustPass) {
				didTurnJustPass = true;
				turnCount++;
				for (ActorAndBoolean f : finalizedToChoose) {
					f.setBool(false);
				}
				currentTurn = 0;
				triggerOnTurnPass();
			}
		}
	}


	private static void act(){
		if(currentTurn != 0) {
			if (!finalList.get(currentTurn - 1).getActor().isPermittedToAct()
					&& !(currentTurn - 1 >= finalList.size())
				&& finalList.get(currentTurn) != null) {

				finalList.get(currentTurn).getActor().permitToMove();
				print("let " + finalList.get(currentTurn).getActor() + " to act");
				currentTurn++;
			}
			else if (finalList.get(currentTurn).getActor().getIsDead())
				currentTurn++;
			else if (finalList.get(currentTurn) == null) {
				printErr("|| ERROR ID: " + startAndEndErrorId() + " || CLASS: [Turns] |METHOD TYPE|: {static} |METHOD NAME|: {act()} %PACKAGE%: {items} ~ERRORTYPE~: Caught a null /finalList/ element.");
				finalList.remove(currentTurn);
			}
			else if (finalList.get(currentTurn-1).getActor().isPermittedToAct()){
				print("previous entity can still act. H O W");
			}
			else {
				printErr("|| ERROR ID: " + startErrorId() + " || CLASS: [Turns] |METHOD TYPE|: {static} |METHOD NAME|: {act()} %PACKAGE%: {items} ~ERRORTYPE~: Caught a weird {finalList} element.");
				printErr("|| ERROR ID: " + continueErrorId() + " /  {currentTurn}: "+currentTurn+" {finalList.size()}: "+finalList.size());
				printErr("|| ERROR ID: " + continueErrorId() + " /  {finalList.get(currentTurn)}: "+finalList.get(currentTurn)+"  {finalList.get(currentTurn).getActor}: "+finalList.get(currentTurn).getActor()+" ||| END OF THE ERROR ID:" +endErrorId());
			}
		}
		else {
			if (!finalList.get(currentTurn).getActor().getIsDead()) {
				finalList.get(currentTurn).getActor().permitToMove();
				print("let " + finalList.get(currentTurn).getActor() + " to act");
			}
			currentTurn++;
		}
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
		for ( ActorAndBoolean l : theListInQuestion){
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


	public static class ActorAndSpeed {
		Actor actor;
		int speed;

		public void setSpeed(int speed){
			this.speed = speed;
		}

		public void setActor(Actor actor) {
			this.actor = actor;
		}

		public int getSpeed() {
			return speed;
		}

		public Actor getActor() {
			return actor;
		}

		public ActorAndSpeed(int speed, Actor actor){
			setSpeed(speed);
			setActor(actor);
		}
	}

	public static class ActorAndBoolean {
		boolean bool;
		Actor actor;

		public ActorAndBoolean(){}

		public ActorAndBoolean(boolean bool, Actor actor){ this.bool = bool; this.actor = actor; }

		public boolean getBool() {return bool;}

		public void setBool (boolean bool) {this.bool = bool; }

	}

	private static void refreshSpeedAddAndSort(List<ActorAndSpeed> listOfSpeeds){
		zero    = new ArrayList<>();
		one     = new ArrayList<>();
		two     = new ArrayList<>();
		three   = new ArrayList<>();
		four    = new ArrayList<>();
		five    = new ArrayList<>();
		six     = new ArrayList<>();
		seven   = new ArrayList<>();
		eight   = new ArrayList<>();
		nine    = new ArrayList<>();
		ten     = new ArrayList<>();
		tenPlus = new ArrayList<>();
		for (ActorAndSpeed e : listOfSpeeds) {
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
		}
		//first shuffling to make the double speed ties random, then ordering. merging them in an easy-to-use arrlist
		Collections.shuffle(zero);
		Collections.shuffle(one);
		Collections.shuffle(two);
		Collections.shuffle(three);
		Collections.shuffle(four);
		Collections.shuffle(five);
		Collections.shuffle(six);
		Collections.shuffle(seven);
		Collections.shuffle(eight);
		Collections.shuffle(nine);
		Collections.shuffle(ten);
		Collections.shuffle(tenPlus);
		zero.sort((o1, o2)    -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		one.sort((o1, o2)     -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		two.sort((o1, o2)     -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		three.sort((o1, o2)   -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		four.sort((o1, o2)    -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		five.sort((o1, o2)    -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		six.sort((o1, o2)     -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		seven.sort((o1, o2)   -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		eight.sort((o1, o2)   -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		nine.sort((o1, o2)    -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		ten.sort((o1, o2)     -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		tenPlus.sort((o1, o2) -> Integer.compare(o2.getSpeed(), o1.getSpeed()));
		finalList = new ArrayList<>(tenPlus);
		finalList.addAll(ten);
		finalList.addAll(nine);
		finalList.addAll(eight);
		finalList.addAll(seven);
		finalList.addAll(six);
		finalList.addAll(five);
		finalList.addAll(four);
		finalList.addAll(three);
		finalList.addAll(two);
		finalList.addAll(one);
		finalList.addAll(zero);
	}

}
