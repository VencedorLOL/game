package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.ClassAndEquipmentChanger;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.guielements.ClassesCards;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Settings.printErr;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.classes;

@SuppressWarnings("all")
public class Savefile {

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

	public void readFile() throws FileNotFoundException {
		Scanner fileReader = new Scanner(new FileReader(savefile));
		ArrayList<String> code = new ArrayList<>();
		String line;
		while (fileReader.hasNext()){
			code.add(fileReader.next());
		}
		if(!code.isEmpty()) {
			line = code.get(0);
			if(line.contains(",")) {
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
		for (int i = 1; i < code.size(); i++){
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
			Files.write(savefile.toPath(), code);
		} catch (IOException ignored){printErr("Failed to make savefile at writeFile in Savefile class");}
	}


}
