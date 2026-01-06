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
import static com.mygdx.game.items.TextureManager.addToPriorityList;
import static com.mygdx.game.items.Wall.walls;
import static java.lang.Math.*;

public class StageCreator {
	public Stage stage;
	int sizeX, sizeY;
	public boolean hasStageBeenCreated = false;

	public StageCreator(){}

	public void update(float sizeX, float sizeY) {
		if (sizeX > 0 && sizeY > 0 && !hasStageBeenCreated) {
			stage = new Stage();
			stage.emptyStageInitializer();
			stage.startX = 0;
			stage.startY = 0;
			stage.finalX = (int) ((sizeX - 1)* globalSize());
			stage.finalY = (int) ((sizeY - 1) * globalSize());
			stage.reseter();
			hasStageBeenCreated = true;
			stage.walls.clear();
			System.out.println("created");
		}
		else if (hasStageBeenCreated) {
			actions();
			stageModification();
			if(target != null)
				target.render();
			stage.stageRenderer();
		}

	}

	// 0 = draw		1 = select		2 = move
	byte action;
	byte mode = 0;
	float startSelectionX, startSelectionY;
	float lastFrameX, lastFrameY;
	float[][] startPos;
	ArrayList<Wall> selectedWalls;
	TargetProcessor target;
	Wall movingWall;
	public void stageModification(){
		if(target == null && !stage.tileset.isEmpty()){
			target = new TargetProcessor();
			target.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,255,255,255,false,.2f,.4f);
			target.circle.circle.clear();
		}
		Vector3 vector = roundedClick();
		if(action == 0) {
			if (leftClickJustPressed() && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY) {
				mode = (byte) (detectWall(vector) ? 1 : -1);
			}
			if (mode == 1 && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY) {
				for (Wall w : stage.walls) {
					if (w.x == vector.x && w.y == vector.y) {
						stage.walls.remove(w);
						walls.remove(w);
						break;
					}
				}
			} else if (mode == -1 && vector.x <= stage.finalX && vector.x >= stage.startX && vector.y >= stage.startY && vector.y <= stage.finalY) {
				if (!detectWall(vector))
					stage.walls.add(new Wall(vector.x, vector.y));

			}
			if (leftClickReleased())
				mode = 0;
		}
		if(action == 1){
			if(leftClickJustPressed() && mode != -1){
				mode = 1;
				startSelectionX = vector.x; startSelectionY = vector.y;
				selectedWalls = new ArrayList<>();
			}
			if(leftClickReleased() && mode == 1){
				mode = 0;
				float endSelectionX = vector.x, endSelectionY = vector.y;
				target.circle.circle.clear();
				for (Wall w : stage.walls)
					if(w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX,endSelectionX) &&
							w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY,endSelectionY)) {
						selectedWalls.add(w);
						target.circle.addToCircle(w.x,w.y);
					}
				target.circle.circle.removeIf(Objects::isNull);
				target.circle.detectCornersOfCircle(target.circle.circle);
				// this is done so it renders all selected walls atop non selected ones.
				stage.walls.removeAll(selectedWalls);
				stage.walls.addAll(selectedWalls);
			}

			if(rightClickJustPressed() && mode != 1){
				mode = -1;
				startSelectionX = vector.x; startSelectionY = vector.y;
				selectedWalls = new ArrayList<>();
			}
			if(rightClickReleased() && mode == -1){
				mode = 0;
				float endSelectionX = vector.x, endSelectionY = vector.y;
				if(!(endSelectionX == startSelectionX && endSelectionY == startSelectionY)) {
					for (Wall w : stage.walls)
						if (w.x >= min(startSelectionX, endSelectionX) && w.x <= max(startSelectionX, endSelectionX) &&
								w.y >= min(startSelectionY, endSelectionY) && w.y <= max(startSelectionY, endSelectionY))
							selectedWalls.add(w);
					stage.walls.removeAll(selectedWalls);
					walls.removeAll(selectedWalls);
				}
				selectedWalls = null;
				target = null;
			}
			if(mode != 0){
				addToPriorityList("SelectionBox",min(startSelectionX,vector.x),min(startSelectionY,vector.y),.5f,0,
						abs(startSelectionX-vector.x)/32 + 4,abs(startSelectionY-vector.y)/32 + 4,true);
			}

		}
		if(action == 2){
			Vector3 auth = roundedClick(); //authenticClick();
			if(selectedWalls != null && !selectedWalls.isEmpty()){
				if(leftClickJustPressed()){
					mode = 1;
					startSelectionX = vector.x; startSelectionY = vector.y;
					lastFrameX = auth.x; lastFrameY = auth.y;
					startPos = new float[selectedWalls.size()][2];
					for(int i = 0; i < selectedWalls.size(); i++){
						startPos[i][0] = selectedWalls.get(i).x;
						startPos[i][1] = selectedWalls.get(i).y;
					}
				}
				if(leftClickPressed()){
					target.circle.circle.clear();
					for(Wall w : selectedWalls){
						w.x += auth.x - lastFrameX;
						w.y += auth.y - lastFrameY;
						target.circle.addToCircle(w.x,w.y);
					}
					target.circle.circle.removeIf(Objects::isNull);
					target.circle.detectCornersOfCircle(target.circle.circle);
				}
				if(leftClickReleased()){
					mode = 0;
					for(int i = 0; i < selectedWalls.size(); i++){
						selectedWalls.get(i).x = startPos[i][0] + vector.x - startSelectionX;
						selectedWalls.get(i).y = startPos[i][1] + vector.y - startSelectionY;
					}
					stage.walls.removeIf(this::samePositionAsSelected);
				}

			} else {
				if(leftClickJustPressed()){
					mode = 1;
					startSelectionX = vector.x; startSelectionY = vector.y;
					lastFrameX = auth.x; lastFrameY = auth.y;
					if (detectWall(vector))
						movingWall = getWall(vector);
					//this is so it renders atop the other walls
					stage.walls.remove(movingWall);
					stage.walls.add(movingWall);
				}

				if(leftClickPressed() && mode == 1){
					movingWall.x += auth.x - lastFrameX;
					movingWall.y += auth.y - lastFrameY;
				}

				if(leftClickReleased()){
					mode = 0;
					if(detectWall(vector))
						for (int i = 0; i < stage.walls.size(); i++)
							if(stage.walls.get(i).x == vector.x && stage.walls.get(i).y == vector.y && stage.walls.get(i) != movingWall){
								stage.walls.remove(stage.walls.get(i));
								walls.remove(stage.walls.get(i));
							}
					movingWall.x = vector.x;
					movingWall.y = vector.y;

					selectedWalls = null;
					target = null;
				}
			}
			if(mode == 1) {
				lastFrameX = auth.x;
				lastFrameY = auth.y;
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

	public boolean isSelected(Wall wall){
		for (Wall w : selectedWalls)
			if (w == wall)
				return true;
		return false;
	}

	public boolean detectWall(Vector3 vector){
		for (Wall w : stage.walls)
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

	public void actions(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			action = 0;
			mode = 0;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			action = 1;
			mode = 0;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			action = 2;
			mode = 0;
		}
	}

}

