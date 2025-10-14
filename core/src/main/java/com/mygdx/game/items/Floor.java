package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class Floor {
// Class will be used for floor textures, animations, etc.


	public String texture;
	public float opacity;
	public TextureManager.DrawableObject[] secondTexture = new TextureManager.DrawableObject[13];
	public boolean corner = false;
	public int ID;
	public static int IDState = 0;

	public Floor(String texture) {
		this.texture = texture;
		opacity = 1;
		ID = IDState + 1;
	}

	public Floor(String texture,float opacity) {
		this.texture = texture;
		this.opacity = opacity;
		ID = IDState + 1;
	}

	public Floor(String texture,float opacity, String[] secondaryTexture, float secondaryOpacity) {
		this.texture = texture;
		this.opacity = opacity;

		ID = IDState + 1;
	}

	public Floor() {
		ID = IDState + 1;
	}

	public void setTexture(String texture){this.texture = texture;}

	public void setOpacity(float opacity){this.opacity = opacity;}


	public void setTexture(String texture,float opacity){this.texture = texture; this.opacity = opacity;}

	public void setSecondaryTexture(String texture, float opacity,int rotationDegrees, boolean flipX, boolean flipY, int numberOfTexture){
		secondTexture[numberOfTexture] = new TextureManager.DrawableObject(texture,0,0,opacity,rotationDegrees,flipX,flipY);
	}

	public void render(float x, float y){
		TextureManager.addToList(texture,x,y,opacity,0);
	}

	public void secondaryReset(){
		for(int i = 0; i < 13; i++) {
			secondTexture[i] = null;
		}
	}

	public void renderCircle(float x, float y){
		for(int i = 0; i < 13; i++) {
			if (secondTexture[i] != null) {
				secondTexture[i].x = x; secondTexture[i].y = y;
				TextureManager.priorityDrawables.add(secondTexture[i]);
			}
		}
	}

}
