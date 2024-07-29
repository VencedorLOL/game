package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

public class StageCreatorScreen implements Screen, Utils {
	MainClass mainClass;
	BitmapFont font;
	Camara camara;
	GUI gui;
	TextureManager tm;

	public StageCreatorScreen(MainClass mainClass) {
		this.mainClass = mainClass;
		font = new BitmapFont();
		gui = new GUI();
		tm = new TextureManager();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(cC(120), cC(160), cC(175), 0);
		tm.batch.begin();
		if (Gdx.input.isKeyJustPressed(Input.Keys.T))
			gui.tesTable();
		if (Gdx.input.isKeyJustPressed(Input.Keys.B))
			gui.testButton();
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
			gui.testUI();
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
			gui.anotherTestUI();
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
			gui.testImageIGDunno();
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5))
			gui.text();
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6))
			gui.lastTetCode();
		gui.renderGUI();


		tm.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		gui.stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
