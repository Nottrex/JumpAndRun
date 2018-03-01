package game.audio;

import game.Options;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {

	private List<Source> afx,toRemove;
	private Source musicSource;

	public AudioPlayer() {
		afx = new ArrayList<>();
		toRemove = new ArrayList<>();

		musicSource = new Source();
		musicSource.setVolume(Options.musicVolume);
		musicSource.setLooping(true);
	}
	public Source getMusicSource() {
		return musicSource;
	}

	public void playAfx(Sound afx) {
		playAfx(new Source(0, 0, 0, Options.effectVolume), afx);
	}

	public void playAfx(Source c, Sound afx) {
		this.afx.add(c);
		c.play(afx);
		c.setVolume(Options.effectVolume);
	}

	public void playAfx(Sound afx, float x, float y, float z) {
		playAfx(new Source(x, y, z, Options.effectVolume), afx);
	}

	public void playAfx(String afx) {
		playAfx(new Source(0, 0, 0, Options.effectVolume), afx);
	}

	public void playAfx(Source c, String afx) {
		this.afx.add(c);
		c.play(afx);
		c.setVolume(Options.effectVolume);
	}

	public void playAfx(String afx, float x, float y, float z) {
		playAfx(new Source(x, y, z, Options.effectVolume), afx);
	}

	public void update() {
		musicSource.setVolume(Options.musicVolume);
		for (Source c : afx) {
			if(!c.isPlaying()) {
				toRemove.add(c);
			}
		}

		for(Source c: toRemove) {
			afx.remove(c);
			c.stop();
			c.delete();
		}

		toRemove.clear();

		for (Source c : afx) {
			c.setVolume(Options.effectVolume);
		}
	}
}