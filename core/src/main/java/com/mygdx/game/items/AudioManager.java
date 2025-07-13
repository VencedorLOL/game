package com.mygdx.game.items;

import com.mygdx.game.Settings;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.mygdx.game.Settings.getVolume;

public class AudioManager {

	public static ArrayList<Sound> sounds = new ArrayList<>();
	static OnVariousScenarios oVE;

	static{
		oVE = new OnVariousScenarios(){
			@Override
			public void onVolumeChange() {
				for (Sound s : sounds){
					s.setVolume(getVolume());
				}
			}
		};
	}

	public static void play(String songName){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.open();
				s.play();
				return;
			}
		load(songName);
		play(songName);
	}

	public static void quickPlay(String songName){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.open();
				s.loops(false);
				s.play();
				return;
			}
		load(songName,false);
		quickPlay(songName);
	}



	public static void load(String songName) {
		load(songName,false,0);
	}

	public static void load(String songName, boolean loops) {
		load(songName,loops,0);
	}

	public static void load(String songName, boolean loops, int startLoopAt) {
		for (Sound s : sounds)
			if (s.getIdentifier().equals(songName))
				return;
		sounds.add(new Sound(songName, loops, startLoopAt));
	}

	public static void loadMultiple(ArrayList<String> songsToStart) {
		for (Sound s : sounds)
			// thanks intellij
			songsToStart.removeIf(ss -> ss.equals(s.getIdentifier()));

		if (!songsToStart.isEmpty())
			for (String s : songsToStart)
				load(s);

	}

	public static void playAtF(String songName, int frames){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.playAtF(frames);
				return;
			}
	}

	public static void playAtMu(String songName, int microseconds){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.playAtMu(microseconds);
				return;
			}
	}

	public static void loops(String songName,boolean loops){
		for (Sound s: sounds)
			if (s.identifier.equals(songName)) {
				s.loops(loops);
				return;
			}
	}

	public static void loopsAtF(String songName, int frames){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.loopAt(frames);
				return;
			}
	}

	public static void loopsAtFSE(String songName, int framesS,int framesE){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.loopAt(framesS,framesE);
				return;
			}
	}

	public static void stop(String songName){
		for (Sound s: sounds)
			if (s.identifier.equals(songName)) {
				s.stop();
				return;
			}
	}

	public static void stopAll(){
		for (Sound s : sounds){
			s.stop();
		}
	}

	public static void setAtMu(String songName,long microseconds){
		for (Sound s: sounds)
			if (s.identifier.equals(songName)) {
				s.setAtMu(microseconds);
				return;
			}
	}

	public static void setAtF(String songName, int frames){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				s.setAtF(frames);
				return;
			}
	}

	public static int getFPosision(String songName){
		for (Sound s : sounds)
			if (s.identifier.equals(songName)) {
				return s.getFPosition();
			}
		return -1;
	}

	public static long getMuPosition(String songName){
		for (Sound s: sounds)
			if (s.identifier.equals(songName)) {
				return s.getMuPosition();
			}
		return -1;
	}

	public static Sound getSound(String songName){
		for (Sound s: sounds)
			if (s.identifier.equals(songName))
				return s;

		return null;
	}

	public static void setVolume(String songName,float volume){
		for (Sound s: sounds)
			if (s.identifier.equals(songName))
				s.setVolume(volume);

	}




	public static class Sound{
		Clip clip;
		File file;
		String identifier;
		boolean willSetLoop;
		int startLoopAt;

		Sound(String songPath){
			identifier = songPath;
			file = new File("SoundMedia//"+songPath+".wav");
		}

		Sound(String songPath, boolean loops){
			identifier = songPath;
			file = new File("SoundMedia//"+songPath+".wav");
			willSetLoop = loops;
		}

		Sound(String songPath, boolean loops,int startLoopAt){
			identifier = songPath;
			file = new File("SoundMedia//"+songPath+".wav");
			willSetLoop = loops;
			this.startLoopAt = startLoopAt;
		}

		String getIdentifier(){return identifier;}


		void open(){
			try {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(sound);
				clip.loop(willSetLoop ? -1 : 0);
				clip.setLoopPoints(startLoopAt,-1);
				setVolume(Settings.getVolume());
			}
			catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}

		void loops(boolean loops){clip.loop(loops ? -1 : 0);}

		void loopAt(int start,int end){clip.setLoopPoints(start,end);}
		void loopAt(int start){clip.setLoopPoints(start,-1);}

		void play(){
			clip.setFramePosition(0);
			clip.start();
		}

		void playAtF(int startFrame){
			clip.setFramePosition(startFrame);
			clip.start();
		}

		void playAtMu(int startMicrosecond){
			clip.setFramePosition(startMicrosecond);
			clip.start();
		}

		void stop(){clip.stop();}

		void setAtF(int frames){clip.setFramePosition(frames);}
		void setAtMu(long microseconds){clip.setMicrosecondPosition(microseconds);}

		int getFPosition(){return clip.getFramePosition();}
		long getMuPosition(){return clip.getMicrosecondPosition();}

		void setVolume(float volume){
			((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20 * (float) Math.log10(volume/100));
		}

	}









}
