package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Interactable;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.VideoManager.createVideo;

public class EvilGuy extends Enemy {
	public static String enemyTexture = "EvilGuy";
	Interactable tenna;
	public EvilGuy(float x, float y) {
		super(x, y, enemyTexture, 25);
		tenna = new Interactable(x,y,globalSize(),globalSize()){
			@Override
			public void onInteract(Character character) {
				createVideo(x,y);
			}
		};
		sightRange = 30;
		followRange = 40;
		speed = 4;
	}

	@Override
	public void destroyOverridable() {
		super.destroyOverridable();
	}
}
