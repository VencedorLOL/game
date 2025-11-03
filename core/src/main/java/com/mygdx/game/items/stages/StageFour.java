package com.mygdx.game.items.stages;

import com.mygdx.game.items.FieldEffects;
import com.mygdx.game.items.Stage;
import static com.mygdx.game.items.FieldEffects.addField;

public class StageFour extends Stage {
	public StageFour(){
		startX = 0;
		startY = 0;
		finalX = 20;
		finalY = 20;
		spawnX = 10;
		spawnY = 0;
		wallX 		= new int[]{5};
		wallY 		= new int[]{15};
		wallType 	= new int[]{2};
		enemySpawnX 	= new int[]{};
		enemySpawnY 	= new int[]{};
		enemyType 		= new int[]{};
		screenWarpX 						= new int[]{1,2};
		screenWarpY 						= new int[]{1,1};
		screenWarpDestinationSpecification = new byte[]{0,1};
		floorTexture = "Sand";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageFive());
		screenWarpDestination.add(new StageSix());
	}

	public void fieldSetter(){
		addField(FieldEffects.FieldNames.LIGHTNING);
	}

}
