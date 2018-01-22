package game.window.shader.shader;

import game.window.shader.ShaderProgram;

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

	public void setBounds(float x, float y, float width, float height) {
		setUniform1f(xLocation, x);
		setUniform1f(yLocation, y);
		setUniform1f(widthLocation, width);
		setUniform1f(heightLocation, height);
	}

	public void setTextureSheetBounds(int x, int y, int width, int height) {
		setUniform1i(texXLocation, x);
		setUniform1i(texYLocation, y);
		setUniform1i(texWLocation, width);
		setUniform1i(texHLocation, height);
	}

	public void setUseCamera(boolean useCamera) {
		setUniform1f(useCameraLocation, useCamera ? 1 : 0);
	}

	public void setColor(float r, float g, float b, float a) {
		setUniform4f(colorLocation, r, g, b, a);
	}
}
