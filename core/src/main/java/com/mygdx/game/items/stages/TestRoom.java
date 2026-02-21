package com.mygdx.game.items.stages;

import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Hazards;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Hazards.hazards;

public class TestRoom extends Stage {
	public TestRoom(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 10;
		finalY 		= 10;
		spawnX 		= 7;
		spawnY 		= 0;
		wallX			= new int[]{0, 1, 1, 0, 2, 10, 7, 7, 8, 8, 9, 9, 10, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 1, 2, 3, 4, 5, 5, 6, 7, 8, 8, 9, 10, 11, 11, 12, 12, 12, 12, 13, 14, 15, 15, 15, 15, 15, 14, 13, 12, 11, 11, 11, 12, 12, 9, 8, 8, 7, 1, 1, 0, -1, -2, -2, -2, -2, -1, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2, -2, -1, 0, 1};
		wallY			= new int[]{10, 10, 9, 9, 10, 10, 10, 9, 9, 10, 10, 9, 9, 9, 9, 9, 9, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, -2, -2, -2, -2, -2, -3, -3, -3, -3, -2, -2, -2, -2, -1, -1, 0, 1, 2, 2, 2, 2, 3, 4, 5, 6, 6, 6, 6, 6, 7, 8, 8, 9, 12, 12, 11, 11, 11, 12, 12, 12, 12, 11, 10, 9, 9, 8, 7, 6, 5, 4, 3, 3, 2, 1, 0, -1, -2, -3, -3, -3, -3};
		wallType		= new int[]{1, 1, 1, 1, 2, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
		enemySpawnX		= new int[]{0, 1, 2, 3, 4, 5};
		enemySpawnY 	= new int[]{7, 7, 7, 7, 7, 7};
		enemyType		= new int[]{-2, -1, 0, 1, 2, 3};
		screenWarpX 	= new int[]{9};
		screenWarpY	= new int[]{8};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		bgTexture = "tree";
		scale();
		staticCameraXmax = true;
		staticCameraXmin = true;
		staticCameraYmax = true;
		staticCameraYmin = true;
	}

	public void reStage() {		
		screenWarpDestination.add(new StageOne());
	}

	public void tilesetCleanup() {		
			tileset.add(createTile(8.0, -1.0));
			tileset.add(createTile(6.0, -1.0));
			tileset.add(createTile(7.0, -1.0));
			tileset.add(createTile(7.0, -2.0));
			tileset.add(createTile(-1.0, 0.0));
			tileset.add(createTile(-1.0, -1.0));
			tileset.add(createTile(0.0, -1.0));
			tileset.add(createTile(0.0, 11.0));
			tileset.add(createTile(-1.0, 11.0));
			tileset.add(createTile(-1.0, 10.0));
			tileset.add(createTile(11.0, 9.0));
			tileset.add(createTile(9.0, 11.0));
			tileset.add(createTile(11.0, 3.0));
			tileset.add(createTile(12.0, 3.0));
			tileset.add(createTile(13.0, 3.0));
			tileset.add(createTile(14.0, 3.0));
			tileset.add(createTile(14.0, 4.0));
			tileset.add(createTile(14.0, 5.0));
			tileset.add(createTile(13.0, 5.0));
			tileset.add(createTile(12.0, 5.0));
			tileset.add(createTile(11.0, 5.0));
			tileset.add(createTile(11.0, 4.0));
			tileset.add(createTile(12.0, 4.0));
			tileset.add(createTile(13.0, 4.0));
			tileset.add(createTile(11.0, 2.0));
			tileset.add(createTile(11.0, 1.0));
			tileset.add(createTile(11.0, 0.0));
			tileset.add(createTile(10.0, -1.0));
			tileset.add(createTile(9.0, -1.0));
			tileset.add(createTile(5.0, -1.0));
			tileset.add(createTile(6.0, -2.0));
			tileset.add(createTile(4.0, -1.0));
			tileset.add(createTile(3.0, -1.0));
			tileset.add(createTile(2.0, -1.0));
			tileset.add(createTile(1.0, -1.0));
			tileset.add(createTile(0.0, -2.0));
			tileset.add(createTile(-1.0, -2.0));
			tileset.add(createTile(-1.0, 1.0));
			tileset.add(createTile(-1.0, 2.0));
	}

	public void hazardSetter() {		
		hazards.add(Hazards.HazardNames.values()[0].getHazard((float) (0.0 * globalSize()), (float) (6.0 * globalSize())));
		hazards.add(Hazards.HazardNames.values()[1].getHazard((float) (0.0 * globalSize()), (float) (5.0 * globalSize())));
		hazards.add(Hazards.HazardNames.values()[2].getHazard((float) (0.0 * globalSize()), (float) (4.0 * globalSize()), (float) 2));
		hazards.add(Hazards.HazardNames.values()[2].getHazard((float) (0.0 * globalSize()), (float) (3.0 * globalSize()), (float) 6));
		hazards.add(Hazards.HazardNames.values()[2].getHazard((float) (2.0 * globalSize()), (float) (4.0 * globalSize()), (float) 3));
		hazards.add(Hazards.HazardNames.values()[2].getHazard((float) (10.0 * globalSize()), (float) (4.0 * globalSize()), (float) 5));
		hazards.add(Hazards.HazardNames.values()[2].getHazard((float) (7.0 * globalSize()), (float) (4.0 * globalSize()), (float) 3));
		hazards.add(new Hazards.TriggerCharacter((float) (5.0 * globalSize()), (float) (2.0 * globalSize())){
			public void overrideOnStep() {
				print("step on trigger 1!");
			}
		});
		hazards.add(new Hazards.TriggerCharacter((float) (6.0 * globalSize()), (float) (1.0 * globalSize())){
			public void overrideOnStep() {
				print("step on trigger 2!");
			}
		});
		hazards.add(new Hazards.TriggerCharacter((float) (5.0 * globalSize()), (float) (1.0 * globalSize())){
			public void overrideOnStep() {
				print("step on trigger 3!");
			}
		});
		hazards.add(new Hazards.TriggerCharacter((float) (6.0 * globalSize()), (float) (2.0 * globalSize())){
			public void overrideOnStep() {
				print("step on trigger 4!");
			}
		});
 }
}
