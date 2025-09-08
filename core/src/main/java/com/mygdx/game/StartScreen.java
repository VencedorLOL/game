package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartScreen implements Screen, Utils {
	MainClass mainClass;
	SpriteBatch menuScreenBatch;
	BitmapFont font;
	public StartScreen(MainClass mainClass) {
		this.mainClass = mainClass;
		menuScreenBatch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(Utils.cC(0), Utils.cC(50), Utils.cC(100), 0);
		mainClass.camara.fixedUpdater();
		menuScreenBatch.begin();
		font.draw(menuScreenBatch, "Java version: " + System.getProperty("java.version"), 100, 150);
		font.draw(menuScreenBatch, "Game Version: vBeta", 100, 125);
		font.draw(menuScreenBatch, "Click to Start", 100, 100);
		font.draw(menuScreenBatch, "Tip: Press F11 to Fullscreen once you start the game.", 100, 175);
		menuScreenBatch.end();
		if (Gdx.input.isTouched()) {
			mainClass.setGameScreen();
			dispose();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) &&
			Gdx.input.isKeyPressed(Input.Keys.F) &&
			Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
			mainClass.setStageCreatorScreen();
			dispose();
		}




	}

	@Override
	public void resize(int width, int height) {

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