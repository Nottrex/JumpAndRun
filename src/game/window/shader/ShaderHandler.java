package game.window.shader;

import com.joml.matrix.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderHandler {
	private Map<ShaderType, ShaderProgram> shaders;

	private FloatBuffer projectionMatrix, viewMatrix;
	private float time;
	private int textureWidth, textureHeight, texture;

	private int lightAmount;
	private FloatBuffer lights;
	private FloatBuffer lightColors;
	private float minimumBrightness;

	public ShaderHandler() {
		shaders = new HashMap<>();

		for (ShaderType shaderType: ShaderType.values()) {
			ShaderProgram shader = shaderType.createShader();
			shaders.put(shaderType, shader);
		}

	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		projectionMatrix.store(buffer);
		this.projectionMatrix = buffer;

		shaders.values().forEach(shader -> {
			buffer.rewind();
			shader.start();
			shader.setProjectionMatrix(buffer);
			shader.stop();
		});
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		viewMatrix.store(buffer);
		this.viewMatrix = buffer;

		shaders.values().forEach(shader -> {
			buffer.rewind();
			shader.start();
			shader.setViewMatrix(buffer);
			shader.stop();
		});

	}

	public void setTime(float time) {
		this.time = time;
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTime(time);
		});
	}

	public void setTextureTotalBounds(int textureWidth, int textureHeight) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTextureTotalBounds(textureWidth, textureHeight);
		});
	}

	public void setTexture(int texture) {
		this.texture = texture;
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTexture(texture);
		});
	}

	public void setLightAmount(int lightAmount) {
		this.lightAmount = lightAmount;
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setLightAmount(lightAmount);
		});
	}

	public void setLights(float[][] lightArray) {
		int length = lightArray.length;
		if (length != 0) length *= lightArray[0].length;

		this.lights = BufferUtils.createFloatBuffer(length);
		for (int i = 0; i < lightArray.length; i++) {
			lights.put(lightArray[i]);
		}

		shaders.values().forEach(shader -> {
			shader.start();
			lights.rewind();
			shader.setLights(lights);
		});
	}

	public void setLightColors(float[][] lightColorsArray) {
		int length = lightColorsArray.length;
		if (length != 0) length *= lightColorsArray[0].length;

		this.lightColors = BufferUtils.createFloatBuffer(length);
		for (int i = 0; i < lightColorsArray.length; i++) {
			lightColors.put(lightColorsArray[i]);
		}

		shaders.values().forEach(shader -> {
			shader.start();
			lightColors.rewind();
			shader.setLightColors(lightColors);
		});
	}

	public void setMinimumBrightness(float minimumBrightness) {
		this.minimumBrightness = minimumBrightness;
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setMinimumBrightness(minimumBrightness);
		});
	}

	public ShaderProgram getShader(ShaderType shaderType) {
		return shaders.get(shaderType);
	}

	public void cleanUp() {
		while (!shaders.isEmpty()) {
			ShaderType shaderType = (ShaderType) shaders.keySet().toArray()[0];
			shaders.get(shaderType).cleanUp();
			shaders.remove(shaderType);
		}
	}
}
