package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class CornerStart extends Stage {
	public CornerStart(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 3;
		finalY 		= 8;
		spawnX 		= 1;
		spawnY 		= 0;
		wallX			= new int[]{2, 2, 3, 3, 3, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 2, 1, 0, 0, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24};
		wallY			= new int[]{0, 1, 1, 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 7, 7, 7, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 6, 7};
		wallType		= new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{23, 23, 23};
		screenWarpY	= new int[]{7, 6, 5};
		screenWarpDestinationSpecification = new byte[]{0, 0, 0};
		floorTexture = "Grass";
		bgTexture = "tree";
		staticCameraXmax = true;
		staticCameraYmin = true;
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new RightForest());
	}

	public void tilesetCleanup() {		
			tileset.remove(getTile(2.0,0.0));
			tileset.remove(getTile(3.0,0.0));
			tileset.remove(getTile(2.0,1.0));
			tileset.remove(getTile(3.0,1.0));
			tileset.remove(getTile(3.0,2.0));
			tileset.remove(getTile(3.0,3.0));
			tileset.remove(getTile(0.0,6.0));
			tileset.remove(getTile(0.0,7.0));
			tileset.remove(getTile(1.0,7.0));
			tileset.remove(getTile(2.0,7.0));
			tileset.remove(getTile(0.0,8.0));
			tileset.remove(getTile(1.0,8.0));
			tileset.remove(getTile(2.0,8.0));
			tileset.remove(getTile(3.0,8.0));
			tileset.add(createTile(4.0,7.0));
			tileset.add(createTile(4.0,6.0));
			tileset.add(createTile(5.0,6.0));
			tileset.add(createTile(5.0,7.0));
			tileset.add(createTile(4.0,5.0));
			tileset.add(createTile(5.0,5.0));
			tileset.add(createTile(6.0,5.0));
			tileset.add(createTile(6.0,6.0));
			tileset.add(createTile(6.0,7.0));
			tileset.add(createTile(7.0,6.0));
			tileset.add(createTile(7.0,5.0));
			tileset.add(createTile(7.0,7.0));
			tileset.add(createTile(8.0,7.0));
			tileset.add(createTile(8.0,6.0));
			tileset.add(createTile(8.0,5.0));
			tileset.add(createTile(9.0,7.0));
			tileset.add(createTile(9.0,6.0));
			tileset.add(createTile(9.0,5.0));
			tileset.add(createTile(10.0,5.0));
			tileset.add(createTile(10.0,6.0));
			tileset.add(createTile(10.0,7.0));
			tileset.add(createTile(11.0,5.0));
			tileset.add(createTile(11.0,6.0));
			tileset.add(createTile(11.0,7.0));
			tileset.add(createTile(12.0,7.0));
			tileset.add(createTile(12.0,6.0));
			tileset.add(createTile(12.0,5.0));
			tileset.add(createTile(13.0,7.0));
			tileset.add(createTile(13.0,6.0));
			tileset.add(createTile(13.0,5.0));
			tileset.add(createTile(14.0,7.0));
			tileset.add(createTile(14.0,6.0));
			tileset.add(createTile(14.0,5.0));
			tileset.add(createTile(15.0,5.0));
			tileset.add(createTile(16.0,5.0));
			tileset.add(createTile(17.0,5.0));
			tileset.add(createTile(18.0,5.0));
			tileset.add(createTile(19.0,5.0));
			tileset.add(createTile(20.0,5.0));
			tileset.add(createTile(21.0,5.0));
			tileset.add(createTile(22.0,5.0));
			tileset.add(createTile(23.0,5.0));
			tileset.add(createTile(23.0,6.0));
			tileset.add(createTile(23.0,7.0));
			tileset.add(createTile(22.0,7.0));
			tileset.add(createTile(21.0,7.0));
			tileset.add(createTile(20.0,7.0));
			tileset.add(createTile(19.0,7.0));
			tileset.add(createTile(18.0,7.0));
			tileset.add(createTile(17.0,7.0));
			tileset.add(createTile(16.0,7.0));
			tileset.add(createTile(15.0,7.0));
			tileset.add(createTile(15.0,6.0));
			tileset.add(createTile(16.0,6.0));
			tileset.add(createTile(17.0,6.0));
			tileset.add(createTile(18.0,6.0));
			tileset.add(createTile(19.0,6.0));
			tileset.add(createTile(20.0,6.0));
			tileset.add(createTile(21.0,6.0));
			tileset.add(createTile(22.0,6.0));
	}
}
