package game.audio;

import org.lwjgl.openal.AL10;

public class Source {

	private float musicVolumeMultiplier;
	private boolean deleteOnFinish; //If this source should be deleted on finishing a replay of the sound
	private int sourceID;			//ID of the source used to change attributes

	/**
	 * Creates new Audio Source at (0, 0, 0)
	 */
	public Source(boolean deleteOnFinish) {
		this(0, 0, deleteOnFinish);
	}

	/**
	 * Creates new Audio Source at (x, y, 0)
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 */
	public Source(float x, float y, boolean deleteOnFinish) {
		this(x, y, .25f, deleteOnFinish);
	}

	/**
	 * Creates new Audio Source at (x, y, 0) and sets its volume to v
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 * @param v volume of Source
	 */
	public Source(float x, float y, float v, boolean deleteOnFinish) {
		this(x, y, 0, v, deleteOnFinish);
	}


	/**
	 * Creates new Audio Source at (x, y, z) and sets its volume to v
	 *
	 * @param x x-Coordinate of Source
	 * @param y y-Coordinate of Source
	 * @param z z-Coordinate of Source
	 * @param v volume of Source
	 */
	public Source(float x, float y, float z, float v, boolean deleteOnFinish) {
		sourceID = AL10.alGenSources();
		setVolume(v);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		setPosition(x, y, z);

		this.deleteOnFinish = deleteOnFinish;
	}

	/**
	 * @return if this source should be deleted after finishing a play of the sound
	 */
	public boolean shouldDeleteOnFinish() {
		return deleteOnFinish;
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
	 * Gets if source is playing something
	 *
	 * @return if source is playing sound
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void setMusicVolumeMultiplier(float musicVolumeMultiplier) {
		this.musicVolumeMultiplier = musicVolumeMultiplier;
	}

	public float getMusicVolumeMultiplier() {
		return musicVolumeMultiplier;
	}

	/**
	 * Deletes this source
	 */
	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
}
