package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.items.TextureManager.addToList;

public class AttackIconRenderer {

	static OnVariousScenarios oVE;

	static{
		oVE = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				attacksList.clear();
				actorsThatAttack.clear();
				actorsThatAttack.add(chara);
			}
		};
	}

	public static ArrayList<Actor> actorsThatAttack = new ArrayList<>();
	public static ArrayList<Actor.Attack> attacksList = new ArrayList<>();

	public static void attackRenderer(){
		attacksList.clear();
		for(Actor a : actorsThatAttack){
			attacksList.addAll(a.attacks);
		}
		for (int i = 0; i < attacksList.size(); i++){
			if(attacksList.get(i).render) {
				byte counter = 0;
				for (int j = i; j < attacksList.size(); j++) {
					if (attacksList.get(j).targetX == attacksList.get(i).targetX && attacksList.get(j).targetY == attacksList.get(i).targetY && attacksList.get(j) != attacksList.get(i))
						counter++;
				}
				addToList("attackIndicator", attacksList.get(i).targetX - 5 * counter, attacksList.get(i).targetY - 5 *counter, 1,
						0,
						attacksList.get(i).isBeingExecuted ? 256 : attacksList.get(i).owner instanceof Friend ? ((Friend) attacksList.get(i).owner).color[0] : 127,
						attacksList.get(i).isBeingExecuted ? 20  : attacksList.get(i).owner instanceof Friend ? ((Friend) attacksList.get(i).owner).color[1] : 127,
						attacksList.get(i).isBeingExecuted ? 68  : attacksList.get(i).owner instanceof Friend ? ((Friend) attacksList.get(i).owner).color[2] : 127);
			}
		}
	}

}
