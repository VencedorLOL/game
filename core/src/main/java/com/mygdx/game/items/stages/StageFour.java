package com.mygdx.game.items.stages;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.FieldEffects;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.FieldEffects.addField;

public class StageFour extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*20;
	public int finalY = globalSize()*20;
	public int spawnX = globalSize() * 10;
	public int spawnY = 0;
	public int[] wallX = {5};
	public int[] wallY = {15};
	public int[] wallType = {2};
	public int[] enemyX = {};
	public int[] enemyY = {};
	public int[] enemyType = {};
	public int[] screenWarpX = {1,2};
	public int[] screenWarpY = {1,1};
	public byte[] screenWarpDestinationSpecification = {0,1};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};
	public String floorTexture = "Sand";
	public StageFour(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY,wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		wallType = new int[]{2};
	}


	@Override
	public void reStage(){
		screenWarpDestination.add(new StageFive());
		screenWarpDestination.add(new StageSix());
	}

	public void fieldSetter(){
		addField(FieldEffects.FieldNames.LIGHTNING);
	}

}
