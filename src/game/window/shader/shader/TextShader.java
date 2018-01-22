package game.window.shader.shader;

import game.window.shader.ShaderProgram;

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

	public void setColor(float r, float g, float b, float a) {
		setUniform4f(colorLocation, r, g, b, a);
	}

	public void setUseCameraLocation(boolean useCamera) {
		setUniform1f(useCameraLocation, useCamera ? 1 : 0);
	}
}
