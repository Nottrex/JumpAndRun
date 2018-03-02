package game.window.shader;

import com.joml.matrix.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderHandler {
	private Map<ShaderType, ShaderProgram> shaders;

	public ShaderHandler() {
		shaders = new HashMap<>();

		for (ShaderType shaderType : ShaderType.values()) {
			ShaderProgram shader = shaderType.createShader();
			shaders.put(shaderType, shader);
		}

	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		projectionMatrix.store(buffer);

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

		shaders.values().forEach(shader -> {
			buffer.rewind();
			shader.start();
			shader.setViewMatrix(buffer);
			shader.stop();
		});

	}

	public void setTime(float time) {
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTime(time);
		});
	}

	public void setTextureTotalBounds(int textureWidth, int textureHeight) {
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTextureTotalBounds(textureWidth, textureHeight);
		});
	}

	public void setTexture(int texture) {
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setTexture(texture);
		});
	}

	public void setLightAmount(int lightAmount) {
		shaders.values().forEach(shader -> {
			shader.start();
			shader.setLightAmount(lightAmount);
		});
	}

	public void setLights(float[][] lightArray) {
		int length = lightArray.length;
		if (length != 0) length *= lightArray[0].length;

		FloatBuffer lights = BufferUtils.createFloatBuffer(length);
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

		FloatBuffer lightColors = BufferUtils.createFloatBuffer(length);
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
