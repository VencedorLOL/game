package com.mygdx.game.items.solids;


import com.mygdx.game.items.Wall;

import static com.mygdx.game.Settings.globalSize;

public class Tree extends Wall {

	public Tree(float x, float y) {
		super(x, y, globalSize(), globalSize(), true);
		setTexture("tree");
	}

}
