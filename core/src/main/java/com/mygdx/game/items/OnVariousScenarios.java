package com.mygdx.game.items;

import java.util.ArrayList;

public class OnVariousScenarios {

	static ArrayList<OnVariousScenarios> onScenarios = new ArrayList<>();


	public OnVariousScenarios(){
		onScenarios.add(this);
	}



	public void onTurnPass(){
		// Anonymously override this method
	}

	public static void triggerOnTurnPass(){
		for (OnVariousScenarios t : onScenarios)
			t.onTurnPass();
	}



	public void onStageChange(){
		// Well, anonymously override this method
	}

	public static void triggerOnStageChange(){
		for (OnVariousScenarios t : onScenarios)
			t.onStageChange();
	}



	public void onTickStart(){
		// yk what to do
	}

	public static void triggerOnTick(){
		for (OnVariousScenarios t : onScenarios)
			t.onTickStart();
	}
}
