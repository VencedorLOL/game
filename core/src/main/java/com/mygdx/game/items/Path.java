package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.Tile.coordentatesInWalkableTile;
import static com.mygdx.game.items.Turns.turnStopTimer;

public class Path {
	Entity testCollision = new Entity(null,0,0,globalSize(),globalSize());
	ArrayList<PathStep> path;
	byte steps;
	float entityX, entityY;
	byte currentNumberOfPaths = 0;
	boolean pathEnded;
	OnVariousScenarios oVS;
	Actor owner;
	// 1 = movement
	// 2 = attack

	boolean renderBlue;

	public Path(float x, float y, int speed,Actor owner){
		getStats(x,y,speed);
		path = new ArrayList<>();
		path.add(new PathStep());
		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange(){
				pathReset();
			}
		};
		this.owner = owner;
	}


	public int[] pathProcess(){
		if (!pathEnded) {
			try {doNothingSoIntelliJShutsUpAlready(path.get(currentNumberOfPaths));}
			catch (java.lang.IndexOutOfBoundsException ignored) {path.add(currentNumberOfPaths, new PathStep());}

			if (currentNumberOfPaths != 0)
				path.get(currentNumberOfPaths - 1).setRender(false);
			int[] speedLeft = new int[2];
			if (currentNumberOfPaths >= steps) currentNumberOfPaths = 0;
			if (cannotContinue(path.get(currentNumberOfPaths).directionX, path.get(currentNumberOfPaths).directionY, owner) || betweenStages) {
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


	public boolean cannotContinue(int x, int y, Actor entityToIgnore){
		testCollision.x = entityX + x;
		testCollision.y = entityY + y;
		return entityToIgnore.overlapsWithStageWithException(stage, testCollision, entityToIgnore);
	}

	public boolean isListSizeOne(){
		return path.size() == 1;
	}


	public void renderLastStep(){
		for(int i = 0; i < path.size(); i++){
			if (i == 0) {
				path.get(0).x = owner.x;
				path.get(0).y = owner.y;
				continue;
			}
			path.get(i).x = path.get(i-1).x + path.get(i).directionX;
			path.get(i).y = path.get(i-1).y + path.get(i).directionY;
		}
		float r =  owner instanceof Friend ? ((Friend) owner).color[0] : 255;
		float g =  owner instanceof Friend ? ((Friend) owner).color[1] : 255;
		float b =  owner instanceof Friend ? ((Friend) owner).color[2] : 255;
		path.get(path.size() - 1).render = true;
		path.get(path.size() - 1).render(0.95f,0,r,g,b);
	}


	public void render(){
		ArrayList<PathStep> renderList = new ArrayList<>();
		for(PathStep p : path)
			if(p.render)
				renderList.add(p);
		for(int i = 0; i < renderList.size(); i++)
			if(i == 0)
				renderList.get(renderList.size()-1).texture = "Center";
			else
				renderList.get(renderList.size()-i-1).texturer(renderList.get(renderList.size()-i).directionX, renderList.get(renderList.size()-i).directionY);
		float r =  renderBlue ? 0   : owner instanceof Friend ? ((Friend) owner).color[0] : 255;
		float g =  renderBlue ? 255 : owner instanceof Friend ? ((Friend) owner).color[1] : 255;
		float b =  renderBlue ? 182 : owner instanceof Friend ? ((Friend) owner).color[2] : 255;
		for (PathStep p : renderList) {
			p.render(0.95f, p.rotation, r, g, b);
			p.glideProcess();
		}
		if(!renderList.isEmpty() && currentNumberOfPaths > 0 && (!renderBlue || currentNumberOfPaths >= steps) && !pathEnded) {
			Camara.smoothAttachment(renderList.get(renderList.size() - 1), 30);
		} else if (owner.controlOfCamara)
			Camara.smoothAttachment(owner,11);
	}

	public void pathStart(){
		pathEnded = false;
		pathReset();
	}

	public void pathReset(){
		currentNumberOfPaths = 0;
		renderBlue = false;
		for (PathStep p : path){
			p.reset();
		}
	}

	public void setPathTo(ArrayList<PathStep> path){
		this.path = path;
	}

	public boolean areGliding(){
		for(PathStep p : path){
			if(p.isGliding)
				return true;
		}
		return false;
	}

	public boolean pathCreate(float x, float y, int speed,Actor typeOfActor){
		getStats(x, y, speed);
		if (!areGliding()) {

			if (currentNumberOfPaths != 0 && (typeOfActor instanceof  Character || typeOfActor instanceof ControllableFriend)) {
				int temporalX = 0, temporalY = 0;
				byte counter = directionalBuffer();
				if (counter % 2 != 0)
					temporalX =  globalSize();
				if(counter - 8 >= 0)
					temporalX = -globalSize();
				if((counter & (1<<2)) != 0)
					temporalY =  globalSize();
				if((counter & (1<<1)) != 0)
					temporalY = -globalSize();

				if (getPreviousPathCoords()[0] == temporalX * -1 && getPreviousPathCoords()[1] == temporalY * -1) {
					path.get(--currentNumberOfPaths).reset();
					renderBlue = false;
					return false;
				}
			}


			if (currentNumberOfPaths >= steps) {
				// set currentNumbe.. to steps for safety and to use getCurrentParthCoords safely
				renderBlue = true;
				currentNumberOfPaths = steps;

				if (typeOfActor instanceof Character || typeOfActor instanceof ControllableFriend) {
					if (getDecidedPathFlexibility() == 1) {
						int temporalX = 0, temporalY = 0;
						byte counter = directionalBuffer();
						if (counter % 2 != 0)
							temporalX =  globalSize();
						if(counter - 8 >= 0)
							temporalX = -globalSize();
						if((counter & (1<<2)) != 0)
							temporalY =  globalSize();
						if((counter & (1<<1)) != 0)
							temporalY = -globalSize();

						if (getPreviousPathCoords()[0] == temporalX && getPreviousPathCoords()[1] == temporalY) {
							Camara.smoothAttachment(chara,12);
							turnStopTimer(10);
							currentNumberOfPaths = 0;
							return true;
						}
					}
					if (getDecidedPathFlexibility() == 2)
						if (directionalBuffer() != 0) {
							Camara.smoothAttachment(chara,13);
							turnStopTimer(10);
							currentNumberOfPaths = 0;
							return true;
						}
					if (getDecidedPathFlexibility() == 3) {
						Camara.smoothAttachment(chara,14);
						turnStopTimer(10);
						currentNumberOfPaths = 0;
						return true;
					}
				}
			}

			if (currentNumberOfPaths < steps) {
				try {
					doNothingSoIntelliJShutsUpAlready(path.get(currentNumberOfPaths));
				} catch (java.lang.IndexOutOfBoundsException ignored) {
					path.add(currentNumberOfPaths, new PathStep());
				}
				if (currentNumberOfPaths == 0) {
					createPathStep(path.get(0), entityX, entityY);
				} else {
					createPathStep(path.get(currentNumberOfPaths),
							path.get(currentNumberOfPaths - 1).getX(),
							path.get(currentNumberOfPaths - 1).getY());
				}
			}

			if (actionConfirmJustPressed()) {
				Camara.smoothAttachment(chara,30);
				turnStopTimer(10);
				currentNumberOfPaths = 0;
				renderBlue = true;
				return true;
			}

			if (actionResetJustPressed()) {
				Camara.smoothAttachment(chara,10);
				pathReset();
			}

			if (!(typeOfActor instanceof Character || typeOfActor instanceof ControllableFriend) && !path.isEmpty()) {
				currentNumberOfPaths = 0;
				return true;
			}
		}

		return false;
	}



	public void getStats(float x, float y, int speed){
		steps = (byte) (speed / 2);
		if (steps <= 0)
			steps = 1;
		entityX = x;
		entityY = y;
	}

	public void createPathStep(PathStep pathStep, float x, float y){
		byte counter = directionalBuffer();
		if (counter % 2 != 0)
			pathStep.directionX =  globalSize();
		if(counter - 8 >= 0)
			pathStep.directionX = -globalSize();
		if((counter & (1<<2)) != 0)
			pathStep.directionY =  globalSize();
		if((counter & (1<<1)) != 0)
			pathStep.directionY = -globalSize();


		testCollision.x = x + pathStep.directionX;
		testCollision.y = y + pathStep.directionY;
		if (!testCollision.overlapsWithWalls(stage,testCollision) && !pathStep.hasNoDirection() && coordentatesInWalkableTile(testCollision.x,testCollision.y)){
			currentNumberOfPaths++;
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
		int rotation = 0;

		public PathStep(){
			texture = "Center";
			reset();
		}

		public PathStep(int x, int y) {
			super(null,x,y,globalSize(),globalSize());
			directionX = x;
			directionY = y;
			texture = "Center";
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

		public void texturer(float directionX, float directionY){
			if((directionY != 0 && directionX == 0) || (directionX != 0 && directionY == 0)){
				texture = "Direction";
				rotation = directionX > 0 ? 0 : directionX < 0 ? 180 : directionY > 0 ? 90 : 270;

			} else if (directionX != 0){
				texture = "DiagonalDirection";
				rotation = directionX < 0 && directionY > 0 ? 90 : directionX < 0 ? 180 : directionY < 0 ? 270 : 0;
			}
		}


	}

	public void doNothingSoIntelliJShutsUpAlready(PathStep shutUpAlready){
		shutUpAlready.base = globalSize();
		shutUpAlready.height = globalSize();
	}

}

/*			if (owner instanceof Character) {
			if (pathStep.directionY > 0)
				if(pathStep.directionX > 0)
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingUR",x,y){public void onFinish(){pathStep.setRender(true);}});
				else if (pathStep.directionX < 0)
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingUL",x,y){public void onFinish(){pathStep.setRender(true);}});
				else
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingU",x,y){public void onFinish(){pathStep.setRender(true);}});
			else if (pathStep.directionY < 0)
				if(pathStep.directionX > 0)
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingDR",x,y){public void onFinish(){pathStep.setRender(true);}});
				else if (pathStep.directionX < 0)
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingDL",x,y){public void onFinish(){pathStep.setRender(true);}});
				else
					TextureManager.animations.add(new TextureManager.Animation("glide//glidingD",x,y){public void onFinish(){pathStep.setRender(true);}});
			else if(pathStep.directionX > 0)
				TextureManager.animations.add(new TextureManager.Animation("glide//glidingR",x,y){public void onFinish(){pathStep.setRender(true);}});
			else if (pathStep.directionX < 0)
				TextureManager.animations.add(new TextureManager.Animation("glide//glidingL",x,y){public void onFinish(){pathStep.setRender(true);}});

			} */