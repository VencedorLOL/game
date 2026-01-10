package com.mygdx.game.items.stagecreatorelements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Wall.walls;
import static java.lang.Math.*;

public class StageCreator {
	public Stage stage;
	public boolean hasStageBeenCreated = false;

	String[] wallsTextures = new String[Wall.Walls.values().length];
	float[][] wallsSize = new float[Wall.Walls.values().length][2];
	String[] enemiesTextures = new String[Enemy.Enemies.values().length];
	float[][] enemiesSize = new float[Enemy.Enemies.values().length][2];

	public ArrayList<String> sWDestStages = new ArrayList<>();
	public StageCreator(){
		for(int i = 0; i < Wall.Walls.values().length; i++){
			Wall wall = Wall.Walls.values()[i].getWall(0,0);
			wallsTextures[i] = wall.getTexture();
			wallsSize[i][0] = wall.getBase();
			wallsSize[i][1] = wall.getHeight();
			walls.remove(wall);
			wall.destroyWall();
		}
		for(int i = 0; i < Enemy.Enemies.values().length; i++){
			Enemy enemy = Enemy.Enemies.values()[i].getEnemy(0,0);
			enemiesTextures[i] = enemy.getTexture();
			enemiesSize[i][0] = enemy.getBase();
			enemiesSize[i][1] = enemy.getHeight();
			enemy.destroy();
		}
	}

	public void update(float sizeX, float sizeY, boolean freeze) {
		if(!freeze) {
			if (sizeX > 0 && sizeY > 0 && !hasStageBeenCreated) {
				stage = new Stage();
				stage.emptyStageInitializer();
				stage.startX = 0;
				stage.startY = 0;
				stage.finalX = (int) ((sizeX - 1) * globalSize());
				stage.finalY = (int) ((sizeY - 1) * globalSize());
				stage.reseter();
				hasStageBeenCreated = true;
				stage.walls.clear();
				System.out.println("created");
			} else if (hasStageBeenCreated) {
				actions();
				stageModification();
				if (target != null)
					target.render();
				stage.stageRenderer();
			}
		} else if(hasStageBeenCreated)
			stage.stageRenderer();


	}

	// 0 = draw		1 = select		2 = move
	byte action;
	byte mode = 0;
	float startSelectionX, startSelectionY;
	float lastFrameX, lastFrameY;
	float[][] startPos;
	ArrayList<Wall> selectedWalls;
	ArrayList<Enemy> selectedEnemies;
	ArrayList<ScreenWarp> selectedScreenWarps;
	TargetProcessor target;
	Wall movingWall;
	Enemy movingEnemy;
	ScreenWarp movingScreenWarp;
	// 0 = wall		1 = enemy 		2 = screen warp
	byte typeOfElement = 0;
	byte typeOfSubElementWall = 0;
	byte typeOfSubElementEnemy = 0;
	byte openInterface = 0;
	boolean interfaceOfTypes;
	int preferredSWDestination;
	public void stageModification(){
		if(target == null && !stage.tileset.isEmpty()){
			target = new TargetProcessor();
			target.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,255,255,255,false,.2f,.4f);
			target.circle.circle.clear();
		}
		Vector3 vector = roundedClick();
		if(action == 0) {
			if(actionConfirmJustPressed()) {
				openInterface = openInterface != 0 ? 0 : typeOfElement == 0 ? 1 : (byte) -1;
			}
			if (leftClickJustPressed() && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY && openInterface == 0 && !interfaceOfTypes) {
				mode = (byte) (detectElement(vector) ? 1 : -1);
			}
			if (mode == 1 && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY && openInterface == 0 && !interfaceOfTypes) {
				if(typeOfElement == 0)
					for (Wall w : stage.walls)
						if (w.x == vector.x && w.y == vector.y) {
							stage.walls.remove(w);
							walls.remove(w);
							break;
						}
				if(typeOfElement == 1)
					for (Enemy w : stage.enemy)
						if (w.x == vector.x && w.y == vector.y) {
							stage.enemy.remove(w);
							w.destroy();
							break;
						}
				if(typeOfElement == 2)
					for (ScreenWarp w : stage.screenWarp)
						if (w.x == vector.x && w.y == vector.y) {
							stage.screenWarp.remove(w);
							break;
						}

			} else if (mode == -1 && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY && openInterface == 0 && !interfaceOfTypes) {
				if (!detectElement(vector)){
					if(typeOfElement == 0)
						stage.walls.add(Wall.Walls.values()[typeOfSubElementWall].getWall(vector.x, vector.y));
					if(typeOfElement == 1)
						stage.enemy.add(Enemy.Enemies.values()[typeOfSubElementEnemy].getEnemy(vector.x, vector.y));
					if(typeOfElement == 2)
						stage.screenWarp.add(new ScreenWarp((int) vector.x, (int) vector.y,preferredSWDestination));
				}

			}
			if (leftClickReleased() || openInterface != 0 || interfaceOfTypes)
				mode = 0;
		}
		if(action == 1){
			if(leftClickJustPressed() && mode != -1){
				mode = 1;
				startSelectionX = vector.x; startSelectionY = vector.y;
				selectedWalls = new ArrayList<>();
				selectedEnemies = new ArrayList<>();
				selectedScreenWarps = new ArrayList<>();
			}
			if(leftClickReleased() && mode == 1){
				mode = 0;
				float endSelectionX = vector.x, endSelectionY = vector.y;
				target.circle.circle.clear();
				if(typeOfElement == 0) {
					for (Wall w : stage.walls)
						if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
								w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY)) {
							selectedWalls.add(w);
							target.circle.addToCircle(w.x, w.y);
						}
					target.circle.circle.removeIf(Objects::isNull);
					target.circle.detectCornersOfCircle(target.circle.circle);
					// this is done so it renders all selected walls atop non selected ones.
					stage.walls.removeAll(selectedWalls);
					stage.walls.addAll(selectedWalls);
				}
				if(typeOfElement == 1) {
					for (Enemy w : stage.enemy)
						if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
								w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY)) {
							selectedEnemies.add(w);
							target.circle.addToCircle(w.x, w.y);
						}
					target.circle.circle.removeIf(Objects::isNull);
					target.circle.detectCornersOfCircle(target.circle.circle);
					// this is done so it renders all selected walls atop non selected ones.
					stage.enemy.removeAll(selectedEnemies);
					stage.enemy.addAll(selectedEnemies);
				}
				if(typeOfElement == 2) {
					for (ScreenWarp w : stage.screenWarp)
						if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
								w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY)) {
							selectedScreenWarps.add(w);
							target.circle.addToCircle(w.x, w.y);
						}
					target.circle.circle.removeIf(Objects::isNull);
					target.circle.detectCornersOfCircle(target.circle.circle);
					// this is done so it renders all selected walls atop non selected ones.
					stage.screenWarp.removeAll(selectedScreenWarps);
					stage.screenWarp.addAll(selectedScreenWarps);
				}
			}

			if(rightClickJustPressed() && mode != 1){
				mode = -1;
				startSelectionX = vector.x; startSelectionY = vector.y;
				selectedWalls = new ArrayList<>();
				selectedScreenWarps = new ArrayList<>();
				selectedEnemies = new ArrayList<>();
			}
			if(rightClickReleased() && mode == -1){
				mode = 0;
				float endSelectionX = vector.x, endSelectionY = vector.y;
				if(!(endSelectionX == startSelectionX && endSelectionY == startSelectionY)) {
					if(typeOfElement == 0) {
						for (Wall w : stage.walls)
							if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
									w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY))
								selectedWalls.add(w);
						stage.walls.removeAll(selectedWalls);
						walls.removeAll(selectedWalls);
					} if(typeOfElement == 1) {
						for (Enemy w : stage.enemy)
							if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
									w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY)) {
								selectedEnemies.add(w);
								w.destroy();
							}
						stage.enemy.removeAll(selectedEnemies);
					} if(typeOfElement == 2) {
						for (ScreenWarp w : stage.screenWarp)
							if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
									w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY))
								selectedScreenWarps.add(w);
						stage.screenWarp.removeAll(selectedScreenWarps);
					}
				}
				selectedWalls = null;
				selectedScreenWarps = null;
				selectedEnemies = null;
				target = null;
			}
			if(mode != 0){
				addToPriorityList("SelectionBox",min(startSelectionX,vector.x),min(startSelectionY,vector.y),.5f,0,
						abs(startSelectionX-vector.x)/32 + 4,abs(startSelectionY-vector.y)/32 + 4,true);
			}

		}
		if(action == 2){
			Vector3 auth = roundedClick(); //authenticClick();
			if((selectedWalls != null || selectedEnemies != null || selectedScreenWarps != null) && (!selectedWalls.isEmpty() || !selectedEnemies.isEmpty() || !selectedScreenWarps.isEmpty())){
				if(leftClickJustPressed()){
					mode = 1;
					startSelectionX = vector.x; startSelectionY = vector.y;
					lastFrameX = auth.x; lastFrameY = auth.y;
					if(typeOfElement == 0) {
						startPos = new float[selectedWalls.size()][2];
						for (int i = 0; i < selectedWalls.size(); i++) {
							startPos[i][0] = selectedWalls.get(i).x;
							startPos[i][1] = selectedWalls.get(i).y;
						}
					} if(typeOfElement == 1) {
						startPos = new float[selectedEnemies.size()][2];
						for (int i = 0; i < selectedEnemies.size(); i++) {
							startPos[i][0] = selectedEnemies.get(i).x;
							startPos[i][1] = selectedEnemies.get(i).y;
						}
					} if(typeOfElement == 2) {
						startPos = new float[selectedScreenWarps.size()][2];
						for (int i = 0; i < selectedScreenWarps.size(); i++) {
							startPos[i][0] = selectedScreenWarps.get(i).x;
							startPos[i][1] = selectedScreenWarps.get(i).y;
						}
					}
				}
				if(leftClickPressed()){
					target.circle.circle.clear();
					if(typeOfElement == 0) {
						for (Wall w : selectedWalls) {
							w.x += auth.x - lastFrameX;
							w.y += auth.y - lastFrameY;
							target.circle.addToCircle(w.x, w.y);
						}
					} if(typeOfElement == 1) {
						for (Enemy w : selectedEnemies) {
							w.x += auth.x - lastFrameX;
							w.y += auth.y - lastFrameY;
							target.circle.addToCircle(w.x, w.y);
						}
					} if(typeOfElement == 2) {
						for (ScreenWarp w : selectedScreenWarps) {
							w.x += auth.x - lastFrameX;
							w.y += auth.y - lastFrameY;
							target.circle.addToCircle(w.x, w.y);
						}
					}
					target.circle.circle.removeIf(Objects::isNull);
					target.circle.detectCornersOfCircle(target.circle.circle);
				}
				if(leftClickReleased()){
					mode = 0;
					if(typeOfElement == 0) {
						for (int i = 0; i < selectedWalls.size(); i++) {
							selectedWalls.get(i).x = startPos[i][0] + vector.x - startSelectionX;
							selectedWalls.get(i).y = startPos[i][1] + vector.y - startSelectionY;
						}
						stage.walls.removeIf(this::samePositionAsSelected);
					} if(typeOfElement == 1) {
						for (int i = 0; i < selectedEnemies.size(); i++) {
							selectedEnemies.get(i).x = startPos[i][0] + vector.x - startSelectionX;
							selectedEnemies.get(i).y = startPos[i][1] + vector.y - startSelectionY;
						}
						stage.enemy.removeIf(this::samePositionAsSelected);
					} if(typeOfElement == 2) {
						for (int i = 0; i < selectedScreenWarps.size(); i++) {
							selectedScreenWarps.get(i).x = startPos[i][0] + vector.x - startSelectionX;
							selectedScreenWarps.get(i).y = startPos[i][1] + vector.y - startSelectionY;
						}
						stage.screenWarp.removeIf(this::samePositionAsSelected);
					}
				}

			} else {
				if(leftClickJustPressed() && detectElement(vector)){
					mode = 1;
					startSelectionX = vector.x; startSelectionY = vector.y;
					lastFrameX = auth.x; lastFrameY = auth.y;
					if (typeOfElement == 0) {
						movingWall = getWall(vector);
						//this is so it renders atop the other walls
						stage.walls.remove(movingWall);
						stage.walls.add(movingWall);
					} if (typeOfElement == 1) {
						movingEnemy = getEnemy(vector);
						//this is so it renders atop the other walls
						stage.enemy.remove(movingEnemy);
						stage.enemy.add(movingEnemy);
					} if (typeOfElement == 2) {
						movingScreenWarp = getScreenWarp(vector);
						//this is so it renders atop the other walls
						stage.screenWarp.remove(movingScreenWarp);
						stage.screenWarp.add(movingScreenWarp);
					}
				}

				if(leftClickPressed() && mode == 1){
					if(typeOfElement == 0) {
						movingWall.x += auth.x - lastFrameX;
						movingWall.y += auth.y - lastFrameY;
					} if(typeOfElement == 1) {
						movingEnemy.x += auth.x - lastFrameX;
						movingEnemy.y += auth.y - lastFrameY;
					} if(typeOfElement == 2) {
						movingScreenWarp.x += auth.x - lastFrameX;
						movingScreenWarp.y += auth.y - lastFrameY;
					}
				}

				if(leftClickReleased() && mode == 1){
					mode = 0;
					if(typeOfElement == 0) {
						if (detectElement(vector))
							for (int i = 0; i < stage.walls.size(); i++)
								if (stage.walls.get(i).x == vector.x && stage.walls.get(i).y == vector.y && stage.walls.get(i) != movingWall) {
									stage.walls.remove(stage.walls.get(i));
									walls.remove(stage.walls.get(i));
								}
						movingWall.x = vector.x;
						movingWall.y = vector.y;
					} if(typeOfElement == 1) {
						if (detectElement(vector))
							for (int i = 0; i < stage.enemy.size(); i++)
								if (stage.enemy.get(i).x == vector.x && stage.enemy.get(i).y == vector.y && stage.enemy.get(i) != movingEnemy) {
									stage.enemy.remove(stage.enemy.get(i));
									stage.enemy.get(i).destroy();
								}
						movingEnemy.x = vector.x;
						movingEnemy.y = vector.y;
					} if(typeOfElement == 2) {
						if (detectElement(vector))
							for (int i = 0; i < stage.screenWarp.size(); i++)
								if (stage.screenWarp.get(i).x == vector.x && stage.screenWarp.get(i).y == vector.y && stage.screenWarp.get(i) != movingScreenWarp) {
									stage.screenWarp.remove(stage.screenWarp.get(i));
								}
						movingScreenWarp.x = vector.x;
						movingScreenWarp.y = vector.y;
					}

					selectedWalls = null;
					selectedEnemies = null;
					selectedScreenWarps = null;
					target = null;
				}
			}
			if(mode == 1) {
				lastFrameX = auth.x;
				lastFrameY = auth.y;
			}
		}
		float x = Gdx.graphics.getWidth()*.9f;
		float y = Gdx.graphics.getHeight()*.2f;
		float size = Gdx.graphics.getHeight()*0.008f;
		String mainTexture = action== 0 ? "Pencil" : action == 2 ? "Move" : action == 1 ? "Select" : "SWConfigure";
		String texture = typeOfElement == 0 ? wallsTextures[typeOfSubElementWall] : typeOfElement == 1 ? enemiesTextures[typeOfSubElementEnemy] : "ScreenWarp";
		float sizeX = typeOfElement == 0 ? wallsSize[typeOfSubElementWall][0] : typeOfElement == 1 ? enemiesSize[typeOfSubElementEnemy][0] : globalSize();
		float sizeY = typeOfElement == 0 ? wallsSize[typeOfSubElementWall][1] : typeOfElement == 1 ? enemiesSize[typeOfSubElementEnemy][1] : globalSize();
		fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",x,y,1, 0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject( mainTexture, x,y,1,0, size,size,true));
		if(action == 0) {
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",x - size*16,y,1, 0,size,size,true));
			fixatedDrawables.add(new TextureManager.DrawableObject(texture
					,x - size*16 + 2*size, y - 2*size,1,
					0,size/sizeX*32f*3/4,size/sizeY*32f*3/4,true));
		} if(action == 3){
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",x - size*16,y,1, 0,size,size,true));
			fixatedText(preferredSWDestination+"",x - size*10,y-size*12,1, size*16,255,255,255);
		}

		if(interfaceOfTypes)
			interfaceOfTypes();
		else if(openInterface == 1)
			wallSubElementSelector();
		else if(openInterface == -1)
			enemySubElementSelector();
		else if (openInterface == 2)
			destinationsInterface();

		if(action==3){
			if(actionConfirmJustPressed()){
				openInterface = 2;
			}

			if(openInterface == 0 && leftClickJustPressed() && (detectElement(vector) || (selectedScreenWarps != null && !selectedScreenWarps.isEmpty()))){
				mode = 1;
				if(selectedScreenWarps == null){
					selectedScreenWarps = new ArrayList<>();
					selectedScreenWarps.add(getScreenWarp(vector));
				}
			}

			if(mode == 1 && openInterface == 0){
				int temp = editScreenWarp();
				if(temp != -1){
					for(ScreenWarp s : selectedScreenWarps)
						s.destination = temp;
					mode = 0;
				}
			}









		}

	}

	public int editScreenWarp(){
		float intX = Gdx.graphics.getWidth()*.1f;
		float boxX = Gdx.graphics.getWidth()*.7f;
		float intY = Gdx.graphics.getHeight()*.25f;
		float intSize = Gdx.graphics.getHeight()*0.007f;
		if(info == null) {
			for (int i = 0; i < sWDestStages.size(); i++) {
				fixatedDrawables.add(new TextureManager.DrawableObject("TextBar", intX, intY+intSize*16*i+intSize*18, 1, 0, intSize * 8, intSize * 2, true));
				fixatedText(sWDestStages.get(i), intX + intSize * 16 * 1 / 4, intY + intSize * 16 * i - intSize * 14.5f, 1, intSize * 16, 255, 255, 255);
				fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox", intX + intSize * 128, intY + intSize * 16 * i, 1, 0, intSize, intSize, true));
				fixatedText(i + "", intX + intSize * 128 + intSize * 16 * 1 / 4, intY + intSize * 16 * i - intSize * 10, 1, intSize * 16, 255, 255, 255);
			}
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox", boxX, intY, 1, 0, intSize, intSize, true));
			fixatedDrawables.add(new TextureManager.DrawableObject("HealerIcon", boxX, intY, 1, 0, intSize, intSize, true));
		}
		if(leftClickJustPressed() && info == null){
			for(int i = 0; i < sWDestStages.size(); i++){
				if(cursorX() >= intX && cursorX() <= intX + intSize*16*8 && cursorY() <= intY+intSize*16*i && cursorY() >= intY+intSize*16*i - intSize*16) {
					return i;
				}
			} if (cursorX() >= boxX && cursorX() <= boxX + intSize*16 && cursorY() <= intY && cursorY() >= intY - intSize*16){
				nameOfStage = new InputText();
				info = nameOfStage.getInfo();
			}
		} if (info != null && info.ready){
			sWDestStages.add(info.string);
			nameOfStage = null;
			info = null;
		}
		return -1;
	}


	InputText nameOfStage;
	InputText.InformationTransferer info;
	public void destinationsInterface(){
		float intX = Gdx.graphics.getWidth()*.1f;
		float boxX = Gdx.graphics.getWidth()*.7f;
		float intY = Gdx.graphics.getHeight()*.25f;
		float intSize = Gdx.graphics.getHeight()*0.007f;
		for(int i = 0; i < sWDestStages.size(); i++){
			fixatedDrawables.add(new TextureManager.DrawableObject("TextBar",intX,intY+intSize*16*i+intSize*18,1,0,intSize*8,intSize*2,true));
			fixatedText(sWDestStages.get(i),intX+intSize*16*1/4,intY+intSize*16*i-intSize*14.f,1,intSize*16,255,255,255);
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",intX+intSize*128,intY+intSize*16*i,1,0,intSize,intSize,true));
			fixatedText(i+"",intX+intSize*128+intSize*16*1/4,intY+intSize*16*i-intSize*10,1,intSize*16,255,255,255);
		}
		fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",boxX,intY,1,0,intSize,intSize,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("HealerIcon",boxX,intY,1,0,intSize,intSize,true));

		if(leftClickJustPressed() && info == null){
			for(int i = 0; i < sWDestStages.size(); i++){
				if(cursorX() >= intX && cursorX() <= intX + intSize*16*8 && cursorY() <= intY+intSize*16*i && cursorY() >= intY+intSize*16*i - intSize*16) {
					preferredSWDestination = i;
					openInterface = 0;
					return;
				}
			} if (cursorX() >= boxX && cursorX() <= boxX + intSize*16 && cursorY() <= intY && cursorY() >= intY - intSize*16){
				nameOfStage = new InputText();
				info = nameOfStage.getInfo();
			}
		} if (info != null && info.ready){
			sWDestStages.add(info.string);
			preferredSWDestination = sWDestStages.size()-1;
			nameOfStage = null;
			info = null;
		}
	}


	public void wallSubElementSelector(){
		float intX = Gdx.graphics.getWidth()*.9f;
		float intY = Gdx.graphics.getHeight()*.35f;
		float intSize = Gdx.graphics.getHeight()*0.006f;
		for(int i = 0; i < Wall.Walls.values().length; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",intX-(intSize*16*(4-(i % 5))), (float) (intY+floor((i/5f))*intSize*32),1,
					0,intSize,intSize,true));
			fixatedDrawables.add(new TextureManager.DrawableObject(wallsTextures[i]
					,intX-(intSize*16*(4-(i % 5))) + 2*intSize, (float) (intY+floor((i/5f))*intSize*32) - 2*intSize,1,
					0,intSize/wallsSize[i][0]*32f*3/4,intSize/wallsSize[i][1]*32f*3/4,true));
		}
		if(leftClickJustPressed()){
			for(int i = 0; i < Wall.Walls.values().length; i++){
				if(cursorX() >=intX-(intSize*16*(4-(i % 5))) && cursorX() <= intX-(intSize*16*(4-(i % 5))) + intSize*16 && cursorY() >= intY+floor((i/5f))*intSize*32 - intSize*16 && cursorY() <= intY+floor((i/5f))*intSize*32 ) {
					typeOfSubElementWall = (byte) (i);
					openInterface = 0;
					return;
				}
			}
		}
	}

	public void enemySubElementSelector(){
		float intX = Gdx.graphics.getWidth()*.9f;
		float intY = Gdx.graphics.getHeight()*.35f;
		float intSize = Gdx.graphics.getHeight()*0.006f;
		for(int i = 0; i < Enemy.Enemies.values().length; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",intX-(intSize*16*(4-(i % 5))), (float) (intY+floor((i/5f))*intSize*32),1,
					0,intSize,intSize,true));
			fixatedDrawables.add(new TextureManager.DrawableObject(enemiesTextures[i]
					,intX-(intSize*16*(4-(i % 5))) + 2*intSize, (float) (intY+floor((i/5f))*intSize*32) - 2*intSize,1,
					0,intSize/enemiesSize[i][0]*32f*3/4,intSize/enemiesSize[i][1]*32f*3/4,true));
		}
		if(leftClickJustPressed()){
			for(int i = 0; i < Enemy.Enemies.values().length; i++){
				if(cursorX() >=intX-(intSize*16*(4-(i % 5))) && cursorX() <= intX-(intSize*16*(4-(i % 5))) + intSize*16 && cursorY() >= intY+floor((i/5f))*intSize*32 - intSize*16 && cursorY() <= intY+floor((i/5f))*intSize*32 ) {
					typeOfSubElementEnemy = (byte) (i);
					openInterface = 0;
					return;
				}
			}
		}
	}

	public void interfaceOfTypes(){
		float intX = Gdx.graphics.getWidth()*.9f;
		float intY = Gdx.graphics.getHeight()*.35f;
		float intSize = Gdx.graphics.getHeight()*0.006f;
		for(int i = 0; i < 3; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject("SelectionBox",intX-(intSize*16*(4-(i % 5))), (float) (intY+floor((i/5f))*intSize*32),1,
					0,intSize,intSize,true));
			fixatedDrawables.add(new TextureManager.DrawableObject(i == 0 ? "Rock" : i == 1 ? "EvilGuy" : "ScreenWarp"
					,intX-(intSize*16*(4-(i % 5))) + 2*intSize, (float) (intY+floor((i/5f))*intSize*32) - 2*intSize,1,
					0,intSize/globalSize()*32*3/4,intSize/globalSize()*32*3/4,true));
		}
		if(leftClickJustPressed()){
			for(int i = 0; i <3; i++){
				if(cursorX() >=intX-(intSize*16*(4-(i % 5))) && cursorX() <= intX-(intSize*16*(4-(i % 5))) + intSize*16 && cursorY() >= intY+floor((i/5f))*intSize*32 - intSize*16 && cursorY() <= intY+floor((i/5f))*intSize*32 ) {
					typeOfElement = (byte) (i);
					interfaceOfTypes = false;
					if(action == 3 && typeOfElement != 2)
						action = 0;
					return;
				}
			}
		}
	}





	public boolean samePositionAsSelected(Wall wall){
		if(!isSelected(wall))
			for(Wall w : selectedWalls)
				if(w.x == wall.x && w.y == wall.y)
					return true;
		return false;
	}

	public boolean samePositionAsSelected(Enemy wall){
		if(!isSelected(wall))
			for(Enemy w : selectedEnemies)
				if(w.x == wall.x && w.y == wall.y)
					return true;
		return false;
	}

	public boolean samePositionAsSelected(ScreenWarp wall){
		if(!isSelected(wall))
			for(ScreenWarp w : selectedScreenWarps)
				if(w.x == wall.x && w.y == wall.y)
					return true;
		return false;
	}

	public boolean isSelected(Wall wall){
		for (Wall w : selectedWalls)
			if (w == wall)
				return true;
		return false;
	}

	public boolean isSelected(Enemy wall){
		for (Enemy w : selectedEnemies)
			if (w == wall)
				return true;
		return false;
	}

	public boolean isSelected(ScreenWarp wall){
		for (ScreenWarp w : selectedScreenWarps)
			if (w == wall)
				return true;
		return false;
	}

	public boolean detectElement(Vector3 vector){
		if(typeOfElement == 0)
			for (Wall w : stage.walls)
				if(w.x == vector.x && w.y == vector.y)
					return true;
		if (typeOfElement == 1)
			for (Enemy e : stage.enemy)
				if(e.x == vector.x && e.y == vector.y)
					return true;
		if (typeOfElement == 2)
			for (ScreenWarp w : stage.screenWarp)
				if(w.x == vector.x && w.y == vector.y)
					return true;
		return false;
	}

	public Wall getWall(Vector3 vector){
		for (Wall w : stage.walls)
			if(w.x == vector.x && w.y == vector.y)
				return w;
		return null;
	}

	public Enemy getEnemy(Vector3 vector){
		for (Enemy w : stage.enemy)
			if(w.x == vector.x && w.y == vector.y)
				return w;
		return null;
	}

	public ScreenWarp getScreenWarp(Vector3 vector){
		for (ScreenWarp w : stage.screenWarp)
			if(w.x == vector.x && w.y == vector.y)
				return w;
		return null;
	}

	public void actions(){
		if(info == null) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				action = 0;
				mode = 0;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				action = 1;
				mode = 0;
				openInterface = 0;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
				action = 2;
				mode = 0;
				openInterface = 0;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
				mode = 0;
				openInterface = 0;
				interfaceOfTypes = !interfaceOfTypes;
				selectedEnemies = null;
				selectedScreenWarps = null;
				selectedWalls = null;
				movingEnemy = null;
				movingScreenWarp = null;
				movingWall = null;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.C) && typeOfElement == 2) {
				action = 3;
				mode = 0;
				openInterface = 0;
			}
		}
	}

}

