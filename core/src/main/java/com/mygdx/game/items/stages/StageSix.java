package com.mygdx.game.items.stages;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.FieldEffects;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.FieldEffects.addField;

public class StageSix extends Stage {
	public int startX = 0;
	public int startY = 0;
	public int finalX = globalSize()*8;
	public int finalY = globalSize()*8;
	public int spawnX = globalSize()*2;
	public int spawnY = globalSize()*2;
	public int[] wallX    = {2,3,3,4,1,0,1,2,4,5,6,3,3,3,3,3,2,1,4,5,2,4,3,5};
	public int[] wallY    = {2,3,4,2,2,3,4,4,4,4,3,5,6,7,8,9,6,6,6,6,8,8,9,2};
	public int[] wallType = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	public int[] enemyX = {0,1};
	public int[] enemyY = {0,8};
	public int[] enemyType = {1,-1};
	public int[] screenWarpX = {4};
	public int[] screenWarpY = {1};
	public byte[] screenWarpDestinationSpecification = {0};
	public ArrayList<Stage> screenWarpDestination = new ArrayList<>(){};
	public String floorTexture = "Grass";
	public StageSix(){
		super.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY,wallType, enemyX, enemyY, screenWarpX,
				screenWarpY,screenWarpDestination,floorTexture,
				screenWarpDestinationSpecification,enemyType);
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
	}


	@Override
	public void reStage(){
		screenWarpDestination.add(new StageOne());
	}

	@Override
	public void fieldSetter() {
		addField(FieldEffects.FieldNames.LIGHTNING);
		addField(FieldEffects.FieldNames.HAILSTORM);
	}
}
