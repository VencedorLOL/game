package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.allowCommands;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.Text.getLetter;
import static com.mygdx.game.items.TextureManager.Text.length;
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

	public static int numberOfStrings(String analyzed, String objective){
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

	public static int indexForwardClosestToTarget(char target, String string, int position){
		for(int i = position; i < string.length(); i++)
			if(string.charAt(i) == target)
				return i;
		return -1;
	}

	public static String replaceCharAt(String original, char newChar, int index){
		if(index > original.length())
			return original;
		char[] strBuilder = new char[original.length()];
		for(int i = 0; i < original.length(); i++)
			if(i != index)
				strBuilder[i] = original.charAt(i);
			else
				strBuilder[i] = newChar;
		return new String(strBuilder);
	}

	public static String insertStringAt(String original, String fragment, int index){
		if(index > original.length())
			return original;
		char[] strBuilder = new char[original.length() + fragment.length()];
		for(int i = 0; i <= original.length(); i++)
			if(i < index)
				strBuilder[i] = original.charAt(i);
			else if (i == index) for (int j = 0; j < fragment.length(); j++)
				strBuilder[i + j] = fragment.charAt(j);
			else if (i > index)
				strBuilder[i + fragment.length() - 1] = original.charAt(i - 1);
		return new String(strBuilder);
	}

	public static void main(String... args){
/*/		//algo word attributes
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
			counter += getLetter(size.charAt(i)).getSize() + 1;
			if(counter > limit){
				int index = indexBackClosestToTarget(' ', size, i);
				size = replaceCharAt(size,'\n',index);
				counter = 0;
			}
		}
		print(size);
		print("\n-----------------------------------------------------\n");
*/
/*		print("instertStringAt tester:");
		String stringTest = "ho world";
		String insertedTest = "ell";
		int position = 1;
		print("string subject: " +stringTest+" inserted string: " + insertedTest + " position at: " + position);
		print("result: " + insertStringAt(stringTest,insertedTest,position));
*/

/*		print("calculator of formatted strings' lenght");
		String fakeCommand = "<abcde>";
		String strinTest = "hello world this is a test string with a fake command: " + fakeCommand;
		print("real len of str: " + strinTest.length() + " length of cmd: " + fakeCommand.length() + " len of formatted: " + length(strinTest));
*/
//this comment is so i can commit something to my git repositories. this is a test. third time's the charm.

	}

	public static String stringSeparator(String string, int pxLimit, char sepChar) {
		float counter = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\n') {
				counter = 0;
				continue;
			}
			boolean proceedNormally = true;
			if(allowCommands())
				if(string.charAt(i) == '<' && (i == 0 || string.charAt(i-1) != '\\') && i+1 != string.length())
					for(int j = i+1; j < string.length(); j++)
						if(string.charAt(j) == '<')
							break;
						else if (string.charAt(j) == '>'){
							counter += 9;
							proceedNormally = false;
							i = j;
							break;
						}
			if (proceedNormally)
				counter += getLetter(string.charAt(i)).getSize() + 1;
			if (counter > pxLimit) {
				int index = indexBackClosestToTarget(sepChar, string, i);
				string = replaceCharAt(string, '\n', index);
				counter = 0;
			}
		}
		return string;
	}

	public static String[] stringSplitter(String string, int pxLimit,char sepChar) {
		return stringSeparator(string,pxLimit,sepChar).split("\n");
	}


	public static String[] stringSuperSplitter(String[] string, int pxLimit,char sepChar) {
		ArrayList<String> finalIsh = new ArrayList<>();
		for(int i = 0; i < string.length; i++) {
			Collections.addAll(finalIsh,stringSplitter(string[i],pxLimit,sepChar));
		}
		return finalIsh.toArray(new String[0]);
	}

	public static String stringCutter(String string, int pxLimit) {
		float counter = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\n') {
				counter = 0;
				continue;
			}
			boolean proceedNormally = true;
			if(allowCommands())
				if(string.charAt(i) == '<' && (i == 0 || string.charAt(i-1) != '\\') && i+1 != string.length())
					for(int j = i+1; j < string.length(); j++)
						if(string.charAt(j) == '<')
							break;
						else if (string.charAt(j) == '>'){
							counter += 9;
							proceedNormally = false;
							i = j;
							break;
						}
			if (proceedNormally)
				counter += getLetter(string.charAt(i)).getSize() + 1;
			if (counter > pxLimit) {
				if(i != 0) {
					string = insertStringAt(string,"\n",i-1);
				}
				counter = 0;
			}
		}
		return string;
	}


	public static int characterCounter(String string, char character){
		int counter = 0;
		for (int i = 0; i < string.length(); i++){
			if (string.charAt(i) == character)
				counter++;
		}
		return counter;
	}

	public static int characterCounter(String[] string, char character){
		int counter = 0;
		for (int i = 0; i < string.length; i++){
			counter += characterCounter(string[i],character);
		}
		return counter;
	}


	public static String[] emptyPurger(String[] string){
		ArrayList<String> mewArray = new ArrayList<>();
		for (int i = 0; i < string.length; i++){
			if(string[i] != "")
				mewArray.add(string[i]);
		}
		return mewArray.toArray(new String[0]);
	}

	public static int charCount(char chara, String string){
		int count = 0;
		for(int i = 0; i < string.length(); i++)
			if(string.charAt(i)==chara)
				count++;
		return count;
	}

	public static int charCount(char chara, String... string){
		int count = 0;
		for(String s : string)
			count += charCount(chara,s);
		return count;

	}

	public static int[] positionsOfChar(String string, char objective){
		int[] pos = new int[numberOfStrings(string,objective+"")];
		int startPos = (string.length() > 0 && string.charAt(0) == objective) ? 1 : 0;
		for(int i = 0; i < string.length(); i++)
			if(string.charAt(i) == objective)
				for(int j = startPos; j < pos.length; j++)
					if(pos[j] == 0) {
						pos[j] = i; break;
					}
		return pos;
	}

}
