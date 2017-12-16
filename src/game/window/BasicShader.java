package game.window;

public class BasicShader extends ShaderProgram {
	private static final String VERTEX_FILE = "squareVertexShader";
	private static final String FRAGMENT_FILE = "squareFragmentShader";
	private int texLocation, xLocation, yLocation, widthLocation, heightLocation, cameraLocation, projectionLocation;
	private int texXLocation, texYLocation, texTWLocation, texTHLocation, texWLocation, texHLocation, timeLocation;

	public BasicShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		texLocation = getUniformLocation("tex");
		xLocation = getUniformLocation("x");
		yLocation = getUniformLocation("y");
		widthLocation = getUniformLocation("width");
		heightLocation = getUniformLocation("height");
		cameraLocation = getUniformLocation("cameraMatrix");
		projectionLocation = getUniformLocation("projectionMatrix");

		texXLocation = getUniformLocation("texX");
		texYLocation = getUniformLocation("texY");
		texTWLocation = getUniformLocation("texTW");
		texTHLocation = getUniformLocation("texTH");
		texHLocation = getUniformLocation("texH");
		texWLocation = getUniformLocation("texW");

		timeLocation = getUniformLocation("time");
	}

	@Override
	protected void getUniformLocations() {

	}

	public void setTexture(int tex) {
		setUniform1i(texLocation, tex);
	}

	public void setBounds(float x, float y, float width, float height) {
		setUniform1f(xLocation, x);
		setUniform1f(yLocation, y);
		setUniform1f(widthLocation, width);
		setUniform1f(heightLocation, height);
	}

	public void setCamera(float[] camera) {
		setUniformMat4(cameraLocation, camera);
	}

	public void setProjectionMatrix(float[] projectionMatrix) {
		setUniformMat4(projectionLocation, projectionMatrix);
	}

	public void setTextureSheetBounds(int x, int y, int width, int height) {
		setUniform1i(texXLocation, x);
		setUniform1i(texYLocation, y);
		setUniform1i(texWLocation, width);
		setUniform1i(texHLocation, height);
	}

	public void setTextureTotalBounds(int texTW, int texTH) {
		setUniform1i(texTWLocation, texTW);
		setUniform1i(texTHLocation, texTH);
	}

	public void setTime(float time) {
		setUniform1f(timeLocation, time);
	}
}
