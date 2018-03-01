package game.audio;

import org.lwjgl.openal.AL10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioHandler {
	private static Map<String, WaveAudio> audio_buffer;
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

			WaveAudio waveFile = WaveAudio.create("res/files/audio/" + s + ".wav");
			audio_buffer.put(s, waveFile);
		}
	}

	public static int getMusicWav(String audioName) {
		WaveAudio waveFile = audio_buffer.get(audioName);
		int buffer = AL10.alGenBuffers();
		AL10.alBufferData(buffer, waveFile.getFormat(), waveFile.getData(), waveFile.getSampleRate());
		waveFile.dispose();
		return buffer;
	}

	public static int getMusicWav(Sound audio) {
		WaveAudio waveFile = audio_buffer.get(audio.fileName);
		int buffer = AL10.alGenBuffers();
		AL10.alBufferData(buffer, waveFile.getFormat(), waveFile.getData(), waveFile.getSampleRate());
		waveFile.dispose();
		return buffer;
	}

	public static void unloadMusicWav(String audioName) {
		audio_buffer.remove(audioName);
	}

	public static void cleanUp() {
		for(int i: currentBuffers) AL10.alDeleteBuffers(i);
	}
}