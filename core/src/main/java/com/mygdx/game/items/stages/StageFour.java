package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class StageFour extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*20;
	public int finalY = globalSize()*20;
	public int spawnX = globalSize() * 10;
	public int spawnY = 0;
	public int[] wallX = {};
	public int[] wallY = {};
	public int[] enemyX = {};
	public int[] enemyY = {};
	public int[] enemyType = {};
	public int[] screenWarpX = {1,2};
	public int[] screenWarpY = {1,1};
	public byte[] screenWarpDestinationSpecification = {0,1};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Sand";
	public StageFour(){
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
