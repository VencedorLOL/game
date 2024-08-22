package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Turns;

import java.util.ArrayList;
import java.util.List;

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

	static float pickValueAUnlessEqualsZeroThenPickB(float a, float b){
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

	// Count sort
	static int[] countSort(int[] inputArray) {
		int N = inputArray.length;
		int M = 0;

		for (int i = 0; i < N; i++) {
			M = Math.max(M, inputArray[i]);
		}

		int[] countArray = new int[M + 1];

		for (int i = 0; i < N; i++) {
			countArray[inputArray[i]]++;
		}

		for (int i = 1; i <= M; i++) {
			countArray[i] += countArray[i - 1];
		}

		int[] outputArray = new int[N];

		for (int i = N - 1; i >= 0; i--) {
			outputArray[countArray[inputArray[i]] - 1] = inputArray[i];
			countArray[inputArray[i]]--;
		}

		return outputArray;
	}

	// modified count sort
	static List<Turns.EntityAndSpeed> countSort(List<Turns.EntityAndSpeed> inputArray) {
		int N = inputArray.size();
		int M = 0;

		for (int i = 0; i < N; i++) {
			M = Math.max(M, inputArray.get(i).getSpeed());
		}

		int[] countArray = new int[M + 1];

		for (int i = 0; i < N; i++) {
			countArray[inputArray.get(i).getSpeed()]++;
		}

		for (int i = 1; i <= M; i++) {
			countArray[i] += countArray[i - 1];
		}

		ArrayList<Turns.EntityAndSpeed> outputArray = new ArrayList<>(N);

		for (int i = 0; i < N; i++){
			outputArray.add(null);
		}


		for (int i = N - 1; i >= 0; i--) {
			outputArray.add((countArray[inputArray.get(i).getSpeed()] - 1), inputArray.get(i));
			countArray[inputArray.get(i).getSpeed()]--;
		}

		for (int i = 0; i < N; i++) {
			outputArray.remove(null);
		}

		return outputArray;
	}


	//Merge sort
	static void merge(List<Turns.EntityAndSpeed> arr, int l, int m, int r)
	{
		// Find sizes of two subarrays to be merged
		int n1 = m - l + 1;
		int n2 = r - m;

		// Create temp arrays
		int[] L = new int[n1];
		int[] R = new int[n2];

		// Copy data to temp arrays
		for (int i = 0; i < n1; ++i)
			L[i] = arr.get(l + i).getSpeed();
		for (int j = 0; j < n2; ++j)
			R[j] = arr.get(m + 1 + j).getSpeed();

		// Merge the temp arrays

		// Initial indices of first and second subarrays
		int i = 0, j = 0;

		// Initial index of merged subarray array
		int k = l;
		while (i < n1 && j < n2) {
			if (L[i] <= R[j]) {
				arr.get(k).setSpeed(L[i]);
				i++;
			}
			else {
				arr.get(k).setSpeed(R[j]);
				j++;
			}
			k++;
		}

		// Copy remaining elements of L[] if any
		while (i < n1) {
			arr.get(k).setSpeed(L[i]);
			i++;
			k++;
		}

		// Copy remaining elements of R[] if any
		while (j < n2) {
			arr.get(k).setSpeed(R[j]);
			j++;
			k++;
		}
	}

	// Main function that sorts arr[l..r] using
	// merge()
	static List<Turns.EntityAndSpeed> sort(List<Turns.EntityAndSpeed> arr, int l, int r)
	{
		if (l < r) {

			// Find the middle point
			int m = l + (r - l) / 2;

			// Sort first and second halves
			sort(arr, l, m);
			sort(arr, m + 1, r);

			// Merge the sorted halves
			merge(arr, l, m, r);
		}
		return arr;
	}
}
