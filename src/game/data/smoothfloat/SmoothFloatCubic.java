package game.data.smoothfloat;

import game.util.TimeUtil;

/**
 * An implementation of SmoothFloat using cubic interpolation
 */
public class SmoothFloatCubic implements SmoothFloat {

	private float tValue;			//current value
	private float targetValue;
	private long beginTime, targetTime;
	private float a, b, c, d;		//values for the cubic function of the transition
	private boolean z;				//is there a transition
	private boolean z2;				//has the value changed

	public SmoothFloatCubic(float value) {
		tValue = value;
		targetValue = tValue;

		beginTime = 0;
		targetTime = 0;
		a = 0;
		b = 0;
		c = 0;
		d = 0;
		z = false;
		z2 = true;
	}

	@Override
	public void set(float value) {
		z = false;
		z2 = true;
		tValue = value;
	}

	@Override
	public void setSmooth(float value, long time) {
		float v = 0;
		if (z) {
			v = calculateCubicDerivative(((float) ((TimeUtil.getTime()) - beginTime)) / (targetTime - beginTime), a, b, c, d);
		}
		targetValue = value;

		d = tValue;
		c = v;
		b = 3 * targetValue - 2 * v - 3 * tValue;
		a = v + 2 * tValue - 2 * targetValue;
		beginTime = TimeUtil.getTime();
		targetTime = beginTime + time;

		z = true;
	}

	@Override
	public float get() {
		return tValue;
	}

	@Override
	public boolean update(long time) {
		boolean change = z || z2;

		if (z) {
			if (time > targetTime) {
				tValue = targetValue;
				z = false;
			} else {
				tValue = calculateCubicFunction(((float) (time - beginTime)) / (targetTime - beginTime), a, b, c, d);
			}
		}

		z2 = false;

		return change;
	}

	private static float calculateCubicFunction(float x, float a, float b, float c, float d) {
		return a * x * x * x + b * x * x + c * x + d;
	}

	private static float calculateCubicDerivative(float x, float a, float b, float c, float d) {
		return 3 * a * x * x + 2 * b * x + c;
	}
}
