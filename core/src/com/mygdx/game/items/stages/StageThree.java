package com.mygdx.game.items.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public class StageThree extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = 128*14;
	public int finalY = 128*14;
	public int spawnX = 128;
	public int spawnY = 0;
	public int[] wallX = {1,1,1,3,3,3};
	public int[] wallY = {1,2,3,1,2,3};
	public int[] enemyX = {2};
	public int[] enemyY = {2};
	public int[] enemyType = {1};
	public int[] screenWarpX = {2};
	public int[] screenWarpY = {8};
	public byte[] screenWarpDestinationSpecification = {0};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Grass";
	public StageThree(){
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
		character.x = spawnX;
		character.y = spawnY;
		enemy = new ArrayList<>();
		walls = new ArrayList<>();
		grass = new ArrayList<>();
		screenWarp = new ArrayList<>();
		screenWarpDestination.add(new StageFour());
		super.refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,enemyX,enemyY,screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
	}

}
