package Misc;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.*;

public class SoundManager {
	HashMap<String, Clip> clips;
	private Set<String> loopingClips;
	private Set<String> pausedClips;
	private boolean isPaused;

	private static SoundManager instance = null;	

	private float volume;

	private SoundManager () {

		Clip clip;

		clips = new HashMap<String, Clip>();
		loopingClips = new HashSet<>();
		pausedClips = new HashSet<>();
		isPaused = false;

		clip = loadClip("sounds/bgm.wav");
		clips.put("bgm", clip);

		clip = loadClip("sounds/mainmenu.wav");
		clips.put("mainmenu", clip);

		clip = loadClip("sounds/death.wav");
		clips.put("game-over", clip);

		clip = loadClip("sounds/start.wav");
		clips.put("start", clip);

		clip = loadClip("sounds/explosion.wav");
		clips.put("explosion", clip);

		clip = loadClip("sounds/canister.wav");
		clips.put("canister", clip);

		clip = loadClip("sounds/gem.wav");
		clips.put("powergem", clip);

		clip = loadClip("sounds/startofboss.wav");
		clips.put("incoming-boss", clip);

		clip = loadClip("sounds/boss.wav");
		clips.put("boss", clip);

		clip = loadClip("sounds/winning.wav");
		clips.put("winning", clip);

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
			if (looping) {
				loopingClips.add(title);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				loopingClips.remove(title);
				clip.start();
			}
		}
	}
	
	public void stopClip(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
			loopingClips.remove(title);
			pausedClips.remove(title);
		}
	}

	public void pauseAll() {
		isPaused = true;
		pausedClips.clear();
		for (String title : clips.keySet()) {
			Clip clip = clips.get(title);
			if (clip != null && clip.isRunning()) {
				pausedClips.add(title);
				clip.stop();
			}
		}
	}

	public void resumeAll() {
		isPaused = false;
		for (String title : pausedClips) {
			resumeClip(title);
		}
		pausedClips.clear();
	}

	public void resumeClip(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			if (loopingClips.contains(title)) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		}
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void stopAll() {
		for (String title : clips.keySet()) {
			Clip clip = clips.get(title);
			if (clip != null) {
				clip.stop();
			}
		}
		loopingClips.clear();
		pausedClips.clear();
		isPaused = false;
	}

	public void playAfter(String firstTitle, String nextTitle, boolean loopNext) {
		Clip firstClip = getClip(firstTitle);
		if (firstClip == null) return;

		LineListener listener = new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP && !isPaused) {
					firstClip.removeLineListener(this);
					playClip(nextTitle, loopNext);
				}
			}
		};

		firstClip.addLineListener(listener);
		playClip(firstTitle, false);
	}
}