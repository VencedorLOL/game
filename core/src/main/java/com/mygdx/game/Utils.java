package com.mygdx.game;

import static com.mygdx.game.GameScreen.chara;

@SuppressWarnings("all")
public class Utils {
	public static float colorConverter(float color){
		return color / 255;
	}

	public static float cC(float color){
		return color / 255;
	}

	public static int intArraySearcher(int[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position)
				return array[i];
		}
		return -1;
	}

	public static byte byteArraySearcherForScreenWarps(byte[] array, int position){
		for (int i = 1; i < array.length + 1; i++){
			if (i == position){
				return array[i - 1];
			}
		}
		return -1;
	}

	public static float pickValueAUnlessEqualsZeroThenPickB(float a, float b){
		if (a == 0)
			return b;
		return a;
	}

	public static int nthIndexOf(String objective, String analyzed, int n){
		if(n < 1)
			return -1;
		for (int i = 1; i <= n; i++){
			if(i == n)
				return analyzed.indexOf(objective) + (objective.length() * i - 1);
			analyzed = analyzed.replaceFirst(objective,"");
		}
		return -1;
	}

	public static int numberOfStrings(String objective, String analyzed){
		int times = 0;
		while(analyzed.contains(objective)){
			times++;
			analyzed = analyzed.replaceFirst(objective,"");
		}
		return times;
	}

	public static float[] toFloat(int[] list){
		float[] finalList = new float[list.length];
		for(int i = 0; i < list.length; i++)
			finalList[i] = list[i];
		return finalList;
	}

	public static float intravalue(float min, float evaluated, float max){
		return evaluated > max ? max : evaluated < min ? min : evaluated;
	}

	public static void paralyzeCharacter(){
		chara.lockClass = true;
		if(chara.walkingAnimation != null)
			chara.walkingAnimation.stop();
		chara.walkingAnimation = null;
		chara.speedLeft[0] = 0; chara.speedLeft[1] = 0;
		if (chara.targetProcessor.circle != null)
			chara.targetProcessor.deleteTexture();
		chara.targetProcessor.reset();
		chara.attacks.clear();
	}

	public static void deparalyzeCharacter(){
		chara.lockClass = false;
	}



}
