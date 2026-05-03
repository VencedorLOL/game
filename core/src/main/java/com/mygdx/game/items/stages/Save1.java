package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Hazards;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Hazards.hazards;

public class Save1 extends Stage {
	public Save1(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 2;
		finalY 		= 5;
		spawnX 		= 0;
		spawnY 		= 0;
		wallX			= new int[]{};
		wallY			= new int[]{};
		wallType		= new int[]{};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{0,0};
		screenWarpY		= new int[]{5,0};
		screenWarpIsHorizontal	= new boolean[]{true,true};
		screenWarpAlignment		= new boolean[]{true,false};
		screenWarpSize			= new float[]{3,3};
		screenWarpType			= new int[]{0,1};
		screenWarpDestinationSpecification = new byte[]{0,1};
		floorTexture = "Grass";
		bgTexture = "tree";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new PathForest());
		screenWarpDestination.add(new CraterStage());

	}

	public void tilesetCleanup() {		
	}

	public void hazardSetter() {		
 }
}
