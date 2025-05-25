package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StageOne extends Stage {
	public int startX = -3*globalSize();
	public int startY = 0;
	public int finalX = globalSize()*6;
	public int finalY = globalSize()*6;
	public int spawnX = globalSize() * 3;
	public int spawnY = 0;
	public int[] wallX = {0,1,2,4,5,6,0,1,5,6,0,6};
	public int[] wallY = {0,0,0,0,0,0,1,1,1,1,2,2};
	public int[] enemyX = {};
	public int[] enemyY = {};
	public int[] enemyType = {};
	public int[] screenWarpX = {3};
	public int[] screenWarpY = {4};
	public byte[] screenWarpDestinationSpecification = {0};
	public String floorTexture = "Grass";
	public ArrayList<Stage> screenWarpDestination = new ArrayList<>(){};
	public StageOne(){
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
