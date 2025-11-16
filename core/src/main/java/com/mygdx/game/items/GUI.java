package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.print;

public class GUI {

	public static ArrayList<GUI> elements = new ArrayList<>();

	public static void renderGUI(){
		for(GUI g : elements)
			g.render();
	}

	public static void delete(GUI trash){
		elements.remove(trash);
	}

	public GUI(){
		elements.add(this);
	}

	public void render(){
		print("rinnun rener");
	}



}
