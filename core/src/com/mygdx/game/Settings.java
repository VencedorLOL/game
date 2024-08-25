package com.mygdx.game;

public class Settings {
	static int animationSpeed = 1;
	static int visualSpeedMultiplier = 8;
	static float camaraZoom;
	static float sCCamaraZoom;
	private final static boolean DEV_MODE = true;
	private final static int GLOBAL_SIZE = 128;


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
}
