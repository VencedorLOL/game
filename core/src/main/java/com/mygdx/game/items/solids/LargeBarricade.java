package com.mygdx.game.items.solids;


import com.mygdx.game.items.Wall;

import static com.mygdx.game.Settings.globalSize;

public class LargeBarricade extends Wall {

	public LargeBarricade(float x, float y) {
		super(x, y, globalSize()*5, globalSize()*2, true);
		setTexture("largeBarricade");
	}

}
