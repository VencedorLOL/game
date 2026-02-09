package com.mygdx.game.items;

import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.MainClass.currentStage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Utils.byteArraySearcherForScreenWarps;
import static com.mygdx.game.items.Background.*;
import static com.mygdx.game.items.FieldEffects.*;
import static com.mygdx.game.items.Hazards.clearHazards;
import static com.mygdx.game.items.Hazards.updateHazards;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnStageChange;
import static com.mygdx.game.items.ScreenWarp.*;

public class Stage  {
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

	public String bgTexture = "tree";

	//not reccomended for small rooms
	public boolean staticCameraXmin = false;
	public boolean staticCameraYmin = false;
	public boolean staticCameraXmax = false;
	public boolean staticCameraYmax = false;

	public float camaraX, camaraY, camaraBase, camaraHeight;

	public Stage(){
		this.screenWarpDestination = new ArrayList<>();
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
	}

	public void emptyStageInitializer() {
		refresh(0,0,0,0,0,0,new int[0],new int[0],new int[0],new int[0],new int[0],new int[0],new int[0],new ArrayList<>(),"Grass",new byte[0],new int[0]);
	}


	public Stage(Character character){
		character.x = spawnX;
		character.y = spawnY;
	}

	public void scale(){
		startX *= globalSize();
		startY *= globalSize();
		finalX *= globalSize();
		finalY *= globalSize();
		spawnX *= globalSize();
		spawnY *= globalSize();
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
		betweenStages = true;
		haveEnemiesBeenRendered = false;
		haveWallsBeenRendered = false;
		haveScreenWarpsBeenRendered = false;
		hasFloorBeenRendered = false;
		IDState = 0;
	}

	public void screenWarpSetter(){
		for (int i = 0; i < screenWarpX.length; i++)
			screenWarp.add(new ScreenWarp(globalSize() * screenWarpX[i], globalSize() * screenWarpY[i], globalSize(), globalSize()));
		haveScreenWarpsBeenRendered = true;
	}

	public void screenWarpRenderer(){
		if (!haveScreenWarpsBeenRendered)
			screenWarpSetter();
		for (ScreenWarp s : screenWarp)
			TextureManager.addToList(s.screenWarpTexture, s.x, s.y);
	}

	public void enemySetter(){
		for (int i = 0; i < enemySpawnX.length; i++)
			enemy.add(enemyClass(enemySpawnX[i], enemySpawnY[i], enemyType == null || enemyType.length < i ? 1 : enemyType[i]));
		customEnemySetter();
		haveEnemiesBeenRendered = true;
	}

	public void customEnemySetter(){

	}

	public void enemyRenderer(){
		if (!haveEnemiesBeenRendered)
			enemySetter();
		for (Enemy e : enemy){
			if (!e.isDead) {
				if(!Objects.equals(currentStage, "Creator"))
					e.update();
				e.render();
			}
		} enemy.removeIf(e -> e.isDead);
	}


	public void tilesetSetter(){
		for (int y = startY; y <= finalY ; y += globalSize())
			for (int x = startX ; x <= finalX ; x += globalSize())
				tileset.add(new Tile(x, y,new Floor(floorTexture)));
		tilesetCleanup();
		hasFloorBeenRendered = true;
	}

	public void tilesetCleanup() {}

	@SuppressWarnings("all")
	public Tile getTile(double x, double y){
		for (Tile t : tileset)
			if (t.x == (float) x*globalSize() && t.y == (float) y*globalSize())
				return t;
		return null;
	}

	@SuppressWarnings("all")
	public Tile createTile(double x, double y){
		return new Tile((float) (x*globalSize()), (float) (y*globalSize()),new Floor(floorTexture));
	}

	public void tilesetRenderer(){
		if(!hasFloorBeenRendered)
			tilesetSetter();
		for (Tile g : tileset)
			g.render();

	}

	/*public void wallBorder(){
	*	if(!currentStage.equals("Creator")) {
			walls.add(new Wall(border.minX - globalSize(), border.minY - globalSize(), globalSize(), border.maxY + globalSize() * 30, false));
			walls.add(new Wall(border.maxX + globalSize(), border.minY - globalSize(), globalSize(), border.maxY + globalSize() * 30, false));
			walls.add(new Wall(border.minX, border.minY - globalSize(), border.maxX + globalSize() * 30, globalSize(), false));
			walls.add(new Wall(border.minX, border.maxY + globalSize(), border.maxX + globalSize() * 30, globalSize(), false));
		}
	}*/


	public void wallSetter(){
	//	wallBorder();
		for (int i = 0; i < wallX.length; i++) {
			walls.add(wallClass(wallX[i], wallY[i],wallType == null ? 1 : wallType[i]));
		}
		haveWallsBeenRendered = true;
	}

	public void wallRenderer(){
		if (!haveWallsBeenRendered){
			wallSetter();
			haveWallsBeenRendered = true;
		}
		for (Wall b : walls)
			b.render();
	}

	//Override if field on stage enter
	public void fieldSetter(){
		//for example, do this:
		//addField(FieldEffects.FieldNames.LIGTHNING);
	}

	public void hazardSetter(){

	}


	public void stageRenderer(){
		if (betweenStages){
			clearBG();
			ScreenUtils.clear(( /* red */ 0), ( /* green */ 0), ( /* blue */ 0), 1);
			enemy.clear();
			walls.clear();
			tileset.clear();
			screenWarp.clear();
			tilesetSetter();
			border.updateCoords(this);
			wallSetter();
			enemySetter();
			screenWarpSetter();
			fieldSetter();
			hazardSetter();
			betweenStages = false;
			addBG(bgTexture);
		}
		else {
			camaraX = getCamara().x;
			camaraY = getCamara().y;
			camaraBase = getCamara().base;
			camaraHeight = getCamara().height;
			renderBGs();
			tilesetRenderer();
			enemyRenderer();
			wallRenderer();
			screenWarpRenderer();
			updateFields();
			updateHazards();
			borderUpdate();
		}
	}

	public void borderUpdate(){
		border.border(chara);
		border.border(this);
	}

	public void screenWarpTrigger(){
		for(ScreenWarp s : screenWarp)
			if (s.doesCharInteractWithMe(chara)) {
				betweenStages = true;
				int pos = screenWarpDestinationSpecification[byteArraySearcherForScreenWarps(screenWarpDestinationSpecification, s.ID)];
				stage = getScreenWarpStage(pos);
				stage.reseter();
			}
	}

	public void reseter(){
		reseterr();
		chara.setX(spawnX);
		chara.setY(spawnY);
		reStage();
	}

	public void reseterr(){
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
		clearFields();
		clearHazards();
		refresh(startX,startY,finalX,finalY,spawnX,spawnY,wallX, wallY,wallType, enemySpawnX, enemySpawnY,screenWarpX,screenWarpY,
				screenWarpDestination,floorTexture,screenWarpDestinationSpecification,enemyType);
		if (currentStage != null && !currentStage.equals("Creator"))
			triggerOnStageChange();
	}

	/**
	 * NOTE TO SELF: THIS METHOD IS NEEDED IN ORDER TO PREVENT A STACK OVERFLOW ERROR
	 */
	public void reStage(){}

	public Stage getScreenWarpStage(int pos){
		return screenWarpDestination.get(pos);
	}

	public Enemy enemyClass(int x, int y, int enemyType){
		try {
			return Enemy.Enemies.values()[enemyType - Enemy.Enemies.listDifference()].getEnemy(globalSize() * x, globalSize() * y);
		} catch (ArrayIndexOutOfBoundsException ignored){printErr("stage error: " + this.getClass().getSimpleName()); return null;}
	}

	public Wall wallClass(int x, int y, int wallType){
		return Wall.Walls.values()[wallType- Wall.Walls.listDifference()].getWall(globalSize() * x,globalSize() * y);
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
