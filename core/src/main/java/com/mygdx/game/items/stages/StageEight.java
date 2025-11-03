package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class StageEight extends Stage {
	public StageEight(){
		startX = -40;
		startY = -40;
		finalX = 49;
		finalY = 49;
		spawnX = 0;
		spawnY = 0;
		wallX 		= new int[]{};
		wallY 		= new int[]{};
		wallType 	= new int[]{};
		enemySpawnX 	= new int[]{-20,-40,-20,-30,-12,-23,-32,-24,-26,10,13,14,15,20,24,25,26,27,30,40};
		enemySpawnY 	= new int[]{-20,30,-40,24,-21,32,-25,-26,24,-26,22,-27,30,-32,-39,-34,40,27,34,20};
		enemyType 		= new int[]{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
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
