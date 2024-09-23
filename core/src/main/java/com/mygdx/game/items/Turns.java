package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

import static com.mygdx.game.items.Stage.betweenStages;

//TODO: Redid, but it still needs this:  method that returns true if a turn passed
// (takes nº of  turn, compares it to this nº of turn, if not the same returns true)
// TODO 2 :
//  turn characterFinalizedToChoose into everyoneFinalizedToChoose starting NOW
//  make act (attack) speed and ties actually random
public class Turns implements Utils {
	static boolean didTurnJustPass;
	static long turnCount;
	static ArrayList<EntityAndBoolean> finalizedToChoose = new ArrayList<>();
	static int currentTurn;

	public static void characterFinalizedToChooseAction(Character chara) {
		if(loopList(finalizedToChoose,chara) != null)
			// it doesnt produce NPO, theres a whole if statement right above this that checks for that specifically.
			loopList(finalizedToChoose,chara).bool = true;
		else
			finalizedToChoose.add(new EntityAndBoolean(true,chara));
	}

	public static void enemyFinalizedChoosing(Enemy enemy){
		if(loopList(finalizedToChoose,enemy) != null)
			// it doesnt produce NPO, theres a whole if statement right above this that checks for that specifically.
			// IntelliJ grow up
			loopList(finalizedToChoose,enemy).bool = true;
		else
			finalizedToChoose.add(new EntityAndBoolean(true,enemy));
	}

	private static boolean checkForTurn(ArrayList<Enemy> listOfEnemies, ArrayList<Character> listOfCharacters){
		int numberOfEntities = listOfEnemies.size() + listOfCharacters.size();
		int numberOfTrueFinalizedChoosers = 0;
		for (EntityAndBoolean f : finalizedToChoose){
			if(f.getBool())
				numberOfTrueFinalizedChoosers++;
		}
		return numberOfEntities == numberOfTrueFinalizedChoosers;
	}

	public static boolean isDecidingWhatToDo(Entity entity){
		boolean foundItself = false;
		for (EntityAndBoolean f : finalizedToChoose){
			if (!f.bool)
				return true;
			if(f.entity == entity)
				foundItself = true;
		}
		if (!foundItself)
			finalizedToChoose.add(new EntityAndBoolean(false,entity));
		return false;
	}

	public static void turnLogic(ArrayList<Enemy> listOfEnemies, ArrayList<Character> listOfCharacters){
		if(betweenStages)
			currentTurn = 0;
		else if (checkForTurn(listOfEnemies,listOfCharacters)){
			didTurnJustPass = false;
			List<EntityAndSpeed> listOfSpeeds = new ArrayList<>();

			for (Enemy e : listOfEnemies){
				listOfSpeeds.add(new EntityAndSpeed( e.speed, e));
			}
			for (Character c : listOfCharacters){
			listOfSpeeds.add(new EntityAndSpeed(c.character.speed, c));
			}

			listOfSpeeds.sort((o1, o2) -> Integer.compare(o2.getSpeed(), o1.getSpeed()));

			if(currentTurn != 0) {

				if ((!listOfSpeeds.get(currentTurn - 1).getEntity().isPermittedToAct() ||
						listOfSpeeds.get(currentTurn).getEntity().getIsDead())
					&& !((currentTurn - 1) >= listOfSpeeds.size())) {

				listOfSpeeds.get(currentTurn).getEntity().permitToMove();
					currentTurn++;
				}

				else if(listOfSpeeds.get(currentTurn).getEntity().getIsDead())
					currentTurn++;
			}
			else {
					listOfSpeeds.get(currentTurn).getEntity().permitToMove();
					currentTurn++;
			}

			if ((currentTurn) >= listOfSpeeds.size()){
				didTurnJustPass = true;
				turnCount++;
				for (EntityAndBoolean f : finalizedToChoose){ f.setBool(false); }
				currentTurn = 0;
			}
		}

	}

	public static boolean nullPointerExceptionChecker(List<?> arrayList,int exceptionPossibleProvocator){
		try{
			arrayList.get(exceptionPossibleProvocator);
			return false;
		}
		catch (NullPointerException ignored){
			return true;
		}
	}

	private static EntityAndBoolean loopList(ArrayList<EntityAndBoolean> theListInQuestion, Entity theValueInQuestion){
		for ( EntityAndBoolean l : theListInQuestion){
			if(l.entity == theValueInQuestion)
				return l;
		}
		return null;
	}


	public static class EntityAndSpeed {
		Entity entity;
		int speed;

		public void setSpeed(int speed){
			this.speed = speed;
		}

		public void setEntity(Entity entity) {
			this.entity = entity;
		}

		public int getSpeed() {
			return speed;
		}

		public Entity getEntity() {
			return entity;
		}

		public EntityAndSpeed(int speed, Entity entity){
			setSpeed(speed);
			setEntity(entity);
		}
	}

	public static class EntityAndBoolean {
		boolean bool;
		Entity entity;

		public EntityAndBoolean(){}

		public EntityAndBoolean (boolean bool,Entity entity){ this.bool = bool; this.entity = entity; }

		public boolean getBool() {return bool;}

		public void setBool (boolean bool) {this.bool = bool; }

	}


}
