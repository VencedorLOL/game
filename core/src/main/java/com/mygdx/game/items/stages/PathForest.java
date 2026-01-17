package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class PathForest extends Stage {
	public PathForest(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 2;
		finalY 		= 12;
		spawnX 		= 0;
		spawnY 		= 0;
		wallX			= new int[]{2, 2, 2, 2, 2, 2};
		wallY			= new int[]{7, 8, 9, 10, 11, 12};
		wallType		= new int[]{4, 4, 4, 4, 4, 4};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{1, 0};
		screenWarpY	= new int[]{12, 12};
		screenWarpDestinationSpecification = new byte[]{0, 0};
		floorTexture = "Grass";
		bgTexture = "tree";
		staticCameraYmax = true;
		staticCameraYmin = true;
		scale();
	}

	public void reStage() {		
		screenWarpDestination.add(new CornerStart());
	}

	public void tilesetCleanup() {		
			tileset.remove(getTile(2.0,7.0));
			tileset.remove(getTile(2.0,8.0));
			tileset.remove(getTile(2.0,9.0));
			tileset.remove(getTile(2.0,10.0));
			tileset.remove(getTile(2.0,11.0));
			tileset.remove(getTile(2.0,12.0));
	}
}
