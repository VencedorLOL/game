package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;


public class StageTwo extends Stage {
	public StageTwo(){
		startX = -3;
		startY = -3;
		finalX = 8;
		finalY = 8;
		spawnX = 0;
		spawnY = 0;
		wallX 		= new int[]{1,3,2,3,5};
		wallY 		= new int[]{2,3,1,2,7};
		wallType 	= new int[]{1,1,1,1,1};
		enemySpawnX 	= new int[]{-3,-2,-1,0,1,2,3,4,5};
		enemySpawnY 	= new int[]{-3,-2,-1,0,1,2,3,4,5};
		enemyType 		= new int[]{2,2,2,2,2,2,2,2,2};
		screenWarpX 						= new int[]{-2,-1,2,3,4,5,6,7,8,-3,0,1};
		screenWarpY 						= new int[]{8,8,8,8,8,8,8,8,8,8,8,8};
		screenWarpDestinationSpecification = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0};
		floorTexture = "Sand";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageThree());
	}

}
