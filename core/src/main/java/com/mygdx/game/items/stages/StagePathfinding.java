package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class StagePathfinding extends Stage {
	public StagePathfinding(){
		startX = 0;
		startY = 0;
		finalX = 10;
		finalY = 10;
		spawnX = 1;
		spawnY = 5;
		wallX 		= new int[]{0,1,1,1,1,1,1,1,1,2,2,2,3,3,4,4,4,4,4,4,4,4,5,5,6,6,6,6,6,6,6,6,6,7,7 ,8,8,8,8,8,8,9,9,9,9,9,10};
		wallY 		= new int[]{4,1,2,3,4,6,7,8,9,4,5,6,1,9,1,3,4,5,6,7,8,9,1,9,1,2,3,4,5,6,7,8,9,9,10,1,2,3,4,5,6,4,6,7,8,9,4 };
		wallType 	= new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 ,1,1,1,1,1,1,1,1,1,1,1,1 };
		enemySpawnX 	= new int[]{9};
		enemySpawnY 	= new int[]{5};
		enemyType 		= new int[]{-2};
		screenWarpX 						= new int[]{};
		screenWarpY 						= new int[]{};
		screenWarpDestinationSpecification = new byte[]{};
		floorTexture = "Grass";
		scale();
	}
}

