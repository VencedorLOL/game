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

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.*;

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
		close =  new CloseButton(endSX,endSY){
			public void onTouchOverridable() {
				sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
				sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
				Vector3 realCoords = (new Vector3(Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f,Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f, 0));
				realCoords = getCamara().camara.unproject(realCoords);
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

	}

	public void render(){
		if(renderr) {
			sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
			sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
			fixatedDrawables.add(new DrawableObject(texture, Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 0.5f, 0, sizeX, sizeY));
			close.render(endSX,endSY);
			if(delete)
				delete(this);
		}
	}

//*	public void drawer(){
//		b.begin();
//		sprite = new Sprite(atlas.findRegion("anima"));
//		b.draw(sprite,200,200,0,0,128,128,1,1,0,false);
//
//		b.end();
//	}


}
