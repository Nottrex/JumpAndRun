package game.window;

import game.util.MathUtil;
import game.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class LightHandler {

	private List<Light> lights;
	private float minimumBrightness;
	private float tMinimumBrightness;
	private float targetMinimumBrightness;
	private long beginTime = 0, targetTime = 0;
	private float a, b, c, d;
	private boolean z = false;

	private boolean changed;

	public LightHandler() {
		changed = true;

		lights = new ArrayList<>();
		minimumBrightness = 0.5f;
		tMinimumBrightness = minimumBrightness;
	}

	public boolean update() {
		boolean change = changed || tMinimumBrightness != minimumBrightness || z;
		for (Light light: lights) {
			change |= light.updateLight();
		}

		long time = TimeUtil.getTime() % 10000000;

		if (z) {
			if (time > targetTime) {
				tMinimumBrightness = targetMinimumBrightness;
				z = false;
			} else {
				tMinimumBrightness = MathUtil.calculateCubicFunction((time * 1.0f - beginTime) / (targetTime - beginTime), a, b, c, d);
			}
		}

		minimumBrightness = tMinimumBrightness;

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
		this.tMinimumBrightness = minimumBrightness;
		z = false;
	}

	public void setMinimumBrightnessSmooth(float brightness, long time) {
		float v = 0;
		float t = brightness;
		if (z) {
			v = MathUtil.calculateCubicDerivative(((TimeUtil.getTime() % 10000000) * 1.0f - beginTime) / (targetTime - beginTime), a, b, c, d);
		}
		float currentZoom = tMinimumBrightness;

		d = currentZoom;
		c = v;
		b = 3 * t - 2 * v - 3 * currentZoom;
		a = v + 2 * currentZoom - 2 * t;
		beginTime = TimeUtil.getTime() % 10000000;
		targetTime = TimeUtil.getTime() % 10000000 + time;
		targetMinimumBrightness = t;

		z = true;
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
