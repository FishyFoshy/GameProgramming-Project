package Misc;
import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
	HashMap<String, Clip> clips;
	HashMap<Clip, Boolean> started;

	private static SoundManager instance = null;	

	private float volume;

	private SoundManager () {

		Clip clip;

		clips = new HashMap<String, Clip>();
		started = new HashMap<Clip, Boolean>();

		clip = loadClip("sounds/bgm.wav");
		clips.put("bgm", clip);
		started.put(clip, false);
		
		clip = loadClip("sounds/start.wav");
		clips.put("start", clip);
		started.put(clip, false);
		
		clip = loadClip("sounds/mainmenu.wav");
		clips.put("mainmenu", clip);
		started.put(clip, false);
		
		clip = loadClip("sounds/collect.wav");
		clips.put("collect", clip);
		started.put(clip, false);
		
		clip = loadClip("sounds/over.wav");
		clips.put("game-over", clip);
		started.put(clip, false);

		volume = 1.0f;
	}


	public static SoundManager getInstance() {
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		

	public Clip loadClip (String fileName) {
		AudioInputStream audioIn;
		Clip clip = null;

		try {
			File file = new File(fileName);
			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		}
		catch (Exception e) {
			System.out.println ("Error opening sound files: " + e);
		}
		return clip;
	}

	public Clip getClip (String title) {
		return clips.get(title);
	}

	public void playClip(String title, boolean looping) {
		Clip clip = getClip(title);
		if (clip != null) {
			if (clip.isRunning())
				return;
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
		started.replace(clip, true);
	}
	
	public void stopClip(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
		started.replace(clip, false);
	}

	public void pauseClip() {
		for (Map.Entry<String, Clip> entry : clips.entrySet()) {
			String key = entry.getKey();
			Clip clip = entry.getValue();

			if (started.get(clip)) {
				clip.stop();
			}
			
			if (clip != null) {
				
			}
		}
	}

	public void resumeClip() {
		for (Map.Entry<String, Clip> entry : clips.entrySet()) {
			String key = entry.getKey();
			Clip clip = entry.getValue();

			if(started.get(clip)){
				clip.start();;
			}
			if (clip != null) {
				
			}
		}
	}
}