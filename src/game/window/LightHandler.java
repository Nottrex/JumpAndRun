package game.window;

import game.SmoothFloat;
import game.SmoothFloatCubic;
import game.util.MathUtil;
import game.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class LightHandler {

	private List<Light> lights;
	private float minimumBrightness;
	private SmoothFloat tMinimumBrightness;

	private boolean changed;

	public LightHandler() {
		changed = true;

		lights = new ArrayList<>();
		minimumBrightness = 0.5f;
		tMinimumBrightness = new SmoothFloatCubic(minimumBrightness);
	}

	public boolean update() {
		boolean change = changed;

		for (Light light: lights) {
			change |= light.updateLight();
		}

		long time = TimeUtil.getTime();
		change |= tMinimumBrightness.update(time);

		minimumBrightness = tMinimumBrightness.get();

		changed = false;

		return change;
	}

	public void addLight(Light light) {
		lights.add(light);
		changed = true;
	}

	public void removeLight(Light light) {
		lights.remove(light);
		changed = true;
	}

	public void setMinimumBrightness(float minimumBrightness) {
		tMinimumBrightness.set(minimumBrightness);
	}

	public void setMinimumBrightnessSmooth(float minimumBrightness, long time) {
		tMinimumBrightness.setSmooth(minimumBrightness, time);
	}

	public float getMinimumBrightness() {
		return minimumBrightness;
	}

	public int getLightAmount() {
		return lights.size();
	}

	public float[][] getLights() {
		float[][] light = new float[lights.size()][3];

		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).getLightPosition(light[i]);
		}

		return light;
	}

	public float[][] getLightColors() {
		float[][] light = new float[lights.size()][3];

		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).getLightColor(light[i]);
		}

		return light;
	}
}
