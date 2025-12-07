package com.mygdx.game.items.guielements;

import com.mygdx.game.items.ClassChanger;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class ClassesCards extends GUI {
	float size,x,y;
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

	public void render(float size,float x, float y,boolean touch){
		this.size = size;
		this.x = x;
		this.y = y;
		texture = selected ? "CardSelected" : hovered ? "CardHovered" : "CardBlank";
		fixatedDrawables.add(new TextureManager.DrawableObject("CardShadow", x , y, .7f, 0, size, size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject(texture, x , y, 1, 0, size, size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject(secTexture, x , y, 1, 0, size, size,true));
		onTouchDetect(x,y,touch);
	}

	public void onTouchDetect(float x, float y, boolean touch){
		if ((touch && leftClickJustPressed() && cursorX() >= x && cursorX() <= x + size*32 &&
				cursorY() >= y - size*32 && cursorY() <= y)  || (actionConfirmJustPressed() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}






	public enum ClsCardObj{

		MELEE("MeleeCard",new ClassChanger.Melee()),
		SPEEDSTER("SpeedsterCard",new ClassChanger.Speedster()),
		HEALER("HealerCard", new ClassChanger.Healer()),
		TANK("TankCard",new ClassChanger.Tank()),
		SUMMONER("SummonerCard",new ClassChanger.Summoner()),

		IMP("ImpCard",new ClassChanger.Imp()),
		CATAPULT("CatapultCard",new ClassChanger.Catapult()),
		MAGE("MageCard", new ClassChanger.Mage()),
		SWORD_MAGE("SwordMageCard",new ClassChanger.SwordMage()),
		STELLAR_EXPLOSION("StellarExplosionCard",new ClassChanger.StellarExplosion()),

		EARTHQUAKER("EarthquakerCard",new ClassChanger.Earthquaker())
		;

		final String texture;
		final ClassChanger.ClassObject clsObj;
		ClsCardObj(String texture, ClassChanger.ClassObject obj){
			this.texture = texture;
			clsObj = obj;
		}

	}




}


