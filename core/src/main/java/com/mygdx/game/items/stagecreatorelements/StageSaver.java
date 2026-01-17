package com.mygdx.game.items.stagecreatorelements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.Floor;
import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Tile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.escapePressed;
import static com.mygdx.game.items.TextureManager.fixatedText;
import static com.mygdx.game.StageCreatorScreen.*;

public class StageSaver {
	String emptyStage =
			"package com.mygdx.game.items.stages;\n" +
					"\n" +
			"import com.mygdx.game.items.Stage;\n"+
					"\n"+
			"public class NAME_CLASS extends Stage {\n" +
			"	public NAME_CLASS(){\n" +
			"		startX 		= 0;\n" +
			"		startY 		= 0;\n" +
			"		finalX 		= FINALX_VARIABLE;\n" +
			"		finalY 		= FINALY_VARIABLE;\n" +
			"		spawnX 		= SPAWNX_VARIABLE;\n" +
			"		spawnY 		= SPAWNY_VARIABLE;\n" +
			"		wallX			= WALLX_VARIABLE;\n" +
			"		wallY			= WALLY_VARIABLE;\n" +
			"		wallType		= WALLTYPE_VARIABLE;\n" +
			"		enemySpawnX		= ENEMYSPAWN_X_VARIABLE;\n" +
			"		enemySpawnY 	= ENEMYSPAWN_Y_VARIABLE;\n" +
			"		enemyType		= ENEMYTYPE_VARIABLE;\n" +
			"		screenWarpX 	= SCREENWARPX_VARIABLE;\n" +
			"		screenWarpY	= SCREENWARPY_VARIABLE;\n" +
			"		screenWarpDestinationSpecification = SCREENWARPDESTINATION_VARIABLE;\n" +
			"		floorTexture = \"FLOOR_VARIABLE\";\n" +
			"		bgTexture = \"BACKGROUND_VARIABLE\";\n" +
			"		scale();\n" +
			"	}\n" +
			"\n" +
			"	public void reStage() {" +
			"		SCREEN_WARPS_DESTINATIONS" +
			"\n	}\n"+
			"\n" +
			"	public void tilesetCleanup() {" +
			"		TILESET_ALGORITHM" +
			"\n	}\n"+
			"}\n";

	Stage stage;
	InputText nameOfStage;
	public InputText.InformationTransferer info;
	public StageSaver(Stage stageToSave){
		stage = stageToSave;
		nameOfStage = new InputText();
		info = nameOfStage.getInfo();
	}

	File stageFile;
	ArrayList<String> destination;
	String finalDestination;
	String tileset;
	public boolean waiter(ArrayList<String> destination){
		if(escapePressed())
			return true;
		if (info.ready){
			freeze = false;
			this.destination = destination;
			setVariables();
			emptyStage = emptyStage.replace("NAME_CLASS",info.string);
			emptyStage = emptyStage.replace("FINALX_VARIABLE", stage.finalX/globalSize()+"");
			emptyStage = emptyStage.replace("FINALY_VARIABLE", stage.finalY/globalSize()+"");
			emptyStage = emptyStage.replace("SPAWNX_VARIABLE", stage.spawnX/globalSize()+"");
			emptyStage = emptyStage.replace("SPAWNY_VARIABLE", stage.spawnY/globalSize()+"");
			emptyStage = emptyStage.replace("WALLX_VARIABLE","new int[]" + Arrays.toString(stage.wallX).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("WALLY_VARIABLE", "new int[]" + Arrays.toString(stage.wallY).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("WALLTYPE_VARIABLE",  "new int[]" + Arrays.toString(stage.wallType).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("ENEMYSPAWN_X_VARIABLE", "new int[]" + Arrays.toString(stage.enemySpawnX).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("ENEMYSPAWN_Y_VARIABLE", "new int[]" + Arrays.toString(stage.enemySpawnY).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("ENEMYTYPE_VARIABLE","new int[]" + Arrays.toString(stage.enemyType).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("SCREENWARPX_VARIABLE","new int[]" + Arrays.toString(stage.screenWarpX).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("SCREENWARPY_VARIABLE","new int[]" + Arrays.toString(stage.screenWarpY).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("SCREENWARPDESTINATION_VARIABLE","new byte[]" + Arrays.toString(stage.screenWarpDestinationSpecification).replace("[","{").replace("]","}"));
			emptyStage = emptyStage.replace("FLOOR_VARIABLE",stage.floorTexture);
			emptyStage = emptyStage.replace("SCREEN_WARPS_DESTINATIONS",finalDestination);
			emptyStage = emptyStage.replace("TILESET_ALGORITHM",tileset);
			emptyStage = emptyStage.replace("BACKGROUND_VARIABLE",stage.bgTexture);
			try {
				fixatedText("Was folder created? " + new File(Gdx.files.getExternalStoragePath() + "/Stages").mkdir(),200,100,300,40,255,255,255);
				stageFile = new File(Gdx.files.getLocalStoragePath() + "Stages/"+info.string+".java");
				fixatedText("Was file created? "+stageFile.createNewFile(),200,160,300,40,255,255,255);

			} catch (IOException ignored) {fixatedText("File wasn't created. Try with another stage name",200,220,300,40,255,0,0);}

			try {
				if(stageFile != null)
					Files.write(stageFile.toPath(), List.of(emptyStage.split("\n")));
			} catch (IOException ignored){fixatedText("File coudn't be saved, for some reason",200,280,300,40,255,255,255);}


			return true;
		}
		return false;
	}

	@SuppressWarnings("all")
	public void setVariables(){
		stage.wallX = new int[stage.walls.size()];
		stage.wallY = new int[stage.walls.size()];
		stage.wallType = new int[stage.walls.size()];
		for(int i = 0; i < stage.walls.size(); i++){
			stage.wallX[i] = (int) (stage.walls.get(i).x/globalSize());
			stage.wallY[i] = (int) (stage.walls.get(i).y/globalSize());
			stage.wallType[i] = stage.walls.get(i).getType();
		}
		stage.enemySpawnX = new int[stage.enemy.size()];
		stage.enemySpawnY = new int[stage.enemy.size()];
		stage.enemyType = new int[stage.enemy.size()];
		for(int i = 0; i < stage.enemy.size(); i++){
			stage.enemySpawnX[i] = (int) (stage.enemy.get(i).x/globalSize());
			stage.enemySpawnY[i] = (int) (stage.enemy.get(i).y/globalSize());
			stage.enemyType[i] = stage.enemy.get(i).getType();
		}
		stage.screenWarpX = new int[stage.screenWarp.size()];
		stage.screenWarpY = new int[stage.screenWarp.size()];
		stage.screenWarpDestinationSpecification = new byte[stage.screenWarp.size()];
		for(int i = 0; i < stage.screenWarp.size(); i++){
			stage.screenWarpX[i] = (int) (stage.screenWarp.get(i).x/globalSize());
			stage.screenWarpY[i] = (int) (stage.screenWarp.get(i).y/globalSize());
			stage.screenWarpDestinationSpecification[i] = (byte) stage.screenWarp.get(i).destination;
		}
		finalDestination = "";
		for(String s : destination){
			finalDestination = finalDestination + "\n		screenWarpDestination.add(new " + s + "());";
		}
		ArrayList<Tile> removables = new ArrayList<>();
		ArrayList<Tile> tilesetCopy = new ArrayList<>(stage.tileset);
		boolean addedTile;
		for (int y = stage.startY; y <= stage.finalY ; y += globalSize())
			for (int x = stage.startX ; x <= stage.finalX ; x += globalSize()) {
				addedTile = false;
				for (Tile t : tilesetCopy)
					if (t.x() == x && t.y() == y) {
						tilesetCopy.remove(t);
						addedTile = true;
						break;
					}
				if(!addedTile)
					removables.add(new Tile(x, y, new Floor()));
			}
		tileset = "";
		for (Tile t : removables){
			tileset = tileset + "\n			tileset.remove(getTile(" + t.x()/globalSize() + "," + t.y()/globalSize() + "));" ;
		}
		for (Tile t : tilesetCopy){
			tileset = tileset + "\n			tileset.add(createTile("+t.x()/globalSize() + "," +t.y()/globalSize() + "));";
		}

	}


}
