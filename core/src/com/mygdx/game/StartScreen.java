package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
		ScreenUtils.clear(cC(0), cC(50), cC(100), 0);
		mainClass.camara.fixedUpdater();
		menuScreenBatch.begin();
		font.draw(menuScreenBatch, "Start Screen", 100, 150);
		font.draw(menuScreenBatch, "Click to Start", 100, 100);
		menuScreenBatch.end();
		if (Gdx.input.isTouched()) {
			mainClass.setGameScreen();
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