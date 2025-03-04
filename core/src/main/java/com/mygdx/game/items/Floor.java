package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;

public class Floor {
// Class will be used for floor textures, animations, etc.


	public String texture;
	public float opacity;
	public String secondaryTexture;
	public float secondaryOpacity;
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

	public Floor(String texture,float opacity, String secondaryTexture, float secondaryOpacity) {
		this.texture = texture;
		this.opacity = opacity;
		this.secondaryTexture = secondaryTexture;
		this.secondaryOpacity = secondaryOpacity;
		ID = IDState + 1;
	}

	public Floor() {
		ID = IDState + 1;
	}

	public void setTexture(String texture){this.texture = texture;}

	public void setSecondaryTexture(String texture){secondaryTexture = texture;}

	public void setOpacity(float opacity){this.opacity = opacity;}

	public void setSecondaryOpacity(float opacity){this.secondaryOpacity = opacity;}

	public void setTexture(String texture,float opacity){this.texture = texture; this.opacity = opacity;}

	public void setSecondaryTexture(String texture, float opacity){secondaryTexture = texture; secondaryOpacity = opacity;}

	public void render(float x, float y){
		TextureManager.addToList(texture,x,y,opacity);
		if (secondaryTexture != null){
			TextureManager.addToList(secondaryTexture,x,y,secondaryOpacity);
		}
	}

}
