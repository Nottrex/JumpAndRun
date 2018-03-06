package game.window.shader.shader;

import game.window.shader.ShaderProgram;

/**
 * A shader used to draw Text
 */
public class TextShader extends ShaderProgram {
	private static final String TEXT_VERTEX_FILE = "textVertexShader";
	private static final String TEXT_FRAGMENT_FILE = "textFragmentShader";

	private int locationLocation, texLocationLocation;
	private int colorLocation, useCameraLocation;

	public TextShader() {
		super(TEXT_VERTEX_FILE, TEXT_FRAGMENT_FILE);
	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();
		colorLocation = getUniformLocation("color");
		useCameraLocation = getUniformLocation("useCamera");
	}

	@Override
	protected void bindAttributes() {
		locationLocation = getAttributeLocation("location");
		texLocationLocation = getAttributeLocation("texLocation");
	}

	public int getLocationLocation() {
		return locationLocation;
	}

	public int getTexLocationLocation() {
		return texLocationLocation;
	}

	/**
	 * Loads a value into the shader
	 * @param r red value of the color
	 * @param g green value of the color
	 * @param b blue value of the color
	 * @param a alpha value of the color
	 */
	public void setColor(float r, float g, float b, float a) {
		setUniform4f(colorLocation, r, g, b, a);
	}

	/**
	 * Loads a value into the shader
	 * @param useCamera whether the transformation by the camera should be discarded
	 */
	public void setUseCamera(boolean useCamera) {
		setUniform1f(useCameraLocation, useCamera ? 1 : 0);
	}
}
