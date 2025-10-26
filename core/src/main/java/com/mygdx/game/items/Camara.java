package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.GameScreen.initalized;
import static com.mygdx.game.StartScreen.startAsPathfinding;

public class Camara {

	public static OrthographicCamera camara;
	static float x,y,base,height;
	public static float zoom = (float) 2;
	static Entity attached;
	public static float finalZoom = -1;
	public void camaraStarter(float zoom){
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara = new OrthographicCamera(0,0);
		camara.setToOrtho(false, base, height);
	}

	public static void attach(Entity entity) {
		if(!startAsPathfinding || !initalized){
			attached = entity;
		}
	}

	public static void zoomToPoint(float x, float y,float base, float height){
		if(!startAsPathfinding || !initalized){
			if ((x < Camara.x - Camara.base / 2 || y < Camara.y - Camara.height / 2 || x + base > Camara.x + Camara.base / 2 || y + height > Camara.y + Camara.height / 2) && !Float.isNaN(Camara.base) && !Float.isNaN(Camara.height)) {
				float distanceX = (Camara.x > x ? Camara.x - x : Camara.x < x ? (x + base) - Camara.x : 0);
				float distanceY = (Camara.y > y ? Camara.y - y : Camara.y < y ? (y + height) - Camara.y : 0);
				float baseBase = Camara.base / zoom;
				float baseHeight = Camara.height / zoom;
				float newBase = distanceX > Camara.base / 2 ? distanceX * 2 : 0;
				float newHeight = distanceY > Camara.height / 2 ? distanceY * 2 : 0;
				float zoom1 = newBase / baseBase;
				float zoom2 = newHeight / baseHeight;
				float newZoom = Math.max(Math.max(zoom1, zoom2), zoom);
				smoothZoom(newZoom, 30);
			}
		}
	}

	public static void zoomToPoint(float x, float y,float base, float height,float minZoom){
		if (!Float.isNaN(Camara.base) && !Float.isNaN(Camara.height)){
			float distanceX  = (Camara.x > x ? Camara.x - x : Camara.x < x ? (x+base) - Camara.x : 0);
			float distanceY  = (Camara.y > y ? Camara.y - y : Camara.y < y ? (y+height) - Camara.y : 0);
			float baseBase = Camara.base / zoom;
			float baseHeight = Camara.height / zoom;
			float newBase = distanceX>Camara.base/2 ? distanceX*2 : 0;
			float newHeight = distanceY>Camara.height/2 ? distanceY*2 : 0;
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
	public static boolean isCamaraMoving(){
		return time > 0 && (xPerTick != 0 || yPerTick != 0);
	}

	public static boolean areCoordsOnTheScreen(float x, float y){
		return (float) Gdx.graphics.getHeight() / 2 + Camara.y > (y+1) || (float) Gdx.graphics.getHeight() / 2 + Camara.y < (y-1) ||
			   (float) Gdx.graphics. getWidth() / 2 + Camara.x > (x+1) || (float) Gdx.graphics. getWidth() / 2 + Camara.x < (x-1);
	}



	static int time;
	static float xPerTick;
	static float yPerTick;
	public static void smoothAttachment(Entity entity,int time){
		if(!startAsPathfinding || !initalized){
			if (attached != entity) {
				attached = entity;
				Camara.time = time;
				xPerTick = (x - (attached.x + (attached.height / 2))) / time;
				yPerTick = (y - (attached.y + (attached.height / 2))) / time;
			}
		}
	}

	public static float zoomTime;
	public static float zoomPerTick;
	public static void smoothZoom(float zoom, int time){
		zoomTime = time;
		zoomPerTick = (Camara.zoom - zoom)/time;
	}

	public static float[] unproject(float x, float y){
		Vector3 coords = camara.unproject(new Vector3(x,y,0));
		float[] result = new float[2];
		result[0] = coords.x; result[1] = coords.y;
		return result;
	}


	public static void updater(float x, float y){
		camara.position.set(x,y,0);
		camara.update();
	}

	public void fixedUpdater(){
		camara.update();
	}
	public static  float getX(){return x;}
	public static float getY(){return y;}
	public static float getBase(){return base;}
	public static float getHeight() {return height;}

	public void setToOrtho(boolean yDown, float x, float y, float zoom){
		Camara.zoom = zoom;
		base = x * zoom;
		height = y * zoom;
		camara.setToOrtho(yDown,base,height);
		updater();
	}

	public void setToOrtho(float zoom){
		Camara.zoom = zoom;
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
