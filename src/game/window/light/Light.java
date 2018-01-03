package game.window.light;

public interface Light {

	boolean updateLight();

	void getLightPosition(float[] values);

	void getLightColor(float[] values);
}
