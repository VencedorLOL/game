package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public class StageSix extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = 128*8;
	public int finalY = 128*8;
	public int spawnX = 256;
	public int spawnY = 256;
	public int[] wallX = {2,3,3,4};
	public int[] wallY = {2,3,4,2};
	public int[] enemyX = {0};
	public int[] enemyY = {0};
	public int[] enemyType = {1};
	public int[] screenWarpX = {4};
	public int[] screenWarpY = {1};
	public byte[] screenWarpDestinationSpecification = {0};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Grass";
	public StageSix(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(Character character){
		screenWarpDestination.add(new StageOne());
	}

}
