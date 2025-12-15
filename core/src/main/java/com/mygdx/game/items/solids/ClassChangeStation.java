package com.mygdx.game.items.solids;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.Wall;
import com.mygdx.game.items.guielements.Background;

public class ClassChangeStation extends Wall {

	public Interactable guiOpen;

	public ClassChangeStation(float x, float y) {
		super(x, y);
		setTexture("ClassChangeSpot");
		guiOpen = new Interactable(this){
			public void onInteract(Character character) {
				character.lockClass = true;
				if(character.walkingAnimation != null)
					character.walkingAnimation.stop();
				character.walkingAnimation = null;
				character.speedLeft[0] = 0; character.speedLeft[1] = 0;
				new Background(character){
					public void onRemoval() {
						character.lockClass = false;
					}
				};
			}
		};
	}
}
