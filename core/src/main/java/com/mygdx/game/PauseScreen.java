package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseScreen implements Screen, Utils {
	MainClass mainClass;
	SpriteBatch menuScreenBatch;
	BitmapFont font;
	public PauseScreen(MainClass mainClass) {
		this.mainClass = mainClass;
		menuScreenBatch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(cC(120), cC(160), cC(175), 0);
		mainClass.camara.fixedUpdater();
		menuScreenBatch.begin();
		font.draw(menuScreenBatch, "Test Game Pause Screen", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		font.draw(menuScreenBatch, "Click to unpause", Gdx.graphics.getWidth()  / 2f, Gdx.graphics.getHeight() * 2f / 3f);
		menuScreenBatch.end();
		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
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
