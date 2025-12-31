package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.ClassAndEquipmentChanger;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Utils.intravalue;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TextureManager.Text.adequateSize;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;
import static java.lang.Math.min;

public class ItemsList  {
	Character chara;
	ClassAndEquipmentChanger.ClassObject classs;
	String[] weapons;
	String[] shields;
	TextureManager.Text[] texts;

	String texture = "TextBar";
	float size, x, y, x2;
	int hovered = -1;
	boolean canHover = true;

	public ItemsList(ClassAndEquipmentChanger.ClassObject clsCardObj, Character chara){
		classs = clsCardObj;
		this.chara = chara;
		nameExtractor();
		texts = new TextureManager.Text[weapons.length + shields.length];

	}



	public void render(float size,float x, float x2, float yIni,boolean touch){
		this.size = size;
		this.x = x;
		this.y = yIni;
		this.x2 = x2;
		texture = "TextBar";
		for (int i = 0; i < weapons.length; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject(texture, x , yIni + (size*11*i), 1, 0, size, size,true));
			fixatedDrawables.add(new TextureManager.DrawableObject("IconBar", x - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			if(getClIns(classs.name).getWeaponName() != null && getClIns(classs.name).getWeaponName().equals(weapons[i])){
				fixatedDrawables.add(new DrawableObject("WeaponMiniIconEquipped", x - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			} else
				fixatedDrawables.add(new DrawableObject("WeaponMiniIcon", x - size*11 , yIni + (size*11*i), 1, 0, size, size,true));

			if(texts[i] == null){
				texts[i] = dynamicFixatedText(weapons[i],0,0,-1,32);
			}
			texts[i].realSize = min(adequateSize(texts[i].text,size*32*.9f),40*Gdx.graphics.getHeight()/1080f);
			texts[i].setColor(255,255,255);
			texts[i].render = true;
			texts[i].onScreenTime = 2;
			texts[i].fakeNull = false;
			texts[i].x = x + size*2 + (texts[i].realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
			texts[i].y = yIni + (size*11*i)	- size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - texts[i].realSize)*.55f*Gdx.graphics.getHeight()/1080;
			if(texts[i].maxVariation != 0)
				texts[i].maxVariation -= .05f;
			if(texts[i].maxVariation < 0)
				texts[i].maxVariation = 0;
		}
		for (int i = 0; i < shields.length; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject("IconBar", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			fixatedDrawables.add(new TextureManager.DrawableObject(texture, x2 , yIni + (size*11*i), 1, 0, size, size,true));
			if(getClIns(classs.name).getShieldName() != null && getClIns(classs.name).getShieldName().equals(shields[i])){
				fixatedDrawables.add(new DrawableObject("ShieldMiniIconEquipped", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			} else
				fixatedDrawables.add(new DrawableObject("ShieldMiniIcon", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			if(texts[i+ weapons.length] == null){
				texts[i+ weapons.length] = dynamicFixatedText(shields[i],0,0,-1,32);
			}
			texts[i+ weapons.length].realSize =min(adequateSize(texts[i + weapons.length].text,size*32*.9f),40*Gdx.graphics.getHeight()/1080f);
			texts[i+ weapons.length].setColor(255,255,255);
			texts[i+ weapons.length].render = true;
			texts[i+ weapons.length].onScreenTime = 2;
			texts[i+ weapons.length].fakeNull = false;
			texts[i+ weapons.length].x = x2+ size*2 + (texts[i + weapons.length].realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
			texts[i+ weapons.length].y = yIni + (size*11*i) - size*27.75f + (20*Gdx.graphics.getHeight()/1080f - texts[i + weapons.length].realSize)*.55f*Gdx.graphics.getHeight()/1080;
			if(texts[i+ weapons.length].maxVariation != 0)
				texts[i+ weapons.length].maxVariation -= .05f;
			if(texts[i+ weapons.length].maxVariation < 0)
				texts[i+ weapons.length].maxVariation = 0;
		}

		if(hovered > -1 && canHover){
			fixatedDrawables.add(new TextureManager.DrawableObject("TextBarSelected", hovered >= weapons.length ? x2 : x , yIni + (size*11*(hovered >= weapons.length ? hovered - weapons.length : hovered)), 1, 0, size, size,true));
		}
		onTouchDetect(touch);
	}

	public void onTouchDetect(boolean touch){
		if ((touch && leftClickJustPressed()) || (actionConfirmJustPressed() && hovered != -1 && canHover)){
			for(int i = 0; i < weapons.length; i++)
				if((cursorX() >= x - size*11 && cursorX() <= x + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20 && leftClickJustPressed())  || (actionConfirmJustPressed() && hovered == i && canHover))
					onTouch(i);
			for(int i = 0; i < shields.length; i++)
				if((cursorX() >= x2 - size*11 && cursorX() <= x2 + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20 && leftClickJustPressed())  || (actionConfirmJustPressed() && hovered == i + weapons.length && canHover))
					onTouch(i + weapons.length);


		}
	}

	public void onTouch(int i){
		if(i < weapons.length) {
			if(getClIns(classs.name).getWeaponName().equals(texts[i].text))
				texts[i].initiateShake(intravalue(2,1f+texts[i].maxVariation,16),10);
			else
				getClIns(classs.name).setWeapon(classs.getWeapon(i, null));
		}
		else {
			if(getClIns(classs.name).getShieldName().equals(texts[i].text))
				texts[i].initiateShake(intravalue(2,1f+texts[i].maxVariation,16),10);
			else
				getClIns(classs.name).setShield(classs.getShield(i - weapons.length, null));
		}
	}

//texture = selected ? "TextBarSelected" : hovered ? "TextBarHovered" : "TextBar";

	public boolean processUp(){
		if(hovered == -1 || hovered == 0) {
			hovered = -1;
			return true;
		}
		hovered--;
		return false;
	}

	public boolean processDown(){
		if(hovered >= weapons.length + shields.length - 1)
			return true;
		hovered++;
		return false;
	}

	public void processRight(){
		if(hovered <= weapons.length){
			hovered += weapons.length;
			if(hovered >= weapons.length + shields.length)
				hovered = weapons.length + shields.length -1;
		}
	}

	public void processLeft(){
		if(hovered >= weapons.length){
			hovered -= weapons.length;
			if(hovered >= weapons.length)
				hovered = weapons.length - 1;
		}
	}

	public boolean processCursor(){
		for(int i = 0; i < weapons.length; i++)
			if((cursorX() >= x - size*11 && cursorX() <= x + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20 ))
				return true;
		for(int i = 0; i < shields.length; i++)
			if((cursorX() >= x2 - size*11 && cursorX() <= x2 + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20))
				return true;
		return false;
	}

	public void saveCursor(){
		for(int i = 0; i < weapons.length; i++)
			if((cursorX() >= x - size*11 && cursorX() <= x + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20 ))
				hovered = i;
		for(int i = 0; i < shields.length; i++)
			if((cursorX() >= x2 - size*11 && cursorX() <= x2 + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20))
				hovered = i + weapons.length;
	}

	public void processHover(){
		canHover = true;
	}


	public void nameExtractor(){
		weapons = new String[classs.weaponAmount()];
		for(int i = 0; i < weapons.length; i++)
			weapons[i] = classs.getWeaponName(i,chara);
		shields = new String[classs.shieldAmount()];
		for(int i = 0; i < shields.length; i++)
			shields[i] = classs.getShieldName(i,chara);
	}

}
