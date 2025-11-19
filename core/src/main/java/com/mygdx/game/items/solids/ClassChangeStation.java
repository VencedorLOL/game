package com.mygdx.game.items.solids;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.Wall;
import com.mygdx.game.items.guielements.Background;

import static com.mygdx.game.GameScreen.chara;

public class ClassChangeStation extends Wall {

	public Interactable guiOpen;

	public ClassChangeStation(float x, float y) {
		super(x, y);
		setTexture("ClassChangeSpot");
		guiOpen = new Interactable(this){
			public void onInteract(Character character) {
				chara.lockClass = true;
				if(chara.walkingAnimation != null)
					chara.walkingAnimation.stop();
				chara.walkingAnimation = null;
				chara.speedLeft[0] = 0; chara.speedLeft[1] = 0;
				new Background(){
					public void onRemoval() {
						chara.lockClass = false;
					}
				};
			}
		};
	}
}
