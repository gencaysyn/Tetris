package application;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class SoundManager {
	private HashMap<String, Sound> musics;
	private HashMap<String, AudioClip> effects;
	private static final String path = "/sounds/";

	public SoundManager() {
		musics = new HashMap<String, Sound>();
		musics.put("menu", new Sound("menu.mp3"));
		musics.put("gameOver", new Sound("gameOver.wav"));
		musics.put("start", new Sound("start.wav"));
		musics.put("inGame", new Sound("inGame.mp3"));
		musics.put("restart", new Sound("restart.wav"));
		musics.put("panic", new Sound("panic.wav"));
		
		effects = new HashMap<>();
		effects.put("drop", new AudioClip(getClass().getResource("/sounds/drop.wav").toString()));
		effects.put("tetris", new AudioClip(getClass().getResource("/sounds/tetris.wav").toString()));
		effects.put("lineClean", new AudioClip(getClass().getResource("/sounds/lineClean.wav").toString()));
	}
	
	private class Sound {
		private Media media;
		private MediaPlayer mediaPlayer;
		
		public Sound(String name) {	
			try {
				media = new Media(Main.class.getResource(path+name).toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.seek(Duration.ZERO);
			} catch (Exception e) {
				System.err.println("File not found!");
				e.printStackTrace();
				System.exit(0);
			}
			
			if(name.equals("menu.mp3") || name.equals("inGame.mp3"))
				mediaPlayer.setVolume(0.1);
			if(name.equals("drop.wav"))
				mediaPlayer.setVolume(0.5);
				
		}
		
		public void play() {
			mediaPlayer.stop();
			mediaPlayer.seek(Duration.ZERO);
			mediaPlayer.play();
		}
		
		public void stop() {
			mediaPlayer.stop();
		}

		public void autoPlay() {
			mediaPlayer.setOnEndOfMedia(new Runnable() {
			       public void run() {
			    	   mediaPlayer.seek(Duration.ZERO);
				       }
				   });
			mediaPlayer.play();
			
		}

		public MediaPlayer getMediaPlayer() {
			return mediaPlayer;
		}
		
		public void pause() {
			if(mediaPlayer.getStatus() == Status.PLAYING)
				mediaPlayer.pause();
		}
		
		
	}

	public void play(String soundName) {
		musics.get(soundName).play();
	}

	public void stop(String soundName) {
		musics.get(soundName).stop();
	}
	
	public void autoPlay(String soundName) {
		musics.get(soundName).autoPlay();
	}
	
	public void playAfterThis(String first, String second) {
		musics.get(first).getMediaPlayer().setOnEndOfMedia(() -> {
			musics.get(second).getMediaPlayer().play();
		});
	}
	
	public void autoPlayAfterThis(String first, String second) {
		musics.get(first).play();
		musics.get(first).getMediaPlayer().setOnEndOfMedia(() -> {
			musics.get(second).autoPlay();
		});
	}
	
	public void stopAll() {
		Iterator<Entry<String, Sound>> it = musics.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.stop();
		}
	}
	
	public void mute(String name) {
		musics.get(name).getMediaPlayer().setVolume(0);
	}
	
	public void pauseAll() {
		Iterator<Entry<String, Sound>> it = musics.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.pause();
		}
	}
	
	public void pause(String name) {
		musics.get(name).pause();
	}
	
	public void playAll() {
		Iterator<Entry<String, Sound>> it = musics.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.play();
		}
	}
	
	public void playEffect(String name) {
		effects.get(name).play();
	}
	
}


