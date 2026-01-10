package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.turnMode;

public class ScreenWarp {
	protected Stage stage;
	protected int base, height;
	public float x;
	public float y;
	protected String screenWarpTexture = "ScreenWarp";
	public int ID;
	public static int IDState = 0;

	//only for stage creation
	public int destination;

	public ScreenWarp(int x, int y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		ID = IDState + 1;
		IDState++;
	}

	public ScreenWarp(int x, int y, int destination) {
		this.x = x;
		this.y = y;
		this.base = globalSize();
		this.height = globalSize();
		this.destination = destination;
		ID = IDState + 1;
		IDState++;
	}

	public boolean doesCharInteractWithMe(Character chara){
		if (!turnMode)
			return x < chara.x + chara.base && x + base > chara.x && y < chara.y + chara.height && y + height > chara.y;
		return chara.x == x && chara.y == y;
	}




}
