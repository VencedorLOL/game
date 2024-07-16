package com.mygdx.game.items.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.items.Enemy;

public class EvilGuy extends Enemy {
	public static float health = 15;
	public static String enemyTexture = "EvilGuy";
	public EvilGuy(float x, float y) {
		super(x, y, enemyTexture, health);
	}
}
