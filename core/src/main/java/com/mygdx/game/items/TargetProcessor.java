package com.mygdx.game.items;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.ClickDetector.wallRayCasting;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.animations;
import static java.lang.Math.*;

public class TargetProcessor {
	public Circle circle;
	Entity fixated;
	float size;
	boolean checkWalkable;
	boolean rayCast;
	public Entity targetsTarget;
	String targetAnimation;
	public TextureManager.Animation target;
	byte r = (byte) 200,g = (byte) 200,b = (byte) 200;
	float x,y;
	public float opacity = 0f;
	public float borderOpacity = .6f;
	String firTexture, secTexture;

	public TargetProcessor(Entity fixated,float size,boolean checkWalkable, boolean rayCast,String texture, String secTexture){
		this.fixated = fixated;
		this.size = size;
		this.checkWalkable = checkWalkable;
		this.rayCast = rayCast;
		targetsTarget = new Entity(null,fixated.getX(),fixated.getY(),false);
		this.firTexture = texture;
		this.secTexture = secTexture;
	}


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

	public TargetProcessor(float x, float y,float size,boolean checkWalkable, boolean rayCast){
		fixated = null;
		this.size = size;
		this.checkWalkable = checkWalkable;
		this.rayCast = rayCast;
		this.x = x; this.y = y;
		targetsTarget = new Entity(null,x,y,false);
		opacity = .2f;
	}

	public TargetProcessor(){}


	public void render(){
		if(targetAnimation != null || (firTexture != null ||secTexture!=null))
			targetProcesor();
		else if(fixated != null)
			circleProcesor();
		else if (targetsTarget != null)
			noFixatedProcesor();
		else
			customCircleProcessor();
	}

	public void changeColor(byte r, byte g, byte b){
		this.r = r;
		this.g = g;
		this.b = b;
		circle.changeColor(r &0xFF,g&0xFF,b&0xFF);
	}

	private void circleProcesor(){
		if (circle == null || circle.center != stage.findATile(fixated.getX(),fixated.getY()) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Circle.CircleTile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, checkWalkable,rayCast,r,g,b,true,opacity,borderOpacity);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		circleOverridable(temporal);
	}

	private void noFixatedProcesor(){
		if (circle == null || circle.center != stage.findATile(x,y) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Circle.CircleTile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Circle(stage.findATile(x, y), stage.tileset, size, checkWalkable,rayCast,r,g,b,false,opacity,borderOpacity);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		circleOverridable(temporal);
	}

	private void customCircleProcessor(){
		if(circle != null)
			circle.renderCircle();
		else
			print("Tried to render circle, but it was null");
		Vector3 temporal = roundedClick();
		circleOverridable(temporal);
	}


	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(fixated.getX(),fixated.getY()) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Circle.CircleTile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, true,false,r,g,b,true,opacity,borderOpacity);

		}
		if(borderOpacity != 0)
			circle.renderCircle();
		Vector3 temporal = roundedClick();
		if (circle.isInsideOfCircle(temporal.x, temporal.y)) {

			if (!cursorMoved() || leftClickJustPressed())
				targetKeyboardMovement();

			if (!circle.isInsideOfCircle(targetsTarget.getX(), targetsTarget.getY()) || (cursorMoved() || leftClickJustPressed())) {
				targetsTarget.setX(roundedClick().x);
				targetsTarget.setY(roundedClick().y);
			}
			if(circle.isInsideOfCircle(targetsTarget.getX(), targetsTarget.getY()))
				targetRender();


		} else if (!cursorMoved() || leftClickJustPressed()){
			targetKeyboardMovement();
			if (!(targetsTarget.getX() == fixated.getX() && targetsTarget.getY() == fixated.getY()) && circle.isInsideOfCircle(targetsTarget.getX(), targetsTarget.getY()))
				targetRender();
		} else {
			animations.remove(target);
			target = null;
			targetsTarget.setX(fixated.getX());
			targetsTarget.setY(fixated.getY());
		}
	}

	public void targetRender(){
		if(secTexture == null) {
			if (target == null) {
				target = new TextureManager.Animation(targetAnimation, targetsTarget) {
					public void updateOverridable() {
						if (leftClickReleased() || actionConfirmJustPressed())
							this.stop();
					}
				};
				animations.add(target);
			}
			if (target.finished) {
				target = new TextureManager.Animation(targetAnimation, targetsTarget) {
					public void updateOverridable() {
						if (leftClickReleased() || actionConfirmJustPressed())
							this.stop();
					}
				};
				animations.add(target);
			}
		} else {
			if (target == null) {
				target = new TextureManager.Animation("target", targetsTarget) {
					public void updateOverridable() {
						if (leftClickReleased() || actionConfirmJustPressed())
							this.stop();
					}

					public void updateOverridableFinal() {
						if ((targetsTarget.getX() == fixated.getX() && targetsTarget.getY() == fixated.getY()))
							target.texture = secTexture;
						else
							target.texture = firTexture;
					}
				};
				animations.add(target);
			}
			if (target.finished) {
				target = new TextureManager.Animation("target", targetsTarget) {
					public void updateOverridable() {
						if (leftClickReleased() || actionConfirmJustPressed())
							this.stop();
					}

					public void updateOverridableFinal() {
						if ((targetsTarget.getX() == fixated.getX() && targetsTarget.getY() == fixated.getY()))
							target.texture = secTexture;
						else
							target.texture = firTexture;
					}
				};
				animations.add(target);
			}
		}
	}


	int[] counter = new int[]{0,0,0,0};
	final int[] times = new int[]{40, 30, 20, 10,9,8,5};
	int[] counterState = new int[]{-1,-1,-1,-1};
	private boolean allowedKey(int key){
		if((key == 0 && upJustPressed()) || (key == 1 && rightJustPressed()) || (key == 2 && downJustPressed()) || (key == 3 && leftJustPressed())){
			counterState[key] = 0;
			return true;
		} else if ((key == 0 && upPressed()) || (key == 1 && rightPressed()) || (key == 2 && downPressed()) || (key == 3 && leftPressed())){
			if (counter[key]++ > times[counterState[key]]){
				counterState[key] += counterState[key] < times.length -1 ? 1 : 0;
				counter[key] = 0;
				return true;
			}
		} else {
			counter[key] = 0;
			counterState[key] = -1;
		}
		return false;
	}

	private void targetKeyboardMovement(){
		float x = targetsTarget.x; float y = targetsTarget.y;
		if (allowedKey(0))
			y += globalSize();
		if (allowedKey(3))
			x -= globalSize();
		if (allowedKey(2))
			y -= globalSize();
		if (allowedKey(1))
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
		targetsTarget.setX(fixated.getX());
		targetsTarget.setY(fixated.getY());

	//	getCamara().smoothZoom(1,30);
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
		int r,g,b;
		float furthestX, furthestY;
		boolean zoom;
		public float opacity;
		public float borderOpacity;

		public Circle(Tile center, ArrayList<Tile> tileset, float radius,boolean checkWalkable,boolean rayCast,int r,int g, int b,boolean zoom,float opacity,float borderOpacity){
			this.opacity = opacity;
			this.borderOpacity = borderOpacity;
			this.center = center;
			this.radius = radius;
			this.tileset = tileset;
			circleTileset = castTileset(tileset);
			this.walkable = checkWalkable;
			this.rayCast = rayCast;
			this.r = r;
			this.g = g;
			this.b = b;
			if (checkWalkable)
				checkWalkable();
			circle = circle();
			detectCornersOfCircle(circle);
			this.zoom = zoom;
			if(zoom) {
				getCamara().zoomToPoint(furthestX + center.x + abs(center.x - getCamara().x), globalSize() +furthestY + center.y + abs(center.y - getCamara().y), globalSize(), globalSize());
				print("zom " +(furthestY + center.y + abs(center.y - getCamara().y)) );
				print("zom fur " + furthestY);
				print("zom centr" + center.y);

			}
		}

		public static ArrayList<CircleTile> simpleCircle(float x, float y, float radius) {
			if(Tile.findATile(stage.tileset,x,y) != null) {
				Circle c = new Circle(Tile.findATile(stage.tileset, x, y),stage.tileset , radius - .5f, false, false, 0,0,0,false,0,0);
				return c.circle();
			} else return null;
		}

		private void changeColor(int r, int g, int b){
			this.r = r;
			this.g = g;
			this.b = b;
			for(CircleTile t : circle){
				t.texture[8].r = r;
				t.texture[8].g = g;
				t.texture[8].b = b;
			}
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
//				Camara.zoomToPoint(t.x,t.y,globalSize(),globalSize());
			}
		}

		public CircleTile findATile(float x, float y){
			for (CircleTile t : circle)
				if (t.x == x && t.y == y)
					return t;
			return null;
		}

		public CircleTile findATsTile(float x, float y){
			for (CircleTile t : circleTileset)
				if (t.x == x && t.y == y)
					return t;
			return null;
		}

		public void addToCircle(float x, float y){
			CircleTile temp = findATsTile(x,y);
			if(temp == null)
				return;
			for (CircleTile c : circle)
				if (c == temp)
					return;
			circle.add(temp);
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
				if(abs(t.x-center.x) > furthestX)
					furthestX = t.x-center.x < 0 ? abs(t.x-center.x) : abs(t.x-center.x+globalSize());
				if(abs(t.y-center.y) > furthestY)
					furthestY = t.y-center.y < 0 ? abs(t.y-center.y) : abs(t.y-center.y+globalSize());
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

				t.setSecondaryTexture("selectionIndicator",opacity,0,false,false,8);
				t.texture[8].r = (this.r & 0xFF)/255f;
				t.texture[8].g = (this.g & 0xFF)/255f;
				t.texture[8].b = (this.b & 0xFF)/255f;
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
