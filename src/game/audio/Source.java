package game.audio;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceID;

	public Source() {
		this(0, 0);
	}

	public Source(float x, float y) {
		this(x, y,  .25f);
	}

	public Source(float x, float y, float v) {
		this(x, y, 0, v);
	}

	public Source(float x, float y, float z, float v) {
		sourceID = AL10.alGenSources();
		setVolume(v);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		setPosition(x, y, z);
	}

	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	public void play(String bufferName) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, AudioHandler.getMusicWav(bufferName));
		AL10.alSourcePlay(sourceID);
	}

	public void play(Sound bufferName) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, AudioHandler.getMusicWav(bufferName));
		AL10.alSourcePlay(sourceID);
	}

	public void pause() {
		AL10.alSourcePause(sourceID);
	}

	public void resume() {
		AL10.alSourcePlay(sourceID);
	}

	public void stop() {
		AL10.alSourceStop(sourceID);
	}

	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}

	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void delete() {
		if (isPlaying()) {
			AudioHandler.removeSource(this);
		} else {
			AL10.alDeleteSources(sourceID);
		}
	}
}
