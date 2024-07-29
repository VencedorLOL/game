package com.mygdx.game.items;

import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;
import com.mygdx.game.items.enemies.EvilGuy;
import com.mygdx.game.items.enemies.LoopingHat;

import java.util.ArrayList;
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
			screenWarp.add(new ScreenWarp(128 * screenWarpX[i], 128 * screenWarpY[i], 128, 128));
		}
		haveScreenWarpsBeenRendered = true;
	}

	public void screenWarpRenderer(TextureManager tm){
		if (!haveScreenWarpsBeenRendered){
			screenWarpSetter();
			haveScreenWarpsBeenRendered = true;
		}
		for (ScreenWarp s : screenWarp){
			tm.addToList(s.screenWarpTexture, s.x, s.y);
		}
	}

	public void enemySetter(){
		for (int i = 0; i < enemySpawnX.length; i++) {
			enemy.add(enemyClass(enemySpawnX[i], enemySpawnY[i], enemyType[i]));
		}
		haveEnemiesBeenRendered = true;
	}

	public void enemyRenderer(TextureManager tm, Stage stage, ParticleManager pm){
		if (!haveEnemiesBeenRendered){
			enemySetter();
			haveEnemiesBeenRendered = true;
		}
		for (Enemy e : enemy){
			if (!e.isDead) {
				e.update(stage,pm);
				tm.addToList(e.texture, e.x, e.y);
			}
		}
	}


	public void floorSetter(Stage stage){
		for (int y = stage.startY; y <= stage.finalY ; y += 128) {
			for (int x = stage.startX ; x <= stage.finalX ; x += 128) {
				floor.add(new Floor(floorTexture,x, y, 128, 128));
			}
		}
		hasFloorBeenRendered = true;
	}

	public void floorRenderer(TextureManager tm){
		if(!hasFloorBeenRendered) {
			floorSetter(this);
			hasFloorBeenRendered = true;
		}
		for (Floor g : floor){
			tm.addToList(floorTexture(), g.x , g.y);
		}
	}

	public void wallBorder(){
		walls.add(new Wall(startX - 128,startY - 128,128,finalY + 128 * 30, false));
		walls.add(new Wall(finalX + 128,startY - 128,128,finalY + 128 * 30, false));
		walls.add(new Wall(startX,startY - 128 ,finalX + 128 * 30,128, false));
		walls.add(new Wall(startX,finalY + 128,finalX + 128 * 30,128 ,false));
	}


	public void wallSetter(){
		wallBorder();
		for (int i = 0; i < wallX.length; i++) {
			walls.add(new Wall(128 * wallX[i], 128 * wallY[i], 128, 128));
		}
		haveWallsBeenRendered = true;
	}

	public void wallRenderer(TextureManager tm){
		if (!haveWallsBeenRendered){
			wallSetter();
		}
		for (Wall b : walls){
			if(b.doesItHaveATexture)
				tm.addToList(b.texture, b.x, b.y);
		}
	}

	public void stageRenderer(GameScreen gs, Stage stage){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
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
			floorRenderer(gs.textureManager);
			enemyRenderer(gs.textureManager, stage, gs.particle);
			screenWarpRenderer(gs.textureManager);
			wallRenderer(gs.textureManager);
			border.border(gs.chara, this);
			border.border(this);
			stageChanger(gs);
		}
	}

	public void stageChanger(GameScreen gs){
		for(ScreenWarp s : screenWarp)
			if (characterX == s.x && characterY == s.y) {
				int pos = screenWarpDestinationSpecification[byteArraySearcherForScreenWarps(screenWarpDestinationSpecification, s.screenWarpID)];
				betweenStages = true;
				gs.stage = getScreenWarpStage(pos);
				gs.stage.reStage(gs.chara);
			}
	}

	public void reStage(Character character){
		betweenStages = true;
		character.x = spawnX;
		character.y = spawnY;
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

	public Stage getScreenWarpStage(int pos){
		return screenWarpDestination.get(pos);
	}


	public Stage setStage(Stage stage){
		return stage;
	}

	public String floorTexture(){
		return floorTexture;
	}

	public Enemy enemyClass(int x, int y, int enemyType){
		switch (enemyType){
			case 1:{
				return new EvilGuy(128 * x, 128 * y);
			}
			case 2 :{
				return new LoopingHat(128 * x, 128 * y);
			}
		}
		return new Enemy(128 * x, 128 * y);

	}



}
