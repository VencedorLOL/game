package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Hazards;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Hazards.hazards;

public class one extends Stage {
	public one(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 0;
		finalY 		= 0;
		spawnX 		= 0;
		spawnY 		= 0;
		wallX			= new int[]{};
		wallY			= new int[]{};
		wallType		= new int[]{};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{};
		screenWarpY		= new int[]{};
		screenWarpIsHorizontal	= new boolean[]{};
		screenWarpAlignment		= new boolean[]{};
		screenWarpSize			= new float[]{};
		screenWarpType			= new int[]{};
		screenWarpDestinationSpecification = new byte[]{};
		floorTexture = "Grass";
		bgTexture = "tree";
		staticCameraXmin = false;
		staticCameraXmax = false;
		staticCameraYmin = false;
		staticCameraYmax = false;
		scale();
	}

	public void reStage() {		
	}

	public void tilesetCleanup() {		
	}

	public void hazardSetter() {		
 }
}
