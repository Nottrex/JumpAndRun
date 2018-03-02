package game.window.light;

public interface Light {

	/**
	 * updates this light
	 * @return if something has changed
	 */
	boolean updateLight();

	/**
	 * get the light positions
	 * @param values the target array that should be filled by x, y and strength of the light 
	 * (values[0]=x, values[1]=y, values[2]=strength)
	 */
	void getLightPosition(float[] values);

	/**
	 * get the light color
	 * @param values the target array that should be filled by red, green and blue value of the light
	 * (values[0]=red, values[1]=green, values[2]=blue)
	 */
	void getLightColor(float[] values);
}
