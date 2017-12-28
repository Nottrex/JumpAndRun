package game.util;

import java.util.Random;

public class MathUtil {

	public static float clamp(float x, float min, float max) {
		return Math.max(min, Math.min(max, x));
	}

	public static float calculateCubicFunction(float x, float a, float b, float c, float d) {
		return a * x * x * x + b * x * x + c * x + d;
	}

	public static float calculateCubicDerivative(float x, float a, float b, float c, float d) {
		return 3 * a * x * x + 2 * b * x + c;
	}

	public static float noise(int seed, float time, int STEP_SIZE) {
		int left = (int) Math.floor(time / STEP_SIZE);
		int right = (int) Math.ceil(time / STEP_SIZE);
		float d = (time % STEP_SIZE) / STEP_SIZE;

		float l = nextFloat(seed, left);
		float r = nextFloat(seed, right);

		float m = (1.0f - (float) Math.cos(d * Math.PI)) / 2.0f;
		return l * (1 - m) + r * m;
		//return 2*(r-l)*d*d*d + (l-r)*d*d + l;    //Cubic
		//return l + d * (r - l);                //Linear
	}

	private static float nextFloat(int seed, int seed2) {
		if (seed2 == 0) return 0.5f;

		Random r1 = new Random(seed);
		r1.nextLong();

		Random r2 = new Random(seed2);
		r2.nextLong();
		r2.nextLong();

		Random r3 = new Random(r1.nextLong() ^ r2.nextLong());
		r3.nextFloat();

		return r3.nextFloat();
	}
}
