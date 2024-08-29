package com.mygdx.game.items;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;
import com.mygdx.game.items.enemies.EvilGuy;
import com.mygdx.game.items.enemies.LoopingHat;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ScreenWarp.*;

public class Stage implements Utils {
	public int startX, startY, finalX, finalY, spawnX,  spawnY;

	public ArrayList<Wall> walls = new ArrayList<>();
	public int[] wallX, wallY;
	public static boolean haveWallsBeenRendered;

	public ArrayList<Enemy> enemy = new ArrayList<>();
	public int[] enemySpawnX, enemySpawnY;
	public static boolean haveEnemiesBeenRendered;
	public int[] enemyType;

	public float characterX, characterY;

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

	// public ArrayList<Entity> otherEntities = new ArrayList<>();

	public float camaraX, camaraY, camaraBase, camaraHeight;
	public Stage(int startX, int startY, int finalX, int finalY, int spawnX, int spawnY,
				 int[] wallX, int[] wallY, int[] enemySpawnX, int[] enemySpawnY, int[] screenWarpX, int[] screenWarpY,
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
		this.enemySpawnX = enemySpawnX;
		this.enemySpawnY = enemySpawnY;
		this.screenWarpX = screenWarpX;
		this.screenWarpY = screenWarpY;
		this.screenWarpDestination = screenWarpDestination;
		this.floorTexture = floorTexture;
		this.enemyType = enemyType;
		this.screenWarpDestinationSpecification = screenWarpDestinationSpecification;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
	}

	public Stage(){	}

	public void emptyStageInitializer() {
		this.startX = 0;
		this.startY = 0;
		this.finalX = 0;
		this.finalY = 0;
		this.spawnX = 0;
		this.spawnY = 0;
		this.wallX = new int[0];
		this.wallY =  new int[0];
		this.enemySpawnX =  new int[0];
		this.enemySpawnY =  new int[0];
		this.screenWarpX =  new int[0];
		this.screenWarpY =  new int[0];
		this.screenWarpDestination =  new ArrayList<>();
		this.floorTexture = "Grass";
		this.enemyType =  new int[0];
		this.screenWarpDestinationSpecification = new byte[0];
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
	}




	public void characterRefresher(float characterX, float characterY){
		this.characterX = characterX;
		this.characterY = characterY;
	}

	public Stage(Character character){
		character.x = spawnX;
		character.y = spawnY;
	}

	public void refresh(int startX, int startY, int finalX, int finalY, int spawnX, int spawnY,
						 int[] wallX, int[] wallY, int[] enemyX, int[] enemyY, int[] screenWarpX, int[] screenWarpY,
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
		for (int i = 0; i < enemySpawnX.length; i++) {
			enemy.add(enemyClass(enemySpawnX[i], enemySpawnY[i], enemyType[i]));
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


	public void floorSetter(Stage stage){
		for (int y = stage.startY; y <= stage.finalY ; y += globalSize()) {
			for (int x = stage.startX ; x <= stage.finalX ; x += globalSize()) {
				floor.add(new Floor(floorTexture,x, y));
			}
		}
		hasFloorBeenRendered = true;
	}

	public void floorRenderer(){
		if(!hasFloorBeenRendered) {
			floorSetter(this);
			hasFloorBeenRendered = true;
		}
		for (Floor g : floor){
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
			walls.add(new Wall(globalSize() * wallX[i], globalSize() * wallY[i], globalSize(), globalSize()));
		}
		haveWallsBeenRendered = true;
	}

	public void wallRenderer(){
		if (!haveWallsBeenRendered){
			wallSetter();
		}
		for (Wall b : walls){
			if(b.doesItHaveATexture)
				TextureManager.addToList(b.texture, b.x, b.y);
		}
	}

	public void stageRenderer(GameScreen gs, Stage stage){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), ( /* green */ 0), ( /* blue */ 0), 1);
			floorSetter(this);
			enemySetter();
			screenWarpSetter();
			wallSetter();
			betweenStages = false;

		}
		else {
			camaraX = gs.camara.x;
			camaraY = gs.camara.y;
			camaraBase = gs.camara.base;
			camaraHeight = gs.camara.height;
			floorRenderer();
			enemyRenderer(stage, gs.particle);
			screenWarpRenderer();
			wallRenderer();
			border.border(gs.chara, this);
			border.border(this);
			screenWarpTrigger(gs);
		}
	}

	public void stageRenderer(Camara camara, Stage stage, TextureManager tm){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), ( /* green */ 0), ( /* blue */ 0), 1);
			floorSetter(this);
			enemySetter();
			screenWarpSetter();
			wallSetter();
			betweenStages = false;

		}
		else {
			camaraX = camara.x;
			camaraY = camara.y;
			camaraBase = camara.base;
			camaraHeight = camara.height;
			floorRenderer();
			enemyRenderer(stage, new ParticleManager(tm));
			screenWarpRenderer();
			wallRenderer();
		}
	}

	public void screenWarpTrigger(GameScreen gs){
		for(ScreenWarp s : screenWarp)
			if (characterX == s.x && characterY == s.y) {
				betweenStages = true;
				int pos = screenWarpDestinationSpecification[byteArraySearcherForScreenWarps(screenWarpDestinationSpecification, s.screenWarpID)];
				gs.stage = getScreenWarpStage(pos);
				gs.stage.reseter(gs.chara);
			}
	}

	public void reseter(Character character){
		reseter();
		character.setX(spawnX);
		character.setY(spawnY);
		reStage(character);
	}

	public void reseter(){
		ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
		enemy = new ArrayList<>();
		walls = new ArrayList<>();
		floor = new ArrayList<>();
		screenWarp = new ArrayList<>();
		refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY, enemySpawnX, enemySpawnY,screenWarpX,screenWarpY,
				screenWarpDestination,floorTexture,screenWarpDestinationSpecification,enemyType);
	}

	public void reStage(Character character){ }

	public Stage getScreenWarpStage(int pos){
		return screenWarpDestination.get(pos);
	}

	public Enemy enemyClass(int x, int y, int enemyType){
		switch (enemyType){
			case 1:{
				return new EvilGuy(globalSize() * x, globalSize() * y);
			}
			case 2 :{
				return new LoopingHat(globalSize() * x, globalSize() * y);
			}
		}
		return new Enemy(globalSize() * x, globalSize() * y);

	}



}
