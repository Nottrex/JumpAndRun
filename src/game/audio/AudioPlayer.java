package game.audio;

import game.Options;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {

	private List<Source> afx, toRemove;
	private Source musicSource;

	public AudioPlayer() {
		afx = new ArrayList<>();
		toRemove = new ArrayList<>();

		musicSource = new Source(false);
		musicSource.setVolume(Options.musicVolume);
		musicSource.setLooping(true);
	}

	public Source getMusicSource() {
		return musicSource;
	}

	public void playAfx(Sound afx, boolean deleteOnFinish) {
		playAfx(new Source(0, 0, 0, Options.effectVolume, deleteOnFinish), afx);
	}

	public void playAfx(Source c, Sound afx) {
		this.afx.add(c);
		c.play(afx);
		c.setVolume(Options.effectVolume);
	}

	public void playAfx(Sound afx, float x, float y, float z, boolean deleteOnFinish) {
		playAfx(new Source(x, y, z, Options.effectVolume, deleteOnFinish), afx);
	}

	public void playAfx(String afx, boolean deleteOnFinish) {
		playAfx(new Source(0, 0, 0, Options.effectVolume, deleteOnFinish), afx);
	}

	public void playAfx(Source c, String afx) {
		this.afx.add(c);
		c.play(afx);
		c.setVolume(Options.effectVolume);
	}

	public void playAfx(String afx, float x, float y, float z, boolean deleteOnFinish) {
		playAfx(new Source(x, y, z, Options.effectVolume, deleteOnFinish), afx);
	}

	public void update() {
		musicSource.setVolume(Options.musicVolume);
		for (int i = 0; i < afx.size(); i++) {
			Source c = afx.get(i);
			if (!c.isPlaying()) {
				toRemove.add(c);
			}
		}

		for (int i = 0; i < toRemove.size(); i++) {
			Source c = toRemove.get(i);
			afx.remove(c);
			c.stop();
			if (c.shouldDeleteOnFinish()) c.delete();
		}

		toRemove.clear();

		for (int i = 0; i < afx.size(); i++) {
			Source c = afx.get(i);
			c.setVolume(Options.effectVolume);
		}
	}
}