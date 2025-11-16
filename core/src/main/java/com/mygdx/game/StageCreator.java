package com.mygdx.game;


import com.mygdx.game.items.Camara;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.Stage;
import com.mygdx.game.items.TextureManager;

public class StageCreator {
	GUI gui;
	String emptyStage =
			"package com.mygdx.game.items.stages;\n" +
			"\n" +
			"import com.badlogic.gdx.utils.ScreenUtils;\n" +
			"import com.mygdx.game.items.Character;\n" +
			"import com.mygdx.game.items.Stage;\n" +
			"import java.util.ArrayList;\n" +
			"\n" +
			"public class  extends Stage {\n" +
			"\tpublic int startX = ;\n" +
			"\tpublic int startY = ;\n" +
			"\tpublic int finalX = ;\n" +
			"\tpublic int finalY = ;\n" +
			"\tpublic int spawnX = ;\n" +
			"\tpublic int spawnY = ;\n" +
			"\tpublic int[] wallX = {};\n" +
			"\tpublic int[] wallY = {};\n" +
			"\tpublic int[] enemySpawnX = {};\n" +
			"\tpublic int[] enemySpawnY = {};\n" +
			"\tpublic int[] enemyType = {};\n" +
			"\tpublic int[] screenWarpX = {};\n" +
			"\tpublic int[] screenWarpY = {};\n" +
			"\tpublic byte[] screenWarpDestinationSpecification = {};\n" +
			"\tpublic ArrayList<Stage> screenWarpDestination = new ArrayList<Stage>(){};\n" +
			"\tpublic String floorTexture = \"\";\n" +

			"\tpublic (){\n" +
			"\t\tsuper.refresh(startX, startY, finalX, finalY, spawnX, spawnY, wallX, wallY, enemySpawnX, enemySpawnY, screenWarpX,\n" +
			"\t\t\t\tscreenWarpY,screenWarpDestination,floorTexture,\n" +
			"\t\t\t\tscreenWarpDestinationSpecification,enemyType);\n" +
			"\t\thaveEnemiesBeenRendered = false;\n" +
			"\t\thaveWallsBeenRendered = false;\n" +
			"\t\thaveScreenWarpsBeenRendered = false;\n" +
			"\t}\n" +
			"\n" +
			"\n" +
			"\t@Override\n" +
			"\tpublic void reStage(Character chara){\n" +
			"\t\tscreenWarpDestination.add(new   );\n" +
			"\t}\n" +
			"}\n"	;

	Stage stage;
	TextureManager tm;
	Camara camara;
	int sizeX, sizeY;
	boolean hasStageBeenCreated = false;
	Character chara;

	public StageCreator(GUI gui, TextureManager tm, Camara camara, Character chara){
		this.gui = gui;
		this.tm = tm;
		this.camara = camara;
		this.chara = chara;

	}

	public void update() {
//		sizeX = gui.textBoxDetector()[0];
//		sizeY = gui.textBoxDetector()[1];
		if (sizeX > 0 && sizeY > 0 && !hasStageBeenCreated) {
			stage = new Stage();
			stage.emptyStageInitializer();
			stage.startX = 0;
			stage.startY = 0;
			stage.finalX = sizeX * 128;
			stage.finalY = sizeY * 128;
			stage.reseter();
			hasStageBeenCreated = true;
			System.out.println("created");
		}
		if (hasStageBeenCreated) {
			stage.stageRendererr();
		}


	}


}
