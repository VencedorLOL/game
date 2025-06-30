package com.mygdx.game.items;

public class ScreenWarp {
	protected Stage stage;
	protected int base, height;
	protected int x,y;
	protected String screenWarpTexture = "ScreenWarp";
	public int ID;
	public static int IDState = 0;

	public ScreenWarp(int x, int y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		ID = IDState + 1;
		IDState++;
	}

	public boolean doesCharInteractWithMe(Character chara){
		if (!chara.turnMode)
			return x < chara.x + chara.base && x + base > chara.x && y < chara.y + chara.height && y + height > chara.y;
		return chara.x == x && chara.y == y;
	}




}
