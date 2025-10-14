package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;

import static com.mygdx.game.items.Camara.zoom;
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
		if (zoom <= 0)
			zoom = 2;
		camara.camaraStarter(zoom);
		testUi = new GUI();
		testUi.testButton();
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
			testUi.renderGUI();
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
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 7 && zoom >= 4)
				camara.setToOrtho(++zoom);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 4 && zoom >= 1)
				camara.setToOrtho(zoom += .5f);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 4)
				camara.setToOrtho(zoom += .125f);
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > 4)
				camara.setToOrtho(--zoom);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > 1 && zoom <= 4)
				camara.setToOrtho(zoom -= .5f);
			else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > .25f && zoom <= 1)
				camara.setToOrtho(zoom -= .125f);
			if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
				System.out.println(zoom);
		}


		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 7 && zoom >= 4)
			camara.setToOrtho(++zoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 4 && zoom >= 1)
			camara.setToOrtho(zoom += .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && zoom < 4)
			camara.setToOrtho(zoom += .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > 4)
			camara.setToOrtho(--zoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > 1 && zoom <= 4)
			camara.setToOrtho(zoom -= .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && zoom > .25f && zoom <= 1)
			camara.setToOrtho(zoom -= .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			System.out.println(zoom);








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
			camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), zoom);
		}
		else
			isScreenChanging = false;
	}







	@Override
	public void resize(int width, int height) {
		testUi.stage.getViewport().update(width, height, true);
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
