package game.window.shader.shader;

import game.Constants;
import game.window.shader.ShaderProgram;

public class StaticShader extends ShaderProgram {

	private int locationLocation, texLocationLocation;
	private int alphaLocation;

	public StaticShader() {
		super(Constants.STATIC_VERTEX_FILE, Constants.STATIC_FRAGMENT_FILE);
	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();
		alphaLocation = getUniformLocation("alpha");
		System.out.println(alphaLocation);
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
