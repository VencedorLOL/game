package com.mygdx.game.items.solids;


import com.mygdx.game.items.Wall;

import static com.mygdx.game.Settings.globalSize;

public class Crater extends Wall {

	public Crater(float x, float y) {
		super(x, y, globalSize()*7, globalSize()*7, true);
		setTexture("crater");
	}

}
