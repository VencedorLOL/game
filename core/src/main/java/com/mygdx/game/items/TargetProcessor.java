package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.ClickDetector.wallRayCasting;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.animations;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TargetProcessor {
	public Circle circle;
	Entity fixated;
	float size;
	boolean checkWalkable;
	boolean rayCast;
	public Entity targetsTarget;
	String targetAnimation;
	public TextureManager.Animation target;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};

	public TargetProcessor(Entity fixated,float size,boolean checkWalkable, boolean rayCast,String targetAnimation){
		this.fixated = fixated;
		this.size = size;
		this.checkWalkable = checkWalkable;
		this.rayCast = rayCast;
		targetsTarget = new Entity(null,fixated.getX(),fixated.getY(),false);
		this.targetAnimation = targetAnimation;
	}

	public TargetProcessor(Entity fixated,float size,boolean checkWalkable, boolean rayCast){
		this.fixated = fixated;
		this.size = size;
		this.checkWalkable = checkWalkable;
		this.rayCast = rayCast;
		targetsTarget = new Entity(null,fixated.getX(),fixated.getY(),false);
	}


	public void render(){
		if(targetAnimation != null)
			targetProcesor();
		else
			circleProcesor();
	}

	private void circleProcesor(){
		if (circle == null || circle.center != stage.findATile(fixated.getX(),fixated.getY()) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Circle.CircleTile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, checkWalkable,rayCast);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		circleOverridable(temporal);
	}



	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(fixated.getX(),fixated.getY()) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Circle.CircleTile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, true,false);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		mouseMoved = !(temporal.x == lastRecordedMousePos[0] && temporal.y == lastRecordedMousePos[1]);
		if (Gdx.input.justTouched())
			mouseMoved = true;
		lastRecordedMousePos[0] = temporal.x; lastRecordedMousePos[1] = temporal.y;
		if (circle.isInsideOfCircle(temporal.x, temporal.y)) {

			if (!mouseMoved)
				targetKeyboardMovement();

			if (!circle.isInsideOfCircle(targetsTarget.getX(), targetsTarget.getY()) || mouseMoved) {
				targetsTarget.setX(roundedClick().x);
				targetsTarget.setY(roundedClick().y);
			}

			targetRender();

		} else if (!mouseMoved){
			targetKeyboardMovement();
			if (!(targetsTarget.getX() == fixated.getX() && targetsTarget.getY() == fixated.getY()))
				targetRender();
		} else {
			animations.remove(target);
			target = null;
			targetsTarget.setX(fixated.getX());
			targetsTarget.setY(fixated.getY());
		}
	}


	public void targetRender(){
		if (target == null) {
			target = new TextureManager.Animation(targetAnimation, targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || actionConfirmJustPressed()) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new TextureManager.Animation(targetAnimation, targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || actionConfirmJustPressed()) this.stop();}};
			animations.add(target);
		}
	}


	private void targetKeyboardMovement(){
		float x = targetsTarget.x; float y = targetsTarget.y;
		if (upJustPressed())
			y += globalSize();
		if (leftJustPressed())
			x -= globalSize();
		if (downJustPressed())
			y -= globalSize();
		if (rightJustPressed())
			x += globalSize();
		if(circle.isInsideOfCircle(x,y)) {
			targetsTarget.x = x;
			targetsTarget.y = y;
		} else if (circle.isInsideOfCircle(x,targetsTarget.y))
			targetsTarget.x = x;
		else if (circle.isInsideOfCircle(targetsTarget.x,y))
			targetsTarget.y = y;
	}

	public void reset(){
		circle = null;
		animations.remove(target);
		target = null;
		Camara.smoothZoom(1,30);
	}


	public float getTargetX(){return targetsTarget.getX();}
	public float getTargetY(){return targetsTarget.getY();}
	public Circle.CircleTile findATile(float x, float y){return circle.findATile(x,y);}
	public boolean isInsideCircle(float x, float y){return circle.isInsideOfCircle(x,y);}
	public void changeCheckWalkable(boolean walkable){checkWalkable = walkable;}
	public void changeRayCast(boolean cast){rayCast = cast;}
	public void changeRadius(float radius){size = radius;}
	public void changeAnimation(String animation){targetAnimation = animation;}

	public void deleteTexture(){
		for (Circle.CircleTile t : circle.circle)
			for (int i = 0; i < 13; i++)
				t.setSecondaryTexture(null,0.8f,0,false,false,i);
	}


	public void circleOverridable(Vector3 click){
		/*example:
	* 	if ((Gdx.input.justTouched() && circle.isInsideOfCircle(temporal.x, temporal.y)) || actionConfirmJustPressed()) {
			explode = true;
			character.actionDecided();
			decidingExplode = false;
		}
		else if (actionResetJustPressed()){
			circle = null;
			explode = false;
			decidingExplode = false;
		}
*/
	}



	public static class Circle{
		public ArrayList<CircleTile> circle = new ArrayList<>();
		public ArrayList<Tile> tileset;
		public ArrayList<CircleTile> circleTileset;
		public float radius;
		public Tile center;
		public boolean walkable;
		boolean rayCast;

		public Circle(Tile center, ArrayList<Tile> tileset, float radius,boolean checkWalkable,boolean rayCast){
			this.center = center;
			this.radius = radius;
			this.tileset = tileset;
			circleTileset = castTileset(tileset);
			this.walkable = checkWalkable;
			this.rayCast = rayCast;
			if (checkWalkable)
				checkWalkable();
			circle = circle();
			detectCornersOfCircle(circle);

		}

		public ArrayList<CircleTile> castTileset(ArrayList<Tile> tileset){
			ArrayList<CircleTile> finalTileset = new ArrayList<>();
			for(Tile t : tileset){
				finalTileset.add(new CircleTile(t.x,t.y));
			}
			return finalTileset;
		}

		public void renderCircle(){
			for (CircleTile t : circle){
				t.renderCircle(t.x,t.y);
				Camara.zoomToPoint(t.x,t.y,globalSize(),globalSize());
			}
		}

		public CircleTile findATile(float x, float y){
			for (CircleTile t : circle){
				if (t.x == x && t.y == y)
					return t;
			}
			return null;
		}


		public void checkWalkable(){
			for (Tile t : tileset)
				t.checkIfWalkable();
		}

		public ArrayList<CircleTile> circle(){return circle(center, tileset,radius,rayCast);}

		public ArrayList<CircleTile> circle(Tile center, ArrayList<Tile> tileset, float radius,boolean raycast){
			radius += 0.5f;
			for (Tile t : center.orthogonalTiles(tileset)) {
				if (center.relativeModuleTo(t) <= radius && (!walkable || t.walkable)) {
					circle.add(CircleTile.findATile(circleTileset,t.x,t.y));
					t.texture.corner = false;
				}
			}
			// +3 jst to be safe
			int failsafe = (int) (radius + 3);
			while(true){
				int addedATileThisLoop = 0;
				ArrayList<CircleTile> avoidConcurrentModification = new ArrayList<>();
				for(CircleTile t : circle){
					for (CircleTile tt : t.orthogonalTiles(circleTileset)) {
						if (tt.relativeModuleTo(center) <= radius && !tt.isTileAlreadyInList(circle) && !tt.isTileAlreadyInList(avoidConcurrentModification) && (!walkable || Tile.findATile(tileset,tt.x,tt.y).walkable)) {
							addedATileThisLoop++;
							avoidConcurrentModification.add(tt);
						}
					}
				}
				circle.addAll(avoidConcurrentModification);
				if (addedATileThisLoop <= 0)
					break;
				failsafe--;
				if(failsafe <= 0)
					break;
			}
			if(raycast)
				circle.removeIf(t -> wallRayCasting(center.x,center.y,t.x,t.y));
			return circle;
		}

		public void detectCornersOfCircle(ArrayList<CircleTile> circle) {
			for (CircleTile t : circle){
				boolean lu = false, u = false, ru = false,r= false, rd = false,d= false, dl = false,l = false;
				for (CircleTile tt : t.orthogonalTiles(circle)){
					if(tt.x == t.x && tt.y == t.y+globalSize())
						u = true;
					if(tt.x == t.x+globalSize() && tt.y == t.y+globalSize())
						ru = true;
					if(tt.x == t.x+globalSize() && tt.y == t.y)
						r = true;
					if(tt.x == t.x+globalSize() && tt.y == t.y-globalSize())
						rd = true;
					if(tt.x == t.x && tt.y == t.y-globalSize())
						d = true;
					if(tt.x == t.x-globalSize() && tt.y == t.y-globalSize())
						dl = true;
					if(tt.x == t.x-globalSize() && tt.y == t.y)
						l = true;
					if(tt.x == t.x-globalSize() && tt.y == t.y+globalSize())
						lu = true;
				}


				t.secondaryReset();

				if(!ru || (!r && !u))
					if(!r&&!u)
						t.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,90,false,false,1);
					else
						t.setSecondaryTexture("selectionIndicatorC",0.6f,90,false,false,1);
				if(!rd || (!r && !d))
					if(!r&&!d)
						t.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,0,false,false,3);
					else
						t.setSecondaryTexture("selectionIndicatorC",0.6f,0,false,false,3);
				if(!dl || (!d && !l))
					if(!d&&!l)
						t.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,270,false,false,5);
					else
						t.setSecondaryTexture("selectionIndicatorC",0.6f,270,false,false,5);
				if(!lu || (!l && !u))
					if(!l&&!u)
						t.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,180,false,false,7);
					else
						t.setSecondaryTexture("selectionIndicatorC",0.6f,180,false,false,7);

				if(!u) {
					t.setSecondaryTexture("selectionIndicatorS", 0.6f, 180, false, false, 0);
					if(l)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 180, false, false, 7);
					if(r)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 180, true, false, 1);
				}
				if(!r) {
					t.setSecondaryTexture("selectionIndicatorS", 0.6f, 90, false, false, 2);
					if(u)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 90, false, false, 1);
					if(d)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 90, true, false, 3);
				}
				if(!d) {
					t.setSecondaryTexture("selectionIndicatorS", 0.6f, 0, false, false, 4);
					if(r)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 0, false, false, 3);
					if(l)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 0, true, false, 5);
				}
				if(!l) {
					t.setSecondaryTexture("selectionIndicatorS", 0.6f, 270, false, false, 6);
					if(d)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 270, false, false, 5);
					if(u)
						t.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 270, true, false, 7);
				}

				t.setSecondaryTexture("selectionIndicator",0f,0,false,false,8);

			}
		}



		public boolean isInsideOfCircle(float x, float y){
			for (CircleTile t : circle)
				if (t.x == x && t.y == y)
					return true;
			return false;
		}

		public static class CircleTile{
			public float x;
			public float y;
			float base;
			float height;
			public TextureManager.DrawableObject[] texture = new TextureManager.DrawableObject[13];

			public CircleTile(float x, float y){
				this.x = x; this.y = y; this.base = globalSize(); this.height = globalSize();
			}

			public static CircleTile findATile(ArrayList<CircleTile> tileset,float x, float y){
				for(CircleTile c : tileset)
					if(c.x == x && c.y==y)
						return c;
				return null;
			}

			public void setSecondaryTexture(String texture, float opacity,int rotationDegrees, boolean flipX, boolean flipY, int numberOfTexture){
				this.texture[numberOfTexture] = new TextureManager.DrawableObject(texture,0,0,opacity,rotationDegrees,flipX,flipY);
			}

			public void secondaryReset(){
				for(int i = 0; i < 13; i++) {
					texture[i] = null;
				}
			}

			public void renderCircle(float x, float y){
				for(int i = 0; i < 13; i++)
					if (texture[i] != null) {
						texture[i].x = x; texture[i].y = y;
						TextureManager.priorityDrawables.add(texture[i]);
					}
			}

			public float relativeModuleTo(Tile tile){
				return (float) sqrt(pow(tile.x - x,2) + pow(tile.y - y,2)) / globalSize();
			}

			public boolean isTileAlreadyInList(ArrayList<CircleTile> tileset){
				for (CircleTile t : tileset)
					if (t == this)
						return true;
				return false;
			}

			public ArrayList<CircleTile> orthogonalTiles(ArrayList<CircleTile> tileset){
				ArrayList<CircleTile> tileSet = new ArrayList<>();
				for (CircleTile s : tileset){
					if( (s.x == x && s.y == y+globalSize()) ||
							(s.x == x+globalSize() && s.y == y+globalSize()) ||
							(s.x == x+globalSize() && s.y == y) ||
							(s.x == x+globalSize() && s.y == y-globalSize()) ||
							(s.x == x && s.y == y-globalSize()) ||
							(s.x == x-globalSize() && s.y == y-globalSize()) ||
							(s.x == x-globalSize() && s.y == y) ||
							(s.x == x-globalSize() && s.y == y+globalSize()))
						tileSet.add(s);
					if(tileSet.size() >= 8)
						break;
				}
				return tileSet;
			}
		}



	}


}
