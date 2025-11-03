package com.mygdx.game.items.stages;


import com.mygdx.game.items.FieldEffects;
import com.mygdx.game.items.Stage;
import static com.mygdx.game.items.FieldEffects.addField;

public class StageThree extends Stage {
	public StageThree(){
		startX = 0;
		startY = 0;
		finalX = 14;
		finalY = 14;
		spawnX = 0;
		spawnY = 0;
		wallX 		= new int[]{1,1,1,1,3,3,3,3,2};
		wallY 		= new int[]{1,2,3,4,1,2,3,4,4};
		wallType 	= new int[]{1,1,1,1,1,1,1,1,1};
		enemySpawnX 	= new int[]{2,1,3,2};
		enemySpawnY 	= new int[]{2,5,5,7};
		enemyType 		= new int[]{-1,-1,1,1};
		screenWarpX 						= new int[]{2};
		screenWarpY 						= new int[]{8};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageFour());
	}

	@Override
	public void fieldSetter() {
		addField(FieldEffects.FieldNames.HAILSTORM);
	}
}
