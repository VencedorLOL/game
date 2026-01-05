package com.mygdx.game.items;

import java.util.ArrayList;

public class GUI {

	public static ArrayList<GUI> elements = new ArrayList<>();
	@SuppressWarnings("all")
	public static void renderGUI(){
		for(int i = 0; i < elements.size(); i++)
			elements.get(i).render();
		elements.removeIf(e -> e.queuedForRemoval);
	}

	public static void delete(GUI trash){
		elements.get(elements.indexOf(trash)).queuedForRemoval = true;
		elements.get(elements.indexOf(trash)).onRemoval();
	}

	boolean queuedForRemoval = false;
	public GUI(){
		elements.add(this);
	}

	public void render(){
	}

	public void onRemoval(){}


}
