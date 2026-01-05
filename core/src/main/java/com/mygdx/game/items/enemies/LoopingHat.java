package com.mygdx.game.items.enemies;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Interactable;
import com.mygdx.game.items.textboxelements.textboxes.tests.Multicolor;

import static com.mygdx.game.Settings.globalSize;

public class LoopingHat extends Enemy {
	Interactable rainbow;
	public static float health = 100;
	public static String enemyTexture = "LoopingHat";
	public LoopingHat(float x, float y) {
		super(x, y, enemyTexture, health);
		rainbow = new Interactable(x,y,globalSize(),globalSize()) {
			public void onInteract(Character character) {
				new Multicolor();
			}
		};
	}
}
