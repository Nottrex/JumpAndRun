package game.audio;

import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioHandler {
	private static Map<String, WaveData> audio_buffer;
	private static List<Integer> currentBuffers;

	static {
		audio_buffer = new HashMap<>();
		currentBuffers = new ArrayList<>();
		loadMusicWav();
	}

	private static void loadMusicWav() {
		Sound[] sounds = Sound.values();

		for (Sound sound: sounds) {
			String s = sound.fileName;

			WaveData waveFile = WaveData.create("res/files/audio/" + s + ".wav");
			audio_buffer.put(s, waveFile);
		}
	}

	public static int getMusicWav(String audioName) {
		WaveData waveFile = audio_buffer.get(audioName);
		int buffer = AL10.alGenBuffers();
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public static int getMusicWav(Sound audio) {
		WaveData waveFile = audio_buffer.get(audio.fileName);
		int buffer = AL10.alGenBuffers();
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public static void unloadMusicWav(String audioName) {
		audio_buffer.remove(audioName);
	}

	public static void cleanUp() {
		for(int i: currentBuffers) AL10.alDeleteBuffers(i);
	}

	private static List<Source> toRemove = new ArrayList<>();
	public static void removeSource(Source source) {
		toRemove.add(source);
	}

	public static void update() {
		for (int i = 0; i < toRemove.size(); i++) {
			Source s = toRemove.get(i);
			if (!s.isPlaying()) {
				toRemove.remove(i);
				s.delete();
				i--;
			}
		}
	}
}