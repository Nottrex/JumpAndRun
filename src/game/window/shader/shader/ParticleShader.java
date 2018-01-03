package game.window.shader.shader;

import game.window.shader.ShaderProgram;

public class ParticleShader extends ShaderProgram {
	private static final String VERTEX_FILE = "particleVertexShader";
	private static final String FRAGMENT_FILE = "particleFragmentShader";

	private int locationLocation, texLocationLocation;

	public ParticleShader() {
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
