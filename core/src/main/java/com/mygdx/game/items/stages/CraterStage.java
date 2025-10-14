package com.mygdx.game.items.stages;

import com.mygdx.game.items.Floor;
import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Tile;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class CraterStage extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = 6;
	public int finalY = 15;
	public int spawnX = 3;
	public int spawnY = 7;
	public int[] wallX = 	{0,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6,0,1,5,6};
	public int[] wallY = {0,7,7,7,7,8,8,8,8,9,9,9,9,10,10,10,10,11,11,11,11,12,12,12,12,13,13,13,13,14,14,14,14,15,15,15,15};
	public int[] wallType = {3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};
	public int[] enemyX = {};
	public int[] enemyY = {};
	public int[] enemyType = {};
	public int[] screenWarpX = {2,3,4};
	public int[] screenWarpY = {15,15,15};
	public byte[] screenWarpDestinationSpecification = {0,0,0};
	public String floorTexture = "Grass";
	public ArrayList<Stage> screenWarpDestination = new ArrayList<>(){};
	public CraterStage(){
		super.refresh(globalSize()*startX, globalSize()*startY, globalSize()*finalX, globalSize()*finalY
				, globalSize()*spawnX, globalSize()*spawnY, wallX, wallY, wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		screenWarpDestination.add(new StageOne());
	}

	public void tilesetSetter(Stage stage){
		for (int y = stage.startY; y <= stage.finalY ; y += globalSize())
			for (int x = stage.startX ; x <= stage.finalX ; x += globalSize()) {
				if(x == 0 || x == globalSize() || x == globalSize()*5 || x == globalSize()*6)
					continue;
				tileset.add(new Tile(x, y,new Floor(floorTexture)));
			}
		hasFloorBeenRendered = true;
	}

}
