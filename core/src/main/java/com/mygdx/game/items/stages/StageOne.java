package com.mygdx.game.items.stages;

import com.mygdx.game.items.Hazards;
import com.mygdx.game.items.Stage;

import static com.mygdx.game.items.Hazards.addHazard;

public class StageOne extends Stage {
	public StageOne(){
		startX = -3;
		startY = 0;
		finalX = 6;
		finalY = 6;
		spawnX = 3;
		spawnY = 0;
		wallX 		= new int[]{0,1,2,4,5,6,0,1,5,6,0,6};
		wallY 		= new int[]{0,0,0,0,0,0,1,1,1,1,2,2};
		wallType 	= new int[]{1,1,1,1,1,1,1,1,1,1,1,1};
		enemySpawnX 	= new int[]{3};
		enemySpawnY 	= new int[]{6};
		enemyType 		= new int[]{1};
		screenWarpX 						= new int[]{3};
		screenWarpY 						= new int[]{4};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageTwo());
	}

	public void hazardSetter(){
		addHazard(Hazards.HazardNames.SPIKES, -2,0);
	}

}
