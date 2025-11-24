package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.InputHandler;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.*;
import static java.lang.Math.min;

public class Background extends GUI {

	boolean renderr = false;
	float sizeX, sizeY;
	float spaceX, spaceY, endSX, endSY;
	String texture = "GUIBackground";
/*	SpriteBatch b = new SpriteBatch();
*	TextureAtlas atlas;
	Sprite sprite;*/

	CloseButton close;
	boolean delete = false;

	float selGapX, selGapWallX, selGapY;
	SelectionButton[] selButtons;
	byte selectedOne = -1;
	byte elementHovered = -2;
	byte persevereHover = -1;

	Slider slider;
	float sliGapX, sliGapY, sliThickness, sliWidth, sliHeight;

	public Background(){
		super();
		// change all of this math so there can be like a 10% of space of screen not covered by the gui on each axis
		sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
		sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
		Vector3 realCoords = (new Vector3(Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f,Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f, 0));
		realCoords = getCamara().camara.unproject(realCoords);
		Animation animation = new Animation("guibackgroundopening",realCoords.x,realCoords.y){
			public void onFinish() {
				renderr = true;
			}
		};
		animation.scaleY = sizeY;
		animation.scaleX = sizeX;
		animation.opacity = 0.5f;
		animations.add(animation);
		spaceX = 8*sizeX;
		spaceY = Gdx.graphics.getHeight() - 8*sizeY;
		endSX = Gdx.graphics.getWidth() - 8*sizeX;
		endSY = 8*sizeY;
		close =  new CloseButton(){
			public void onTouchOverridable() {
					sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
				sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
				Animation animation = new Animation("guibackgroundclosing",chara);
				animation.scaleY = sizeY;
				animation.scaleX = sizeX;
				animation.opacity = 0.5f;
				animations.add(animation);
				renderr = false;
				delete(close);
				delete = true;
			}
		};

		//*render
		//atlas = new TextureAtlas(Gdx.files.internal("Atlas/AtlasOne.atlas"));
		selGapX = (endSX-spaceX)/(classSlots.length + 2);
		selGapWallX = (endSX-spaceX) - selGapX * (classSlots.length-1) - classSlots.length * min(endSX,endSY);
		selButtons = new SelectionButton[classSlots.length];
		for(int i = 0; i < selButtons.length; i++) {
			byte finalI = (byte) i;
			selButtons[i] = new SelectionButton(){
				public void onTouchOverridable() {
					deselect();
					selected = true;
					hovered = false;
					if(elementHovered == finalI)
						elementHovered = -2;
					selectedOne = finalI;
				}
			};
			selButtons[i].secTexture = classSlots[i].texture;
		}
		sliWidth = (sizeX-spaceX*2)*globalSize()*.7f;
		sliGapX = ((sizeX-spaceX*2)*globalSize()  - sliWidth)/2f + spaceX;
		sliHeight = (sizeY-endSY*2)*globalSize()*.2f;
		sliGapY = ((sizeY-endSY*2)*globalSize() * .2f) + endSY;
		sliThickness = min(sizeX,sizeY);
		slider = new Slider();
	}

	private void deselect(){
		for (SelectionButton s : selButtons)
			if (s != null)
				s.selected = false;
	}

	public void render(){
		if(renderr) {
			calculateMath();
			fixatedDrawables.add(new DrawableObject(texture, Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 0.5f, 0, sizeX, sizeY));
//			fixatedDrawables.add(new DrawableObject("GUIBackgroundBorder", Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 1f, 0, sizeX, sizeY));
			hoverCheck();
			close.render(endSY/globalSize(),endSX,endSY);
			for(int i = 0; i < selButtons.length; i++) {
				selButtons[i].secTexture = classSlots[i].texture;
				selButtons[i].render(sizeY*.25f,selGapWallX + i*(selGapX + sizeY*.25f*32),selGapY);
			}
			slider.render(sliGapX,sliGapY,sliWidth,sliHeight,sliThickness);

			if(delete)
				delete(this);
		}
	}

	private void dehover(){
		close.hovered = false;
		for(SelectionButton s : selButtons){
			s.hovered = false;
		}
	}

	private void hoverCheck(){
		float tX = Gdx.input.getX();
		float tY = Gdx.input.getY();
		if(elementHovered != -2){
			if(upJustPressed()) {
				elementHovered = elementHovered > -1 && elementHovered < classSlots.length ? -1 : elementHovered;
				persevereHover = 1;
			}
			if(downJustPressed()) {
				elementHovered = elementHovered == -1 ? 0 : elementHovered;
				persevereHover = 1;
			}
			if(leftJustPressed()) {
				elementHovered = elementHovered > 0 && elementHovered < classSlots.length ? --elementHovered : elementHovered;
				persevereHover = 1;
			}
			if(rightJustPressed()) {
				elementHovered = elementHovered > -1 && elementHovered < classSlots.length - 1 ? ++elementHovered : elementHovered;
				persevereHover = 1;
			}
		}
		if(persevereHover != 1) {
			dehover();
		}
		if(tX >= endSX && tX <= endSX + endSY &&
				tY >= 0 && tY <= endSY) {
			elementHovered = -1;
			persevereHover = -1;
		}
		for(int i = 0; i < selButtons.length; i++)
			if(tX >= selGapWallX + i*(selGapX + sizeY*.25f*32) && tX <= selGapWallX + i*(selGapX + sizeY*.25f*32) + sizeY*8 &&
					tY >= selGapY - sizeY*8 && tY <= selGapY) {
				elementHovered = (byte) i;
				persevereHover = -1;
			}
		if(persevereHover != 0) {
			if (elementHovered == -1) {
				dehover();
				close.hovered = true;
				persevereHover = persevereHover == 1 ? persevereHover : 0;
			} else if (elementHovered != -2) {
				dehover();
				for (int i = 0; i < selButtons.length; i++)
					if (i == elementHovered) {
						selButtons[i].hovered = true;
						persevereHover = persevereHover == 1 ? persevereHover : 0;
					}
			}
		}

	}

	private void calculateMath(){
		sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
		sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
		spaceX = 8*sizeX;
		spaceY = Gdx.graphics.getHeight() - 8*sizeY;
		endSX = Gdx.graphics.getWidth() - 8*sizeX;
		endSY = 8*sizeY;
		selGapX = (endSX-spaceX)/(classSlots.length + 4);
		selGapWallX = ((endSX-spaceX) - (selGapX * (classSlots.length-1) + classSlots.length * sizeY*.25f * globalSize()*.25f))/2f + spaceX;
		selGapY = endSY + Gdx.graphics.getHeight()*.12f;

		sliWidth = (sizeX*globalSize()-spaceX*2)*.7f;
		sliGapX = ((sizeX*globalSize()-spaceX*2)  - sliWidth)/2f + spaceX;
		sliHeight = (sizeY*globalSize()-endSY*2)*.1f;
		sliGapY = ((sizeY*globalSize()-endSY*2) * .5f) + endSY;
		sliThickness = min(sizeX,sizeY);
	}




//*	public void drawer(){
//		b.begin();
//		sprite = new Sprite(atlas.findRegion("anima"));
//		b.draw(sprite,200,200,0,0,128,128,1,1,0,false);
//
//		b.end();
//	}


}
