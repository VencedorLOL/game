package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

/* TODO: Need to redo this so it has: turn counter, system in which the faster moves first but character can choose what
 to do first, even if it doesn't necessarily move first (pokemon-like), method that returns true if a turn passed (takes nº of
 turn, compares it to this nº of turn, if not the same returns true)
 */
public class Turns implements Utils {
	// true means is character's turn
	static boolean canCallNextTurn = true;
	static int amountOfEnemies;
	static boolean didTurnJustPass;
	static long turnCount;
	static boolean startOfTurn = true;
	static boolean startedToDoTurns = false;
	static int currentTurn;

	public static void characterFinalizedToChooseAction(){
		startOfTurn = false;
	}

	public static void turnLogic(ArrayList<Enemy> listOfEnemies, ArrayList<Character> listOfCharacters){
		if (!startOfTurn){
		List<EntityAndSpeed> listOfSpeeds = new ArrayList<>();

		for (Enemy e : listOfEnemies){
			listOfSpeeds.add(new EntityAndSpeed( e.speed, e));
		}
		for (Character c : listOfCharacters){
			listOfSpeeds.add(new EntityAndSpeed(c.character.speed, c));
		}

		listOfSpeeds.sort(new Comparator<EntityAndSpeed>() {
			@Override
			public int compare(EntityAndSpeed o1, EntityAndSpeed o2) {
				return Integer.compare(o2.getSpeed(), o1.getSpeed());
			}
		});

		System.out.println(currentTurn);


		if(currentTurn != 0) {
			if (!listOfSpeeds.get(currentTurn - 1).getEntity().isPermittedToMove()
					&& !((currentTurn - 1) >= listOfSpeeds.size())) {
				listOfSpeeds.get(currentTurn).getEntity().permitToMove();
				currentTurn++;
			}
		}
		else {
				listOfSpeeds.get(currentTurn).getEntity().permitToMove();
				currentTurn++;
			}

		if ((currentTurn) >= listOfSpeeds.size()){
			System.out.println(currentTurn);
			didTurnJustPass = true;
			turnCount++;
			startOfTurn = true;
			startedToDoTurns = false;
			currentTurn = 0;
			System.out.println("Turn passed");
			}
		}

	}




	public void moveNext() {}


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


	public static void reset(Stage stage){
		amountOfEnemies = stage.enemy.size();
	}

}
