package game.window.shader.shader;

import game.window.shader.ShaderProgram;

public class StaticShader extends ShaderProgram {
	private static final String STATIC_VERTEX_FILE = "staticVertexShader";
	private static final String STATIC_FRAGMENT_FILE = "staticFragmentShader";

	private int locationLocation, texLocationLocation;
	private int alphaLocation;

	public StaticShader() {
		super(STATIC_VERTEX_FILE, STATIC_FRAGMENT_FILE);
	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();
		alphaLocation = getUniformLocation("alpha");
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

	public void setAlpha(float alpha) {
		setUniform1f(alphaLocation, alpha);
	}
}
