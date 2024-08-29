package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public class StageSeven extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = 128*40;
	public int finalY = 128*40;
	public int spawnX = 0;
	public int spawnY = 0;
	public int[] wallX = {37,37,37,38,38,39,39,40,40};
	public int[] wallY = {36,37,38,36,38,36,38,36,38};
	public int[] enemyX = {38};
	public int[] enemyY = {37};
	public int[] enemyType = {7};
	public int[] screenWarpX = {1,2};
	public int[] screenWarpY = {1,1};
	public byte[] screenWarpDestinationSpecification = {0,1};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Grass";
	public StageSeven(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(Character character){
		screenWarpDestination.add(new StageFive());
		screenWarpDestination.add(new StageSix());
	}
}
