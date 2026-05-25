package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.Text.adequateSize;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static java.lang.Math.min;

public class ClassInfoBox extends GUI {
	CAETexts.Classes classs;
	TextureManager.Text title;
	TextureManager.Text text;
	TextureManager.Text abilities;

	float sSize;
	boolean sTouched;
	boolean sSelected;
	float sYCursor;
	float sRealHeight;
	float sBarHeight;
	boolean sWasTouched = false;
	float sCursorLastY;

	//TODO: this
	// and..... well, the rest of the class too.
	float totalHeightOfTextbox;

	float size, x, y;

	boolean aumentedVersion;

	public ClassInfoBox(CAETexts.Classes clsCardObj){
		classs = clsCardObj;
		//nameExtractor();
		title = new TextureManager.Text();
		text = new TextureManager.Text();
		abilities = new TextureManager.Text();

	}



	public void render(float size,float x, float yIni,boolean touch){
		this.size = size;
		this.x = x;
		this.y = yIni;
		renderTitleBox(x,y,size,size);
		renderTextBox(x,yIni-size+sYCursor*totalHeightOfTextbox / sRealHeight,size,totalHeightOfTextbox);
		//renderSlider(x,y,);


		//onTouchDetect(touch);
	}

	public void renderTitleBox(float x, float y, float width, float height){
		fixatedDrawables.add(new TextureManager.DrawableObject("TextBar", x , y , 1, 0, width, height,true));
		if(title == null){
			title = dynamicFixatedText(classs.name,0,0,-1,32);
		}
		title.realSize = min(adequateSize(title.getText(),size*32*.9f),40* Gdx.graphics.getHeight()/1080f);
		title.setColor(255,255,255);
		title.render = true;
		title.onScreenTime = 2;
		title.fakeNull = false;
		title.x = x + size*2 + (title.realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
		title.y = y + (size*11)	- size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - title.realSize)*.55f*Gdx.graphics.getHeight()/1080;
	}


	public void renderTextBox(float x, float y, float width, float height){
		fixatedDrawables.add(new TextureManager.DrawableObject("default",x,y,1,0,(width/32-size*2),(height/32-size*2),true));

		fixatedDrawables.add(new TextureManager.DrawableObject("CornerA",x,y,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideAB",x,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerB",x,y-size*32-(height/32-size*2)*32,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideBC",x+size*32,y-size*32-(height/32-size*2)*32,1,0,(width/32-size*2),size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("CornerC",x+size*32+(width/32-size*2)*32,y-size*32-(height/32-size*2)*32,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideCD",x+size*32+(width/32-size*2)*32,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerD",x+size*32+(width/32-size*2)*32,y,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideDA",x+size*32,y,1,0,(width/32-size*2),size,true));

		if(text == null){
			text = dynamicFixatedText(classs.name,0,0,-1,32);
		}
		text.realSize = min(adequateSize(text.getText(),size*32*.9f),40* Gdx.graphics.getHeight()/1080f);
		text.setColor(255,255,255);
		text.render = true;
		text.onScreenTime = 2;
		text.fakeNull = false;
		text.x = x + size*2 + (text.realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
		text.y = y + (size*11)	- size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - text.realSize)*.55f*Gdx.graphics.getHeight()/1080;

	}


	public void renderSlider(float x, float y,float width, float height,float widthness,float totalXSpace){
		this.size = widthness/6;
		sWasTouched = sTouched;
		sTouched = false;
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerA",x,y,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideAB",x,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerB",x,y-size*32-(height/32-size*2)*32,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideBC",x+size*32,y-size*32-(height/32-size*2)*32,1,0,(width/32-size*2),size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("CornerC",x+size*32+(width/32-size*2)*32,y-size*32-(height/32-size*2)*32,1,0,size,size,true));
		fixatedDrawables.add(new TextureManager.DrawableObject("SideCD",x+size*32+(width/32-size*2)*32,y-size*32,1,0,size,(height/32-size*2),true));
		fixatedDrawables.add(new TextureManager.DrawableObject("CornerD",x+size*32+(width/32-size*2)*32,y,1,0,size,size,true));

		fixatedDrawables.add(new TextureManager.DrawableObject("SideDA",x+size*32,y,1,0,(width/32-size*2),size,true));
		sRealHeight = (size*3/2+height/32-size*2)*32;
		sBarHeight = totalXSpace>= Gdx.graphics.getWidth() ?  Gdx.graphics.getWidth()*sRealHeight/totalXSpace : sRealHeight;
		onTouchDetect(x+size*8,y-size*8-sYCursor,(height/globalSize()-size/(globalSize()/128f*8))*globalSize(),sBarHeight);

		TextureManager.DrawableObject grabber = new TextureManager.DrawableObject("selectionIndicator",x+size*8,y-size*8-sYCursor,1,0,(height/globalSize()-size/(globalSize()/128f*8)),sBarHeight/globalSize(),true);
		grabber.r = sTouched || sSelected ? 1 : .8f; grabber.g = sTouched || sSelected ? 1 : .8f; grabber.b = sTouched || sSelected ? 1 : .8f;
		fixatedDrawables.add(grabber);
	}





	public void onTouchDetect(float x, float y,float w, float h){
		if(leftClickPressed() || (sWasTouched && !leftClickReleased()))
			if ((cursorX() >= x && cursorX() <= x + w &&
					cursorY() >= y - h && cursorY() <= y) || sWasTouched){
				sTouched = true;
				sSelected = true;
				if (sWasTouched && sYCursor + cursorY() - sCursorLastY >= 0 && sYCursor + cursorY() - sCursorLastY + h <= sRealHeight)
					sYCursor += (cursorY() - sCursorLastY);
				sCursorLastY = cursorY();
				onTouchOverridable();
			}
		if(sSelected){
			if(upPressed()){
				if(sYCursor - 2*Gdx.graphics.getHeight()/640f >= 0)
					sYCursor -= 2*Gdx.graphics.getHeight()/640f;
				else
					sYCursor = 0;
			}
			if(downPressed()){
				if(sYCursor + 2*Gdx.graphics.getHeight()/640f + h <= sRealHeight)
					sYCursor += 2*Gdx.graphics.getHeight()/640f;
				else
					sYCursor = sRealHeight - h;
			}
		}
	}

	public void onTouchOverridable(){}

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
