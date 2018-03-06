package game.window.shader.shader;

import game.window.shader.ShaderProgram;

/**
 * A shader used to draw rectangles with only a single image
 */
public class BasicShader extends ShaderProgram {
	private static final String BASIC_FRAGMENT_FILE = "basicFragmentShader";
	private static final String BASIC_VERTEX_FILE = "basicVertexShader";

	private int xLocation, yLocation, widthLocation, heightLocation;
	private int texXLocation, texYLocation, texWLocation, texHLocation, colorLocation, useCameraLocation;

	public BasicShader() {
		super(BASIC_VERTEX_FILE, BASIC_FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {

	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();

		xLocation = getUniformLocation("x");
		yLocation = getUniformLocation("y");
		widthLocation = getUniformLocation("width");
		heightLocation = getUniformLocation("height");

		texXLocation = getUniformLocation("texX");
		texYLocation = getUniformLocation("texY");
		texHLocation = getUniformLocation("texH");
		texWLocation = getUniformLocation("texW");

		colorLocation = getUniformLocation("color");
		useCameraLocation = getUniformLocation("useCamera");
	}

	/**
	 * Loads a value into the shader
	 * @param x the x position of the rectangle
	 * @param y the y position of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public void setBounds(float x, float y, float width, float height) {
		setUniform1f(xLocation, x);
		setUniform1f(yLocation, y);
		setUniform1f(widthLocation, width);
		setUniform1f(heightLocation, height);
	}

	/**
	 * Loads a value into the shader
	 * @param x the x position of the rectangle on the texture sheet
	 * @param y the y position of the rectangle on the texture sheet
	 * @param width the width of the rectangle on the texture sheet
	 * @param height the height of the rectangle on the texture sheet
	 */
	public void setTextureSheetBounds(int x, int y, int width, int height) {
		setUniform1i(texXLocation, x);
		setUniform1i(texYLocation, y);
		setUniform1i(texWLocation, width);
		setUniform1i(texHLocation, height);
	}

	/**
	 * Loads a value into the shader
	 * @param useCamera whether the transformation by the camera should be discarded
	 */
	public void setUseCamera(boolean useCamera) {
		setUniform1f(useCameraLocation, useCamera ? 1 : 0);
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
}
