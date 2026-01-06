package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.items.Camara;

import static com.mygdx.game.GameScreen.getCamara;

public class MainClass extends Game implements ApplicationListener {
	BitmapFont font;
	GameScreen gameScreen;
	Camara camara = new Camara();
	public static String currentStage;

	@Override
	public void create () {
        System.out.println(System.getProperty("java.version"));
		camara.camaraStarter(getCamara().zoom);
		this.setStartScreen();
		font = new BitmapFont();
		

	}
	public void setStartScreen() {
		StartScreen startScreen = new StartScreen(this);
		setScreen(startScreen);
		currentStage = "Start";
	}
	public void setGameScreen() {
		if(gameScreen == null)
			gameScreen = new GameScreen();
		setScreen(gameScreen);
		currentStage = "Game";
	}
	@SuppressWarnings("all")
	public void setPauseScreen() {
		PauseScreen pauseScreen = new PauseScreen(this);
		setScreen(pauseScreen);
		currentStage = "Pause";
	}
	public void setStageCreatorScreen() {
		StageCreatorScreen sCScreen = new StageCreatorScreen();
		setScreen(sCScreen);
		currentStage = "Creator";
	}

	@Override
	public void render () {
		super.render();
		// ScreenUtils.clear( colorConverter( /* red */ 153), colorConverter(/* green */ 176),colorConverter(/* blue */ 240), 1);

	}


	@Override
	public void dispose () {
	}

	}
