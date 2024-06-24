package com.mygdx.game;

public class Settings {
	static int animationSpeed = 1;
	static int visualSpeedMultiplier = 8;
	static float camaraZoom;

	public static int animationSpeedGetter(){
		return animationSpeed;
	}
	public static int visualSpeedMultiplierGetter(){
		return visualSpeedMultiplier;
	}
	public static float camaraZoomGetter(){
		return camaraZoom;
	}
	public static void camaraZoomSetter(float camaraZoom){
		Settings.camaraZoom = camaraZoom;
	}
}
