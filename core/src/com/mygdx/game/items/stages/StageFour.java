package com.mygdx.game.items.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public class StageFour extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = 128*20;
	public int finalY = 128*20;
	public int spawnX = 128 * 10;
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
	public Texture floorTexture = new Texture("Sand.png");
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
		ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
		betweenStages = true;
		character.x = spawnX;
		character.y = spawnY;
		enemy = new ArrayList<>();
		walls = new ArrayList<>();
		grass = new ArrayList<>();
		screenWarp = new ArrayList<>();
		screenWarpDestination.add(new StageFive());
		screenWarpDestination.add(new StageSix());
		super.refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,enemyX,enemyY,screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
	}
}
