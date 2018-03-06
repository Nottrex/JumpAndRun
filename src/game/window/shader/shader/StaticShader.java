package game.window.shader.shader;

import game.window.shader.ShaderProgram;

/**
 * A shader used to draw walls and backgrounds
 */
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

	/**
	 * @return the OpenGL VBO location for the locations
	 */
	public int getLocationLocation() {
		return locationLocation;
	}

	/**
	 * @return the OpenGL VBO location for the texture locations
	 */
	public int getTexLocationLocation() {
		return texLocationLocation;
	}

	/**
	 * Loads a value into the shader
	 * @param alpha the alpha value of the drawn object
	 */
	public void setAlpha(float alpha) {
		setUniform1f(alphaLocation, alpha);
	}
}
