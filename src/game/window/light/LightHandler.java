package game.window.light;

import game.data.smoothfloat.SmoothFloat;
import game.data.smoothfloat.SmoothFloatCubic;
import game.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class LightHandler {

	private List<Light> lights;					//all lights
	private float minimumBrightness;			//the current default brightness of the pixels
	private SmoothFloat tMinimumBrightness;		//the theoretical default brightness of the pixels

	private boolean changed;					//has something changed since the last update

	public LightHandler() {
		changed = true;

		lights = new ArrayList<>();
		minimumBrightness = 0.4f;
		tMinimumBrightness = new SmoothFloatCubic(minimumBrightness);
	}

	/**
	 * updates the light
	 *
	 * @return if something has changed
	 */
	public boolean update() {
		boolean change = changed;

		for (Light light : lights) {
			change |= light.updateLight();		//Update lights
		}

		long time = TimeUtil.getTime();
		change |= tMinimumBrightness.update(time);

		minimumBrightness = tMinimumBrightness.get();

		changed = false;

		return change;
	}

	/**
	 * adds a light to the game
	 *
	 * @param light to be added
	 */
	public void addLight(Light light) {
		lights.add(light);
		changed = true;
	}

	/**
	 * removes a light from the game
	 *
	 * @param light to be removed
	 */
	public void removeLight(Light light) {
		lights.remove(light);
		changed = true;
	}

	/**
	 * sets the minimum brightness instantly
	 *
	 * @param minimumBrightness the new minimumBrightness
	 */
	public void setMinimumBrightness(float minimumBrightness) {
		tMinimumBrightness.set(minimumBrightness);
	}

	/**
	 * sets the minimum brightness by interpolating between the current and the target values over time
	 *
	 * @param minimumBrightness the target minimumBrightness
	 * @param time              the time for the interpolations
	 */
	public void setMinimumBrightnessSmooth(float minimumBrightness, long time) {
		tMinimumBrightness.setSmooth(minimumBrightness, time);
	}

	/**
	 * @return the current minimumBrightness
	 */
	public float getMinimumBrightness() {
		return minimumBrightness;
	}

	/**
	 * @return the current amount of lights
	 */
	public int getLightAmount() {
		return lights.size();
	}

	/**
	 * @return a two dimensional array of all light positions
	 */
	public float[][] getLights() {
		float[][] light = new float[lights.size()][3];

		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).getLightPosition(light[i]);
		}

		return light;
	}

	/**
	 * @return a two dimensional array of all light colors
	 */
	public float[][] getLightColors() {
		float[][] light = new float[lights.size()][3];

		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).getLightColor(light[i]);
		}

		return light;
	}
}
