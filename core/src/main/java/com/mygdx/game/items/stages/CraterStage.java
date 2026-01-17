package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

public class CraterStage extends Stage {
	public CraterStage(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 6;
		finalY 		= 15;
		spawnX 		= 3;
		spawnY 		= 7;
		wallX			= new int[]{0};
		wallY			= new int[]{0};
		wallType		= new int[]{3};
		enemySpawnX		= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType		= new int[]{};
		screenWarpX 	= new int[]{4, 3, 2};
		screenWarpY	= new int[]{25, 25, 25};
		screenWarpDestinationSpecification = new byte[]{0, 0, 0};
		floorTexture = "Grass";
		bgTexture = "tree";
		scale();
		staticCameraYmax = true;
	}

	public void reStage() {		
		screenWarpDestination.add(new PathForest());
	}

	public void tilesetCleanup() {		
			tileset.remove(getTile(0.0,0.0));
			tileset.remove(getTile(1.0,0.0));
			tileset.remove(getTile(5.0,0.0));
			tileset.remove(getTile(6.0,0.0));
			tileset.remove(getTile(0.0,1.0));
			tileset.remove(getTile(1.0,1.0));
			tileset.remove(getTile(5.0,1.0));
			tileset.remove(getTile(6.0,1.0));
			tileset.remove(getTile(0.0,2.0));
			tileset.remove(getTile(1.0,2.0));
			tileset.remove(getTile(5.0,2.0));
			tileset.remove(getTile(6.0,2.0));
			tileset.remove(getTile(0.0,3.0));
			tileset.remove(getTile(1.0,3.0));
			tileset.remove(getTile(5.0,3.0));
			tileset.remove(getTile(6.0,3.0));
			tileset.remove(getTile(0.0,4.0));
			tileset.remove(getTile(1.0,4.0));
			tileset.remove(getTile(5.0,4.0));
			tileset.remove(getTile(6.0,4.0));
			tileset.remove(getTile(0.0,5.0));
			tileset.remove(getTile(1.0,5.0));
			tileset.remove(getTile(5.0,5.0));
			tileset.remove(getTile(6.0,5.0));
			tileset.remove(getTile(0.0,6.0));
			tileset.remove(getTile(1.0,6.0));
			tileset.remove(getTile(5.0,6.0));
			tileset.remove(getTile(6.0,6.0));
			tileset.remove(getTile(0.0,7.0));
			tileset.remove(getTile(1.0,7.0));
			tileset.remove(getTile(5.0,7.0));
			tileset.remove(getTile(6.0,7.0));
			tileset.remove(getTile(0.0,8.0));
			tileset.remove(getTile(1.0,8.0));
			tileset.remove(getTile(5.0,8.0));
			tileset.remove(getTile(6.0,8.0));
			tileset.remove(getTile(0.0,9.0));
			tileset.remove(getTile(1.0,9.0));
			tileset.remove(getTile(5.0,9.0));
			tileset.remove(getTile(6.0,9.0));
			tileset.remove(getTile(0.0,10.0));
			tileset.remove(getTile(1.0,10.0));
			tileset.remove(getTile(5.0,10.0));
			tileset.remove(getTile(6.0,10.0));
			tileset.remove(getTile(0.0,11.0));
			tileset.remove(getTile(1.0,11.0));
			tileset.remove(getTile(5.0,11.0));
			tileset.remove(getTile(6.0,11.0));
			tileset.remove(getTile(0.0,12.0));
			tileset.remove(getTile(1.0,12.0));
			tileset.remove(getTile(5.0,12.0));
			tileset.remove(getTile(6.0,12.0));
			tileset.remove(getTile(0.0,13.0));
			tileset.remove(getTile(1.0,13.0));
			tileset.remove(getTile(5.0,13.0));
			tileset.remove(getTile(6.0,13.0));
			tileset.remove(getTile(0.0,14.0));
			tileset.remove(getTile(1.0,14.0));
			tileset.remove(getTile(5.0,14.0));
			tileset.remove(getTile(6.0,14.0));
			tileset.remove(getTile(0.0,15.0));
			tileset.remove(getTile(1.0,15.0));
			tileset.remove(getTile(5.0,15.0));
			tileset.remove(getTile(6.0,15.0));
			tileset.add(createTile(3.0,16.0));
			tileset.add(createTile(3.0,17.0));
			tileset.add(createTile(3.0,18.0));
			tileset.add(createTile(3.0,19.0));
			tileset.add(createTile(3.0,20.0));
			tileset.add(createTile(3.0,21.0));
			tileset.add(createTile(3.0,22.0));
			tileset.add(createTile(3.0,23.0));
			tileset.add(createTile(2.0,16.0));
			tileset.add(createTile(2.0,17.0));
			tileset.add(createTile(2.0,18.0));
			tileset.add(createTile(2.0,19.0));
			tileset.add(createTile(2.0,20.0));
			tileset.add(createTile(2.0,21.0));
			tileset.add(createTile(2.0,22.0));
			tileset.add(createTile(2.0,23.0));
			tileset.add(createTile(4.0,23.0));
			tileset.add(createTile(4.0,22.0));
			tileset.add(createTile(4.0,21.0));
			tileset.add(createTile(4.0,20.0));
			tileset.add(createTile(4.0,19.0));
			tileset.add(createTile(4.0,18.0));
			tileset.add(createTile(4.0,17.0));
			tileset.add(createTile(4.0,16.0));
			tileset.add(createTile(2.0,24.0));
			tileset.add(createTile(3.0,24.0));
			tileset.add(createTile(4.0,24.0));
			tileset.add(createTile(4.0,25.0));
			tileset.add(createTile(3.0,25.0));
			tileset.add(createTile(2.0,25.0));
	}
}
