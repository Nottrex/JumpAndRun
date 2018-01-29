package game.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AudioHandler {
	private static Map<String, Clip> audio_wav;

	static {
		audio_wav = new HashMap<>();
		loadMusicWav();
	}

	private static void loadMusicWav() {
		Sound[] sounds = Sound.values();

		for (Sound sound: sounds) {
			String s = sound.fileName;
			Clip clip = null;
			try {
				clip = AudioSystem.getClip(null);
				clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(ClassLoader.getSystemResourceAsStream("res/files/audio/" + s + ".wav"))));
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}

			audio_wav.put(s, clip);
		}
	}

	public static Clip getMusicWav(String audioName) {
		return audio_wav.get(audioName);
	}

	public static void unloadMusicWav(String audioName) {
		audio_wav.remove(audioName);
	}
}