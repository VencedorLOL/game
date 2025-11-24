package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class Slider extends GUI {


	float size;
	boolean touched;
	float xCursor;
	float realWidth;
	float barWidth;

	public Slider(){
		super();
	}

	public void render(float x, float y,float width, float height,float widthness,float totalXSpace){
		this.size = widthness/6;
		wasTouched = touched;
		touched = false;
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerA",x,y,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideAB",x,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerB",x,y-size*32-(height/32-size*2)*32,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideBC",x+size*32,y-size*32-(height/32-size*2)*32,1,0,(width/32-size*2),size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("CornerC",x+size*32+(width/32-size*2)*32,y-size*32-(height/32-size*2)*32,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideCD",x+size*32+(width/32-size*2)*32,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerD",x+size*32+(width/32-size*2)*32,y,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideDA",x+size*32,y,1,0,(width/32-size*2),size,true));
		realWidth = (size*3/2+width/32-size*2)*32;
		barWidth = totalXSpace>= Gdx.graphics.getWidth() ?  Gdx.graphics.getWidth()*realWidth/totalXSpace : realWidth;
		onTouchDetect(x+size*8+ xCursor,y-size*8,barWidth,(height/128-size/8)*globalSize());

		TextureManager.DrawableObject grabber = new TextureManager.DrawableObject("selectionIndicator",x+size*8+ xCursor,y-size*8,1,0,barWidth/128,(height/128-size/8),true);
		grabber.r = touched ? 1 : .8f; grabber.g = touched ? 1 : .8f; grabber.b = touched ? 1 : .8f;
		fixatedDrawables.add(grabber);
	}

	public void onTouchDetect(float x, float y,float w, float h){
		if(leftClickPressed() || (wasTouched && !leftClickReleased()))
			if ((cursorX() >= x && cursorX() <= x + w &&
					cursorY() >= y - h && cursorY() <= y) || wasTouched){
				touched = true;
					if (wasTouched && xCursor + cursorX() - cursorLastX >= 0 && xCursor + cursorX() - cursorLastX + w <= realWidth)
						xCursor += (cursorX() - cursorLastX);
				cursorLastX = cursorX();
				onTouchOverridable();
			}

	}

	boolean wasTouched = false;
	float cursorLastX;
	public void onTouchOverridable(){}

}






