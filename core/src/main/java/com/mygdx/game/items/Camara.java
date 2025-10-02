package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.Settings.print;

public class Camara {

	// ---
	// if i ever need to instantiaze another camara, remove the static coordinates and figure out a way
	// of doing the static things other way
	// ---

	public static OrthographicCamera camara;
	static float x,y,base,height;
	static float zoom = (float) 2;
	static Entity attached;
	public void camaraStarter(float zoom){
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara = new OrthographicCamera(0,0);
		camara.setToOrtho(false, base, height);
	}

	public static void attach(Entity entity){
		attached = entity;
	}

	public void updater(){
		if (attached != null && time <= 0){
			x = attached.getX() + attached.getBase() / 2;
			y = attached.getY() + attached.getHeight() / 2;
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
		if (attached != entity) {
			attached = entity;
			Camara.time = time;
			xPerTick = (x - (attached.x + attached.getBase() / 2)) / time;
			yPerTick = (y - (attached.y + attached.getHeight() / 2)) / time;
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
