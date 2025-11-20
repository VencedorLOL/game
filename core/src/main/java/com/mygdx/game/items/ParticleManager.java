package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Objects;

public class ParticleManager {
	ArrayList<ParticleGenerator> particles = new ArrayList<>();

	public void particleEmitter(String particleName, float x, float y){
		particleEmitter(particleName,x,y,1,1,true,false);
	}

	public void particleEmitter(String particleName, float x, float y,boolean allowCompletion){
		particleEmitter(particleName,x,y,1,1,allowCompletion,false);
	}

	public void particleEmitter(String particleName, float x, float y,float scale){
		particleEmitter(particleName,x,y,scale,1,true,false);
	}

	public void particleEmitter(String particleName, float x, float y,float scale,boolean flipY){
		particleEmitter(particleName,x,y,scale,1,true,flipY);
	}

	public void particleEmitter(String particleName, float x, float y,int amount){
		particleEmitter(particleName,x,y,1,amount,true,false);
	}

	public void particleEmitter(String particleName, float x, float y,float scale, int amount){
		particleEmitter(particleName,x,y,scale,amount,true,false);
	}

	public void particleEmitter(String particleName, float x, float y,float scale, int amount, boolean allowCompletion,
								boolean flipY){
		for (ParticleGenerator p : particles){
			if(Objects.equals(p.particleName, particleName)) {
				p.particleEmitter(x,y,scale,amount,allowCompletion,flipY);
				return;
			}
		}
		ParticleGenerator newParticle = new ParticleGenerator(particleName);
		particles.add(newParticle);
		particleEmitter(particleName, x, y);
	}


	public void particleRenderer(){
		for (ParticleGenerator p : particles){
			p.rendering();
		}
	}

}
