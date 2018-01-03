package game.window.shader.shader;

import game.window.shader.ShaderProgram;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "staticVertexShader";
	private static final String FRAGMENT_FILE = "staticFragmentShader";

	private int locationLocation, texLocationLocation;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();
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
}
