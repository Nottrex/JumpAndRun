package game.util;

public class MathUtil {

	public static float clamp(float x, float min, float max) {
		return Math.max(min, Math.min(max, x));
	}
}
