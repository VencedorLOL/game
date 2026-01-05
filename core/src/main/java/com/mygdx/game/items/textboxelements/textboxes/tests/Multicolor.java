package com.mygdx.game.items.textboxelements.textboxes.tests;

import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;

public class Multicolor extends Textbox {

	public Multicolor(){
		paralyzeCharacter();
		storedText = "The Rainbow Text";
	}

	boolean initializedRainbow = false;
	public void beforeTextOverridable() {
		if (text != null) {
			if(!initializedRainbow) {
				text.initiateRainbow(200, 0);
				initializedRainbow = true;
			}
			textColor = text.getColor();
		}
	}

	public void onRemoval() {
		new RainbowWave();
	}

	public static class RainbowWave extends Textbox{

		public RainbowWave(){
			framesTilNextLetter = 5;
			storedText = "THE RAINBOW WAVEEEEEEE!!!!!\n!!!!!!!!!!!";
		}

		boolean initializedRainbow = false;
		public void beforeTextOverridable() {
			if (text != null) {
				if(!initializedRainbow) {
					text.initiateRainbow(200, 2f);
					initializedRainbow = true;
				}
				textColor = text.getColor();
			}
		}

		public void onRemoval() {
			new SlowRainbow();
		}
	}

	public static class SlowRainbow extends Textbox{
		public SlowRainbow(){
			framesTilNextLetter = 20;
			storedText = "slow... rainbow............................\n...................................................\n...................................................";
		}

		boolean initializedRainbow = false;
		public void beforeTextOverridable() {
			if (text != null) {
				if(!initializedRainbow) {
					text.initiateRainbow(1000, 20);
					initializedRainbow = true;
				}
				textColor = text.getColor();
			}
		}

		public void onRemoval() {
			new very_slow_rainbow();
		}
	}

	public static class FAST_RAINBOW extends Textbox{
		public FAST_RAINBOW(){
			framesTilNextLetter = 3;
			storedText = "FAST RAINBOW!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
		}

		boolean initializedRainbow = false;
		public void beforeTextOverridable() {
			if (text != null) {
				if(!initializedRainbow) {
					text.initiateRainbow(76.5f, 1);
					initializedRainbow = true;
				}
				textColor = text.getColor();
			}
		}

		public void onRemoval() {
			new VERY_FAST_RAINBOW();
		}
	}

	public static class VERY_FAST_RAINBOW extends Textbox{
		public VERY_FAST_RAINBOW(){
			framesTilNextLetter = 1;
			storedText = "VERY FAST RAINBOW!!!!!!!!!!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
		}

		boolean finalizedInitializing = false;
		boolean initializedRainbow = false;
		public void beforeTextOverridable() {
			if (text != null) {
				if(!finalizedInitializing)
					text.initiateShake(5,20);
				if(!initializedRainbow) {
					text.initiateRainbow(30, 1.5f);
					initializedRainbow = true;
				}
				textColor = text.getColor();
			}
			if(amountOfTextWritten == storedText.length())
				finalizedInitializing = true;
		}

		public void onRemoval() {
			deparalyzeCharacter();
		}
	}

	public static class very_slow_rainbow extends Textbox{
		public very_slow_rainbow(){
			framesTilNextLetter = 30;
			storedText = "very... slow... rainbow................\n...................................................\n...................................................";
		}

		boolean initializedRainbow = false;
		boolean finalizedInitializing = false;
		public void beforeTextOverridable() {
			if (text != null) {
				if(!finalizedInitializing)
					text.initiateShake(1,10);
				if(!initializedRainbow) {
					text.initiateRainbow(3060, 20);
					initializedRainbow = true;
				}
				textColor = text.getColor();
			}
			if(amountOfTextWritten == storedText.length())
				finalizedInitializing = true;
		}

		public void onRemoval() {
			new FAST_RAINBOW();
		}
	}

}
