package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GUI {
	Stage stage;
	Skin skin;


	public GUI(){
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());

	}


	public void testButton(){
		final TextButton button = new TextButton("Test omg", skin, "default");
		button.setWidth((float) Gdx.graphics.getWidth() /10);
		button.setHeight((float) Gdx.graphics.getHeight() /20);
		button.align(Align.center|Align.top);
		final Dialog dialog = new Dialog("Test message" , skin);;

		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				dialog.show(stage);
			}
		});
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}







	public void testButtonRunner(){
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void renderGUI(){

	}



}
