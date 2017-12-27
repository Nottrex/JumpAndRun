package game.window;

import java.util.ArrayList;
import java.util.List;

public class LightHandler {

	private List<Light> lights;
	private boolean changed;

	public LightHandler() {
		lights = new ArrayList<>();
		changed = true;
	}

	public boolean update() {
		boolean change = changed;
		for (Light light: lights) {
			change |= light.updateLight();
		}

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
