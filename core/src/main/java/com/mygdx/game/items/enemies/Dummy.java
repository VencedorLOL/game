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


	@Override
	public void damage(float damage, String damageReason) {
		super.damage(damage, damageReason);
		int fontSize = 40;
		text(""+damage,getX()
					+(fontSize-(float) ((damage + "").toCharArray().length)+16-fontSize)/(fontSize*2)*globalSize()
				// original: +(16-(float) ((damage + "").toCharArray().length))/32*globalSize()
				, (float) (getY()+(globalSize()*1.3*min(max(fontSize/25,1),2))),200, TextureManager.Fonts.ComicSans,fontSize,255,0,0,1,100);
	}
}
