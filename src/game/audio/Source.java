package game.audio;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceID;			//ID of the source used to change attributes

	/**
	 * Creates new Audio Source at (0, 0, 0)
	 */
	public Source() {
		this(0, 0);
	}

	/**
	 * Creates new Audio Source at (x, y, 0)
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 */
	public Source(float x, float y) {
		this(x, y, .25f);
	}

	/**
	 * Creates new Audio Source at (x, y, 0) and sets its volume to v
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 * @param v volume of Source
	 */
	public Source(float x, float y, float v) {
		this(x, y, 0, v);
	}


	/**
	 * Creates new Audio Source at (x, y, z) and sets its volume to v
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 * @param z z-Coordinate of Source
	 * @param v volume of Source
	 */
	public Source(float x, float y, float z, float v) {
		sourceID = AL10.alGenSources();
		setVolume(v);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		setPosition(x, y, z);
	}

	/**
	 * Plays a track
	 *
	 * @param buffer buffer of the track that should play
	 */
	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	/**
	 * Plays a track
	 *
	 * @param bufferName Name of the Sound that should be played
	 */
	public void play(String bufferName) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, AudioHandler.getMusicWav(bufferName));
		AL10.alSourcePlay(sourceID);
	}

	/**
	 * Plays a track
	 *
	 * @param bufferName Sound that should be played
	 */
	public void play(Sound bufferName) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, AudioHandler.getMusicWav(bufferName));
		AL10.alSourcePlay(sourceID);
	}


	/**
	 * Pauses current playing sound
	 */
	public void pause() {
		AL10.alSourcePause(sourceID);
	}


	/**
	 * Resumes playing current sound
	 */
	public void resume() {
		AL10.alSourcePlay(sourceID);
	}


	/**
	 * Stops current track
	 */
	public void stop() {
		AL10.alSourceStop(sourceID);
	}


	/**
	 * Changes the volume
	 *
	 * @param volume value of volume
	 */
	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}


	/**
	 * Changes position of the source
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 * @param z z-Coordinate of Source
	 */
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}


	/**
	 * Enables/ Disables sound-looping
	 *
	 * @param loop true if sound should loop, false if sound should not loop
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}


	/**
	 * Gets if soure is playing somethin
	 *
	 * @return if source is playing sound
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	/**
	 * Deletes this source
	 */
	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
}
