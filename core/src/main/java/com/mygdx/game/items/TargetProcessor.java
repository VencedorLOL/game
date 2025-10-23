package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.animations;

public class TargetProcessor {
	Tile.Circle circle;
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
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, checkWalkable,rayCast);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		circleOverridable(temporal);
	}



	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(fixated.getX(),fixated.getY()) || circle.tileset != stage.tileset || circle.radius != size || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(fixated.getX(), fixated.getY()), stage.tileset, size, true,false);

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
	public Tile findATile(float x, float y){return circle.findATile(x,y);}
	public boolean isInsideCircle(float x, float y){return circle.isInsideOfCircle(x,y);}
	public void changeCheckWalkable(boolean walkable){checkWalkable = walkable;}
	public void changeRayCast(boolean cast){rayCast = cast;}
	public void changeRadius(float radius){size = radius;}
	public void changeAnimation(String animation){targetAnimation = animation;}



















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

}
