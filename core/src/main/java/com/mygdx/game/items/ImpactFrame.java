package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.items.Camara.attached;

public class ImpactFrame {


	public static void startImpact(float xStart,float yStart, float xEnd, float yEnd){
		startImpact((xStart+xEnd)/2f,(yStart+yEnd)/2f);
	}

	public static void startImpact(float x, float y){
		Entity entity = new Entity(null,x,y);
		Entity previous = attached;
		float prevX = 0,prevY = 0;
		if(previous == null){
			prevX = getCamara().x; prevY = getCamara().y;
		}
		entity.render = false;
		getCamara().smoothAttachment(entity,15);
		getCamara().smoothZoom(0.8f,15);
		float finalPrevX = prevX;
		float finalPrevY = prevY;
		new OnVariousScenarios.CounterObject(30){
			public void onCounterFinish() {
				getCamara().smoothZoom(1,15);
				if(attached != null)
					getCamara().smoothAttachment(previous,15);
				else{
					getCamara().x = finalPrevX; getCamara().y = finalPrevY;
				}

			}
		};
	}


}
