package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
	public static boolean turnMode = true;

	static final boolean releaseVersion = true;

	static float desiredGUISize;
	static float minimumGUISize;

	private static final OnVariousScenarios oVS = new OnVariousScenarios(){
		@Override
		public void onTickStart() {
			onCycleStart();
		}
	};

	public static void setRender(boolean renderr){render = renderr;}
	public static boolean getRender(){return render;}
	public static float getDesiredGUISize(){return desiredGUISize;}
	public static float getMinimumGUISize(){return minimumGUISize;}
	public static void setDesiredGUISize(float size){desiredGUISize = size;}
	public static void setMinimumGUISize(float size){minimumGUISize = size;}
	public static boolean getReleaseVersion() {return releaseVersion;}
	public static boolean getFastMode() {return fastMode;}
	public static void setFastMode(boolean fastMode) {Settings.fastMode = fastMode;}
	public static boolean getPathMode() {return pathPerTurn;}
	public static void setPathMode(boolean pathPerTurn) {Settings.pathPerTurn = pathPerTurn;}
	public static int animationSpeedGetter(){
		return animationSpeed;
	}
	public static int getVisualSpeedMultiplier(){return visualSpeedMultiplier;}
	public static boolean isDevMode(){
		return DEV_MODE;
	}
	public static int globalSize() {return GLOBAL_SIZE; }
	public static byte getTakeEnemiesIntoConsideration() {return takeEnemiesIntoConsideration; }
	public static void setTakeEnemiesIntoConsideration(byte takeEnemiesIntoConsideration) {Settings.takeEnemiesIntoConsideration = takeEnemiesIntoConsideration;}
	public static byte getExtraAllowedPath() {return extraAllowedPath;}
	public static boolean isOverridingEscAllowed() {return shouldOverrideEsc;}
	public static byte getDecidedPathFlexibility() {return decidedPathFlexibility;}
	public static float getVolume() {return mute ? 0 : volume;}
	public static float getRealVolume() {return volume;}
	public static void setVolume(float volume) {Settings.volume = volume;
		fixatedText(Settings.volume+"",40,50,100, 40);
		triggerOnVolume();}
	public static void setMute(boolean mute) {Settings.mute = mute;
		fixatedText("mute is now " + mute,40,54,100, 40);
		triggerOnVolume();}
	public static boolean getMute() {return mute;}

	public static long startErrorId(){
		if (errorId % 2 == 0) {
			throw new IllegalErrorState(errorId);
		}
		return ++errorId;
	}
	public static long continueErrorId(){
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		return errorId;
	}
	public static long endErrorId() {
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		return errorId++;
	}

	public static long startAndEndErrorId(){
		if (errorId % 2 != 0) {
			throw new IllegalErrorState(errorId);
		}
		errorId += 2;
		return --errorId;
	}

	public static void setVisualSpeedMultiplier(int visualSpeedMultiplier) {
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

	public static void print(String text){
		if (print)
			System.out.println(text);
	}

	public static void printErr(String text){
		if (print)
			System.err.println(text);
	}

	private static class IllegalErrorState extends Error {
		private IllegalErrorState(long faultyErrorState){
			super(" CLASS: [Settings] :: The state of the low-relevant errors was caught in an impossible state. Said state is: " + faultyErrorState);
		}
	}

	public static boolean touchDetect(){
		if (touchedGate) {
			touchedGate = false;
			return (Gdx.input.justTouched());
		}
		return false;
	}

	private static void onCycleStart(){
		touchedGate = true;
	}


}
