package com.mygdx.game;

import com.mygdx.game.items.TextureManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.Text.getTexture;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.String.valueOf;

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


	public static int[] toInt(String... list){
		int finalListLength = 0;
		for(int i = 0; i < list.length; i++)
			try{
				Integer.parseInt(list[i]);
				finalListLength++;
			} catch (NumberFormatException ignored){}

		int[] finalList = new int[finalListLength];
		for (int i = 0; i < finalListLength; i++)
			finalList[i] = Integer.parseInt(list[i]);
		return finalList;
	}

	// it's actually shorter to write the return expression each time rather than typing the method why am I making this.
	public static boolean floatInRange(float min, float max, float value){
		return value >= min && value <= max;
	}


	public static ArrayList<?>[] getDifference(ArrayList<?> first, ArrayList<?> second){
		ArrayList<?> missingFirst = new ArrayList<>(second);
		missingFirst.removeIf(e -> elementExistsInList((ArrayList<Object>) first,e));
		ArrayList<?> missingSecond = new ArrayList<>(first);
		missingFirst.removeIf(e -> elementExistsInList((ArrayList<Object>) second,e));
		return new ArrayList<?>[]{missingSecond,missingFirst};
	}

	public static boolean elementExistsInList(ArrayList<Object> list, Object element){
		for(Object o : list)
			if(element == o)
				return true;
		return false;
	}

	public static float distance(float iX, float iY, float fX, float fY){
		return (float) sqrt(pow((iX)-(fX),2)+pow((iY)-(fY),2));
	}

	public static int indexBackClosestToTarget(char target, String string, int position){
		position = string.length() - 1 - position;
		string = new StringBuilder(string).reverse().toString();
		int finalPos = -1;
		for(int i = position; i < string.length(); i++)
			if(string.charAt(i) == target) {
				finalPos = i;
				break;
			}
		return string.length() - 1 - finalPos;
	}

	public static String replaceCharAt(String original, char newChar, int index){
		if(index > original.length())
			return original;
		char[] strBuilder = new char[original.length()];
		for(int i = 0; i < original.length(); i++){
			if(i != index)
				strBuilder[i] = original.charAt(i);
			else
				strBuilder[i] = newChar;
		}
		return new String(strBuilder);
	}

	public static void main(String... args){
		//algo word attributes
		String str1 = "";
		String str2 = "";
		print("Start at: "+ (str1.length()-1) + ", end at: "+  (str2.length()-1));
		//algo line breaks
		//works like a charm
		//TODO: implement this into the textboxes
		String size = "hello world, this is a kinda long sentence kinda to test the new algorithm i made at detecting where line breaks should go.";
		print("Final result: \n-----------------------------------------------------\n");
		int limit = 203;
		float counter = 0;
		for(int i = 0; i < size.length(); i++) {
			if (size.charAt(i) == '\n' ){
				counter = 0;
				continue;
			}
			counter += getTexture(size.charAt(i)).getSize() + 1;
			if(counter > limit){
				int index = indexBackClosestToTarget(' ', size, i);
				size = replaceCharAt(size,'\n',index);
				counter = 0;
			}
		}
		print(size);
		print("\n-----------------------------------------------------\n");
	}

	public static String stringCutter(String string, int pxLimit,char sepChar) {
		float counter = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\n') {
				counter = 0;
				continue;
			}
			counter += getTexture(string.charAt(i)).getSize() + 1;
			if (counter > pxLimit) {
				int index = indexBackClosestToTarget(sepChar, string, i);
				string = replaceCharAt(string, '\n', index);
				counter = 0;
			}
		}
		return string;
	}

	public static String[] stringSplitter(String string, int pxLimit,char sepChar) {
		return stringCutter(string,pxLimit,sepChar).split("\n");
	}


	public static String[] stringSuperSplitter(String[] string, int pxLimit,char sepChar) {
		ArrayList<String> finalIsh = new ArrayList<>();
		for(int i = 0; i < string.length; i++) {
			Collections.addAll(finalIsh,stringSplitter(string[i],pxLimit,sepChar));
		}
		return finalIsh.toArray(new String[0]);
	}

}
