package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Report {
	File report;
	Report(){
		try {
			new File(Gdx.files.getLocalStoragePath() + "/Table").mkdir();
			report = new File("times/PATimesTable.txt");
			report.createNewFile();

		} catch (IOException ignored) {}
	}


	public void writeFile(long time){
		try (FileWriter reportReader = new FileWriter("times/times.txt",true)){reportReader.write("Time: "+time +"ns" + "\n");
		} catch (IOException ignored){}
	}



}
