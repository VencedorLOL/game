package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.game.items.Entity;
import com.mygdx.game.items.Stage;

import java.util.ArrayList;

public interface Utils {
	public default float colorConverter(float color){
		return color / 255;
	}

	public default float cC(float color){
		return color / 255;
	}

	public default int intArraySearcher(int[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position){
				return array[i];
			}
		}
		return -1;
	}

	public default byte byteArraySearcher(byte[] array, int position){
		for (int i = 0; i < array.length; i++){
			if (i == position){
				return array[i];
			}
		}
		return -1;
	}

	public default byte byteArraySearcherForScreenWarps(byte[] array, int position){
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

}
