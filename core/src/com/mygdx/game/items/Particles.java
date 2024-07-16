package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.GameScreen;

public class Particles {
	ParticleEffect effect = new ParticleEffect();

	public Particles(GameScreen gs){
		effect.load(Gdx.files.internal("Sprites//particle.p"), gs.textureManager.atlas);
		effect.start();
	}


	public void damageParticle(float x, float y, GameScreen gs){
		effect.setPosition(x, y);
		effect.draw(gs.textureManager.batch, Gdx.graphics.getDeltaTime());
	}

	public void damageParticle(float x, float y, TextureManager tm){
		effect.setPosition(x, y);
		effect.draw(tm.batch, Gdx.graphics.getDeltaTime());
	}


}
