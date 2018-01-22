package game.window.shader.shader;

import game.window.shader.ShaderProgram;

public class ParticleShader extends ShaderProgram {
	private static final String PARTICLE_VERTEX_FILE = "particleVertexShader";
	private static final String PARTICLE_FRAGMENT_FILE = "particleFragmentShader";

	private int locationLocation, texLocationLocation;

	public ParticleShader() {
		super(PARTICLE_VERTEX_FILE, PARTICLE_FRAGMENT_FILE);
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
