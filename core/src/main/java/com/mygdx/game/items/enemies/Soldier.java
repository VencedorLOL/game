package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;

public class Soldier extends Enemy {

	public Interactable action;
	public Soldier(float x, float y) {
		super(x, y, "Soldier", 100);
		team = 1;
		defense = 50;
		damage = 60;
		setAction();

	}
	public void setAction(){
		action = new Interactable(this){
			public void onInteract(Character character) {
				new Textbox(){
					public void beforeRenderOverridable() {
						paralyzeCharacter();
					}

					public void onRemoval() {
						deparalyzeCharacter();
					}

					public void beforeTextOverridable() {
						storedText = "This is the default textbox for the \nsoldier class.";
					}
				};
			}
		};
	}

	public boolean didItAct(){
		return true;
	}

	public void attack(){
	if (isPermittedToAct())
		finalizedTurn();
	else if (isDecidingWhatToDo(this))
		actionDecided();}


	@Override
	public void movement() {
		if (isPermittedToAct())
			finalizedTurn();
		else if (isDecidingWhatToDo(this))
			actionDecided();
	}

	@Override
	public void destroyOverridable() {
		if(action != null)
			action.removeInteractable();
	}
}
