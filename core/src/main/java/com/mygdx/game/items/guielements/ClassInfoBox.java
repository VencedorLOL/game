package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Utils.stringSplitter;
import static com.mygdx.game.Utils.stringSuperSplitter;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.Text.adequateSize;
import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static java.lang.Math.*;

public class ClassInfoBox extends GUI {
	CAETexts.Classes classs;
	TextureManager.Text title;
	TextureManager.Text[] text;
	ArrayList<String> rawText;
	TextureManager.Text abilities;
	Box titleBox;
	Box textBox;

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

	float propConst;

	public ClassInfoBox(CAETexts.Classes clsCardObj,boolean aumentedVersion){
		classs = clsCardObj;
		//nameExtractor();
		rawText = new ArrayList<>();
		Collections.addAll(rawText, stringSplitter(classs.text,112,' '));
		Collections.addAll(rawText, stringSuperSplitter(classs.abilities,112,' '));
		Collections.reverse(rawText);
		text = new TextureManager.Text[rawText.size()];
		abilities = new TextureManager.Text();
		this.aumentedVersion = aumentedVersion;
		titleBox = new Box(true);
		titleBox.color((byte)109,(byte)109,(byte)109);
		titleBox.colorBg((byte)36,(byte)36,(byte)36);
		textBox = new Box(true);
		textBox.color((byte)109,(byte)109,(byte)109);
		textBox.colorBg((byte)36,(byte)36,(byte)36);

	}



	public void render(float size,float x, float yIni,boolean touch,float height){
		this.size = size;
		this.x = x;
		this.y = yIni;
		propConst = Gdx.graphics.getHeight()/1080f;
		renderTextBox(x,y,size*1.5f,size,height);
		renderTitleBox(x,y,size * 1.5f,size);

		//renderSlider(x,y,);
		

		//onTouchDetect(touch);
	}

	//yIni-size+sYCursor*totalHeightOfTextbox / sRealHeight

	public void renderTitleBox(float x, float y, float width, float height){
		titleBox.render(x,y - height*31,x + width*32 - height/24*32,y - height*19,height/24);
	//	fixatedDrawables.add(new TextureManager.DrawableObject("TextBar", x , y , 1, 0, width, height,true));
		if(title == null){
			title = dynamicFixatedText(classs.name,0,0,-1,32);
		}
		title.realSize = min(adequateSize(title.getText(),size*32*.9f*1.5f),90* propConst);
		title.setColor(255,255,255);
		title.render = true;
		title.onScreenTime = 2;
		title.fakeNull = false;
//		title.x = x + size*2 + (title.realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
		title.x = x + max((width*28f - textSize(title.getText(),title.realSize))/2,width*2f);

		//title.y = y - size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - title.realSize)*.45f*Gdx.graphics.getHeight()/1080;
		title.y = y - 351 * propConst;
	}


	public void renderTextBox(float x, float y, float width, float height,float endY){
		float upY = y - height*19.5f;
		textBox.render(x,endY,x + width*32 - height/24*32,upY,height/24);
		for(int i = 0; i < text.length; i++) {
			if (text[i] == null) {
				text[i] = dynamicFixatedText(rawText.get(i), 0, 0, -1, 32);
			}
			text[i].realSize =  32 * propConst;
			text[i].setColor(255, 255, 255);
			text[i].render = true;
			text[i].onScreenTime = 2;
			text[i].fakeNull = false;
			text[i].x = x + 24 * propConst;
			text[i].y = y - propConst * 264 - text[i].realSize*1.25f*i + text[i].realSize*text.length*1.25f;
		}

	}



	public void renderSlider(float x, float y, float width, float height, float heigtness, float totalXSpace){
		this.size = heigtness /6;
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
