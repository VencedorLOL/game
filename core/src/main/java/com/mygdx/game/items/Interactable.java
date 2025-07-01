package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.TextureManager.text;

public class Interactable extends Entity {


	public static ArrayList<Interactable> interactables = new ArrayList<>();
	static OnVariousScenarios oVE;

	static{
		oVE = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				interactables.clear();
			}
		};
	}

	public Interactable(String texture, float x, float y, float base, float height){
		super(texture,x,y,base,height);
		interactables.add(this);
	}



	public void onInteract(){
		text("This is the test text. \n To add different interactions, \n override onInteract() on your \n " +
				"interactable subclass or anonymous class.", x,y+globalSize(),200, TextureManager.Fonts.ComicSans, 40, 255,255,255,1,150);
	}





}
