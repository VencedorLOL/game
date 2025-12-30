package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.TextureManager.addToList;
import static com.mygdx.game.items.TextureManager.text;

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

		public AttackText(Entity follow, DamageReasons reason, float damage){
			this.follow = follow;
			TextureManager.Text aidText = new TextureManager.Text();
			aidText.render = false;
			aidText.onScreenTime = -1;
			textDamageAndReason.add(new TextDamageAndReason(aidText,damage,reason));
			text.add(aidText);
			process();
		}

		public void addNew(DamageReasons reason, float damage){
			TextureManager.Text aidText = new TextureManager.Text();
			aidText.render = false;
			aidText.onScreenTime = -1;
			textDamageAndReason.add(new TextDamageAndReason(aidText,damage,reason));
			text.add(aidText);
			process();
		}

		public void process(){
			textDamageAndReason.removeIf(t -> !text.contains(t.text));
			int aid = 0;
			for (TextDamageAndReason t : textDamageAndReason){
				t.text.render = true;
				t.text.text = String.format("%.2f", t.damage);
				t.text.x = follow.x + (globalSize()/2f - textSize(t.text.text,40)/2f) - 32;
				t.text.y = follow.y + globalSize() + 50 * (++aid);
				t.text.realSize = 40;
				t.text.setColor(t.reason.getColor());
				t.text.opacity = 1;
				addToList(t.reason.texture, t.text.x - 40, t.text.y - 22,1,0,t.reason.r,t.reason.g,t.reason.b,5,5);
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
			textDamageAndReason.removeIf(t -> !text.contains(t.text));
			int aid = 0;
			for (TextDamageAndReason t : textDamageAndReason) {
				t.text.x = follow.x + (globalSize()/2f - textSize(t.text.text,40)/2f) - 32;
				t.text.y = follow.y + globalSize() + 50 * (++aid);
				float textureOpacity = t.text.vanishingThreshold >= t.text.onScreenTime && t.text.vanishingThreshold > 0 ? t.text.opacity * (1 - (t.text.vanishingThreshold - t.text.onScreenTime) / (t.text.vanishingThreshold)) : t.text.opacity;
				addToList(t.reason.texture, t.text.x - 40, t.text.y - 22,textureOpacity,0,t.reason.r,t.reason.g,t.reason.b,5,5);
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
				turn = TurnManager.getTurnCount();
			}
		}

	}



	public enum DamageReasons{
		MELEE		(255,0,0,"MeleeIndicator"),
		SELF		(255,128,128,null),
		ABSORBED	(0,150,250,"AbsorbedIndicator"),
		UNIVERSAL	(255,0,255,"UniversalIndicator"),
		BURNT		(214,128,128,"BurntIndicator"),
		FROSTBITE	(11,255,250,"FrostbiteIndicator"),
		MAGICAL		(0,168,244,"MagicalIndicator"),
		ELECTRIC	(245,237,0,"LightningIndicator"),
		ICE_BALL	(100,250,250,"IceIndicator"),
		RANGED		(120,59,38,"RangedIndicator"),
		HEALING		(50,250,0,"HealingIndicator"),
		EARTHQUAKE	(150,100,25,"EarthquakeIndicator"),
		PIERCING	(145,0,0,"PiercingIndicator"),
		PRESSURE	(175,175,175,"PressureIndicator"),
		RADIATION	(255,255,0,"RadioactiveIndicator"),
		;

		public final int r,g,b;
		public final String texture;
		DamageReasons(int r,int g, int b,String texture) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.texture = texture;
		}


		public int[] getColor() {
			return new int[]{r, g, b};
		}
	}




}
