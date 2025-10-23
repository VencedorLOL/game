package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.mygdx.game.Settings.print;

public class Report {
	File report;
	Report(){
		try {
			print("Was folder created? " + new File(Gdx.files.getLocalStoragePath() + "/Table").mkdir());
			report = new File(Gdx.files.getLocalStoragePath() + "Table/times.txt");
			print("Was file created? "+report.createNewFile());

		} catch (IOException ignored) {print("nope");}
	}


	public void writeFile(long time){
		try (FileWriter reportReader = new FileWriter(Gdx.files.getLocalStoragePath() + "Table/times.txt",true)){reportReader.write("Time: "+time +"ns" + "\n");
		} catch (IOException ignored){}
	}


}
