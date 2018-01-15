package game.data.smoothfloat;

public interface SmoothFloat {
	void set(float value);

	void setSmooth(float value, long time);

	boolean update(long time);

	float get();
}
