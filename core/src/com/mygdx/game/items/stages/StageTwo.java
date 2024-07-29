package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public class StageTwo extends Stage {
	public int startX = -128 * 3;
	public int startY = -128 * 3;
	public int finalX = 128*8;
	public int finalY = 128*8;
	public int spawnX = 256;
	public int spawnY = 256;
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
	public void reStage(Character character){
		ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
		betweenStages = true;
		character.setX(spawnX);
		character.setY(spawnY);
		enemy = new ArrayList<>();
		walls = new ArrayList<>();
		floor = new ArrayList<>();
		screenWarp = new ArrayList<>();
		screenWarpDestination.add(new StageThree());
		super.refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,enemyX,enemyY,screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
	}

}
