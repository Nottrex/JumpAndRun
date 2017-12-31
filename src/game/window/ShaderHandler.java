package game.window;

import com.joml.matrix.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderHandler {
	private Map<ShaderType, ShaderProgram> shaders;
	private Map<ShaderType, Integer> shaderUse;

	private FloatBuffer projectionMatrix, viewMatrix;
	private float time;
	private int textureWidth, textureHeight, texture;

	private int lightAmount;
	private FloatBuffer lights;
	private FloatBuffer lightColors;
	private float minimumBrightness;

	public ShaderHandler() {
		shaders = new HashMap<>();
		shaderUse = new HashMap<>();
	}

	public ShaderProgram loadShader(ShaderType shaderType) {
		shaderUse.put(shaderType, getUseAmount(shaderType) + 1);
		if (!shaders.containsKey(shaderType)) {
			ShaderProgram shader = shaderType.getShader();

			shaders.put(shaderType, shader);


			shader.start();
			shader.setMinimumBrightness(minimumBrightness);
			shader.setLightAmount(lightAmount);
			if (lights != null) {
				lights.rewind();
				shader.setLights(lights);
			}
			if (lightColors != null) {
				lightColors.rewind();
				shader.setLightColors(lightColors);
			}
			shader.setTexture(texture);
			shader.setTextureTotalBounds(textureWidth, textureHeight);
			shader.setTime(time);
			if (viewMatrix != null) {
				viewMatrix.rewind();
				shader.setViewMatrix(viewMatrix);
			}
			if (projectionMatrix != null) {
				projectionMatrix.rewind();
				shader.setProjectionMatrix(projectionMatrix);
			}
			shader.stop();

			return shader;
		}
		return shaders.get(shaderType);
	}

	public void unloadShader(ShaderType shaderType) {
		shaderUse.put(shaderType, getUseAmount(shaderType) - 1);
		if (getUseAmount(shaderType) <= 0) {
			ShaderProgram shader = shaders.get(shaderType);

			shader.cleanUp();
			shaders.remove(shaderType);
			shaderUse.remove(shaderType);
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

	private int getUseAmount(ShaderType shaderType) {
		if (shaderUse.containsKey(shaderType)) return shaderUse.get(shaderType);
		return 0;
	}

	public void cleanUp() {
		while (!shaders.isEmpty()) {
			ShaderType shaderType = (ShaderType) shaders.keySet().toArray()[0];
			shaders.get(shaderType).cleanUp();
			shaders.remove(shaderType);
		}
	}
}
