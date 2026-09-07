package com.mygdx.game.items.textboxelements.textboxes.tests;

import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;

public class Multicolor extends Textbox {

	public Multicolor(){
		paralyzeCharacter();
		setText("The Rainbow Text");
	}


	public void onOpenOverridable() {
		text.initiateRainbow(200, 0);
		textColor = text.getDefaultColor();
	}

	public void onRemoval() {
		new RainbowWave();
	}

	public static class RainbowWave extends Textbox{

		public RainbowWave(){
			framesTilNextLetter = 5;
			setText("THE RAINBOW WAVEEEEEEE!!!!!!!!!!!!!!!!");
		}

		public void onOpenOverridable() {
			useCutter = true;
			text.initiateRainbow(200, 2f);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new SlowRainbow();
		}
	}

	public static class SlowRainbow extends Textbox{
		public SlowRainbow(){
			framesTilNextLetter = 20;
			setText("slow... rainbow..................................................................................................................................");
		}

		public void onOpenOverridable() {
			useCutter = true;
			text.initiateRainbow(1000, 20);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new very_slow_rainbow();
		}
	}

	public static class FAST_RAINBOW extends Textbox{
		public FAST_RAINBOW(){
			framesTilNextLetter = 3;
			setText("FAST RAINBOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}

		public void onOpenOverridable() {
			useCutter = true;
			text.initiateRainbow(76.5f, 1);
		}

		public void onRemoval() {
			new VERY_FAST_RAINBOW();
		}
	}

	public static class VERY_FAST_RAINBOW extends Textbox{
		public VERY_FAST_RAINBOW(){
			framesTilNextLetter = 1;
			setText("VERY FAST RAINBOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}

		public void onOpenOverridable() {
			useCutter = true;
			text.initiateShake(5,20);
			text.initiateRainbow(30, 1.5f);
			textColor = text.getDefaultColor();
		}

	}

	public static class very_slow_rainbow extends Textbox{
		public very_slow_rainbow(){
			framesTilNextLetter = 30;
			setText("very... slow... rainbow......................................................................................................................");
		}

		public void onOpenOverridable() {
			useCutter = true;
			text.initiateShake(1,10);
			text.initiateRainbow(3060, 20);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new FAST_RAINBOW();
		}
	}

}
