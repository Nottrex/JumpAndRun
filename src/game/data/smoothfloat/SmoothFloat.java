package game.data.smoothfloat;

/**
 * Interpolation of float values for smooth transitions
 */
public interface SmoothFloat {

	/**
	 * instantly sets the value of this float
	 * @param value the target value
	 */
	void set(float value);

	/**
	 * sets the value to be changed over time
	 * @param value the target value
	 * @param time total time of the transition (in ms)
	 */
	void setSmooth(float value, long time);

	/**
	 * Updates the value
	 * @param time the current time (in ms)
	 * @return if the value has changed
	 */
	boolean update(long time);

	/**
	 * @return the current value
	 */
	float get();
}
