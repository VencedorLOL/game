package com.mygdx.game.items.solids;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.Wall;
import com.mygdx.game.items.guielements.Background;

import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;

public class ClassChangeStation extends Wall {

	public Interactable guiOpen;

	public ClassChangeStation(float x, float y) {
		super(x, y);
		setTexture("ClassChangeSpot");
		guiOpen = new Interactable(this){
			public void onInteract(Character character) {
				paralyzeCharacter();
				new Background(character){
					public void onRemoval() {
						deparalyzeCharacter();
					}
				};
			}
		};
	}
}
