package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StageFive extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*8;
	public int finalY = globalSize()*8;
	public int spawnX = globalSize()*2;
	public int spawnY = globalSize()*2;
	public int[] wallX = {};
	public int[] wallY = {};
	public int[] wallType = {};
	public int[] enemyX = {};
	public int[] enemyY = {};
	public int[] enemyType = {};
	public int[] screenWarpX = {2,3,4};
	public int[] screenWarpY = {8,8,8};
	public byte[] screenWarpDestinationSpecification = {0,0,0,0,0};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Grass";
	public StageFive(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY,wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(){
		screenWarpDestination.add(new StageSeven());
	}

}
