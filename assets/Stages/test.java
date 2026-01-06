package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class test extends Stage {
	public test(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 19;
		finalY 		= 19;
		spawnX 		= 0;
		spawnY 		= 0;
		wallX			= new int[]{};
		wallY			= new int[]{};
		wallType		= new int[]{};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{};
		screenWarpY	= new int[]{};
		screenWarpDestinationSpecification = new byte[]{};
		floorTexture = "Grass";
		scale();
	}
}
