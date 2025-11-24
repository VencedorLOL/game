package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class Slider extends GUI {


	float size;
	boolean touched;
	float xCursor;

	public Slider(){
		super();
	}

	public void render(float x, float y,float width, float height,float widthness){
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

		onTouchDetect(x+size*8+ xCursor,y-size*8,(height/32-(size))*globalSize(),(height/128-size/8)*globalSize(),x,width);

		TextureManager.DrawableObject grabber = new TextureManager.DrawableObject("selectionIndicator",x+size*8+ xCursor,y-size*8,1,0,(height/32-(size)),(height/128-size/8),true);
		grabber.r = touched ? 1 : .8f; grabber.g = touched ? 1 : .8f; grabber.b = touched ? 1 : .8f;
		fixatedDrawables.add(grabber);
	}

	public void onTouchDetect(float x, float y,float w, float h,float realX, float realWidth){
		if(leftClickPressed() || (wasTouched && !leftClickReleased())) {
			float tX = Gdx.input.getX();
			float tY = Gdx.input.getY();
			if ((tX >= x && tX <= x + w &&
					tY >= y - h && tY <= y) || wasTouched){
				touched = true;
				if (wasTouched)
					if (xCursor + Gdx.input.getX() - cursorLastX >= 0 && xCursor + Gdx.input.getX() - cursorLastX + w <= (size*3/2+realWidth/32-size*2)*32)
						xCursor += (Gdx.input.getX() - cursorLastX);
				cursorLastX = Gdx.input.getX();
				onTouchOverridable();
			}
		}
	}

	boolean wasTouched = false;
	float cursorLastX;
	public void onTouchOverridable(){



	}

}






