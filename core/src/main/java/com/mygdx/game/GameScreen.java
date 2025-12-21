package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.stages.CraterStage;
import com.mygdx.game.items.stages.StagePathfinding;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.StartScreen.startAsPathfinding;
import static com.mygdx.game.items.AttackIconRenderer.attackRenderer;
import static com.mygdx.game.items.Entity.generalRender;
import static com.mygdx.game.items.GUI.renderGUI;
import static com.mygdx.game.items.InputHandler.escapePressed;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTick;
import static com.mygdx.game.items.TextureManager.dinamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedText;
import static com.mygdx.game.items.TurnManager.*;
import static com.mygdx.game.Settings.camaraZoom;

public class GameScreen implements Screen, Utils {
	public static float delta;
	public static Character chara;
	public static Camara camara = new Camara();
	public static Stage stage = new Stage();
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public ClickDetector clickDetector;
	public boolean fullscreen;
	public int latestNonFullScreenX = 640;
	public int latestNonFullScreenY = 400;
	TextureManager.Text text;
	Entity attacher = new Entity(null,5 * globalSize(), 5 * globalSize(),false);
	InputHandler handler;
	public static boolean initalized = false;

	public void create () {
		camaraZoom = 1;
		camara.zoom = 1;
		camara.camaraStarter(camaraZoom);
		stage = startAsPathfinding ? new StagePathfinding() : new CraterStage();
		print("start as path is " + startAsPathfinding);
		chara = new Character(512, 512, globalSize(), globalSize());
		stage.reseter();
		clickDetector = new ClickDetector();
		InputHandler.defaultKeybinds();
		camara.attach(startAsPathfinding ? attacher : chara);
		text = dinamicFixatedText(Gdx.graphics.getFramesPerSecond()+"",10,10,-1, TextureManager.Fonts.ComicSans,30);
		handler = new InputHandler();
		Gdx.input.setInputProcessor(handler);
		initalized = true;

	}

	public GameScreen(){
		create();
	}

	public static Camara getCamara(){
		return camara;
	}


	public void start(){
		setRender(true);
		//Gdx.graphics.setForegroundFPS(120);
		delta = Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
		if (escapePressed()){
			print("YAY");
		}
//		System.out.println(Gdx.graphics.getFramesPerSecond());
		text.text = Gdx.graphics.getFramesPerSecond()+"";
		turnLogic();
		fullscreenDetector();
		screenSizeChangeDetector();
		TextureManager.batch.begin();
		screenSizeChangeDetector();
		stage.screenWarpTrigger();
		stage.stageRenderer();
		chara.update();
		generalRender();
		attackRenderer();
		renderGUI();
		zoomManagement();
		TextureManager.render(camara);
	}


	public void finish(){
		camara.updater();
		ParticleManager.particleRenderer();
		camara.finalizer(TextureManager.batch);
		TextureManager.batch.end();
		InputHandler.resetter();
	}


	public void fullscreenDetector(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			setRender(false);
			fullscreen = !fullscreen;
			if (fullscreen)
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			else
				Gdx.graphics.setWindowedMode(latestNonFullScreenX, latestNonFullScreenY);

		}
	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			setRender(false);
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
		if (Gdx.input.isKeyPressed(Input.Keys.V))
			camara.setToOrtho(camaraZoom = 1);
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 7 && camaraZoom >= 4)
			getCamara().smoothZoom(++camaraZoom,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4 && camaraZoom >= 1)
			getCamara().smoothZoom(camaraZoom += .5f,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4)
			getCamara().smoothZoom(camaraZoom += .125f,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 4)
			getCamara().smoothZoom(--camaraZoom,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 1 && camaraZoom <= 4)
			getCamara().smoothZoom(camaraZoom -= .5f,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > .25f && camaraZoom <= 1)
			getCamara().smoothZoom(camaraZoom -= .125f,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			fixatedText("Zoom level is of: "+getCamara().zoom, 100,100,100, TextureManager.Fonts.ComicSans, (int) (20 * getCamara().zoom));

	}




	@Override
	public void show() {
		getCamara().setToOrtho(getCamara().zoom = 1);
	}

	@Override
	public void render(float delta) {
		triggerOnTick();
		start();
		finish();
	}

	@Override
	public void resize(int width, int height) {
		getCamara().setToOrtho(getCamara().zoom = 1);
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
