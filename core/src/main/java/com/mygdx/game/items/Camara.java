package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.GameScreen.initalized;
import static com.mygdx.game.StartScreen.startAsPathfinding;

@SuppressWarnings("all")
public class Camara {

	public OrthographicCamera camara;
	float x,y,base,height;
	public float zoom = 2f;
	static Entity attached;
	public float finalZoom = -1;
	public void camaraStarter(float zoom){
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara = new OrthographicCamera(0,0);
		camara.setToOrtho(false, base, height);
	}

	public void attach(Entity entity) {
		if(!startAsPathfinding || !initalized){
			attached = entity;
		}
	}

	public void zoomToPoint(float x, float y,float base, float height){
		if(!startAsPathfinding || !initalized){
			if ((x < this.x - this.base / 2 || y < this.y - this.height / 2 || x + base > this.x + this.base / 2 || y + height > this.y + this.height / 2) && !Float.isNaN(this.base) && !Float.isNaN(this.height)) {
				float distanceX = (this.x > x ? this.x - x : this.x < x ? (x + base) - this.x : 0);
				float distanceY = (this.y > y ? this.y - y : this.y < y ? (y + height) - this.y : 0);
				float baseBase = this.base / zoom;
				float baseHeight = this.height / zoom;
				float newBase = distanceX > this.base / 2 ? distanceX * 2 : 0;
				float newHeight = distanceY > this.height / 2 ? distanceY * 2 : 0;
				float zoom1 = newBase / baseBase;
				float zoom2 = newHeight / baseHeight;
				float newZoom = Math.max(Math.max(zoom1, zoom2), zoom);
				smoothZoom(newZoom, 30);
			}
		}
	}

	public void zoomToPoint(float x, float y,float base, float height,float minZoom){
		if (!Float.isNaN(this.base) && !Float.isNaN(this.height)){
			float distanceX  = (this.x > x ? this.x - x : this.x < x ? (x+base) - this.x : 0);
			float distanceY  = (this.y > y ? this.y - y : this.y < y ? (y+height) - this.y : 0);
			float baseBase = this.base / zoom;
			float baseHeight = this.height / zoom;
			float newBase = distanceX>this.base/2 ? distanceX*2 : 0;
			float newHeight = distanceY>this.height/2 ? distanceY*2 : 0;
			float zoom1 = newBase/baseBase;
			float zoom2 = newHeight/baseHeight;
			float newZoom = Math.max(Math.max(zoom1, zoom2), minZoom);
			smoothZoom(newZoom,zoom > minZoom ? 30 : 160);
		}
	}

	public void updater(){
		if (attached != null && time <= 0) {
			x = attached.getX() + attached.getBase() / 2;
			y = attached.getY() + attached.getHeight() / 2;
			xPerTick = 0;
			yPerTick = 0;
		}
		else {
			time--;
			x -= xPerTick;
			y -= yPerTick;
		}
		if(zoomTime > 0){
			zoomTime--;
			zoom -= zoomPerTick;
			base = Gdx.graphics.getWidth() * zoom;
			height = Gdx.graphics.getHeight() * zoom;
			if(finalZoom != -1){
				base = Gdx.graphics.getWidth() * finalZoom;
				height = Gdx.graphics.getHeight() * finalZoom;
				finalZoom = -1;
			}
			camara.setToOrtho(false, base , height);
		}
		camara.position.set(x,y,0);
		camara.update();

	}
	public boolean isCamaraMoving(){
		return time > 0 && (xPerTick != 0 || yPerTick != 0);
	}

	public boolean areCoordsOnTheScreen(float x, float y){
		return (float) Gdx.graphics.getHeight() / 2 + this.y > (y+1) || (float) Gdx.graphics.getHeight() / 2 + this.y < (y-1) ||
			   (float) Gdx.graphics. getWidth() / 2 + this.x > (x+1) || (float) Gdx.graphics. getWidth() / 2 + this.x < (x-1);
	}



	int time;
	float xPerTick;
	float yPerTick;
	public void smoothAttachment(Entity entity,int time){
		if(!startAsPathfinding || !initalized){
			if (attached != entity) {
				attached = entity;
				this.time = time;
				xPerTick = (x - (attached.x + (attached.height / 2))) / time;
				yPerTick = (y - (attached.y + (attached.height / 2))) / time;
			}
		}
	}

	public float zoomTime;
	public float zoomPerTick;
	public void smoothZoom(float zoom, int time){
		zoomTime = time;
		zoomPerTick = (this.zoom - zoom)/time;
	}

	public float[] unproject(float x, float y){
		Vector3 coords = camara.unproject(new Vector3(x,y,0));
		float[] result = new float[2];
		result[0] = coords.x; result[1] = coords.y;
		return result;
	}


	public void updater(float x, float y){
		camara.position.set(x,y,0);
		camara.update();
	}

	public void fixedUpdater(){
		camara.update();
	}
	public float getX(){return x;}
	public float getY(){return y;}
	public float getBase(){return base;}
	public float getHeight() {return height;}

	public void setToOrtho(boolean yDown, float x, float y, float zoom){
		this.zoom = zoom;
		base = x * zoom;
		height = y * zoom;
		camara.setToOrtho(yDown,base,height);
		updater();
	}

	public void setToOrtho(float zoom){
		this.zoom = zoom;
		zoomPerTick = 0;
		zoomTime = 0;
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara.setToOrtho(false, base , height);
		updater();
	}
	public void finalizer(SpriteBatch batch){
		batch.setProjectionMatrix(camara.combined);
	}
}
