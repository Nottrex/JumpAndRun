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
import java.util.Map;

public class AudioHandler {
	private static Map<String, Integer> audio_buffer;

	static {
		audio_buffer = new HashMap<>();
		loadMusicWav();
	}

	private static void loadMusicWav() {
		Sound[] sounds = Sound.values();

		for (Sound sound: sounds) {
			String s = sound.fileName;

			int buffer = AL10.alGenBuffers();
			WaveData waveFile = WaveData.create(s);
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();

			audio_buffer.put(s.toLowerCase(), buffer);
		}
	}

	public static int getMusicWav(String audioName) {
		return audio_buffer.get(audioName);
	}

	public static void unloadMusicWav(String audioName) {
		AL10.alDeleteBuffers(audio_buffer.get(audioName));
		audio_buffer.remove(audioName);
	}
}