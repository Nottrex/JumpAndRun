package game.window.shader.shader;

import game.window.shader.ShaderProgram;

public class ColorShader extends ShaderProgram {
	private static final String COLOR_VERTEX_FILE = "colorVertexShader";
	private static final String COLOR_FRAGMENT_FILE = "colorFragmentShader";

	private int xLocation, yLocation, widthLocation, heightLocation;
	private int colorLocation;

	public ColorShader() {
		super(COLOR_VERTEX_FILE, COLOR_FRAGMENT_FILE);
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

		colorLocation = getUniformLocation("color");
	}

	public void setBounds(float x, float y, float width, float height) {
		setUniform1f(xLocation, x);
		setUniform1f(yLocation, y);
		setUniform1f(widthLocation, width);
		setUniform1f(heightLocation, height);
	}

	public void setColor(float r, float g, float b, float a) {
		setUniform4f(colorLocation, r, g, b, a);
	}
}
