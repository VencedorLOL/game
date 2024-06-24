package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.GameScreen;

public class Particles {
	TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("particles//AttackMode.atlas"));
	ParticleEffect effect = new ParticleEffect();

	public void damageParticle(float x, float y, GameScreen gs){
		effect.load(Gdx.files.internal("particles//attackMode.p"), textureAtlas);
		effect.start();
		effect.setPosition(x, y);
		effect.draw(gs.batch, Gdx.graphics.getDeltaTime());
	}

}
