package com.mygdx.game.items.stages;
import com.mygdx.game.items.Stage;

public class StageFive extends Stage {
	public StageFive(){
		startX = 0;
		startY = 0;
		finalX = 8;
		finalY = 8;
		spawnX = 2;
		spawnY = 2;
		wallX 		= new int[]{};
		wallY 		= new int[]{};
		wallType 	= new int[]{};
		enemySpawnX 	= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType 		= new int[]{};
		screenWarpX 						= new int[]{2,3,4};
		screenWarpY 						= new int[]{8,8,8};
		screenWarpDestinationSpecification = new byte[]{0,1,0};
		floorTexture = "Grass";
		scale();
	}
	public void reStage() {
		screenWarpDestination.add(new StageSeven());
		screenWarpDestination.add(new StageEight());
	}

}
