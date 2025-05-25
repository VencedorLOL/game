package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StageTwo extends Stage {
	public int startX = -globalSize() * 3;
	public int startY = -globalSize() * 3;
	public int finalX = globalSize()*8;
	public int finalY = globalSize()*8;
	public int spawnX = 0;
	public int spawnY = 0;
	public int[] wallX = {1,3,2,3,5};
	public int[] wallY = {2,3,1,2,7};
	public int[] enemyX = {-3,-2,-1,0,1,2,3,4,5};
	public int[] enemyY = {-3,-2,-1,0,1,2,3,4,5};
	public int[] enemyType = {2,2,2,2,2,2,2,2,2};
	public int[] screenWarpX = {-2,-1,2,3,4,5,6,7,8,-3,0,1};
	public int[] screenWarpY = {8,8,8,8,8,8,8,8,8,8,8,8};
	public byte[] screenWarpDestinationSpecification = {0,0,0,0,0,0,0,0,0,0,0,0};
	public String floorTexture = "Sand";
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public StageTwo(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(){
		screenWarpDestination.add(new StageThree());
	}

}
