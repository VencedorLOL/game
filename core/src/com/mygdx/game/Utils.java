package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.floor;

public interface Utils {
	default float colorConverter(float color){
		return color / 255;
	}
	/*
	Color Converter
	*/
	default float cC(float color){
		return color / 255;
	}

	default int intArraySearcher(int[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position){
				return array[i];
			}
		}
		return -1;
	}

	default byte byteArraySearcher(byte[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position){
				return array[i];
			}
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

	default float pickValueAUnlessEqualsZeroThenPickB(float a, float b){
		if (a == 0)
			return b;
		return a;
	}



	default Vector3 unproject(Vector3 screenCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
		float x = screenCoords.x - viewportX;
		float y = (float)Gdx.graphics.getHeight() - screenCoords.y - viewportY;
		screenCoords.x = 2.0F * x / viewportWidth - 1.0F;
		screenCoords.y = 2.0F * y / viewportHeight - 1.0F;
		screenCoords.z = 2.0F * screenCoords.z - 1.0F;
		screenCoords.prj(new Matrix4());
		return screenCoords;
	}


}
