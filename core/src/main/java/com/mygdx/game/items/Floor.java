package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;

public class Floor {
// Class will be used for floor textures, animations, etc.


	public String texture;
	public float opacity;
	public String[] secondaryTexture = new String[9];
	public int[] rotationDegrees = new int[9];
	public boolean[] flipX = new boolean[9];
	public boolean[] flipY = new boolean[9];
	public float[] secondaryOpacity = new float[9];
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
		this.secondaryTexture = secondaryTexture;
		this.secondaryOpacity = null;
		ID = IDState + 1;
	}

	public Floor() {
		ID = IDState + 1;
	}

	public void setTexture(String texture){this.texture = texture;}

	public void setOpacity(float opacity){this.opacity = opacity;}


	public void setTexture(String texture,float opacity){this.texture = texture; this.opacity = opacity;}

	public void setSecondaryTexture(String texture, float opacity,int rotationDegrees, boolean flipX, boolean flipY, int numberOfTexture){
		secondaryTexture[numberOfTexture] = texture;
		secondaryOpacity[numberOfTexture] = opacity;
		this.flipX[numberOfTexture] = flipX; this.flipY[numberOfTexture] = flipY;
		this.rotationDegrees[numberOfTexture] = rotationDegrees;

	}

	public void render(float x, float y){
		TextureManager.addToList(texture,x,y,opacity,0);
		for(int i = 0; i < 9; i++) {
			if (secondaryTexture[i] != null) {
				TextureManager.addToPriorityList(secondaryTexture[i], x, y, secondaryOpacity[i], rotationDegrees[i], flipX[i], flipY[i]);
			}
		}
	}

}
