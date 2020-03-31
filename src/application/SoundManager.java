package application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class SoundManager {
	private HashMap<String, Sound> medias;
	private static final String path = "/sounds/";

	public SoundManager() {
		medias = new HashMap<String, Sound>();
		medias.put("menu", new Sound("menu.mp3"));
		medias.put("drop", new Sound("drop.wav"));
		medias.put("tetris", new Sound("tetris.wav"));
		medias.put("lineClean", new Sound("lineClean.wav"));
		medias.put("gameOver", new Sound("gameOver.wav"));
		medias.put("start", new Sound("start.wav"));
		medias.put("inGame", new Sound("inGame.mp3"));
		medias.put("restart", new Sound("restart.wav"));
		medias.put("panic", new Sound("panic.wav"));
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
		medias.get(soundName).play();
	}

	public void stop(String soundName) {
		medias.get(soundName).stop();
	}
	
	public void autoPlay(String soundName) {
		medias.get(soundName).autoPlay();
	}
	
	public void playAfterThis(String first, String second) {
		medias.get(first).getMediaPlayer().setOnEndOfMedia(() -> {
			medias.get(second).getMediaPlayer().play();
		});
	}
	
	public void autoPlayAfterThis(String first, String second) {
		medias.get(first).play();
		medias.get(first).getMediaPlayer().setOnEndOfMedia(() -> {
			medias.get(second).autoPlay();
		});
	}
	
	public void stopAll() {
		Iterator<Entry<String, Sound>> it = medias.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.stop();
		}
	}
	
	public void mute(String name) {
		medias.get(name).getMediaPlayer().setVolume(0);
	}
	
	public void pauseAll() {
		Iterator<Entry<String, Sound>> it = medias.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.pause();
		}
	}
	
	public void pause(String name) {
		medias.get(name).pause();
	}
	
	public void playAll() {
		Iterator<Entry<String, Sound>> it = medias.entrySet().iterator();	
		while(it.hasNext()) {
			Entry<String, Sound> pair = it.next();
			Sound sound = (Sound) pair.getValue();
			sound.play();
		}
	}
	
}


