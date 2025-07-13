package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.mygdx.game.Settings.*;

public class VideoManager {

	static ArrayList<Video> video = new ArrayList<>();
	static OnVariousScenarios oVE;

	static{
		oVE = new OnVariousScenarios(){
			@Override
			public void onVolumeChange() {
				for (Video v : video){
					v.setVolume(getVolume());
				}
			}

			@Override
			public void onTickStart() {
				renderVideo();
			}
		};
	}

	public static void createVideo(float x, float y){
		video.add(new Video(x,y));
		print("video stack is of " + video.size());
	}

	public static void renderVideo(){
		VideoManager.video.removeIf(video1 -> video1.hasCalledStop);
		for (Video v : video)
			if (v != null && !v.hasCalledStop)
				v.playVideo();
	}

	public static void stopVideo(Video video){
		for (Video v : VideoManager.video){
			if (v == video) {
				v.video.dispose();
				break;
			}
		}
	}

	public static void stopAll(){
		for (Video v : VideoManager.video)
			v.video.dispose();
		video.clear();
	}


	public static class Video {
		boolean hasCalledStop = false;
		VideoPlayer video;
		float x, y;
		public Video(float x, float y){
			this.x = x;
			this.y = y;
			video = VideoPlayerCreator.createVideoPlayer();
			FileHandle file = Gdx.files.internal("Video//featurepresentation.webm");
			video.setOnCompletionListener(new VideoPlayer.CompletionListener() {
				@Override
				public void onCompletionListener (FileHandle file) {
					stopVideo(thisVideo());
				}
			});
			try {video.load(file);} catch (FileNotFoundException ignored) {}
			setVolume(getVolume());
		}

		public void playVideo(){
			if (!hasCalledStop) {
				if (isVideoLoaded() && !video.isPlaying()) {
					video.play();
				}

				video.update();
				TextureManager.renderVideo(video.getTexture(), x, y);
			}
		}

		public boolean isVideoLoaded(){
			return video.isBuffered();
		}

		public Video thisVideo(){
			hasCalledStop = true;
			return this;
		}

		public void setVolume(float volume){
			video.setVolume(volume/100);
		}


	}
}
