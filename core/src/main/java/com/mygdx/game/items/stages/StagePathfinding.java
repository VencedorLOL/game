package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StagePathfinding extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*10;
	public int finalY = globalSize()*10;
	public int spawnX = globalSize();
	public int spawnY = globalSize() * 5;
	public int[] wallX = 	{0,1,1,1,1,1,1,1,1,2,2,2,3,3,4,4,4,4,4,4,4,4,5,5,6,6,6,6,6,6,6,6,6,7,7 ,8,8,8,8,8,8,9,9,9,9,9,10};
	public int[] wallY = 	{4,1,2,3,4,6,7,8,9,4,5,6,1,9,1,3,4,5,6,7,8,9,1,9,1,2,3,4,5,6,7,8,9,9,10,1,2,3,4,5,6,4,6,7,8,9,4 };
	public int[] wallType = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 ,1,1,1,1,1,1,1,1,1,1,1,1 };
	public int[] enemyX = {9};
	public int[] enemyY = {5};
	public int[] enemyType = {1};
	public int[] screenWarpX = {};
	public int[] screenWarpY = {};
	public byte[] screenWarpDestinationSpecification = {};
	public String floorTexture = "Grass";
	public ArrayList<Stage> screenWarpDestination = new ArrayList<>(){};
	public StagePathfinding(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY, wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		screenWarpDestination.add(new StageTwo());
	}



}

