package com.mygdx.game.items;

public class Turns {
	public static boolean characterTurn = true;
	public static boolean canCallNextTurn = true;
	public static int amountOfEnemies;

	public static void spendCharacterTurn() {
		characterTurn = false;
	}

	public static void spendNotCharacterTurn() {
		characterTurn = true;
	}

	public static boolean whatTurnIsIt() {
		return characterTurn;
	}

	public static void nextEnemyTurnCaller(){
		canCallNextTurn = true;
	}

	public static void reset(Stage stage){
		characterTurn = true;
		amountOfEnemies = stage.enemyGetter().size();
	}

	public static void swapToCharacterTurn(Stage stage) {
		amountOfEnemies = stage.enemyGetter().size();
		for (Enemy e : stage.enemyGetter()) {
			if (!e.isOnTurn) {
				amountOfEnemies--;
			}
		}
		if (amountOfEnemies == 0) {
			characterTurn = true;
			for (Enemy e : stage.enemyGetter())
				e.hasHadItsTurn = false;
		}
	}

	public static void whatEnemiesTurnIsIt(Stage stage){
		amountOfEnemies = stage.enemyGetter().size();
		if(!characterTurn && canCallNextTurn)
			for (Enemy e : stage.enemyGetter())
				if (!e.isOnTurn && !e.hasHadItsTurn && !e.isDead){
					e.isOnTurn = true;
					e.hasHadItsTurn = true;
					canCallNextTurn = false;
					break;
				}
	}
}
