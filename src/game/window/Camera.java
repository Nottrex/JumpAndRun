package game.window;

import java.util.LinkedList;
import java.util.List;

public class Camera {
	private static final int TIME_FRAC = 25;
	private static final float MIN_AMP = 0.0001f;
	private static final float DECAY = 0.8f;

	public float zoom, x, y, tilt;

	private List<Screenshake> screenshakeList;

	private class Screenshake {
		private Screenshake(long startTime, float decay, float amp_x, float amp_y, float phase_x, float phase_y, float freq_x, float freq_y) {
			this.startTime = startTime;
			this.decay = decay;
			this.amp_x = amp_x;
			this.amp_y = amp_y;
			this.phase_x = phase_x;
			this.phase_y = phase_y;
			this.freq_x = freq_x;
			this.freq_y = freq_y;
		}

		long startTime;
		float decay;
		float amp_x, amp_y, phase_x, phase_y, freq_x, freq_y;
	}

	private float tx, ty;
	private boolean z2 = false;
	private float targetX, targetY;
	private long beginTime2 = 0, targetTime2 = 0;
	private float a2, b2, c2, d2;
	private float a3, b3, c3, d3;

	private float tzoom;
	private float targetZoom = tzoom;
	private long beginTime = 0, targetTime = 0;
	private float a, b, c, d;
	private boolean z = false;

	private float ttilt;
	private float targetTilt = ttilt;
	private long beginTime3 = 0, targetTime3 = 0;
	private float a4, b4, c4, d4;
	private boolean z3 = false;

	private int delayFrameAmount;
	private float[][] delayFrames;

	public Camera() {
		zoom = 0.1f;
		x = 0;
		y = 0;
		tilt = 0;
		delayFrameAmount = 0;
		delayFrames = new float[delayFrameAmount][4];
		for (int t = 0; t < delayFrameAmount; t++) {
			delayFrames[t][0] = zoom;
			delayFrames[t][1] = x;
			delayFrames[t][2] = y;
			delayFrames[t][3] = tilt;
		}
		tzoom = zoom;
		tx = x;
		ty = y;
		ttilt = tilt;

		screenshakeList = new LinkedList<>();
	}

	/**
	 * Takes t-Values and put it to the inUse values
	 */
	public boolean update() {
		boolean b5 = (delayFrameAmount != 0) || (zoom != tzoom) || (x != tx) || (y != ty) || z || z2 || z3 || (tilt != ttilt) || !screenshakeList.isEmpty();
		long time = System.currentTimeMillis() % 10000000;

		if (z) {
			if (time > targetTime) {
				tzoom = targetZoom;
				z = false;
			} else {
				tzoom = calculateFunction((time * 1.0f - beginTime) / (targetTime - beginTime), a, b, c, d);
			}
		}

		if (z2) {
			if (time > targetTime2) {
				tx = targetX;
				ty = targetY;
				z2 = false;
			} else {
				tx = calculateFunction((time * 1.0f - beginTime2) / (targetTime2 - beginTime2), a2, b2, c2, d2);
				ty = calculateFunction((time * 1.0f - beginTime2) / (targetTime2 - beginTime2), a3, b3, c3, d3);
			}
		}

		if (z3) {
			if (time > targetTime3) {
				ttilt = targetTilt;
				z3 = false;
			} else {
				ttilt = calculateFunction((time * 1.0f - beginTime3) / (targetTime3 - beginTime3), a4, b4, c4, d4);
			}
		}

		float sx = 0, sy = 0;
		for (int i = 0; i < screenshakeList.size(); i++) {
			Screenshake s = screenshakeList.get(i);
			double d = Math.pow(s.decay, (time - s.startTime)/TIME_FRAC);
			if (d * s.amp_x < MIN_AMP && d * s.amp_y < MIN_AMP) {
				screenshakeList.remove(s);
			} else {
				sx += d * s.amp_x * Math.cos(s.freq_x * (time-s.startTime) / TIME_FRAC + s.phase_x);
				sy += d * s.amp_y * Math.cos(s.freq_y * (time-s.startTime) / TIME_FRAC + s.phase_y);
			}
		}

		if (delayFrameAmount != 0) {
			zoom = delayFrames[0][0];
			x = delayFrames[0][1] + sx / zoom;
			y = delayFrames[0][2] + sy / zoom;
			tilt = delayFrames[0][3];

			for (int t = 0; t < delayFrameAmount-1; t++) {
				delayFrames[t][0] = delayFrames[t+1][0];
				delayFrames[t][1] = delayFrames[t+1][1];
				delayFrames[t][2] = delayFrames[t+1][2];
				delayFrames[t][3] = delayFrames[t+1][3];
			}
			delayFrames[delayFrameAmount-1][0] = tzoom;
			delayFrames[delayFrameAmount-1][1] = tx;
			delayFrames[delayFrameAmount-1][2] = ty;
			delayFrames[delayFrameAmount-1][3] = ttilt;
		} else {
			zoom = tzoom;
			x = tx + sx / zoom;
			y = ty + sy / zoom;
			tilt = ttilt;
		}

		return b5;
	}

	public void addScreenshake(float strength) {
		screenshakeList.add(new Screenshake(System.currentTimeMillis() % 10000000, (float) DECAY, strength, strength, (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI), 1, 1));
	}

	public void zoomSmooth(float a2) {
		zoomSmooth(a2, 300);
	}

	private void zoomSmooth(float a2, long time) {
		float v = 0;
		float t = tzoom;
		if (z) {
			v = calculateDerivative(((System.currentTimeMillis() % 10000000) * 1.0f - beginTime) / (targetTime - beginTime), a, b, c, d);
			t = targetZoom;
		}
		t *= a2;
		float currentZoom = tzoom;

		d = currentZoom;
		c = v;
		b = 3 * t - 2 * v - 3 * currentZoom;
		a = v + 2 * currentZoom - 2 * t;
		beginTime = System.currentTimeMillis() % 10000000;
		targetTime = System.currentTimeMillis() % 10000000 + time;
		targetZoom = t;

		z = true;
	}

	public void setZoomSmooth(float tzoom, long time) {
		zoomSmooth(tzoom / this.tzoom, time);
		z = true;
	}

	public void setPosition(float x, float y) {
		z2 = false;
		z = false;
		this.tx = x;
		this.ty = y;
	}

	public void setPositionSmooth(float x, float y, long time) {
		float v2 = 0, v3 = 0;
		float t2 = x, t3 = y;
		if (z2) {
			v2 = calculateDerivative(((System.currentTimeMillis() % 10000000) * 1.0f - beginTime2) / (targetTime2 - beginTime2), a2, b2, c2, d2);
			v3 = calculateDerivative(((System.currentTimeMillis() % 10000000) * 1.0f - beginTime2) / (targetTime2 - beginTime2), a3, b3, c3, d3);
		}
		float currentX = tx, currentY = ty;

		d2 = currentX;
		c2 = v2;
		b2 = 3 * t2 - 2 * v2 - 3 * currentX;
		a2 = v2 + 2 * currentX - 2 * t2;

		d3 = currentY;
		c3 = v3;
		b3 = 3 * t3 - 2 * v3 - 3 * currentY;
		a3 = v3 + 2 * currentY - 2 * t3;

		beginTime2 = System.currentTimeMillis() % 10000000;
		targetTime2 = System.currentTimeMillis() % 10000000 + time;
		targetX = x;
		targetY = y;

		z2 = true;
	}

	public void setTiltSmooth(float tilt, long time) {
		float v = 0;
		float t = Math.max(Math.min(tilt, 0.5f), -0.5f);
		if (z3) {
			v = calculateDerivative(((System.currentTimeMillis() % 10000000) * 1.0f - beginTime3) / (targetTime3 - beginTime3), a4, b4, c4, d4);
		}
		float currentTilt = ttilt;

		d4 = currentTilt;
		c4 = v;
		b4 = 3 * t - 2 * v - 3 * currentTilt;
		a4 = v + 2 * currentTilt - 2 * t;
		beginTime3 = System.currentTimeMillis() % 10000000;
		targetTime3 = System.currentTimeMillis() % 10000000 + time;
		targetTilt = t;

		z3 = true;
	}

	public float getTilt() {
		return tilt;
	}

	public void setTilt(float tilt) {
		ttilt = tilt;
		z3 = false;
	}

	public void raiseTilt() {
		setTiltSmooth(targetTilt + 0.1f, 300);
	}

	public void decreaseTilt() {
		setTiltSmooth(targetTilt - 0.1f, 300);
	}

	public float getZoom() {
		return tzoom;
	}

	public void setZoom(float tzoom) {
		z = false;
		this.tzoom = tzoom;
	}

	public void setDelayFrameAmount(int amount) {
		delayFrameAmount = amount;
		delayFrames = new float[delayFrameAmount][4];
		for (int t = 0; t < delayFrameAmount; t++) {
			delayFrames[t][0] = zoom;
			delayFrames[t][1] = x;
			delayFrames[t][2] = y;
			delayFrames[t][3] = tilt;
		}
	}

	public float getX() {
		return tx;
	}

	public float getY() {
		return ty;
	}

	private float calculateFunction(float x, float a, float b, float c, float d) {
		return a * x * x * x + b * x * x + c * x + d;
	}

	private float calculateDerivative(float x, float a, float b, float c, float d) {
		return 3 * a * x * x + 2 * b * x + c;
	}
}
