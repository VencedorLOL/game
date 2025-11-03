package com.mygdx.game.items.stages;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.FieldEffects;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.FieldEffects.addField;

public class StageSix extends Stage {
	public StageSix(){
		startX = 0;
		startY = 0;
		finalX = 8;
		finalY = 8;
		spawnX = 2;
		spawnY = 2;
		wallX 		= new int[]{2,3,3,4,1,0,1,2,4,5,6,3,3,3,3,3,2,1,4,5,2,4,3,5};
		wallY 		= new int[]{2,3,4,2,2,3,4,4,4,4,3,5,6,7,8,9,6,6,6,6,8,8,9,2};
		wallType 	= new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		enemySpawnX 	= new int[]{0,1};
		enemySpawnY 	= new int[]{0,8};
		enemyType 		= new int[]{1,-1};
		screenWarpX 						= new int[]{4};
		screenWarpY 						= new int[]{1};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		scale();
	}


	public void reStage() {
		screenWarpDestination.add(new StageOne());
	}

	@Override
	public void fieldSetter() {
		addField(FieldEffects.FieldNames.LIGHTNING);
		addField(FieldEffects.FieldNames.HAILSTORM);
	}
}
