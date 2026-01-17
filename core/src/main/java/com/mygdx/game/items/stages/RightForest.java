package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class RightForest extends Stage {
	public RightForest(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 14;
		finalY 		= 2;
		spawnX 		= 0;
		spawnY 		= 1;
		wallX			= new int[]{14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 26, 27, 28, 29, 30};
		wallY			= new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1};
		wallType		= new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{30};
		screenWarpY	= new int[]{0};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		bgTexture = "tree";
		staticCameraXmax = true;
		staticCameraXmin = true;
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageOne());
	}

	public void tilesetCleanup() {		
			tileset.remove(getTile(14.0,2.0));
			tileset.add(createTile(15.0,1.0));
			tileset.add(createTile(15.0,0.0));
			tileset.add(createTile(16.0,0.0));
			tileset.add(createTile(16.0,1.0));
			tileset.add(createTile(17.0,1.0));
			tileset.add(createTile(17.0,0.0));
			tileset.add(createTile(18.0,1.0));
			tileset.add(createTile(18.0,0.0));
			tileset.add(createTile(19.0,0.0));
			tileset.add(createTile(19.0,1.0));
			tileset.add(createTile(20.0,1.0));
			tileset.add(createTile(21.0,1.0));
			tileset.add(createTile(22.0,1.0));
			tileset.add(createTile(22.0,0.0));
			tileset.add(createTile(21.0,0.0));
			tileset.add(createTile(20.0,0.0));
			tileset.add(createTile(23.0,0.0));
			tileset.add(createTile(23.0,1.0));
			tileset.add(createTile(24.0,1.0));
			tileset.add(createTile(25.0,1.0));
			tileset.add(createTile(24.0,0.0));
			tileset.add(createTile(25.0,0.0));
			tileset.add(createTile(26.0,0.0));
			tileset.add(createTile(27.0,0.0));
			tileset.add(createTile(28.0,0.0));
			tileset.add(createTile(29.0,0.0));
			tileset.add(createTile(30.0,0.0));
	}
}
