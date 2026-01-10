package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.textboxelements.textboxes.tests.Test1;

import static com.mygdx.game.Settings.globalSize;

public class GoalDummy extends Dummy{
	Interactable text;
	public GoalDummy(float x, float y) {
		super(x, y);
		setTexture("EndGoal");
		text = new Interactable(x,y,globalSize(),globalSize()){
			public void onInteract(Character character) {
				new Test1();
			}
		};
	}

	@Override
	public void destroyOverridable() {
		text.removeInteractable();
	}
}
