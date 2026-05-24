package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.TextureManager.Text.adequateSize;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static java.lang.Math.min;

public class ClassInfoBox extends GUI {
	CAETexts.Classes classs;
	TextureManager.Text text;

	String texture = "TextBar";
	float size, x, y;

	public ClassInfoBox(CAETexts.Classes clsCardObj){
		classs = clsCardObj;
		//nameExtractor();
		text = new TextureManager.Text();

	}



	public void render(float size,float x, float yIni,boolean touch){
		this.size = size;
		this.x = x;
		this.y = yIni;
		texture = "TextBar";
		fixatedDrawables.add(new TextureManager.DrawableObject(texture, x , yIni + (size*11), 1, 0, size, size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("IconBar", x - size*11 , yIni + (size*11), 1, 0, size, size,true));
		if(text == null){
			text = dynamicFixatedText(classs.text,0,0,-1,32);
		}
		text.realSize = min(adequateSize(text.getText(),size*32*.9f),40* Gdx.graphics.getHeight()/1080f);
		text.setColor(255,255,255);
		text.render = true;
		text.onScreenTime = 2;
		text.fakeNull = false;
		text.x = x + size*2 + (text.realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
		text.y = yIni + (size*11)	- size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - text.realSize)*.55f*Gdx.graphics.getHeight()/1080;
		if(text.maxVariation != 0)
			text.maxVariation -= .05f;
		if(text.maxVariation < 0)
			text.maxVariation = 0;



		//onTouchDetect(touch);
	}

	/*public void onTouchDetect(boolean touch){
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
			if(getClIns(classs.name).getWeaponName() != null && getClIns(classs.name).getWeaponName().equals(texts[i].getText()))
				texts[i].initiateShake(intravalue(2,1f+texts[i].maxVariation,16),10);
			else
				getClIns(classs.name).setWeapon(classs.getWeapon(i, null));
		}
		else {
			if(getClIns(classs.name).getShieldName() != null && getClIns(classs.name).getShieldName().equals(texts[i].getText()))
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
	}*/


	/*public void nameExtractor(){
		weapons = new String[classs.weaponAmount()];
		for(int i = 0; i < weapons.length; i++)
			weapons[i] = classs.getWeaponName(i,chara);
		shields = new String[classs.shieldAmount()];
		for(int i = 0; i < shields.length; i++)
			shields[i] = classs.getShieldName(i,chara);
	}

}*/


}
