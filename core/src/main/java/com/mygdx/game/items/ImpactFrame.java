package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.items.Camara.attached;

public class ImpactFrame {


	public static void startImpact(float xStart,float yStart, float xEnd, float yEnd, int time){
		startImpact((xStart+xEnd)/2f,(yStart+yEnd)/2f, time);
	}

	public static void startImpact(float x, float y, int time){
		Entity entity = new Entity(null,x,y);
		Entity previous = attached;
		float prevX = 0,prevY = 0;
		if(previous == null){
			prevX = getCamara().x; prevY = getCamara().y;
		}
		entity.render = false;
		getCamara().smoothAttachment(entity,time);
		getCamara().smoothZoom(0.8f,time);
		float finalPrevX = prevX;
		float finalPrevY = prevY;
		new OnVariousScenarios.CounterObject(time* 2l){
			public void onCounterFinish() {
				getCamara().smoothZoom(1,time);
				if(attached != null)
					getCamara().smoothAttachment(previous,time);
				else{
					getCamara().x = finalPrevX; getCamara().y = finalPrevY;
				}

			}
		};
	}


}
