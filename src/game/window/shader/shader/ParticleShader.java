package game.window.shader.shader;

import game.Constants;
import game.window.shader.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private int locationLocation, texLocationLocation;

	public ParticleShader() {
		super(Constants.PARTICLE_VERTEX_FILE, Constants.PARTICLE_FRAGMENT_FILE);
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
