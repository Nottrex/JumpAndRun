package game.window;

import game.Constants;
import game.audio.Source;
import game.data.smoothfloat.SmoothFloat;
import game.data.smoothfloat.SmoothFloatCubic;
import game.util.MathUtil;
import game.util.TimeUtil;
import org.lwjgl.openal.AL10;

import java.util.LinkedList;
import java.util.List;

public class Camera {

	private float zoom, x, y, rotation;				//the current position  
	private SmoothFloat tZoom, tX, tY, tRotation;			//the theoretical position

	private List<Screenshake> screenshakeList;			//all active screenshakes

	public Camera() {
		zoom = 0.1f;
		x = 0;
		y = 0;
		rotation = 0;

		tZoom = new SmoothFloatCubic(zoom);
		tX = new SmoothFloatCubic(x);
		tY = new SmoothFloatCubic(y);
		
		//Rotation should be looping
		tRotation = new SmoothFloatCubic(rotation) {
			@Override
			public void setSmooth(float value, long time) {
				value = MathUtil.mod(value, (float) (Math.PI * 2));
				float currentValue = get();
				float tValue = MathUtil.mod(currentValue, (float) (Math.PI * 2));

				if (tValue < Math.PI && value - tValue > Math.PI) {
					value -= 2 * Math.PI;
				} else if (tValue > Math.PI && tValue - value > Math.PI) {
					value += 2 * Math.PI;
				}

				super.setSmooth(currentValue - tValue + value, time);
			}
		};

		screenshakeList = new LinkedList<>();
	}

	/**
	 * updates the camera
	 * theoretical position are turned into the real values
	 * @return if something has changed
	 */
	public boolean update() {
		boolean change = !screenshakeList.isEmpty();
		long time = TimeUtil.getTime();

		//Update theoretical values
		change |= tZoom.update(time);
		change |= tX.update(time);
		change |= tY.update(time);
		change |= tRotation.update(time);

		//Calculate current screenshake
		float sx = 0, sy = 0, sr = 0;
		for (int i = 0; i < screenshakeList.size(); i++) {
			Screenshake s = screenshakeList.get(i);
			float t = 1.0f * (time - s.startTime) / Constants.TIME_FRAC;
			double d = Math.pow(s.decay, t);
			if (d * s.amp_x < Constants.MIN_AMP && d * s.amp_y < Constants.MIN_AMP && d * s.amp_r < Constants.MIN_AMP) {
				//Screenshake is small and should be removed
				screenshakeList.remove(s);
			} else {
				//Calculate x, y and rotation of the screenshake
				sx += d * s.amp_x * (MathUtil.noise((int) (10000 * s.phase_x), t, Math.round(s.freq_x)) * 2 - 1);
				sy += d * s.amp_y * (MathUtil.noise((int) (10000 * s.phase_y), t, Math.round(s.freq_y)) * 2 - 1);
				sr += d * s.amp_r * (MathUtil.noise((int) (10000 * s.phase_r), t, Math.round(s.freq_r)) * 2 - 1);
			}
		}

		//Update values
		zoom = tZoom.get();
		x = tX.get() + sx / zoom;
		y = tY.get() + sy / zoom;
		rotation = tRotation.get() + sr;

		return change;
	}

	/**
	 * adds a screenshake
	 * @param strength amplitude of the screenshake
	 */
	public void addScreenshake(float strength) {
		screenshakeList.add(new Screenshake(TimeUtil.getTime(), Constants.DECAY, strength, strength, strength, (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI), 1, 1, 1));
	}

	/**
	 * sets the zoom of the camera instantly
	 * @param zoom the zoom
	 */
	public void setZoom(float zoom) {
		tZoom.set(zoom);
	}

	/**
	 * sets the zoom of the camera by interpolating the current value with the target value over time
	 * @param zoom the target zoom
	 * @param time the time of the interpolation 
	 */
	public void setZoomSmooth(float zoom, long time) {
		tZoom.setSmooth(zoom, time);
	}

	/**
	 * sets the position of the camera instantly
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setPosition(float x, float y) {
		tX.set(x);
		tY.set(y);
	}

	/**
	 * sets the position of the camera by interpolating the current value with the target value over time
	 * @param x the target x coordinate
	 * @param y the target y coordinate
	 * @param time the time of the interpolation
	 */
	public void setPositionSmooth(float x, float y, long time) {
		tX.setSmooth(x, time);
		tY.setSmooth(y, time);
	}

	/**
	 * sets the rotation of the camera instantly
	 * @param rotation the rotation
	 */
	public void setRotation(float rotation) {
		tRotation.set(rotation);
	}

	/**
	 * sets the rotation of the camera by interpolating the current value with the target value over time
	 * @param rotation the target rotation
	 * @param time the time of the interpolation
	 */
	public void setRotationSmooth(float rotation, long time) {
		tRotation.setSmooth(rotation, time);
	}

	/**
	 * @return the current x position of the camera
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the current y position of the camera
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the current zoom of the camera
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * @return the current rotation of the camera
	 */
	public float getRotation() {
		return rotation;
	}

	//screenshake
	private class Screenshake {
		long startTime;			//the time the screenshake was added
		float decay;			//the rate at which the screenshake disappears 
		float amp_x, amp_y, amp_r, phase_x, phase_y, phase_r, freq_x, freq_y, freq_r; //Amplitude, Phase and Frequency of x, y and rotation

		private Screenshake(long startTime, float decay, float amp_x, float amp_y, float amp_r, float phase_x, float phase_y, float phase_r, float freq_x, float freq_y, float freq_r) {
			this.startTime = startTime;
			this.decay = decay;
			this.amp_x = amp_x;
			this.amp_y = amp_y;
			this.amp_r = amp_r;
			this.phase_x = phase_x;
			this.phase_y = phase_y;
			this.phase_r = phase_r;
			this.freq_x = freq_x;
			this.freq_y = freq_y;
			this.freq_r = freq_r;
		}
	}
}
