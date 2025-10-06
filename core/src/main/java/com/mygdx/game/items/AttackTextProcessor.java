package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.security.Key;
import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.Text.createFont;

public class AttackTextProcessor {
	static ArrayList<AttackText> textList = new ArrayList<>();
	static OnVariousScenarios oVS;

	static {
		oVS = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				for (AttackText t : textList){
					t.fade();
				}
			}
			public void onStageChange(){
				delete();
			}

		};
	}


	public static void addAttackText(float damage, DamageReasons reason, Entity victim){
		for (AttackText a : textList){
			if(a.follow == victim) {
				a.addNew(reason, damage);
				return;
			}
		}
		textList.add(new AttackText(victim,reason,damage));
	}


	public static void coordsUpdater() {
		for (AttackText a : textList)
			a.coordsUpdater();
	}


	public static void delete(){
		textList.clear();
	}




	static class AttackText{
		ArrayList<TextDamageAndReason> textDamageAndReason = new ArrayList<>();

		Entity follow;
		boolean delete = true;

		public AttackText(Entity follow, DamageReasons reason, float damage){
			this.follow = follow;
			TextureManager.Text aidText = new TextureManager.Text(){
				public void onFinishOverridable(){
					if (delete)
						delete();
				}};
			aidText.render = false;
			textDamageAndReason.add(new TextDamageAndReason(aidText,damage,reason));
			TextureManager.text.add(aidText);
			process();
		}

		public void addNew(DamageReasons reason, float damage){
			TextureManager.Text aidText = new TextureManager.Text();
			aidText.render = false;
			textDamageAndReason.add(new TextDamageAndReason(aidText,damage,reason));
			TextureManager.text.add(aidText);
			process();
		}

		public void process(){
			textDamageAndReason.removeIf(t -> t.text.fakeNull);
			int aid = 0;
			for (TextDamageAndReason t : textDamageAndReason){
				t.text.render = true;
				if (Turns.isTurnRunning() && Turns.getTurnCount() == t.turn)
					t.text.onScreenTime = -1;
				else if (t.text.onScreenTime < 1 && Turns.getTurnCount() == t.turn){
					t.text.onScreenTime = 120;
					t.text.vanishingThreshold = 60;
					delete = false;
				}
				t.text.text = Float.toString(t.damage);
				t.text.x = follow.x + ((float) globalSize() /2 - (float) ((String.format("%.2f", t.damage).length() - 1) * 20 + 8) / 2);
				t.text.y = follow.y + globalSize() + 50 * (++aid);
				t.text.font = createFont(TextureManager.Fonts.ComicSans,40);
				t.text.setColor(t.reason.getColor());
				t.text.opacity = 1;
			}
		}

		public void fade(){
			for (TextDamageAndReason t : textDamageAndReason)
				if(t.text.onScreenTime <= -1) {
					t.text.onScreenTime = 120;
					t.text.vanishingThreshold = 60;
				}
		}

		public void coordsUpdater() {
			textDamageAndReason.removeIf(t -> t.text.fakeNull);
			int aid = 0;
			for (TextDamageAndReason t : textDamageAndReason) {
				t.text.x = follow.x + ((float) globalSize() / 2 - (float) ((String.format("%.2f", t.damage).length() - 1) * 20 + 8) / 2);
				t.text.y = follow.y + globalSize() + 50 * (++aid);
			}

		}


		public static class TextDamageAndReason{
			TextureManager.Text text;
			float damage;
			DamageReasons reason;
			long turn;

			TextDamageAndReason(TextureManager.Text text, float damage, DamageReasons reason){
				this.text = text;
				this.damage = damage;
				this.reason = reason;
				turn = Turns.getTurnCount();
			}
		}




	}



	public enum DamageReasons{
		MELEE     (255,0,0),
		SELF      (255,128,128),
		ABSORBED  (0,0,255),
		UNIVERSAL (255,0,255),
		BURNT     (214,128,128),
		FROSTBITE (11,255,250),
		MAGICAL   (0,168,244),
		LIGHTNING (245,237,0),
		ICE_BALL  (100,250,250),
		RANGED(120,59,38),

		;

		public final int r,g,b;
		DamageReasons(int r,int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}


		public int[] getColor() {
			return new int[]{r, g, b};
		}
	}




}
