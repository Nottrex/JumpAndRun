package game.window.shader;

import game.util.ErrorUtil;
import game.util.FileHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private int texLocation, viewMatrixLocation, projectionMatrixLocation;
	private int texTWLocation, texTHLocation, timeLocation;
	private int lightAmountLocation, lightsLocation, lightColorsLocation, minBrightnessLocation;

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();

		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		bindAttributes();
		getUniformLocations();
	}

	private static int loadShader(String file, int type) {
		String shaderSource = FileHandler.loadFile("shader/" + file + ".txt");
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			ErrorUtil.printError("Could not compile shader " + file + ": " + GL20.glGetShaderInfoLog(shaderID, 500));
		}

		return shaderID;
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	protected void setUniform1f(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void setUniform1i(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	protected void setUniform1b(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	protected void setUniformMat4(int location, FloatBuffer matrix) {
		GL20.glUniformMatrix4fv(location, false, matrix);
	}

	protected void setUniform3f(int location, FloatBuffer buffer) {
		GL20.glUniform3fv(location, buffer);
	}

	protected void setUniform2f(int location, FloatBuffer buffer) {
		GL20.glUniform2fv(location, buffer);
	}

	protected abstract void bindAttributes();

	protected void getUniformLocations() {
		texLocation = getUniformLocation("tex");
		viewMatrixLocation = getUniformLocation("viewMatrix");
		projectionMatrixLocation = getUniformLocation("projectionMatrix");
		texTWLocation = getUniformLocation("texTW");
		texTHLocation = getUniformLocation("texTH");
		timeLocation = getUniformLocation("time");
		lightAmountLocation = getUniformLocation("lightAmount");
		lightsLocation = getUniformLocation("lights");
		lightColorsLocation = getUniformLocation("lightColors");
		minBrightnessLocation = getUniformLocation("minBrightness");

	}

	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}

	protected int getAttributeLocation(String name) {
		return GL20.glGetAttribLocation(programID, name);
	}


	public void setLightAmount(int lightAmount) {
		setUniform1i(lightAmountLocation, lightAmount);
	}

	public void setLights(FloatBuffer lights) {
		setUniform3f(lightsLocation, lights);
	}

	public void setLightColors(FloatBuffer lightColors) {
		setUniform3f(lightColorsLocation, lightColors);
	}

	public void setMinimumBrightness(float minimumBrightness) {
		setUniform1f(minBrightnessLocation, minimumBrightness);
	}

	public void setTexture(int tex) {
		setUniform1i(texLocation, tex);
	}

	public void setViewMatrix(FloatBuffer viewMatrix) {
		setUniformMat4(viewMatrixLocation, viewMatrix);
	}

	public void setProjectionMatrix(FloatBuffer projectionMatrix) {
		setUniformMat4(projectionMatrixLocation, projectionMatrix);
	}

	public void setTextureTotalBounds(int texTW, int texTH) {
		setUniform1i(texTWLocation, texTW);
		setUniform1i(texTHLocation, texTH);
	}

	public void setTime(float time) {
		setUniform1f(timeLocation, time);
	}

}
