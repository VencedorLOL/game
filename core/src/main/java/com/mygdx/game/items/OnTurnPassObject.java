package com.mygdx.game.items;

import java.util.ArrayList;

public class OnTurnPassObject {

	static ArrayList<OnTurnPassObject> onTurnPass = new ArrayList<>();


	public OnTurnPassObject(){
		onTurnPass.add(this);
	}


	public void onTurnPass(){
		// Anonymously override this method
	}

}
