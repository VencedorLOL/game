package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.items.OnVariousScenarios;


import static com.mygdx.game.items.OnVariousScenarios.triggerOnVolume;
import static com.mygdx.game.items.TextureManager.fixatedText;

@SuppressWarnings("all")
public class Settings {
	static int animationSpeed = 1;
	static int visualSpeedMultiplier = 8;
	static float camaraZoom;
	private final static boolean DEV_MODE = true;
	private final static int GLOBAL_SIZE = 128;
	static boolean print = true;
	static boolean pathPerTurn = true;
	static byte takeEnemiesIntoConsideration = 0;
	static byte extraAllowedPath = 2;
	static long errorId = 1;
	static boolean touchedGate;
	static boolean shouldOverrideEsc;
	static boolean render;
	static Lwjgl3ApplicationConfiguration config;
	private static boolean allowCommands = true;
	private final static boolean COMPLETE_SAVEFILE = false;
	private final static boolean CHEBYSHEV_OVER_OCTILE = false;
	private final static boolean TURN_SPEED_Y_CAPPED = true;

	// 0: never take enemies into consideration
	// 1: take enemies in consideration if path is the same lenght | probably default
	// 2: take enemies in consideration if path is the same lenght or longer by some amount
	// 3: take enemies in consideration, if there's an avilable path taking enemies in consideration
	// 4: take always enemies in consideration, even if there would be a possible path that now isn't because of the enemies

	// current implementation:
	// 0: stage path
	// 1: actor path
	// TODO: change to desired implementation

	static byte decidedPathFlexibility = 2;
	// When should decide a path has been made
	// 0: exclusively on spacebar. default
	// 1: on spacebar or if pressed the key of the direction of the last path if path was completed
	// 2: on spacebar or on any other directional key pressed if path was completed
	// 3: on spacebar or if path was completed
	static boolean fastMode;
	static float volume = 100;
	static boolean mute = false;
	public static boolean turnMode = false;

	static final boolean releaseVersion = true;

	static float desiredGUISize;
	static float minimumGUISize;

	private static final OnVariousScenarios oVS = new OnVariousScenarios(){
		@Override
		public void onTickStart() {
			onCycleStart();
		}
	};

	private static boolean trapsTickOnActorTurn = true;

	public static final boolean turnSpeedCapY(){return TURN_SPEED_Y_CAPPED;}
	public static final boolean punishDiagonal(){return !CHEBYSHEV_OVER_OCTILE;}
	public static final boolean completeSavefile(){return COMPLETE_SAVEFILE && isDevMode();}
	public static final boolean allowCommands(){return allowCommands;}
	public static final void setConfig(Lwjgl3ApplicationConfiguration config){Settings.config = config;}
	public static final Lwjgl3ApplicationConfiguration getConfig(){return config;}
	public static final boolean getTrapsTick(){return trapsTickOnActorTurn;}
	public static final void setRender(boolean renderr){render = renderr;}
	public static final boolean getRender(){return render;}
	public static final float getDesiredGUISize(){return desiredGUISize;}
	public static final float getMinimumGUISize(){return minimumGUISize;}
	public static final void setDesiredGUISize(float size){desiredGUISize = size;}
	public static final void setMinimumGUISize(float size){minimumGUISize = size;}
	public static final boolean getReleaseVersion() {return releaseVersion;}
	public static final boolean getFastMode() {return fastMode;}
	public static final void setFastMode(boolean fastMode) {Settings.fastMode = fastMode;}
	public static final boolean getPathMode() {return pathPerTurn;}
	public static final void setPathMode(boolean pathPerTurn) {Settings.pathPerTurn = pathPerTurn;}
	public static final int animationSpeedGetter(){
		return animationSpeed;
	}
	public static final int getVisualSpeedMultiplier(){return visualSpeedMultiplier;}
	public static final boolean isDevMode(){
		return DEV_MODE;
	}
	public static final int globalSize() {return GLOBAL_SIZE; }
	public static final byte getTakeEnemiesIntoConsideration() {return takeEnemiesIntoConsideration; }
	public static final void setTakeEnemiesIntoConsideration(byte takeEnemiesIntoConsideration) {Settings.takeEnemiesIntoConsideration = takeEnemiesIntoConsideration;}
	public static final byte getExtraAllowedPath() {return extraAllowedPath;}
	public static final boolean isOverridingEscAllowed() {return shouldOverrideEsc;}
	public static final byte getDecidedPathFlexibility() {return decidedPathFlexibility;}
	public static final float getVolume() {return mute ? 0 : volume;}
	public static final float getRealVolume() {return volume;}
	public static final void setVolume(float volume) {Settings.volume = volume;
		fixatedText(Settings.volume+"",40,50,100, 40);
		triggerOnVolume();}
	public static final void setMute(boolean mute) {Settings.mute = mute;
		fixatedText("mute is now " + mute,40,54,100, 40);
		triggerOnVolume();}
	public static final boolean getMute() {return mute;}

	public static final long startErrorId(){
		if (errorId % 2 == 0) {
			throw new IllegalErrorState(errorId);
		}
		return ++errorId;
	}
	public static final long continueErrorId(){
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		return errorId;
	}
	public static final long endErrorId() {
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		return errorId++;
	}

	public static final long startAndEndErrorId(){
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		errorId += 2;
		return --errorId;
	}

	public static final void setVisualSpeedMultiplier(int visualSpeedMultiplier) {
		Settings.visualSpeedMultiplier = visualSpeedMultiplier;
		if(!(GLOBAL_SIZE % Settings.visualSpeedMultiplier == 0)) {
			if(Settings.visualSpeedMultiplier > GLOBAL_SIZE)
				Settings.visualSpeedMultiplier = GLOBAL_SIZE;
			else if(Settings.visualSpeedMultiplier < GLOBAL_SIZE / 32 )
				Settings.visualSpeedMultiplier = GLOBAL_SIZE / 32;
			else if(Settings.visualSpeedMultiplier < GLOBAL_SIZE / 16 )
				Settings.visualSpeedMultiplier = GLOBAL_SIZE / 16;
			else if(Settings.visualSpeedMultiplier < GLOBAL_SIZE / 8 )
				Settings.visualSpeedMultiplier = GLOBAL_SIZE / 8;
			else if(Settings.visualSpeedMultiplier <  GLOBAL_SIZE / 4 )
				Settings.visualSpeedMultiplier = GLOBAL_SIZE / 4;
			else if(Settings.visualSpeedMultiplier < GLOBAL_SIZE / 2)
				Settings.visualSpeedMultiplier = GLOBAL_SIZE / 2;
			else if(Settings.visualSpeedMultiplier < GLOBAL_SIZE)
				Settings.visualSpeedMultiplier = GLOBAL_SIZE;
		}
	}

	public static final void print(String text){
		if (print)
			System.out.println(text);
	}

	public static final void printErr(String text){
		if (print)
			System.err.println(text);
	}

	private static final class IllegalErrorState extends Error {
		private IllegalErrorState(long faultyErrorState){
			super(" CLASS: [Settings] :: The state of the low-relevant errors was caught in an impossible state. Said state is: " + faultyErrorState);
		}
	}

	public static final boolean touchDetect(){
		if (touchedGate) {
			touchedGate = false;
			return (Gdx.input.justTouched());
		}
		return false;
	}

	private static final void onCycleStart(){
		touchedGate = true;
	}


}
