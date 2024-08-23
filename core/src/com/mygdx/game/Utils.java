package com.mygdx.game;

public interface Utils {
	default float colorConverter(float color){
		return color / 255;
	}

	default float cC(float color){
		return color / 255;
	}

	default int intArraySearcher(int[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position)
				return array[i];
		}
		return -1;
	}

	default byte byteArraySearcherForScreenWarps(byte[] array, int position){
		for (int i = 1; i < array.length + 1; i++){
			if (i == position){
				return array[i - 1];
			}
		}
		return -1;
	}

	static float pickValueAUnlessEqualsZeroThenPickB(float a, float b){
		if (a == 0)
			return b;
		return a;
	}

}
