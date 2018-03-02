package game.util;

import java.util.Random;

/**
 * a small helper class for math related problems
 */
public class MathUtil {

	/**
	 * @param x   the value to be checked
	 * @param min the min allowed value
	 * @param max the max allowed value
	 * @return min for x < min, max for x > max or x otherwise
	 */
	public static float clamp(float x, float min, float max) {
		return Math.max(min, Math.min(max, x));
	}

	/**
	 * extends the modulo function to work properly with negative numbers
	 *
	 * @param value the value
	 * @param mod   the modulo
	 * @return the smallest positive float r, that fulfills:  r == value (modulo mod)
	 */
	public static float mod(float value, float mod) {
		while (value < 0) value += mod;
		return value % mod;
	}

	/**
	 * calculates a cosine interpolated noise
	 * generates an infinite function by creating random values between 0 and 1 for every multiple of STEP_SIZE
	 * and interpolates between them by the given value
	 *
	 * @param seed      determines the randomness -> same seed, same values
	 * @param time      the x value that is used to evaluate the noise
	 * @param STEP_SIZE the distance between random values
	 * @return the interpolated noise
	 */
	public static float noise(int seed, float time, float STEP_SIZE) {
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

	/**
	 * generates a random float from two seeds
	 * such that every change in any seed leads to a significant change in the value
	 * using the same seed leads to the same value
	 *
	 * @param seed  first seed
	 * @param seed2 second seed
	 * @return the generated float
	 */
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
