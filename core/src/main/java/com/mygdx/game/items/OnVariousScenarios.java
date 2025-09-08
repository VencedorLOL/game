package com.mygdx.game.items;

import java.util.ArrayList;

public class OnVariousScenarios {
 	//convenient listener class
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
		for (OnVariousScenarios onScenario : onScenarios)
			onScenario.onStageChange();
	}



	public void onTickStart(){
		// yk what to do
	}

	public static void triggerOnTick(){
		for (OnVariousScenarios t : onScenarios)
			t.onTickStart();
	}

	public void onVolumeChange(){}

	public static void triggerOnVolume() {
		for (OnVariousScenarios o : onScenarios){
			o.onVolumeChange();
		}
	}


	public void onDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source){}

	public static void triggerOnDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source) {
		for (OnVariousScenarios o : onScenarios)
			o.onDamagedActor(damagedActor,source);
	}


	public static void destroyListener(OnVariousScenarios listener){
		onScenarios.removeIf(o -> o == listener);
	}



}
