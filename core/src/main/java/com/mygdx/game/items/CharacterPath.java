package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;

public class CharacterPath {
	byte numberOfKeysPressed;
	Entity testCollision = new Entity();
	ArrayList<PathStep> path;
	int steps;
	Stage stage;
	float charX, charY, thisStepX, thisStepY, prevTurnEndingX, prevTurnEndingY;
	int currentPath = 0;
	boolean pathEnded;

	public CharacterPath(float x, float y, int speed, Stage stage){
		getStats(x,y,speed,stage);
		path = new ArrayList<>();
		path.add(new PathStep());
	}

	public int[] pathProcess(){
		print("pathProcess");
		int[] speedLeft = new int[2];
		if(currentPath >= steps) currentPath = 0;
		if (!canContinue(path.get(currentPath).direction,path.get(currentPath).secondaryDirection)){
			pathEnded = true;
			return new int[]{0,0};
		}
		speedLeft[0] = (int) xPathToCoordinates(path.get(currentPath));
		speedLeft[1] = (int) yPathToCoordinates(path.get(currentPath));
		thisStepX += speedLeft[0];
		thisStepY += speedLeft[1];
		currentPath++;
		if (currentPath >= steps) {
			pathEnded = true;
			currentPath = 0;
			print("path Ended");
		}
		return speedLeft;
	}

	public boolean canContinue(char firstDirection, char secondDirection){
		int[] speedChecker = new int[2];

		switch(firstDirection){
			case 'W': speedChecker[1] = globalSize(); break;
			case 'S': speedChecker[1] = -globalSize(); break;
			case 'D': speedChecker[0] = globalSize(); break;
			case 'A': speedChecker[0] = -globalSize(); break;
		}
		switch(secondDirection) {
			case 'W': speedChecker[1] = globalSize(); break;
			case 'S': speedChecker[1] = -globalSize(); break;
			case 'D': speedChecker[0] = globalSize(); break;
			case 'A': speedChecker[0] = -globalSize(); break;
		}
		testCollision.x = thisStepX + speedChecker[0];
		testCollision.y = thisStepY + speedChecker[1];
		return !testCollision.overlapsWithStage(stage, testCollision);
	}

	public void render(){ for(PathStep p : path) p.render();}

	public void pathStarter(){
		print("started path");
		pathEnded = false;
		for (PathStep p : path){
			p.reset();
		}
	}

	public boolean pathBegin(){
		render();
		if (currentPath < steps) {
			try { doNothingSoIntelliJShutsUpAlready(path.get(currentPath));
			} catch (java.lang.IndexOutOfBoundsException ignored) {
				path.add(currentPath, new PathStep());
			}

			if (currentPath == 0)
				pathStep(path.get(0), charX, charY);
			else
				pathStep(path.get(currentPath),
					prevTurnEndingX,
					prevTurnEndingY);
		}
		else print("Path completed. Press 'c' to continue. ");

		if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			currentPath = 0;
			for (PathStep p : path)
				p.setRender(false);
			print("currentPath is: " + currentPath + " steps is: " + steps);
			return true;
		}return false;
	}

	public float xPathToCoordinates(PathStep pathStep){
		switch (pathStep.direction){
			case 'A' : {return -globalSize();}
			case 'D' : {return globalSize();}
		}
		switch (pathStep.secondaryDirection){
			case 'A' : {return -globalSize();}
			case 'D' : {return globalSize();}
			case 'N' : {return 0;}
		}
		return 0;
	}

	public float yPathToCoordinates(PathStep pathStep){
		switch (pathStep.direction) {
			case 'W': return globalSize();
			case 'S': return -globalSize();
		}
		switch (pathStep.secondaryDirection) {
			case 'W': return globalSize();
			case 'S': return -globalSize();
			case 'N': return 0;
		}
		return 0;
	}



	public void getStats(float x, float y, int speed, Stage stage){
		steps = speed / 2;
		if (steps == 0)
			steps = 1;
		this.stage = stage;
		charX = x;
		charY = y;
	}

	public void pathStep(PathStep pathStep, float x, float y){
		testCollision.x = x;
		testCollision.y = y;
		if (Gdx.input.isKeyJustPressed(Input.Keys.A) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x = x - globalSize();
			if (pathStep.direction == 'N' || pathStep.direction == 'A')
				pathStep.direction = 'A';
			else
				pathStep.secondaryDirection = 'A';
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.D) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.x = x + globalSize();
			if (pathStep.direction == 'N' || pathStep.direction == 'D')
				pathStep.direction = 'D';
			else
				pathStep.secondaryDirection = 'D';
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.W) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y = y + globalSize();
			if (pathStep.direction == 'N' || pathStep.direction == 'W')
				pathStep.direction = 'W';
			else
				pathStep.secondaryDirection = 'W';
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S) && numberOfKeysPressed < 2) {
			numberOfKeysPressed++;
			testCollision.y = y - globalSize();
			if (pathStep.direction == 'N' || pathStep.direction == 'S')
				pathStep.direction = 'S';
			else
				pathStep.secondaryDirection = 'S';
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			pathStep.direction = 'N';
			pathStep.secondaryDirection = 'N';
			pathStep.skipStep = true;
		}
		if (!testCollision.overlapsWithWalls(stage,testCollision) && (pathStep.direction != 'N' || pathStep.skipStep)){
			print("Success");
			print("current path is: " + currentPath);
			print("testx: " + testCollision.x);
			print("testy: " + testCollision.y);
			print("charx: " + charX);
			print("chary: " + charY);
			print("movement was: " + pathStep.direction);
			if(pathStep.secondaryDirection != 'N')
				print("and: " + pathStep.secondaryDirection);
			print("Success");
			currentPath++;
			pathStep.x = testCollision.x;
			pathStep.y = testCollision.y;
			prevTurnEndingX = testCollision.x;
			prevTurnEndingY = testCollision.y;
			pathStep.setRender(true);
		}
		else {
			print("failed");
			print("current path is: " + currentPath);
			print("testx: " + testCollision.x);
			print("testy: " + testCollision.y);
			print("charx: " + charX);
			print("chary: " + charY);
			print("movement was: " + pathStep.direction);
			if(pathStep.secondaryDirection != 'N')
				print("and: " + pathStep.secondaryDirection);
			print("failed");
			pathStep.reset();
		}
		numberOfKeysPressed = 0;
	}


	public static class PathStep extends Entity {
		char direction;
		char secondaryDirection;
		boolean skipStep;

		public PathStep () {
			texture = "PathStepLocation";
			setRender(false);
			reset();
		}

		public void reset(){
			direction = 'N';
			secondaryDirection = 'N';
			skipStep = false;
			setRender(false);
		}
	}

	public void doNothingSoIntelliJShutsUpAlready(PathStep shutUpAlready){
		shutUpAlready.base = globalSize();
	}

}
