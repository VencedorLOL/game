package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.Tile.coordentatesInWalkableTile;

public class Path {
	Entity testCollision = new Entity();
	ArrayList<PathStep> path;
	byte steps;
	float entityX, entityY;
	byte currentNumberOfPaths = 0;
	boolean pathEnded;
	OnVariousScenarios oVS;
	// 1 = movement
	// 2 = attack

	public Path(float x, float y, int speed, Stage stage){
		getStats(x,y,speed);
		path = new ArrayList<>();
		path.add(new PathStep());
		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange(){
				pathReset();
				print("Path resetted because stage changed.");
			}
		};
	}


	public int[] pathProcess(Entity entity){
		if (!pathEnded) {
			try {doNothingSoIntelliJShutsUpAlready(path.get(currentNumberOfPaths));}
			catch (java.lang.IndexOutOfBoundsException ignored) {path.add(currentNumberOfPaths, new PathStep());}

			if (currentNumberOfPaths != 0)
				path.get(currentNumberOfPaths - 1).setRender(false);
			int[] speedLeft = new int[2];
			if (currentNumberOfPaths >= steps) currentNumberOfPaths = 0;
			if (cannotContinue(path.get(currentNumberOfPaths).directionX, path.get(currentNumberOfPaths).directionY, entity) || betweenStages) {
				pathReset();
				pathEnded = true;
				return new int[]{0, 0};
			}
			speedLeft[0] = path.get(currentNumberOfPaths).directionX;
			speedLeft[1] = path.get(currentNumberOfPaths).directionY;
			currentNumberOfPaths++;
			if (currentNumberOfPaths >= steps) {
				pathEnded = true;
				currentNumberOfPaths = 0;
			}
			return speedLeft;
		}
		return null;
	}

	public int[] getPreviousPathCoords(){
		int[] coords = new int[2];
		if (currentNumberOfPaths != 0){
			coords[0] = path.get(currentNumberOfPaths - 1).directionX;
			coords[1] = path.get(currentNumberOfPaths - 1).directionY;
		}
		else {
			coords[0] = path.get(0).directionX;
			coords[1] = path.get(0).directionY;
		}
		return coords;
	}

	public int[] getCurrentPathCoords(){
		int[] coords = new int[2];
		coords[0] = path.get(currentNumberOfPaths).directionX;
		coords[1] = path.get(currentNumberOfPaths).directionY;
		return coords;
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
		currentNumberOfPaths = 0;
		for (PathStep p : path){
			p.reset();
		}
	}

	public void setPathTo(ArrayList<PathStep> path){
		this.path = path;
	}


	public boolean pathCreate(float x, float y, int speed, byte typeOfPath){
		getStats(x, y, speed);

		if (currentNumberOfPaths >= steps){
			// set currentNumbe.. to steps for safety and to use getCurrentParthCoords safely
			currentNumberOfPaths = steps;
			if (getDecidedPathFlexibility() == 1){
				int temporalX = 0, temporalY = 0;
				if (Gdx.input.isKeyJustPressed(Input.Keys.W))
					temporalY = globalSize();
				if (Gdx.input.isKeyJustPressed(Input.Keys.A))
					temporalX = -globalSize();
				if (Gdx.input.isKeyJustPressed(Input.Keys.S))
					temporalY = -globalSize();
				if (Gdx.input.isKeyJustPressed(Input.Keys.D))
					temporalX = globalSize();

				if (getPreviousPathCoords()[0] == temporalX && getPreviousPathCoords()[1] == temporalY){
					currentNumberOfPaths = 0;
					return true;
				}
			}
			if (getDecidedPathFlexibility() == 2)
				if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A) ||
						Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
					currentNumberOfPaths = 0;
					return true;
				}
			if (getDecidedPathFlexibility() == 3){
				currentNumberOfPaths = 0;
				return true;
			}
		}

		if (currentNumberOfPaths < steps) {
			try { doNothingSoIntelliJShutsUpAlready(path.get(currentNumberOfPaths));
			} catch (java.lang.IndexOutOfBoundsException ignored) {
				path.add(currentNumberOfPaths, new PathStep()); }
			if (currentNumberOfPaths == 0) {
				createPathStep(path.get(0), entityX, entityY, typeOfPath);
			}
			else {
				createPathStep(path.get(currentNumberOfPaths),
					path.get(currentNumberOfPaths - 1).getX(),
					path.get(currentNumberOfPaths - 1).getY(),typeOfPath);
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			currentNumberOfPaths = 0;
			return true;
		}


		if(Gdx.input.isKeyJustPressed(Input.Keys.R))
			pathReset();

		return false;
	}



	public void getStats(float x, float y, int speed){
		steps = (byte) (speed / 2);
		if (steps <= 0)
			steps = 1;
		entityX = x;
		entityY = y;
	}

	public void createPathStep(PathStep pathStep, float x, float y, byte typeOfPath){
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			pathStep.directionY =  globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			pathStep.directionX = -globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			pathStep.directionY = -globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			pathStep.directionX =  globalSize();

		testCollision.x = x + pathStep.directionX;
		testCollision.y = y + pathStep.directionY;
		if (!testCollision.overlapsWithWalls(stage,testCollision) && !pathStep.hasNoDirection() && coordentatesInWalkableTile(testCollision.x,testCollision.y)){
			currentNumberOfPaths++;
			pathStep.x = testCollision.x;
			pathStep.y = testCollision.y;
			pathStep.setRender(true);
			pathStep.setPath(typeOfPath);
		}
		else
			pathStep.reset();
	}


	public static class PathStep extends Entity {
		int directionX;
		int directionY;
		boolean procesed = false;

		public PathStep(){
			texture = "PathStepLocation";
			reset();
		}

		public PathStep(int x, int y, byte type) {
			directionX = x;
			directionY = y;
			if (type == 1)
				texture = "PathStepLocation";
			if (type == 2)
				texture = "attackStep";
			setRender(false);
		}

		public void setPath(byte type){
			if (type == 1)
				texture = "PathStepLocation";
			if (type == 2)
				texture = "attackStep";
		}

		public void reset(){
			directionX = 0;
			directionY = 0;
			setRender(false);
			procesed = false;
		}

		public boolean hasNoDirection(){
			return directionX == 0 && directionY == 0;
		}

	}

	public void doNothingSoIntelliJShutsUpAlready(PathStep shutUpAlready){
		shutUpAlready.base = globalSize();
	}

}
