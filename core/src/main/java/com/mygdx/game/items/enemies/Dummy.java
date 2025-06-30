package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.TextureManager.text;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class Dummy extends Enemy {


	public Dummy(float x, float y) {
		super(x, y, "Dummy", 100);
		float maxHealth = 100;
	}


	@Override
	public void onDeath() {
		if (health <= 0) {
			maxHealth += 100;
			health = maxHealth;
		}
	}

	@Override
	public void movement() {
		if (isPermittedToAct())
			finalizedMove();
		else if (canDecide())
			actionDecided();
	}
}
