package com.mygdx.game.items;

import java.util.ArrayList;

public class OnVariousScenarios {
 	//convenient listener class
	static ArrayList<OnVariousScenarios> onScenarios = new ArrayList<>();
	boolean queuedForDeletion = false;


	public OnVariousScenarios(){
		onScenarios.add(this);
	}



	public void onTurnPass(){
		// Anonymously override this method
	}

	public static void triggerOnTurnPass(){
		for (OnVariousScenarios t : onScenarios)
			if(!t.queuedForDeletion)
				t.onTurnPass();
	}



	public void onStageChange(){
		// Well, anonymously override this method
	}

	public static void triggerOnStageChange(){
		for (OnVariousScenarios onScenario : onScenarios)
			if(!onScenario.queuedForDeletion)
				onScenario.onStageChange();
	}



	public void onTickStart(){
		// yk what to do
	}

	public static void triggerOnTick(){
		for (OnVariousScenarios t : onScenarios)
			if(!t.queuedForDeletion)
				t.onTickStart();
		triggerOnCounter();
	}

	public static void triggerOnCounter(){
		onScenarios.removeIf(t -> t instanceof CounterObject && ((CounterObject) t).didItDie);
		for(OnVariousScenarios t : onScenarios)
			if(t instanceof CounterObject)
				((CounterObject) t).trueOnCounterFinish();
	}

	public void onVolumeChange(){}

	public static void triggerOnVolume() {
		for (OnVariousScenarios o : onScenarios){
			if(!o.queuedForDeletion)
				o.onVolumeChange();
		}
	}


	public void onDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source){}

	public static void triggerOnDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source) {
		for (OnVariousScenarios o : onScenarios)
			if(!o.queuedForDeletion)
				o.onDamagedActor(damagedActor,source);
	}

	public void onActorDeath(Actor deadActor){}

	public static void triggerOnActorDeath(Actor deadActor){
		for(OnVariousScenarios o : onScenarios){
			if(!o.queuedForDeletion)
				o.onActorDeath(deadActor);
		}
	}

	public static void destroyListener(OnVariousScenarios listener){
		for(OnVariousScenarios o : onScenarios){
			if(o == listener)
				o.queuedForDeletion = true;
		}
	}

	public static class CounterObject extends OnVariousScenarios{
		long timer;
		boolean didItDie;

		public CounterObject(long counter){
			super();
			timer = counter;
		}

		private void trueOnCounterFinish(){
			onCounterFinish();
			if (--timer <= 0)
				didItDie = true;
		}

		public void onCounterFinish(){}

	}

}
