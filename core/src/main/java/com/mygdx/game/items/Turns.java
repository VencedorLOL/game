package com.mygdx.game.items;

import com.mygdx.game.Utils;

import java.util.*;

import static com.mygdx.game.items.Stage.betweenStages;

//TODO: Redid, but it still needs this:  method that returns true if a turn passed
// (takes nº of  turn, compares it to this nº of turn, if not the same returns true)
public class Turns implements Utils {
	static boolean didTurnJustPass;
	static long turnCount;
	static boolean characterFinalizedToChoose = true;
	static int currentTurn;

	public static void characterFinalizedToChooseAction(){ characterFinalizedToChoose = true;}

	public static boolean isCharacterDecidingWhatToDo(){
		return !characterFinalizedToChoose;
	}

	public static void turnLogic(ArrayList<Enemy> listOfEnemies, ArrayList<Character> listOfCharacters){
		if(betweenStages)
			currentTurn = 0;
		else if (characterFinalizedToChoose){
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
				characterFinalizedToChoose = false;
				currentTurn = 0;
			}
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

}
