package game.audio;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceID;

	public Source() {
		this(0, 0);
	}

	public Source(float x, float y) {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, 0);
	}

	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
}
