package com.mygdx.game.items;


import com.badlogic.gdx.graphics.Texture;

public class ScreenWarp {
	protected Stage stage;
	protected int base, height;
	protected int x,y;
	protected Texture screenWarpTexture = new Texture("ScreenWarp.png");
	public int screenWarpID;
	public static int IDState = 0;

	public ScreenWarp(int x, int y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		screenWarpID = IDState + 1;
		IDState++;
	}
}
