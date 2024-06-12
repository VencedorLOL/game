package com.mygdx.game.items;

public class Border {

	public void border (Character chara, Stage stage){
		if (chara.x <= stage.startX)
			chara.x = stage.startX;
		else if (chara.x >= stage.finalX)
			chara.x = stage.finalX;
		if (chara.y <= stage.startY)
			chara.y = stage.startY;
		else if (chara.y >= stage.finalY)
			chara.y = stage.finalY;
	}
	public void border (Stage stage){
		for (Enemy e : stage.enemy) {
			if (e.x <= stage.startX)
				e.x = stage.startX;
			else if (e.x >= stage.finalX)
				e.x = stage.finalX;
			if (e.y <= stage.startY)
				e.y = stage.startY;
			else if (e.y >= stage.finalY)
				e.y = stage.finalY;
		}
	}
}
