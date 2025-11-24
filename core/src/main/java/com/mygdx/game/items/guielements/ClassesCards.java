package com.mygdx.game.items.guielements;

import com.mygdx.game.items.ClassChanger;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class ClassesCards extends GUI {
	float size;
	boolean selected = false;
	boolean hovered = false;
	String texture;
	String secTexture;
	ClassChanger.ClassObject classs;

	public ClassesCards(ClsCardObj clsCardObj){
		super();
		classs = clsCardObj.clsObj;
		secTexture = clsCardObj.texture;
	}

	public void render(float size,float x, float y){
		this.size = size;
		texture = selected ? "CardSelected" : hovered ? "CardHovered" : "CardBlank";
		fixatedDrawables.add(new TextureManager.DrawableObject(texture, x , y, 1, 0, size, size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject(secTexture, x , y, 1, 0, size, size,true));
		onTouchDetect(x,y);
	}

	public void onTouchDetect(float x, float y){
		if ((leftClickJustPressed() && cursorX() >= x && cursorX() <= x + size*32 &&
				cursorY() >= y - size*32 && cursorY() <= y)  || (actionConfirmJustPressed() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}






	public enum ClsCardObj{

		MELEE("MeleeCard",new ClassChanger.Melee()),
		SPEEDSTER("SpeedsterCard",new ClassChanger.Speedster()),
		HEALER("HealerCard", new ClassChanger.Healer()),
		TANK("TankCard",new ClassChanger.Tank()),

		;

		final String texture;
		final ClassChanger.ClassObject clsObj;
		ClsCardObj(String texture, ClassChanger.ClassObject obj){
			this.texture = texture;
			clsObj = obj;
		}

	}




}


