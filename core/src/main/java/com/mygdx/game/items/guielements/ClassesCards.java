package com.mygdx.game.items.guielements;

import com.mygdx.game.items.ClassAndEquipmentChanger;
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
	ClassAndEquipmentChanger.ClassObject classs;

	public ClassesCards(ClsCardObj clsCardObj){
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
				cursorY() >= y - size*32 && cursorY() <= y)  || (actionConfirmReleased() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}






	public enum ClsCardObj{
		MELEE("MeleeCard",new ClassAndEquipmentChanger.Melee()),
		SPEEDSTER("SpeedsterCard",new ClassAndEquipmentChanger.Speedster()),
		HEALER("HealerCard", new ClassAndEquipmentChanger.Healer()),
		TANK("TankCard",new ClassAndEquipmentChanger.Tank()),
		MAGE("MageCard", new ClassAndEquipmentChanger.Mage()),
		SWORD_MAGE("SwordMageCard",new ClassAndEquipmentChanger.SwordMage()),
		SUMMONER("SummonerCard",new ClassAndEquipmentChanger.Summoner()),
		IMP("ImpCard",new ClassAndEquipmentChanger.Imp()),
		CATAPULT("CatapultCard",new ClassAndEquipmentChanger.Catapult()),
		STELLAR_EXPLOSION("StellarExplosionCard",new ClassAndEquipmentChanger.StellarExplosion()),
		EARTHQUAKER("EarthquakerCard",new ClassAndEquipmentChanger.Earthquaker()),
		;

		public final String texture;
		public final ClassAndEquipmentChanger.ClassObject clsObj;
		ClsCardObj(String texture, ClassAndEquipmentChanger.ClassObject obj){
			this.texture = texture;
			clsObj = obj;
		}

	}




}


