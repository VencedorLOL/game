package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;
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
	public int[] enemyX, enemyY;
	public static boolean haveEnemiesBeenRendered;
	public int[] enemyType;

	public float characterX, characterY;

	public ArrayList<ScreenWarp> screenWarp = new ArrayList<>();
	public int[] screenWarpX, screenWarpY;
	public static boolean haveScreenWarpsBeenRendered;
	public ArrayList<Stage> screenWarpDestination;
	public byte[] screenWarpDestinationSpecification;
	public static boolean betweenStages = false;

	public ArrayList<Grass> grass = new ArrayList<>();
	public static boolean haveGrassBeenRendered;
	public String floorTexture = "default";

	public Border border = new Border();

	public float camaraX, camaraY, camaraBase, camaraHeight;
	public Stage(int startX, int startY, int finalX, int finalY, int spawnX, int spawnY,
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
		this.enemyX = enemyX;
		this.enemyY = enemyY;
		this.screenWarpX = screenWarpX;
		this.screenWarpY = screenWarpY;
		this.screenWarpDestination = screenWarpDestination;
		this.floorTexture = floorTexture;
		this.enemyType = enemyType;
		this.screenWarpDestinationSpecification = screenWarpDestinationSpecification;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		haveGrassBeenRendered = false;
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

	public int startXGetter(){
		return startX;
	}
	public int startYGetter(){
		return startY;
	}
	public int finalXGetter(){
		return finalX;
	}
	public int finalYGetter(){
		return finalY;
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
		this.enemyX = enemyX;
		this.enemyY = enemyY;
		this.screenWarpX = screenWarpX;
		this.screenWarpY = screenWarpY;
		this.screenWarpDestination = screenWarpDestination;
		this.floorTexture = floorTexture;
		this.screenWarpDestinationSpecification = screenWarpDestinationSpecification;
		this.enemyType = enemyType;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		haveGrassBeenRendered = false;
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
			tm.drawer(s.screenWarpTexture, s.x, s.y);
		}
	}

	public void enemySetter(){
		for (int i = 0; i < enemyX.length; i++) {
			enemy.add(enemyClass(enemyX[i], enemyY[i], enemyType[i]));
		}
		haveEnemiesBeenRendered = true;
	}

	public void enemyRenderer(TextureManager tm, Stage stage){
		if (!haveEnemiesBeenRendered){
			enemySetter();
			haveEnemiesBeenRendered = true;
		}
		for (Enemy e : enemy){
			if (!e.isDead) {
				e.update(stage);
				tm.drawer(e.enemyTexture, e.x, e.y);
				//dies();
			}
		}
	}

	public void dies(){
		enemy.removeIf(e -> e.health <= 0);
	}

	public void grassSetter(Stage stage){
		for (int y = stage.startYGetter(); y <= stage.finalYGetter() ; y += 128) {
			for (int x = stage.startXGetter() ; x <= stage.finalXGetter() ; x += 128) {
				grass.add(new Grass(x, y, 128, 128));
			}
		}
		haveGrassBeenRendered = true;
	}

	public void grassRenderer(TextureManager tm){
		if(!haveGrassBeenRendered) {
			grassSetter(this);
			haveGrassBeenRendered = true;
		}
		for (Grass g : grass){
			tm.drawer(floorTexture(), g.x , g.y);
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
				tm.drawer(b.box, b.x, b.y);
		}
	}

	public ArrayList<Enemy> enemyGetter(){
		return enemy;
	}

	public ArrayList<Wall> wallGetter(){
		return walls;
	}

	public ArrayList<ScreenWarp> screenWarpGetter(){
		return screenWarp;
	}

	public ArrayList<Grass> grassGetter(){
		return grass;
	}

	public void stageRenderer(GameScreen gs, Stage stage){
		if (betweenStages){
			ScreenUtils.clear(( /* red */ 0), (/* green */ 0), (/* blue */ 0), 1);
			grassSetter(this);
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
			grassRenderer(gs.textureManager);
			enemyRenderer(gs.textureManager, stage);
			screenWarpRenderer(gs.textureManager);
			wallRenderer(gs.textureManager);
			border.border(gs.chara, this);
			border.border(this);
			stageChanger(gs);
		}
	}

	public void stageChanger(GameScreen gs){
		for(ScreenWarp s : screenWarpGetter())
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
		haveGrassBeenRendered = false;
		IDState = 0;
		enemy = new ArrayList<>();
		walls = new ArrayList<>();
		grass = new ArrayList<>();
		screenWarp = new ArrayList<>();
		refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,enemyX,enemyY,screenWarpX,screenWarpY,
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
