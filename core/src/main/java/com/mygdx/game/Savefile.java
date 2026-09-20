package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.ClassAndEquipmentChanger;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.guielements.ClassesCards;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Settings.printErr;
import static com.mygdx.game.Utils.indexForwardClosestToTarget;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.classes;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.valueOf;

@SuppressWarnings("all")
public class Savefile {
	ArrayList<Flag> flags = new ArrayList<>();
	File savefile;
	Savefile(){
		try {
			print("Was folder created? " + new File(Gdx.files.getLocalStoragePath() + "/Saves").mkdir());
			savefile = new File(Gdx.files.getLocalStoragePath() + "saves/save.txt");
			print("Was file created? "+savefile.createNewFile());

		} catch (IOException ignored) {print("nope");}
		try {
			readFile();
		} catch (FileNotFoundException ignored){}
	}

	/**
	 * This returns the requested flag. If it doesn't exist, it makes one with the specified defaultValue.
	 */
	public String getOrMakeFlag(int pos, String value, String defaultValue){
		String valu = getFlag(pos,value);
		if(valu == null)
			setFlag(pos,defaultValue);
		return valu;
	}

	/**
	 * This returns the requested flag. If it doesn't exist, it makes one with the specified defaultValue.
	 */
	public String getOrMakeFlag(String name, String value, String defaultValue){
		String valu = getFlag(name,value);
		if(valu == null)
			setFlag(name,defaultValue);
		return valu;
	}


	public String getFlag(int pos, String value){
		for(Flag f : flags){
			if(f.pos() == pos && pos != MAX_VALUE)
				return f.value;
		} printErr("No flag set with the requested position: " + pos); return null;
	}

	public String getFlag(String name, String value){
		for(Flag f : flags){
			if(f.name == name && name != null)
				return f.value;
		} printErr("No flag set with the requested name: " + name); return null;
	}


	public void setFlag(int pos, String value){
		for(Flag f : flags)
			if(f.pos() == pos && pos != MAX_VALUE) {
				f.value = value;
				return;
			}
		flags.add(new Flag(pos,value));
	}

	public void setFlag(String name, String value){
		for(Flag f : flags)
			if(f.name == name && name != null) {
				f.value = value;
				return;
			}
		flags.add(new Flag(name,value));
	}


	public void readFile() throws FileNotFoundException {
		Scanner fileReader = new Scanner(new FileReader(savefile));
		ArrayList<String> code = new ArrayList<>();
		String line;
		while (fileReader.hasNext())
			code.add(fileReader.next());
		int flagStart = -1;
		if(!code.isEmpty()) {
			for(int i = 0 ; i < code.size(); i++){
				if(flagStart != -1){
					if(code.get(i).indexOf(0) == '\"')
						flags.add(new Flag(indexForwardClosestToTarget('\"',code.get(i),1),code.get(i).substring(code.get(i).indexOf(":")+2)));
					else
						flags.add(new Flag(i-flagStart,code.get(i).substring(code.get(i).indexOf(":")+2)));
				}
				if((code.get(i).equals("\n") || code.get(i).equals("")) && flagStart != -1)
					flagStart = i;
			}

			line = code.get(0);
			if(line.contains(",") && !line.contains(":")) {
				String[] types = line.split(",");
				for (int i = 0; i < types.length; i++) {
					ClassAndEquipmentChanger.ClassObject temp = null;
					for (int j = 0; j < ClassesCards.ClsCardObj.values().length; j++)
						if (ClassesCards.ClsCardObj.values()[j].clsObj.name.equals(types[i])) {
							temp = ClassesCards.ClsCardObj.values()[j].clsObj;
							break;
						}
					if(temp != null)
						classSlots[i] = temp;
				}
			}
		}
		flagStart = flagStart == -1 ? code.size() : flagStart;
		for (int i = 1; i < flagStart; i++){
			line = code.get(i);
			if(line.contains(",") && line.indexOf(",") == line.lastIndexOf(",")){
				if (!line.substring(0, line.indexOf(",")).equals("-1")) {
					classes[i].setWeapon(ClassesCards.ClsCardObj.values()[i-1].clsObj.getWeapon(Integer.parseInt(line.substring(0, line.indexOf(","))), null));
				} else
					classes[i].setWeapon(new Weapons.NoWeapon(null,true));
				if(!line.substring(line.indexOf(",") + 1).equals("-1")) {
					classes[i].setShield(ClassesCards.ClsCardObj.values()[i-1].clsObj.getShield(Integer.parseInt(line.substring(line.indexOf(",") + 1)), null));
				} else
					classes[i].setShield(new Shields.NoShield(null,true));
			}


		}


	}

	/**
	 * DO NOT USE. I DID NOT ADAPT IT TO THE NEW SYSTEM.
	 */
	@Deprecated
	public void writeLine(int line) {
		try {
			ArrayList<String> code = new ArrayList<>(Files.readAllLines(savefile.toPath()));
			if (line == 0) {
				String string = "";
				for (int i = 0; i < classes.length; i++) {
					if (i == 0)
						string = classSlots[i].name;
					else
						string = string + "," + classSlots[i].name;
				}
				code.set(line, string);
			} else
				code.set(line, getWeapInt(line-1) + "," + getShieldInt(line-1));
			Files.write(savefile.toPath(), code);
		} catch (IOException ignored){printErr("Failed to make savefile at writeLine(int line) in Savefile class");}
	}

	public int getWeapInt(int line){
		for(int i = 0; i < ClassesCards.ClsCardObj.values()[line-1].clsObj.weaponer.size(); i++)
			if(classes[line].weaponClass == ClassesCards.ClsCardObj.values()[line-1].clsObj.getWeapon(i,null).getClass())
				return i;
		return -1;
	}

	public int getShieldInt(int line){
		for(int i = 0; i < ClassesCards.ClsCardObj.values()[line-1].clsObj.shielder.size(); i++)
			if(classes[line].shieldClass == ClassesCards.ClsCardObj.values()[line-1].clsObj.getShield(i,null).getClass())
				return i;
		return -1;
	}

	public void writeFile() {
		try {
			// Saved classes and equipment
			ArrayList<String> code = new ArrayList<>();
			for (int i = 0; i < classes.length; i++) {
				if (i == 0) {
					String string = "";
					for (int j = 0; j < classSlots.length; j++) {
						if (j == 0)
							string = classSlots[j].name;
						else
							string = string + "," + classSlots[j].name;
					}
					code.add(string);
				} else
					code.add(getWeapInt(i) + "," + getShieldInt(i));
			}
			code.add("");
			// Other flags
			ArrayList<Flag> numbered = new ArrayList<>();
			ArrayList<Flag> named = new ArrayList<>();
			int biggestPos = -1;
			for(Flag f : flags)
				if(f.pos() != MAX_VALUE) {
					numbered.add(f);
					biggestPos = f.pos() > biggestPos ? f.pos() : biggestPos;
				} else
					named.add(f);

			for (int i = 0; i < Math.max(biggestPos,flags.size()); i++){
				Flag flag = null;
				for(Flag f : numbered)
					if(f.pos() == i) {
						flag = f;
						code.add(": " + f.value);
						break;
					}
				if(flag != null)
					numbered.remove(flag);
				else {
					for(Flag f : named){
						flag = f;
						code.add("\"" + f.name + "\": " + f.value);
						break;
					} if(flag != null)
						named.remove(flag);
				}
			}

			Files.write(savefile.toPath(), code);
		} catch (IOException ignored){printErr("Failed to make savefile at writeFile in Savefile class");}
	}



	public float getFFloat(String name, String value){
		try {
			return Float.valueOf(getFlag(name, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to float the flag named: " + name + " because the flag is: " + value);}
		return 0;
	}

	public float getFFloat(int pos, String value){
		try {
			return Float.valueOf(getFlag(pos, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to float the flag numbered: " + pos + " because the flag is: " + value);}
		return 0;
	}

	public int getFInt(String name, String value){
		try {
			return Integer.valueOf(getFlag(name, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to int the flag named: " + name + " because the flag is: " + value);}
		return 0;
	}

	public int getFInt(int pos, String value){
		try {
			return Integer.valueOf(getFlag(pos, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to int the flag numbered: " + pos + " because the flag is: " + value);}
		return 0;
	}

	public boolean getFBool(String name, String value){
		try {
			return Boolean.valueOf(getFlag(name, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to boolean the flag named: " + name + " because the flag is: " + value);}
		return false;
	}

	public boolean getFBool(int pos, String value){
		try {
			return Boolean.valueOf(getFlag(pos, value));
		} catch (NumberFormatException ignored) {printErr("Could not convert to boolean the flag numbered: " + pos + " because the flag is: " + value);}
		return false;
	}

	public void setFFloat(int pos, float value){
		setFlag(pos,value+"");
	}

	public void setFFloat(String name, float value){
		setFlag(name,value+"");
	}

	public void setFInt(int pos, int value){
		setFlag(pos,value+"");
	}

	public void setFInt(String name, int value){
		setFlag(name,value+"");
	}

	public void setFBool(int pos, boolean value){
		setFlag(pos,value+"");
	}

	public void setFBool(String name, boolean value){
		setFlag(name,value+"");
	}

	public static class Flag{
		int pos = MAX_VALUE;
		String name;
		String value;

		Flag(String name, String value){
			this.name = name;
			this.value = value;
		}

		Flag(int pos, String value){
			this.pos = pos;
			this.value = value;
		}

		public int pos(){
			return pos;
		}

	}


}
