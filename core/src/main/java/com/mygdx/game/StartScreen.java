package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Utils.colorConverter;

public class StartScreen implements Screen {
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public boolean fullscreen;
	public int latestNonFullScreenX = 640;
	public int latestNonFullScreenY = 400;
	MainClass mainClass;
	SpriteBatch menuScreenBatch;
	BitmapFont font;
	String[] tips = new String[]{
			"Press F11 to toggle fullscreen (once you start the game)",
			"Press F1, F2, or F3 to swap between different characters!",
			"Press R to reset your path.",
			"You can always walk your path backwards to undo the last step!",
			"Press 4 to equip a melee weapon (only for F2 character) \nand 5 to equip a very powerful sword (only for F3 character).",
			"The key \"I\" gives lots of debug information of the characer.",
			"Press T to switch between attack and movement mode!",
			"Press L to switch between free and grid movement!",
			"Flurry of Attacks lets you hit four times in a turn! \nCancel attack mode if you wish to change your attack distribution.",
			"Dummies will let you test your damage!",
			"Use B or L if you ever get stuck midturn... \nand report the developer how did you get into that situation...",
			"Press K before entering attack mode to toggle \non or off your selecting area reaching into tiles you cannot hit (due to walls)",
			"Press G to deal universal damage to all enemies on command \n(this is a debug command)",
			"Press the left or right arrow to decrease or increase \nvolume respectively, or M to toggle mute altogether.",
			"You can move the crosshair with WASD keys! \nYou can also click on a location in the grid to create a path to that location!",
			"Combat in this game is turn-based, you can only give orders during your turn.\nThe character moves when the turn reaches their acting speed bracket,\nthe higher the acting speed the earlier you move!"
	};
	int tipSelected;
	public static boolean startAsPathfinding;

	public StartScreen(MainClass mainClass) {
		this.mainClass = mainClass;
		menuScreenBatch = new SpriteBatch();
		font = new BitmapFont();
		tipSelected = random(0,tips.length-1);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(Utils.cC(0), Utils.cC(50), Utils.cC(100), 0);
		mainClass.camara.fixedUpdater();
		screenSizeChangeDetector();
		menuScreenBatch.begin();
		font.draw(menuScreenBatch, "Java version: " + System.getProperty("java.version"), 100, 175);
		font.draw(menuScreenBatch, "Game Version: vB.3", 100, 150);
		font.draw(menuScreenBatch, "Click to Start", 100, 125);
		font.draw(menuScreenBatch, "Tip: " + tips[tipSelected], 100, 100);
		menuScreenBatch.end();
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			startAsPathfinding = true;
			print("set path as true");
			mainClass.setGameScreen();
			dispose();
		}
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


	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			mainClass.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getCamara().zoom);
			if (!fullscreen){
				latestNonFullScreenX = screenSizeX;
				latestNonFullScreenY = screenSizeY;
			}
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