package game.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {

	private List<Clip> removeBuffer;
	private List<Clip> currentlyPlaying;
	private List<Clip> queue;
	private List<Integer> loops;

	private Clip currentClip;
	private int currentLoops;

	private int pausedFrame;
	private boolean forcedStop;

	public AudioPlayer() {
		removeBuffer = new ArrayList<>();
		currentlyPlaying = new ArrayList<>();
		queue = new ArrayList<>();
		loops = new ArrayList<>();
	}

	public AudioPlayer(String startClip) {
		this(startClip, 1);
	}

	public AudioPlayer(String startClip, int startloop) {
		removeBuffer = new ArrayList<>();
		currentlyPlaying = new ArrayList<>();
		queue = new ArrayList<>();
		loops = new ArrayList<>();

		currentClip = AudioHandler.getMusicWav(startClip);
		setVolume(currentClip, -20f);
		currentLoops = startloop;

		pausedFrame = 0;
		forcedStop = false;

		currentClip.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP && !forcedStop) {

					if (currentLoops > 0 || currentLoops == Clip.LOOP_CONTINUOUSLY) {
						setVolume(currentClip, -20f);
						currentClip.setFramePosition(0);
						currentClip.start();

						if (currentLoops > 0) currentLoops -= 1;
					} else if (currentLoops == 0) {

						currentClip = queue.get(0);
						currentLoops = loops.get(0);

						setVolume(currentClip, -20f);
						currentClip.setFramePosition(0);
						currentClip.start();

						queue.remove(0);
						loops.remove(0);
					}
				}
			}
		});
	}

	/**
	 * Starts current {@link Clip} from its beginning
	 */
	public void start() {
		currentClip.start();

		if (!forcedStop && currentLoops != Clip.LOOP_CONTINUOUSLY) currentLoops--;
		forcedStop = false;
	}

	/**
	 * Stops current {@link Clip}. Current position is not safed.
	 */
	public void stop() {
		pausedFrame = 0;
		forcedStop = true;
		currentClip.stop();
	}

	/**
	 * Stops current {@link Clip}. It will resume where it stopped.
	 */
	public void pause() {
		pausedFrame = currentClip.getFramePosition();
		forcedStop = true;
		currentClip.stop();
	}

	/**
	 * Plays current {@link Clip} at the position where it stopped.
	 */
	public void resume() {
		currentClip.setFramePosition(pausedFrame);
		start();
	}

	public void next() {
		currentClip = queue.get(0);
		currentLoops = loops.get(0);

		setVolume(currentClip, -20f);
		currentClip.setFramePosition(0);
		currentClip.start();

		queue.remove(0);
		loops.remove(0);
	}

	/**
	 * Plays a {@link Clip} once
	 *
	 * @param audioName to get AudioFile from {@link AudioHandler}
	 */
	public void playAudio(String audioName) {
		Clip c = AudioHandler.getMusicWav(audioName);
		setVolume(c, -20f);
		c.setFramePosition(0);
		c.start();

		currentlyPlaying.add(c);
		c.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {

					removeBuffer.add(c);
				}
			}
		});
	}

	/**
	 * Loops a {@link Clip} multiple times
	 *
	 * @param audioName to get AudioFile from {@link AudioHandler}
	 * @param loop      Number of loops
	 */
	public void loopAudio(String audioName, int loop) {
		Clip c = AudioHandler.getMusicWav(audioName);
		setVolume(c, -20f);
		c.loop(loop);

		currentlyPlaying.add(c);
		c.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					removeBuffer.add(c);
					c.close();
				}
			}
		});
	}

	/**
	 * Adds a {@link Clip} to the playing queue
	 *
	 * @param audioName to get AudioFile from {@link AudioHandler}
	 */
	public void addMusic(String audioName) {
		addMusic(audioName, 1);
	}

	/**
	 * Adds a {@link Clip} to the playing queue to be played multiple times
	 *
	 * @param audioName to get AudioFile from {@link AudioHandler}
	 * @param loop      Number of loops
	 */
	public void addMusic(String audioName, int loop) {
		queue.add(AudioHandler.getMusicWav(audioName));
		loops.add(loop);
	}

	/**
	 * Sets the volume of a {@link Clip} to the given value
	 *
	 * @param c      Clip which volume should be updated
	 * @param volume the new volume
	 */
	private void setVolume(Clip c, float volume) {
		FloatControl fc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		fc.setValue(volume);
	}

	/**
	 * Updates volume for the current music {@link Clip} and all effects
	 */
	public void updateVolume() {
		setVolume(currentClip, -20f);
		for (Clip c : removeBuffer) {
			currentlyPlaying.remove(c);
		}
		removeBuffer.clear();

		for (Clip c : currentlyPlaying) {
			setVolume(c, -20f);
		}
	}
}