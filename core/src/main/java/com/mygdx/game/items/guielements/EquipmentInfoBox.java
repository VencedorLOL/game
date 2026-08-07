package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.Utils.*;
import static com.mygdx.game.items.TextureManager.Text.adequateSize;
import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.TextureManager.dynamicPriorityFixatedText;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class EquipmentInfoBox extends GUI {
	CAETexts.EquipmentUnifier equipment;
	int element;
	TextureManager.Text title;
	TextureManager.Text[] text;
	ArrayList<String> rawText;
	Box titleBox;
	Box textBox;
	Box sliderBox;

	float totalYSpace;


	float size, x, y;

	float propConst;

	public static final float FONT_SIZE_CONSTANT = 24;

	public EquipmentInfoBox(CAETexts.EquipmentUnifier clsCardObj,int element){
		equipment = clsCardObj;
		this.element = element;
		//nameExtractor();
		rawText = new ArrayList<>();
		Collections.addAll(rawText, stringSplitter(equipment.getElement()[element].getText(), (int) (110 * 32 / FONT_SIZE_CONSTANT), ' '));
		text = new TextureManager.Text[rawText.size()];
		titleBox = new Box(1);
		titleBox.color((byte)109,(byte)109,(byte)109);
		titleBox.colorBg((byte)36,(byte)36,(byte)36);
		textBox = new Box(1);
		textBox.color((byte)109,(byte)109,(byte)109);
		textBox.colorBg((byte)36,(byte)36,(byte)36);
		sliderBox = new Box(2);
		sliderBox.colorBg();
		sliderBox.colorBg();
		sliderBox.aBg = 0f;
	}



	public void render(float size,float x, float yIni,float height){
		this.size = size;
		this.x = x;
		this.y = yIni;
		propConst = Gdx.graphics.getHeight()/1080f;
		totalYSpace = ySpace();
		renderTitleBox(x,y,size * 1.5f,size);
		renderTextBox(x,y,size*1.5f,size,height);
		//onTouchDetect(touch);
	}
	public float ySpace(){
		return FONT_SIZE_CONSTANT*propConst*text.length*1.25f;
	}

	//yIni-size+sYCursor*totalHeightOfTextbox / sRealHeight

	public void renderTitleBox(float x, float y, float width, float height){
		titleBox.gigaRender(x,y - height*31,x + width*32 - height/24*32,y - height*19,height/24);
	//	fixatedDrawables.add(new TextureManager.DrawableObject("TextBar", x , y , 1, 0, width, height,true));
		if(title == null){
			title = dynamicPriorityFixatedText(equipment.getElement()[element].getName(),0,0,-1,32);
		}
		title.realSize = min(adequateSize(title.getText(),size*32*.9f*1.5f),90* height/12);
		title.setColor(255,255,255);
		title.render = true;
		title.onScreenTime = 2;
		title.fakeNull = false;
//		title.x = x + size*2 + (title.realSize - 20*Gdx.graphics.getHeight()/1080f)*.2f*Gdx.graphics.getHeight()/1080;
		title.x = x + max((width*28f - textSize(title.getText(),title.realSize))/2,width*2f);

		//title.y = y - size*27.75f  + (20*Gdx.graphics.getHeight()/1080f - title.realSize)*.45f*Gdx.graphics.getHeight()/1080;
		title.y = y - 351 * height/12;
	}


	public void renderTextBox(float x, float y, float width, float height,float endY){
		float upY = y - height*19.5f;
		textBox.gigaRender(x,endY,x + width*32 - height/24*32,upY,height/24);
		for(int i = 0; i < text.length; i++) {
			if (text[i] == null) {
				text[i] = dynamicPriorityFixatedText(rawText.get(i), 0, 0, -1, 32);
			}

			text[i].realSize =  FONT_SIZE_CONSTANT * height / 12;
			text[i].setColor(255, 255, 255);
			text[i].onScreenTime = 2;
			text[i].fakeNull = false;
			text[i].x = x + 2 * height;
			text[i].y = y - height / 12 * 200 + text[i].realSize*1.25f*i - text[i].realSize;
			text[i].render = true;
		}

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
			if(getClIns(equipment.name).getWeaponName() != null && getClIns(equipment.name).getWeaponName().equals(texts[i].getText()))
				texts[i].initiateShake(intravalue(2,1f+texts[i].maxVariation,16),10);
			else
				getClIns(equipment.name).setWeapon(equipment.getWeapon(i, null));
		}
		else {
			if(getClIns(equipment.name).getShieldName() != null && getClIns(equipment.name).getShieldName().equals(texts[i].getText()))
				texts[i].initiateShake(intravalue(2,1f+texts[i].maxVariation,16),10);
			else
				getClIns(equipment.name).setShield(equipment.getShield(i - weapons.length, null));
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
		weapons = new String[equipment.weaponAmount()];
		for(int i = 0; i < weapons.length; i++)
			weapons[i] = equipment.getWeaponName(i,chara);
		shields = new String[equipment.shieldAmount()];
		for(int i = 0; i < shields.length; i++)
			shields[i] = equipment.getShieldName(i,chara);
	}

}*/


}
