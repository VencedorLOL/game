package com.mygdx.game.items;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;
import com.mygdx.game.items.enemies.Dummy;
import com.mygdx.game.items.enemies.EvilGuy;
import com.mygdx.game.items.enemies.LoopingHat;
import com.mygdx.game.items.solids.LargeBarricade;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnStageChange;
import static com.mygdx.game.items.ScreenWarp.*;

public class Stage implements Utils {
	public int startX, startY, finalX, finalY, spawnX, spawnY;

	public ArrayList<Wall> walls = new ArrayList<>();
	public int[] wallX, wallY;
	public static boolean haveWallsBeenRendered;
	public int[] wallType;

	public ArrayList<Enemy> enemy = new ArrayList<>();
	public int[] enemySpawnX, enemySpawnY;
	public static boolean haveEnemiesBeenRendered;
	public int[] enemyType;

	public ArrayList<ScreenWarp> screenWarp = new ArrayList<>();
	public int[] screenWarpX, screenWarpY;
	public static boolean haveScreenWarpsBeenRendered;
	public ArrayList<Stage> screenWarpDestination;
	public byte[] screenWarpDestinationSpecification;
	public static boolean betweenStages = false;

	public ArrayList<Floor> floor = new ArrayList<>();
	public static boolean hasFloorBeenRendered;
	public String floorTexture = "default";

	public Border border = new Border();

	public ArrayList<Tile> tileset = new ArrayList<>();

	// public ArrayList<Entity> otherEntities = new ArrayList<>();

	public float camaraX, camaraY, camaraBase, camaraHeight;
	public Stage(int startX, int startY, int finalX, int finalY, int spawnX, int spawnY,
				 int[] wallX, int[] wallY, int[] wallType ,int[] enemySpawnX, int[] enemySpawnY, int[] screenWarpX, int[] screenWarpY,
				 ArrayList<Stage> screenWarpDestination, String floorTexture,
				 byte[] screenWarpDestinationSpecification, int[] enemyType){
		refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX,wallY,wallType,enemySpawnX,enemySpawnY,screenWarpX,screenWarpY,screenWarpDestination,floorTexture,screenWarpDestinationSpecification,enemyType);
	}

	public Stage(){	}

	public void emptyStageInitializer() {
		refresh(0,0,0,0,0,0,new int[0],new int[0],new int[0],new int[0],new int[0],new int[0],new int[0],new ArrayList<>(),"Grass",new byte[0],new int[0]);
	}


	public Stage(Character character){
		character.x = spawnX;
		character.y = spawnY;
	}

	public void refresh(int startX, int startY, int finalX, int finalY, int spawnX, int spawnY,
						 int[] wallX, int[] wallY, int[] wallType, int[] enemyX, int[] enemyY, int[] screenWarpX, int[] screenWarpY,
						ArrayList<Stage> screenWarpDestination, String floorTexture,
						byte[] screenWarpDestinationSpecification, int[] enemyType){
		this.startX = startX;
		this.startY = startY;
		this.finalX = finalX;
		this.finalY = finalY;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.wallX = wallX;
		this.wallY = wallY;
		this.wallType = wallType;
		this.enemySpawnX = enemyX;
		this.enemySpawnY = enemyY;
		this.screenWarpX = screenWarpX;
		this.screenWarpY = screenWarpY;
		this.screenWarpDestination = screenWarpDestination;
		this.floorTexture = floorTexture;
		this.screenWarpDestinationSpecification = screenWarpDestinationSpecification;
		this.enemyType = enemyType;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
	}

	public void screenWarpSetter(){
		for (int i = 0; i < screenWarpX.length; i++) {
			screenWarp.add(new ScreenWarp(globalSize() * screenWarpX[i], globalSize() * screenWarpY[i], globalSize(), globalSize()));
		}
		haveScreenWarpsBeenRendered = true;
	}

	public void screenWarpRenderer(){
		if (!haveScreenWarpsBeenRendered){
			screenWarpSetter();
			haveScreenWarpsBeenRendered = true;
		}
		for (ScreenWarp s : screenWarp){
			TextureManager.addToList(s.screenWarpTexture, s.x, s.y);
		}
	}

	public void enemySetter(){
		printErr("x,y,type len: " + enemyType.length + " " + enemySpawnX.length + " " + enemySpawnY.length);
		for (int i = 0; i < enemySpawnX.length; i++) {
			printErr("x,y,type len: " + enemyType.length + " " + enemySpawnX.length + " " + enemySpawnY.length);
			enemy.add(enemyClass(enemySpawnX[i], enemySpawnY[i], enemyType == null || enemyType.length < i ? 1 : enemyType[i]));
		}
		haveEnemiesBeenRendered = true;
	}

	public void enemyRenderer(Stage stage, ParticleManager pm){
		if (!haveEnemiesBeenRendered){
			enemySetter();
			haveEnemiesBeenRendered = true;
		}
		for (Enemy e : enemy){
			if (!e.isDead) {
				e.update(stage,pm);
				e.render();
			}
		}
	}


	public void tilesetSetter(Stage stage){
		for (int y = stage.startY; y <= stage.finalY ; y += globalSize()) {
			for (int x = stage.startX ; x <= stage.finalX ; x += globalSize()) {
				tileset.add(new Tile(x, y,new Floor(floorTexture)));
			}
		}
		hasFloorBeenRendered = true;
	}

	public void tilesetRenderer(){
		if(!hasFloorBeenRendered) {
			tilesetSetter(this);
			hasFloorBeenRendered = true;
		}
		for (Tile g : tileset){
			g.render();
		}
	}

	public void wallBorder(){
		walls.add(new Wall(startX - globalSize(),startY - globalSize(),globalSize(),finalY + globalSize() * 30, false));
		walls.add(new Wall(finalX + globalSize(),startY - globalSize(),globalSize(),finalY + globalSize() * 30, false));
		walls.add(new Wall(startX,startY - globalSize() ,finalX + globalSize() * 30,globalSize(), false));
		walls.add(new Wall(startX,finalY + globalSize(),finalX + globalSize() * 30,globalSize() ,false));
	}


	public void wallSetter(){
		wallBorder();
		for (int i = 0; i < wallX.length; i++) {
			walls.add(wallClass(wallX[i], wallY[i],wallType == null ? 1 : wallType[i]));
		}
		haveWallsBeenRendered = true;
	}

	public void wallRenderer(){
		if (!haveWallsBeenRendered){
			wallSetter();
		}
		for (Wall b : walls)
			b.render();
	}

	public void stageRenderer(GameScreen gs, Stage stage){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), ( /* green */ 0), ( /* blue */ 0), 1);
			tilesetSetter(this);
			enemySetter();
			screenWarpSetter();
			wallSetter();
			betweenStages = false;

		}
		else {
			camaraX = Camara.x;
			camaraY = Camara.y;
			camaraBase = Camara.base;
			camaraHeight = Camara.height;
			tilesetRenderer();
			enemyRenderer(stage, gs.particle);
			screenWarpRenderer();
			wallRenderer();
			border.border(chara, this);
			border.border(this);
			screenWarpTrigger();
		}
	}

	public void stageRenderer(Stage stage, TextureManager tm){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), ( /* green */ 0), ( /* blue */ 0), 1);
			tilesetSetter(this);
			enemySetter();
			screenWarpSetter();
			wallSetter();
			betweenStages = false;

		}
		else {
			camaraX = Camara.x;
			camaraY = Camara.y;
			camaraBase = Camara.base;
			camaraHeight = Camara.height;
			tilesetRenderer();
			enemyRenderer(stage, new ParticleManager(tm));
			screenWarpRenderer();
			wallRenderer();
		}
	}

	public void screenWarpTrigger(){
		for(ScreenWarp s : screenWarp)
			if (s.doesCharInteractWithMe(chara)) {
				betweenStages = true;
				int pos = screenWarpDestinationSpecification[byteArraySearcherForScreenWarps(screenWarpDestinationSpecification, s.ID)];
				stage = getScreenWarpStage(pos);
				stage.reseter(chara);
			}
	}

	public void reseter(Character character){
		reseter();
		character.setX(spawnX);
		character.setY(spawnY);
		reStage();
	}

	public void reseter(){
		ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
		enemy = new ArrayList<>(); Actor.flushActorListButCharacter();
		walls = new ArrayList<>(); Wall.flushList();
		floor = new ArrayList<>();
		screenWarp = new ArrayList<>();
		refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,wallType, enemySpawnX, enemySpawnY,screenWarpX,screenWarpY,
				screenWarpDestination,floorTexture,screenWarpDestinationSpecification,enemyType);
		triggerOnStageChange();
	}

	public void reStage(){}

	public Stage getScreenWarpStage(int pos){
		return screenWarpDestination.get(pos);
	}

	public Enemy enemyClass(int x, int y, int enemyType){
		switch (enemyType){
			case -1:{
				return new Dummy(globalSize() * x, globalSize() * y);
			}
			case 1:{
				return new EvilGuy(globalSize() * x, globalSize() * y);
			}
			case 2 :{
				return new LoopingHat(globalSize() * x, globalSize() * y);
			}
		}
		return new Enemy(globalSize() * x, globalSize() * y);

	}

	public Wall wallClass(int x, int y, int wallType){
		switch (wallType){
			case 1:{
				return new Wall(globalSize() * x, globalSize() * y);
			}
			case 2 :{
				return new LargeBarricade(globalSize() * x, globalSize() * y);
			}
		}
		return new Wall(globalSize() * x, globalSize() * y);

	}


	public Tile findATile(float x, float y){
		for (Tile t : tileset){
			if (t.x() == x && t.y() == y)
				return t;
		}
		return null;
	}

	public Tile findATile(float[] xY){
		for (Tile t : tileset){
			if (t.xAndY() == xY)
				return t;
		}
		return null;
	}


}
