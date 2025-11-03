package com.mygdx.game.items.stages;

import com.mygdx.game.items.Floor;
import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Tile;

import static com.mygdx.game.Settings.globalSize;

public class CraterStage extends Stage {
	public CraterStage(){
		startX = 0;
		startY = 0;
		finalX = 6;
		finalY = 15;
		spawnX = 3;
		spawnY = 7;
		wallX 		= new int[]{0,0,1,5,6,0,1,5,6,0,1,5,6,0 ,1 ,5 ,6 ,0 ,1 ,5 ,6 ,0 ,1 ,5 ,6 ,0 ,1 ,5 ,6 ,0 ,1 ,5 ,6 ,0 ,1 ,5 ,6 };
		wallY 		= new int[]{0,7,7,7,7,8,8,8,8,9,9,9,9,10,10,10,10,11,11,11,11,12,12,12,12,13,13,13,13,14,14,14,14,15,15,15,15};
		wallType 	= new int[]{3,4,4,4,4,4,4,4,4,4,4,4,4,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 };
		enemySpawnX 	= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType 		= new int[]{};
		screenWarpX 						= new int[]{2 ,3 ,4 };
		screenWarpY 						= new int[]{15,15,15};
		screenWarpDestinationSpecification = new byte[]{0,0,0};
		floorTexture = "Grass";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageOne());
	}

	public void tilesetSetter(){
		for (int y = startY; y <= finalY ; y += globalSize())
			for (int x = startX ; x <= finalX ; x += globalSize()) {
				if(x == 0 || x == globalSize() || x == globalSize()*5 || x == globalSize()*6)
					continue;
				tileset.add(new Tile(x, y,new Floor(floorTexture)));
			}
		hasFloorBeenRendered = true;
	}

}
