package com.mygdx.game;

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
	// Temp comment:
	// 0: never take enemies into consideration
	// 1: take enemies in consideration if path is the same lenght | probably default
	// 2: take enemies in consideration if path is the same lenght or longer by some amount
	// 3: take enemies in consideration always, if there's an avilable path taking enemies in consideration
	// 4: take always enemies in consideration, even if there would be a possible path that now isn't because of the enemies


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

	public static void setVisualSpeedMultiplier(int visualSpeedMultiplier) {
		Settings.visualSpeedMultiplier = visualSpeedMultiplier;
		if(!(128 % Settings.visualSpeedMultiplier == 0)) {
			if(Settings.visualSpeedMultiplier > 128)
				Settings.visualSpeedMultiplier = 128;
			else if(Settings.visualSpeedMultiplier < 4 )
				Settings.visualSpeedMultiplier = 4;
			else if(Settings.visualSpeedMultiplier < 8 )
				Settings.visualSpeedMultiplier = 8;
			else if(Settings.visualSpeedMultiplier < 16 )
				Settings.visualSpeedMultiplier = 16;
			else if(Settings.visualSpeedMultiplier < 32 )
				Settings.visualSpeedMultiplier = 32;
			else if(Settings.visualSpeedMultiplier < 64 )
				Settings.visualSpeedMultiplier = 64;
			else if(Settings.visualSpeedMultiplier < 128)
				Settings.visualSpeedMultiplier = 128;
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



}
