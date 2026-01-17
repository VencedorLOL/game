package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;

public class Border {

	float maxX, maxY, minX, minY;

	@SuppressWarnings("all")
	public void updateCoords(Stage stage){
		maxX = -1/0f;
		minX = 1/0f;
		maxY = -1/0f;
		minY = 1/0f;
		for (Tile t : stage.tileset){
			if(t.x > maxX)
				maxX = t.x;
			if(t.x < minX)
				minX = t.x;
			if(t.y > maxY)
				maxY = t.y;
			if(t.y < minY)
				minY = t.y;
		}
		if(maxX == -1/0f || maxX == 1/0f || Float.isNaN(maxX))
			maxX = globalSize();
		if(maxY == -1/0f || maxY == 1/0f || Float.isNaN(maxY))
			maxY = globalSize();
		if(minX == -1/0f || minX == 1/0f || Float.isNaN(minX))
			minX = 0;
		if(minY == -1/0f || minY == 1/0f || Float.isNaN(minY))
			minY = 0;
	}

	public void border (Character chara){
		if (chara.x <= minX && !chara.noClip)
			chara.x = minX;
		else if (chara.x >= maxX && !chara.noClip)
			chara.x = maxX;
		if (chara.y <= minY && !chara.noClip)
			chara.y = minY;
		else if (chara.y >= maxY && !chara.noClip)
			chara.y = maxY;
	}
	public void border (Stage stage){
		for (Enemy e : stage.enemy) {
			if (e.x <= minX)
				e.x = minX;
			else if (e.x >= maxX)
				e.x = maxX;
			if (e.y <= minY)
				e.y = minY;
			else if (e.y >= maxY)
				e.y = maxY;
		}
	}
}
