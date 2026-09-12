package com.mygdx.game.items.textboxelements.textboxes.tests;

import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;

public class Multicolor extends Textbox {


	public void onOpenOverridable() {
		setText("The Rainbow Text");
		initiateRainbow(200, 0);
		setDefaultAttribute(1,0);
		textColor = text.getDefaultColor();
	}

	public void onRemoval() {
		new RainbowWave();
	}

	public static class RainbowWave extends Textbox{

		public void onOpenOverridable() {
			framesTilNextLetter = 5;
			useCutter = true;
			setText("THE RAINBOW WAVEEEEEEE!!!!!!!!!!!!!!!!");
			initiateRainbow(200, 2f);
			setDefaultAttribute(1,0);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new SlowRainbow();
		}
	}

	public static class SlowRainbow extends Textbox{
		public void onOpenOverridable() {
			framesTilNextLetter = 20;
			useCutter = true;
			setText("slow.... rainbow..................................................................................................................................");
			initiateRainbow(1000, 20);
			setDefaultAttribute(1,0);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new very_slow_rainbow();
		}
	}

	public static class FAST_RAINBOW extends Textbox{

		public void onOpenOverridable() {
			framesTilNextLetter = 3;
			useCutter = true;
			setText("FAST RAINBOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			initiateRainbow(76.5f, 1);
			setDefaultAttribute(1,0);
		}

		public void onRemoval() {
			new VERY_FAST_RAINBOW();
		}
	}

	public static class VERY_FAST_RAINBOW extends Textbox{

		public void onOpenOverridable() {
			framesTilNextLetter = 1;
			useCutter = true;
			setText("VERY FAST RAINBOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			initiateShake(5,20);
			initiateRainbow(30, 1.5f);
			setDefaultAttribute(1,0);
			setDefaultAttribute(0,0);
			textColor = text.getDefaultColor();
		}

	}

	public static class very_slow_rainbow extends Textbox{
		public void onOpenOverridable() {
			framesTilNextLetter = 30;
			useCutter = true;
			setText("very... slow... rainbow......................................................................................................................");
			initiateShake(1,10);
			initiateRainbow(3060, 20);
			setDefaultAttribute(1,0);
			setDefaultAttribute(0,0);
			textColor = text.getDefaultColor();
		}

		public void onRemoval() {
			new FAST_RAINBOW();
		}
	}

}
