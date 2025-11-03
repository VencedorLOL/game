package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class StageSeven extends Stage {
	public StageSeven(){
		startX = 0;
		startY = 0;
		finalX = 40;
		finalY = 40;
		spawnX = 0;
		spawnY = 0;
		wallX 		= new int[]{37,37,37,38,38,39,39,40,40};
		wallY 		= new int[]{36,37,38,36,38,36,38,36,38};
		wallType 	= new int[]{1,1,1,1,1,1,1,1,1,1};
		enemySpawnX 	= new int[]{38};
		enemySpawnY 	= new int[]{37};
		enemyType 		= new int[]{7};
		screenWarpX 						= new int[]{1,2};
		screenWarpY 						= new int[]{1,1};
		screenWarpDestinationSpecification = new byte[]{0,1};
		floorTexture = "Grass";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageFive());
		screenWarpDestination.add(new StageSix());
	}
}
