package com.mygdx.game.items;

/* TODO: Need to redo this so it has: turn counter, system in which the faster moves first but character can choose what
 to do first, even if it doesn't necessarily move first (pokemon-like), method that returns true if a turn passed (takes nº of
 turn, compares it to this nº of turn, if not the same returns true)
 */
public class Turns {
	// true means is character's turn
	static boolean turn = true;
	static boolean canCallNextTurn = true;
	static int amountOfEnemies;
	static boolean didTurnJustPass;


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
			if (!e.isOnTurn) {
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
				if (!e.isOnTurn && !e.hasHadItsTurn && !e.isDead){
					e.isOnTurn = true;
					e.hasHadItsTurn = true;
					canCallNextTurn = false;
					break;
				}
	}
}
