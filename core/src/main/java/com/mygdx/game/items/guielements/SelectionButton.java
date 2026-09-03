package com.mygdx.game.items.guielements;

import com.mygdx.game.items.GUI;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.InputHandler.cursorY;
import static com.mygdx.game.items.TextureManager.*;

public class SelectionButton extends GUI {
	float size,x,y;
	boolean selected = false;
	boolean hovered = false;
	String texture = "SelectionBox";
	String secTexture = null;

	public SelectionButton(){}

	public void render(float size,float x, float y,boolean touch){
		this.size = size*32;
		this.x = x;
		this.y = y;
		fixatedDrawables.add(getDrawable(texture, x , y,0, 1, 0,false,false, size, size,true));
		fixatedDrawables.add(getDrawable(secTexture, x , y,0, 1, 0,false,false, size, size,true));
		if(selected)
			fixatedDrawables.add(getDrawable("SelectedSelection", x , y,0, 0.7f, 0,false,false, size, size,true));
		else if(hovered)
			fixatedDrawables.add(getDrawable("HoveringSelection", x , y,0, 0.7f, 0,false,false, size, size,true));
		onTouchDetect(x,y,touch);
	}

	public void onTouchDetect(float x, float y, boolean touch){
		if (((leftClickJustPressed() && cursorX() >= x && cursorX() <= x + size &&
				cursorY() >= y - size && cursorY() <= y)  || (actionConfirmReleased() && hovered)) && touch)
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

}
