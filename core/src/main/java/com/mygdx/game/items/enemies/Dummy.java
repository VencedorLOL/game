package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.TextureManager.text;

public class Dummy extends Enemy {

	Interactable test;

	public Dummy(float x, float y) {
		super(x, y, "Dummy", 100);
		float maxHealth = 100;
		test = new Interactable(null,x,y,globalSize(),globalSize()){
			@Override
			public void onInteract() {
				text("Hi, I am a dummy!", x,y+globalSize()*2,200, TextureManager.Fonts.ComicSans, 40, 255,255,255,1,150);
			}
		};
	}


	@Override
	public void onDeath() {
		if (health <= 0) {
			maxHealth += 100;
			health = maxHealth;
		}}

	@Override
	public void movement() {
		if (isPermittedToAct())
			finalizedTurn();
		else if (canDecide())
			actionDecided();
	}
}
