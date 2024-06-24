package com.mygdx.game.items.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.items.Enemy;

public class EvilGuy extends Enemy {
	public static float health = 15;
	public static Texture enemyTexture = new Texture("EvilGuy.jpg");
	public EvilGuy(float x, float y) {
		super(x, y, enemyTexture, health);
	}
}
