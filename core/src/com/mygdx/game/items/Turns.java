package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.ArrayList;
import java.util.List;

/* TODO: Need to redo this so it has: turn counter, system in which the faster moves first but character can choose what
 to do first, even if it doesn't necessarily move first (pokemon-like), method that returns true if a turn passed (takes nº of
 turn, compares it to this nº of turn, if not the same returns true)
 */
public class Turns implements Utils {
	// true means is character's turn
	static boolean turn = true;
	static boolean canCallNextTurn = true;
	static int amountOfEnemies;
	static boolean didTurnJustPass;

	static long turnCount;
	static boolean startOfTurn;


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

		listOfSpeeds = Utils.sort(listOfSpeeds, 0, listOfSpeeds.size() - 1);
		listOfSpeeds.reversed();

		for (EntityAndSpeed e : listOfSpeeds){
			e.getEntity().permitToMove();
			while (!e.getEntity().isPermittedToMove()){}
		}
		didTurnJustPass = true;
		turnCount++;

		}

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




	public static boolean didTurnJustPass(){
		return false;
	}

	public static void spendTurn() {
		turn = false;
	}

	public static void spendTurnReverse() {
		turn = true;
	}

	public static boolean whatTurnIsIt() {
		return turn;
	}

	public static void nextEnemyTurnCaller(){
		canCallNextTurn = true;
	}

	public static void reset(Stage stage){
		turn = true;
		amountOfEnemies = stage.enemy.size();
	}

	public static void swapToCharacterTurn(Stage stage) {
		amountOfEnemies = stage.enemy.size();
		for (Enemy e : stage.enemy) {
			if (!e.allowedToMove) {
				amountOfEnemies--;
			}
		}
		if (amountOfEnemies == 0) {
			spendTurnReverse();
			for (Enemy e : stage.enemy)
				e.hasHadItsTurn = false;
		}
	}

	public static void whatEnemiesTurnIsIt(Stage stage){
		amountOfEnemies = stage.enemy.size();
		if(!turn && canCallNextTurn)
			for (Enemy e : stage.enemy)
				if (!e.allowedToMove && !e.hasHadItsTurn && !e.isDead){
					e.allowedToMove = true;
					e.hasHadItsTurn = true;
					canCallNextTurn = false;
					break;
				}
	}
}
