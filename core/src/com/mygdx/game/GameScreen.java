package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.stages.StageOne;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.Turns.*;
import static com.mygdx.game.Settings.camaraZoom;

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
		if (camaraZoom <= 0)
			camaraZoom = 2;
		camara.camaraStarter(camaraZoom);
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
			stage.characterRefresher(chara.getX(), chara.getY());
			stage.stageRenderer(this, stage);
			chara.update(stage, this);
			if (!betweenStages)
				batch.draw(chara.character, chara.getX(), chara.getY());
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
	}

	public void screenSizeChangeDetector(){
		if(screenSizeX != Gdx.graphics.getWidth() || screenSizeY != Gdx.graphics.getHeight()){
			screenSizeX = Gdx.graphics.getWidth();
			screenSizeY = Gdx.graphics.getHeight();
			ScreenUtils.clear(colorConverter( /* red */ 0), colorConverter(/* green */ 0), colorConverter(/* blue */ 0), 1);
			isScreenChanging = true;
			camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraZoom);
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
