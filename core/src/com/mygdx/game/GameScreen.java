package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.stages.StageOne;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.Turns.*;

public class GameScreen implements Screen, Utils {
	public MainClass mainClass;
	public SpriteBatch batch;
	public Character chara = new Character(512, 512, 128, 128);
	public Camara camara = new Camara();
	public Stage stage = new Stage();
	public int screenSizeX = Gdx.graphics.getWidth();
	public int screenSizeY = Gdx.graphics.getHeight();
	public boolean isScreenChanging = false;
	public void create () {
		batch = new SpriteBatch();
		camara.camaraStarter();
		stage = stage.setStage(new StageOne());
		stage.reStage(chara);
	}

	public GameScreen(MainClass mainClass){
		this.mainClass = mainClass;
		create();
	}


	public void start(){
		ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
		// System.out.println(Gdx.graphics.getFramesPerSecond());
		batch.begin();
		screenSizeChangeDetector();
		if(!isScreenChanging) {
			screenSizeChangeDetector();
			camara.updater(chara);
			chara.checkerGetterAndUpdater(stage);
			stage.characterRefresher(chara.getX(), chara.getY());
			stage.stageRenderer(this, stage);
			if (!betweenStages)
				batch.draw(chara.character, chara.getX(), chara.getY());
			if(betweenStages)
				System.out.println("charaSholdntBeRendering");
			if (Gdx.input.isKeyPressed(Input.Keys.P)) {
				mainClass.setPauseScreen();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.C))
				camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 2);
			if (Gdx.input.isKeyPressed(Input.Keys.R)) {
				stage.reStage(chara);
				reset(stage);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.X))
				System.out.println(chara.x);
			if (Gdx.input.isKeyPressed(Input.Keys.Y))
				System.out.println(chara.y);
			if (Gdx.input.isKeyPressed(Input.Keys.B))
				betweenStages = true;
			if (Gdx.input.isKeyPressed(Input.Keys.O))
				betweenStages = false;
		}
	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			isScreenChanging = true;
			camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 2);
		}
		else
			isScreenChanging = false;
	}

	public void finish(){
		camara.finalizer(batch);
		batch.end();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
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
