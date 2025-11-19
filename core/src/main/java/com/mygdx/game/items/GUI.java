package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static com.mygdx.game.Settings.print;

public class GUI {

	public static ArrayList<GUI> elements = new ArrayList<>();

	public static void renderGUI(){
		for(GUI g : elements)
			g.render();
		elements.removeIf(e -> e.queuedForRemoval);
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
			print("size: " + elements.size());
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
