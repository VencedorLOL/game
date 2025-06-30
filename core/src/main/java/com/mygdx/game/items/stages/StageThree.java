package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StageThree extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*14;
	public int finalY = globalSize()*14;
	public int spawnX = 0;
	public int spawnY = 0;
	public int[] wallX = {1,1,1,1,3,3,3,3,2};
	public int[] wallY = {1,2,3,4,1,2,3,4,4};
	public int[] wallType = {1,1,1,1,1,1,1,1,1};
	public int[] enemyX = {2,1};
	public int[] enemyY = {2,5};
	public int[] enemyType = {-1,-1};
	public int[] screenWarpX = {2};
	public int[] screenWarpY = {8};
	public byte[] screenWarpDestinationSpecification = {0};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<>(){};
	public String floorTexture = "Grass";
	public StageThree(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY,wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(){
		screenWarpDestination.add(new StageFour());
	}

}
