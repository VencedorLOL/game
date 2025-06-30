package com.mygdx.game.items.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.items.Enemy;

public class LoopingHat extends Enemy {
	public static float health = 100;
	public static String enemyTexture = "LoopingHat";
	public LoopingHat(float x, float y) {
		super(x, y, enemyTexture, health);
	}
}
