package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

import static com.mygdx.game.GameScreen.delta;
import static com.mygdx.game.Settings.printErr;
import static com.mygdx.game.items.TextureManager.atlas;

public class ParticleGenerator {
	String particleName;
	ParticleEffect particle;
	ParticleEffectPool pool;
	Array<ParticleEffectPool.PooledEffect> effects;
	int time;
	int counter;

	public ParticleGenerator(String particleName){
		this.particleName = particleName;
		particle = new ParticleEffect();
		particle.load(Gdx.files.internal(getPath()), atlas);
		pool = new ParticleEffectPool(particle, 0, 2048);
		effects = new Array<>();
	}

	public void rendering(){
		if(counter > 0 ){
			createParticles();
			counter--;
		}
		for (ParticleEffectPool.PooledEffect effect : new Array.ArrayIterator<>(effects)){
			effect.update(delta);
			effect.draw(TextureManager.batch, delta);
			if (effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.reset();
				effect.free();
			}
		}
	}

	float x, y, scale; int amount; boolean allowCompletion; boolean flipY; Entity entity;
	public void particleEmitter(float x, float y, float scale, int amount, boolean allowCompletion, boolean flipY,int time,Entity entity){
		if (amount <= 0) {
			printErr("");
			printErr("</|------------------------------------------------------------|\\>");
			printErr("           |----------------ERROR----------------|           ");
			printErr("The amount of particles emitted can't be less or equal to 0");
			printErr("The number of particles wanted to be generated was of: " + amount);
			printErr("           |----------------ERROR----------------|           ");
			printErr("</|------------------------------------------------------------|\\>");
			printErr("");
		} else {
			this.time = time;
			counter = time;
			this.x = x;
			this.y = y;
			this.scale = scale;
			this.amount = amount;
			this.allowCompletion = allowCompletion;
			this.flipY = flipY;
			this.entity = entity;
		}
	}

	public void createParticles(){
		for (int i = 0; i < amount; i++) {
			ParticleEffectPool.PooledEffect effect = pool.obtain();
			if(entity == null)
				effect.setPosition(x, y);
			else
				effect.setPosition(entity.x + x, entity.y + y);
			effect.scaleEffect(scale);
			if (allowCompletion)
				effect.allowCompletion();
			if (flipY)
				effect.flipY();
			effects.add(effect);
		}
	}

	public String getPath(){
		for (ParticleList p : ParticleList.values()){
			if (p.toString().equals(particleName)){
				return p.path;
			}
		}
		return null;
	}

	public enum ParticleList{
		OP ("Particles\\opathFindAlgorithm.p"),
		ATTACK ("Particles\\attack.p"),
		PARTICLE ("Particles\\particle.p"),
		CANON ("Particles\\canon.p"),
		BLOB ("Particles\\blob.p"),
		DEMONIZE("Particles\\demonizeEffect.p")
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
