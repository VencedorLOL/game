package com.mygdx.game.items.guielements;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.ClassAndEquipmentChanger;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;

public class ItemsList  {
	Character chara;
	ClassAndEquipmentChanger.ClassObject classs;
	String[] weapons;
	String[] shields;
	TextureManager.Text[] texts;

	String texture = "TextBar";
	float size, x, y, x2;
	boolean[] selected;
	boolean[] hovered;

	public ItemsList(ClassAndEquipmentChanger.ClassObject clsCardObj, Character chara){
		classs = clsCardObj;
		this.chara = chara;
		nameExtractor();
		texts = new TextureManager.Text[weapons.length + shields.length];
		selected = new boolean[weapons.length + shields.length];
		hovered = new boolean[weapons.length + shields.length];

	}

	public void clearHovered(){
		hovered = new boolean[weapons.length + shields.length];
	}

	public void clearSelected(){
		selected = new boolean[weapons.length + shields.length];
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
				texts[i] = dinamicFixatedText(weapons[i],0,0,-1,Fonts.ComicSans,32);
			}
			texts[i].render = true;
			texts[i].onScreenTime = 2;
			texts[i].fakeNull = false;
			texts[i].x = x + size  +size;
			texts[i].y = yIni + (size*11*i)	- size*27.55f;

		}
		for (int i = 0; i < shields.length; i++){
			fixatedDrawables.add(new TextureManager.DrawableObject(texture, x2 , yIni + (size*11*i), 1, 0, size, size,true));
			fixatedDrawables.add(new TextureManager.DrawableObject("IconBar", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			if(getClIns(classs.name).getShieldName() != null && getClIns(classs.name).getShieldName().equals(shields[i])){
				fixatedDrawables.add(new DrawableObject("ShieldMiniIconEquipped", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			} else
				fixatedDrawables.add(new DrawableObject("ShieldMiniIcon", x2 - size*11 , yIni + (size*11*i), 1, 0, size, size,true));
			if(texts[i+ weapons.length] == null){
				texts[i+ weapons.length] = dinamicFixatedText(shields[i],0,0,-1,Fonts.ComicSans,32);
			}
			texts[i+ weapons.length].render = true;
			texts[i+ weapons.length].onScreenTime = 2;
			texts[i+ weapons.length].fakeNull = false;
			texts[i+ weapons.length].x = x2 + size  +size;
			texts[i+ weapons.length].y = yIni + (size*11*i) - size*27.55f;
		}
		onTouchDetect(touch);
	}

	public void onTouchDetect(boolean touch){
		if (touch && leftClickJustPressed()){
			for(int i = 0; i < weapons.length; i++)
				if((cursorX() >= x - size*11 && cursorX() <= x + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20 )  || (actionConfirmJustPressed() && hovered[i]))
					onTouch(i);
			for(int i = 0; i < shields.length; i++)
				if((cursorX() >= x2 - size*11 && cursorX() <= x2 + size*32 && cursorY() >= y - size*32 + (size*11*i) && cursorY() <= y + (size*11*i) - size*20)  || (actionConfirmJustPressed() && hovered[i + weapons.length]))
					onTouch(i + weapons.length);


		}
	}

	public void onTouch(int i){
		if(i < weapons.length)
			getClIns(classs.name).setWeapon(classs.getWeapon(i,chara));
		else
			getClIns(classs.name).setShield(classs.getShield(i- weapons.length,chara));
	}

//texture = selected ? "TextBarSelected" : hovered ? "TextBarHovered" : "TextBar";




	public void nameExtractor(){
		weapons = new String[classs.weaponAmount()];
		for(int i = 0; i < weapons.length; i++)
			weapons[i] = classs.getWeaponName(i,chara);
		shields = new String[classs.shieldAmount()];
		for(int i = 0; i < shields.length; i++)
			shields[i] = classs.getShieldName(i,chara);
	}

}
