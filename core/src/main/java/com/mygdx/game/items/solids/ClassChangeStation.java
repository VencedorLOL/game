package com.mygdx.game.items.solids;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.Wall;

public class ClassChangeStation extends Wall {

	public Interactable guiOpen;

	public ClassChangeStation(float x, float y) {
		super(x, y);
		setTexture("ClassChangeSpot");
		guiOpen = new Interactable(this){
			@Override
			public void onInteract(Character character) {

			}
		};
	}
}
