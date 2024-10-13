package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.items.Camara;

import static com.mygdx.game.Settings.camaraZoom;

public class MainClass extends Game implements Utils, ApplicationListener {
	BitmapFont font;
	GameScreen gameScreen;
	Camara camara = new Camara();

	@Override
	public void create () {
        System.out.println(System.getProperty("java.version"));
		gameScreen = new GameScreen(this);
		camara.camaraStarter(camaraZoom);
		this.setGameScreen();
		this.setStartScreen();
		font = new BitmapFont();
		

	}
	public void setStartScreen() {
		StartScreen startScreen = new StartScreen(this);
		setScreen(startScreen);
	}
	public void setGameScreen() {
		setScreen(gameScreen);
	}
	public void setPauseScreen() {
		PauseScreen pauseScreen = new PauseScreen(this);
		setScreen(pauseScreen);
	}
	public void setStageCreatorScreen() {
		StageCreatorScreen sCScreen = new StageCreatorScreen(this);
		setScreen(sCScreen);
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
