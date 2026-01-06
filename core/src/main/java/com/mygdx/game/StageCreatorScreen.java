package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.stagecreatorelements.InputDimensions;
import com.mygdx.game.items.stagecreatorelements.StageCreator;
import com.mygdx.game.items.stagecreatorelements.StageSaver;


import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Utils.colorConverter;
import static com.mygdx.game.items.AttackIconRenderer.attackRenderer;
import static com.mygdx.game.items.Entity.generalRender;
import static com.mygdx.game.items.GUI.renderGUI;
import static com.mygdx.game.items.OnVariousScenarios.triggerOnTick;
import static com.mygdx.game.items.TextureManager.fixatedText;

@SuppressWarnings("all")
public class StageCreatorScreen implements Screen{
	public GUI testUi;
	public ParticleManager particle;
	public MainClass mainClass;
	public static Camara camera = new Camara();
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public boolean isScreenChanging = false;
	public static StageCreator sc;
	InputDimensions text;
	InputDimensions.InformationTransferer info;
	InputHandler handler;
	StageSaver saver;

	StageCreatorScreen() {
		camera.camaraStarter(2);
		particle = new ParticleManager();
		sc = new StageCreator();
		text = new InputDimensions();
		info = text.getInfo();
		handler = new InputHandler();
		Gdx.input.setInputProcessor(handler);
		chara = new Character(0, 0, globalSize(), globalSize());
		camera.attach(chara);
		InputHandler.defaultKeybinds();
		turnMode = false;
	}


	@Override
	public void render(float delta) {
			triggerOnTick();
			start();
			finish();
		}

	public void testinterface() {
		if(info.ready){
			sc.update(info.x,info.y);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			text = new InputDimensions();
			info = text.getInfo();
			sc.hasStageBeenCreated = false;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
			saver = new StageSaver(sc.stage);
		}

	}


	public void start(){
		setRender(true);
		ScreenUtils.clear(colorConverter( /* red */ 20), colorConverter(/* green */ 120), colorConverter(/* blue */ 40), 1);
		fullscreenDetector();
		screenSizeChangeDetector();
		TextureManager.batch.begin();
		testinterface();
		chara.update();
		if(sc.stage != null)
			sc.stage.borderUpdate();
		if(saver != null)
			if(saver.waiter())
				saver = null;
		generalRender();
		attackRenderer();
		renderGUI();
		zoomManagement();
		TextureManager.render();
	}

	public void finish(){
		camera.updater();
		ParticleManager.particleRenderer();
		camera.finalizer(TextureManager.batch);
		TextureManager.batch.end();
		InputHandler.resetter();
	}

	public void zoomManagement(){
		if (Gdx.input.isKeyPressed(Input.Keys.V))
			camera.setToOrtho(camaraZoom = 1);
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 7 && camaraZoom >= 4)
			camera.smoothZoom(++camaraZoom,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4 && camaraZoom >= 1)
			camera.smoothZoom(camaraZoom += .5f,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && camaraZoom < 4)
			camera.smoothZoom(camaraZoom += .125f,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 4)
			camera.smoothZoom(--camaraZoom,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > 1 && camaraZoom <= 4)
			camera.smoothZoom(camaraZoom -= .5f,40);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && camaraZoom > .25f && camaraZoom <= 1)
			camera.smoothZoom(camaraZoom -= .125f,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			fixatedText("Zoom level is of: "+getCamara().zoom, 100,100,100, (int) (20 * getCamara().zoom),255,255,255);

	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			isScreenChanging = true;
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getCamara().zoom);
		}
		else
			isScreenChanging = false;
	}


	public int latestNonFullScreenX = 640;

	public int latestNonFullScreenY = 400;
	public boolean fullscreen;
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

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	public void show() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
