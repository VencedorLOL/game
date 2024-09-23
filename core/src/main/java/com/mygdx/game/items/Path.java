package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Stage.betweenStages;

public class Path {
	Entity testCollision = new Entity();
	ArrayList<PathStep> path;
	int steps;
	Stage stage;
	float entityX, entityY;
	int currentPath = 0;
	boolean pathEnded;

	public Path(float x, float y, int speed, Stage stage){
		getStats(x,y,speed,stage);
		path = new ArrayList<>();
		path.add(new PathStep());
	}


	public int[] pathProcess(Entity entity){
		try { doNothingSoIntelliJShutsUpAlready(path.get(currentPath));
		} catch (java.lang.IndexOutOfBoundsException ignored) {
			path.add(currentPath, new PathStep()); }
		if (currentPath != 0)
			path.get(currentPath - 1).setRender(false);
		int[] speedLeft = new int[2];
		if(currentPath >= steps) currentPath = 0;
		if (cannotContinue(path.get(currentPath).directionX,path.get(currentPath).directionY,entity) || betweenStages){
			pathReset();
			pathEnded = true;
			return new int[]{0,0}; }
		speedLeft[0] = path.get(currentPath).directionX;
		speedLeft[1] = path.get(currentPath).directionY;
		currentPath++;
		if (currentPath >= steps) {
			pathEnded = true;
			currentPath = 0;
		}
		return speedLeft;
	}

	public boolean cannotContinue(int x, int y,Entity entityToIgnore){
		testCollision.x = entityX + x;
		testCollision.y = entityY + y;
		return testCollision.overlapsWithStage(stage, testCollision, entityToIgnore);
	}

	public void render(){ for(PathStep p : path) p.render();}

	public void pathStart(){
		pathEnded = false;
		pathReset();
	}

	public void pathReset(){
		currentPath = 0;
		for (PathStep p : path){
			p.reset();
		}
	}

	public void setPathTo(ArrayList<PathStep> path){
		this.path = path;
	}


	public boolean pathCreate(float x, float y, int speed, Stage stage){
		getStats(x, y, speed, stage);
		if (currentPath < steps) {
			try { doNothingSoIntelliJShutsUpAlready(path.get(currentPath));
			} catch (java.lang.IndexOutOfBoundsException ignored) {
				path.add(currentPath, new PathStep()); }

			if (currentPath == 0)
				createPathStep(path.get(0), entityX, entityY);
			else
				createPathStep(path.get(currentPath),
					path.get(currentPath - 1).getX(),
					path.get(currentPath - 1).getY());
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			currentPath = 0;
			return true;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.R))
			pathReset();


		return false;
	}



	public void getStats(float x, float y, int speed, Stage stage){
		steps = speed / 2;
		if (steps == 0)
			steps = 1;
		this.stage = stage;
		entityX = x;
		entityY = y;
	}

	public void createPathStep(PathStep pathStep, float x, float y){
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			pathStep.directionY = globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			pathStep.directionX = -globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			pathStep.directionY = -globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			pathStep.directionX = globalSize();

		testCollision.x = x + pathStep.directionX;
		testCollision.y = y + pathStep.directionY;
		if (!testCollision.overlapsWithWalls(stage,testCollision) && !pathStep.hasNoDirection()){
			currentPath++;
			pathStep.x = testCollision.x;
			pathStep.y = testCollision.y;
			pathStep.setRender(true);
		}
		else
			pathStep.reset();

	}


	public static class PathStep extends Entity {
		int directionX;
		int directionY;

		public PathStep(){
			texture = "PathStepLocation";
			setRender(false);
			reset();
		}

		public PathStep(int x, int y) {
			directionX = x;
			directionY = y;
			texture = "PathStepLocation";
			setRender(false);
		}


		public void reset(){
			directionX = 0;
			directionY = 0;
			setRender(false);
		}

		public boolean hasNoDirection(){
			return directionX == 0 && directionY == 0;
		}

	}

	public void doNothingSoIntelliJShutsUpAlready(PathStep shutUpAlready){
		shutUpAlready.base = globalSize();
	}

}
