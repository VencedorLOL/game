package com.mygdx.game.items;

import java.util.ArrayList;

public class OnVariousScenarios {

	static ArrayList<OnVariousScenarios> onTurnPass = new ArrayList<>();


	public OnVariousScenarios(){
		onTurnPass.add(this);
	}


	public void onTurnPass(){
		// Anonymously override this method
	}

	public static void triggerOnTurnPass(){
		for (OnVariousScenarios t : onTurnPass)
			t.onTurnPass();
	}


	public void onStageChange(){
		// Well, anonymously override this method
	}

	public static void triggerOnStageChange(){
		for (OnVariousScenarios t : onTurnPass)
			t.onStageChange();
	}



}
