package game.window.shader.shader;

import game.Constants;
import game.window.shader.ShaderProgram;

public class BasicShader extends ShaderProgram {
	private int xLocation, yLocation, widthLocation, heightLocation;
	private int texXLocation, texYLocation, texWLocation, texHLocation;

	public BasicShader() {
		super(Constants.BASIC_VERTEX_FILE, Constants.BASIC_FRAGMENT_FILE);
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
}
