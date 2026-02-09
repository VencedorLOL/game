package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.text;

public class Interactable {

	public static ArrayList<Interactable> interactables = new ArrayList<>();
	static OnVariousScenarios oVE;
	float x,y,base,height;
	Entity attached;
	static{
		oVE = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				interactables.clear();
			}
		};
	}

	public Interactable(float x, float y, float base, float height){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		interactables.add(this);
	}

	public Interactable (Entity entity){
		this.attached = entity;
		interactables.add(this);
	}

	public void removeInteractable(){
		destroyListener(oVE);
		interactables.remove(this);
	}

	public boolean detectInteract(float x, float y, float base, float height){
		if(attached == null)
			return this.x < x + base && this.x + this.base > x && this.y < y + height && this.y + this.height > y;
		else
			return attached.x < x + base && attached.x + attached.base > x && attached.y < y + height && attached.y + attached.height > y;
	}

	public boolean detectInteract(Entity entity) {
		if(attached == null)
			return x < entity.x + entity.base && x + base > entity.x && y < entity.y + entity.height && y + height > entity.y;
		else
			return attached.x < entity.x + entity.base && attached.x + attached.base > entity.x && attached.y < entity.y + entity.height && attached.y + attached.height > entity.y;
	}


	public void onInteract(Character character){
		text("This is the test text. \n To add different interactions, \n override onInteract() on your \n " +
				"interactable subclass or anonymous class.", x,y+globalSize(),200, 40, 255,255,255,1,150);
	}





}
