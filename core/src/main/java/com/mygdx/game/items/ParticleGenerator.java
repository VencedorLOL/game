package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

import static com.mygdx.game.GameScreen.delta;

public class ParticleGenerator {
	String particleName;
	TextureManager tm;
	ParticleEffect particle;
	ParticleEffectPool pool;
	Array<ParticleEffectPool.PooledEffect> effects;

	public ParticleGenerator(TextureManager tm, String particleName){
		this.tm = tm;
		this.particleName = particleName;
		particle = new ParticleEffect();
		particle.load(Gdx.files.internal(getPath()), tm.atlas);
		pool = new ParticleEffectPool(particle, 0, 2048);
		effects = new Array<>();
	}

	public void rendering(){
		for (ParticleEffectPool.PooledEffect effect : new Array.ArrayIterator<>(effects)){
			effect.update(delta);
			effect.draw(tm.batch, delta);
			if (effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.reset();
				effect.free();
			}
		}
	}


	public void particleEmitter(float x, float y, float scale, int amount, boolean allowCompletion, boolean flipY){
		if (amount <= 0) {
			System.err.println();
			System.err.println("</|------------------------------------------------------------|\\>");
			System.err.println("           |----------------ERROR----------------|           ");
			System.err.println("The amount of particles emitted can't be less or equal to 0");
			System.err.println("The number of particles wanted to be generated was of: " + amount);
			System.err.println("           |----------------ERROR----------------|           ");
			System.err.println("</|------------------------------------------------------------|\\>");
			System.err.println();
		}
		else
			for (int i = 0; i < amount; i++) {
				ParticleEffectPool.PooledEffect effect = pool.obtain();
				effect.setPosition(x, y);
				effect.scaleEffect(scale);
				if (allowCompletion)
					effect.allowCompletion();
				if (flipY)
					effect.flipY();
				effects.add(effect);
		}
	}

	public String getPath(){
		for (ParticleList pathList : ParticleList.values()){
			if (Objects.equals(pathList.toString(), particleName)){
				return pathList.path;
			}
		}
		return null;
	}

	public enum ParticleList{
		OP ("Particles\\opathFindAlgorythm.pathFindAlgorythm"),
		ATTACK ("Particles\\attack.pathFindAlgorythm"),
		PARTICLE ("Particles\\particle.pathFindAlgorythm"),
		CANON ("Particles\\canon.pathFindAlgorythm"),
		BLOB ("Particles\\blob.pathFindAlgorythm")
		;

		public final String path;
		ParticleList(String path) {
			this.path = path;
		}
	}





/* Useless, but it is so precious...
	public static int fileLifeGetter(String fileDirectory){
		try {
			FileReader file = new FileReader(fileDirectory);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				if (scanner.nextLine().contains("- Life -")) {
					scanner.nextLine();
					scanner.nextLine();
					String life = scanner.nextLine();
					if (life.contains("highMin")){
						System.out.println(life);
						life = life.replace("highMin: ","");
						float temporalFloatToStoreLifeValue = Float.parseFloat(life);
						return (int) temporalFloatToStoreLifeValue;
					}
				}
			}
		}
		catch (FileNotFoundException e){
			System.err.println("File to read a texture's life value not found.");
			System.err.println("This error comes from fileLifeGetter method from ParticleGenerator class.");
			System.err.println("Path not found was this one:" + fileDirectory);
			e.printStackTrace();
		}
		return -1;
	}
*/
}
