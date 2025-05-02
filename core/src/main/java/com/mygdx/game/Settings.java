package com.mygdx.game;

import com.badlogic.gdx.Gdx;

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
	// 0: never take enemies into consideration
	// 1: take enemies in consideration if path is the same lenght | probably default
	// 2: take enemies in consideration if path is the same lenght or longer by some amount
	// 3: take enemies in consideration always, if there's an avilable path taking enemies in consideration
	// 4: take always enemies in consideration, even if there would be a possible path that now isn't because of the enemies

	static byte decidedPathFlexibility = 3;
	// When should decide a path has been made
	// 0: exclusively on spacebar. default
	// 1: on spacebar or if pressed the key of the direction of the last path if path was completed
	// 2: on spacebar or on any other directional key pressed if path was completed
	// 3: on spacebar or if path was completed
	static boolean fastMode;


	public static boolean getFastMode() {return fastMode;}
	public static void setFastMode(boolean fastMode) {Settings.fastMode = fastMode;}
	public static boolean getPathMode() {return pathPerTurn;}
	public static void setPathMode(boolean pathPerTurn) {Settings.pathPerTurn = pathPerTurn;}
	public static int animationSpeedGetter(){
		return animationSpeed;
	}
	public static int getVisualSpeedMultiplier(){
		return visualSpeedMultiplier;
	}
	public static boolean isDevMode(){
		return DEV_MODE;
	}
	public static int globalSize(){
		return GLOBAL_SIZE;
	}
	public static byte getTakeEnemiesIntoConsideration() {return takeEnemiesIntoConsideration; }
	public static byte getExtraAllowedPath() {return extraAllowedPath;}
	public static boolean isOverridingEscAllowed() {return shouldOverrideEsc;}
	public static byte getDecidedPathFlexibility() {return decidedPathFlexibility;}

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
			return (Gdx.input.isTouched());
		}
		return false;
	}

	public static void onCycleStart(){
		touchedGate = true;
	}


}
