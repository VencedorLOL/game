package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class Labitynth extends Stage {
	public Labitynth(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 99;
		finalY 		= 99;
		spawnX 		= 0;
		spawnY 		= 0;
		wallX			= new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 1};
		wallY			= new int[]{0, 1, 2, 3, 4, 5, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 6, 7, 8, 9, 10, 11, 12, 13, 29, 28, 27, 26, 20, 22, 23, 24, 25, 19, 18, 17, 16, 15, 14, 13, 12, 11, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 21, 14};
		wallType		= new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		enemySpawnX		= new int[]{29};
		enemySpawnY 	= new int[]{29};
		enemyType		= new int[]{-2};
		screenWarpX 	= new int[]{};
		screenWarpY	= new int[]{};
		screenWarpDestinationSpecification = new byte[]{};
		floorTexture = "Grass";
		bgTexture = "tree";
		scale();
	}

}
