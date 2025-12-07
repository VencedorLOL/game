package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Objects;

public class ParticleManager {
	static ArrayList<ParticleGenerator> particles = new ArrayList<>();

	static public void particleEmitter(String particleName, float x, float y,float scale, int amount, boolean allowCompletion, boolean flipY,int time,Entity entity){
		for (ParticleGenerator p : particles){
			if(p.particleName.equals(particleName)) {
				p.particleEmitter(x,y,scale,amount,allowCompletion,flipY,time,entity);
				return;
			}
		}
		ParticleGenerator particle = new ParticleGenerator(particleName);
		particle.particleEmitter(x,y,scale,amount,allowCompletion,flipY,time,entity);
		particles.add(particle);
	}




	static public void particleRenderer(){
		for (ParticleGenerator p : particles){
			p.rendering();
		}
	}

}
