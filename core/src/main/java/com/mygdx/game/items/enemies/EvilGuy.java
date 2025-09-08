package com.mygdx.game.items.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.VideoManager.createVideo;

public class EvilGuy extends Enemy {
	public static float health = 15;
	public static String enemyTexture = "EvilGuy";
	public EvilGuy(float x, float y) {
		super(x, y, enemyTexture, health);
		tenna = new Interactable(null,x,y,globalSize(),globalSize()){
			@Override
			public void onInteract() {
				createVideo(x,y);
			}
		};
		sightRange = 30;
		followRange = 40;
		speed = 4;
	}

	Interactable tenna;





}
