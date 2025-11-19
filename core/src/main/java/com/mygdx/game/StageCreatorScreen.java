package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.items.Stage.betweenStages;

public class StageCreatorScreen implements Screen, Utils {
	public GUI testUi;
	public ParticleManager particle;
	public MainClass mainClass;
	public TextureManager textureManager = new TextureManager();
	public Character chara = new Character(0, 0, 128, 128);
	public Camara camara = new Camara();
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public boolean isScreenChanging = false;
	StageCreator sc;

	StageCreatorScreen() {
		if (getCamara().zoom <= 0)
			getCamara().zoom = 2;
		camara.camaraStarter(getCamara().zoom);
		particle = new ParticleManager(textureManager);
		sc = new StageCreator(testUi,textureManager,camara,chara);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(colorConverter( /* red */ 128), colorConverter(/* green */ 180), colorConverter(/* blue */ 128), 1);
		// System.out.println(Gdx.graphics.getFramesPerSecond());
		textureManager.batch.begin();
		screenSizeChangeDetector();
		if(!isScreenChanging) {
			screenSizeChangeDetector();
			camara.updater();
			sc.update();
			if (!betweenStages)
				textureManager.addToList(chara.getTexture(), chara.getX(), chara.getY());
			textureManager.render(camara);
			// Hotkeys
			if (Gdx.input.isKeyPressed(Input.Keys.C))
				camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 2);
			if (Gdx.input.isKeyPressed(Input.Keys.R)) {
				sc.stage.reseter();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.X))
				System.out.println(chara.getX());
			if (Gdx.input.isKeyPressed(Input.Keys.Y))
				System.out.println(chara.getY());
			// Zoom management
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&getCamara(). zoom < 7 && getCamara().zoom >= 4)
				camara.setToOrtho(++getCamara().zoom);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && getCamara().zoom < 4 && getCamara().zoom >= 1)
				camara.setToOrtho(getCamara().zoom += .5f);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && getCamara().zoom < 4)
				camara.setToOrtho(getCamara().zoom += .125f);
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > 4)
				camara.setToOrtho(--getCamara().zoom);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > 1 && getCamara().zoom <= 4)
				camara.setToOrtho(getCamara().zoom -= .5f);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > .25f && getCamara().zoom <= 1)
				camara.setToOrtho(getCamara().zoom -= .125f);
			if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
				System.out.println(getCamara().zoom);
		}


		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&getCamara(). zoom < 7 && getCamara().zoom >= 4)
			camara.setToOrtho(++getCamara().zoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && getCamara().zoom < 4 && getCamara().zoom >= 1)
			camara.setToOrtho(getCamara().zoom += .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && getCamara().zoom < 4)
			camara.setToOrtho(getCamara().zoom += .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > 4)
			camara.setToOrtho(--getCamara().zoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > 1 && getCamara().zoom <= 4)
			camara.setToOrtho(getCamara().zoom -= .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getCamara().zoom > .25f && getCamara().zoom <= 1)
			camara.setToOrtho(getCamara().zoom -= .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			System.out.println(getCamara().zoom);








/*
		if (attackModeJustPressed())
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

 */

		particle.particleRenderer();
		camara.finalizer(textureManager.batch);
		textureManager.batch.end();

	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			isScreenChanging = true;
			camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getCamara().zoom);
		}
		else
			isScreenChanging = false;
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
