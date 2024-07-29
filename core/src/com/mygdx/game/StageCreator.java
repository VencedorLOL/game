package com.mygdx.game;

public class StageCreator {
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
			"\tpublic void reStage(Character character){\n" +
			"\t\tScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);\n" +
			"\t\tbetweenStages = true;\n" +
			"\t\tcharacter.setX(spawnX);\n" +
			"\t\tcharacter.setY(spawnY);\n" +
			"\t\tenemy = new ArrayList<>();\n" +
			"\t\twalls = new ArrayList<>();\n" +
			"\t\tfloor = new ArrayList<>();\n" +
			"\t\tscreenWarp = new ArrayList<>();\n" +
			"\t\tscreenWarpDestination.add(new );\n" +
			"\t\tscreenWarpDestination.add(new );\n" +
			"\t\tsuper.refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,enemySpawnX,enemySpawnY,screenWarpX,\n" +
			"\t\t\t\tscreenWarpY,screenWarpDestination,floorTexture,\n" +
			"\t\t\t\tscreenWarpDestinationSpecification,enemyType);\n" +
			"\t}\n" +
			"}\n";



}
