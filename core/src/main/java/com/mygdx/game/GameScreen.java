package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.stages.StageOne;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.InputHandler.isEscapePressed;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTick;
import static com.mygdx.game.items.Turns.*;
import static com.mygdx.game.Settings.camaraZoom;

public class GameScreen implements Screen, Utils {
	public static float delta;
	// ---------------
	public GUI testUi;
	// ---------------
	public ParticleManager particle;
	public MainClass mainClass;
	public TextureManager textureManager = new TextureManager();
	public static Character chara;
	public Camara camara = new Camara();
	public static Stage stage = new Stage();
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public ClickDetector clickDetector;
	public boolean fullscreen;
	public int latestNonFullScreenX = 640;
	public int latestNonFullScreenY = 400;


	public void create () {
		if (camaraZoom <= 0)
			camaraZoom = 2;
		camara.camaraStarter(camaraZoom);
		stage = new StageOne();
		chara = new Character(512, 512, globalSize(), globalSize());
		stage.reseter(chara);
		testUi = new GUI();
		testUi.testButton();
		particle = new ParticleManager(textureManager);
		clickDetector = new ClickDetector(camara);
		testUi.textBox();
		InputHandler.defaultKeybinds();
		Camara.attach(chara);
	}

	public GameScreen(MainClass mainClass){
		this.mainClass = mainClass;
		create();
	}

	public void start(){
		delta = Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
		InputHandler.resetter();
		if (isEscapePressed()){
			print("YAY");
		}
		//System.out.println(Gdx.graphics.getFramesPerSecond());
		turnLogic();
		fullscreenDetector();
		screenSizeChangeDetector();
		textureManager.batch.begin();
			clickDetector.camaraUpdater(camara);
			screenSizeChangeDetector();
			stage.stageRenderer(this);
			chara.update(this);
			textureManager.render(camara);
			zoomManagement();


	}


	public void finish(){
		camara.updater();
		particle.particleRenderer();
		camara.finalizer(textureManager.batch);
		textureManager.batch.end();
	}


	public void fullscreenDetector(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			fullscreen = !fullscreen;
			if (fullscreen)
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			else
				Gdx.graphics.setWindowedMode(latestNonFullScreenX, latestNonFullScreenY);

		}
	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraZoom);
			if (!fullscreen){
				latestNonFullScreenX = screenSizeX;
				latestNonFullScreenY = screenSizeY;
			}
		}
	}

	public void zoomManagement(){
		if (Gdx.input.isKeyPressed(Input.Keys.C))
			camara.setToOrtho(camaraZoom = 2);
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 7 && camaraZoom >= 4)
			camara.setToOrtho(++camaraZoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4 && camaraZoom >= 1)
			camara.setToOrtho(camaraZoom += .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4)
			camara.setToOrtho(camaraZoom += .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 4)
			camara.setToOrtho(--camaraZoom);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 1 && camaraZoom <= 4)
			camara.setToOrtho(camaraZoom -= .5f);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > .25f && camaraZoom <= 1)
			camara.setToOrtho(camaraZoom -= .125f);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			System.out.println(camaraZoom);
	}




	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		triggerOnTick();
		start();
		finish();
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
