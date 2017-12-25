package game.window;

import com.joml.matrix.Matrix4f;
import game.util.ErrorUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderHandler {
	private Map<String, ShaderProgram> shaders;
	private Map<String, Integer> shaderUse;

	private FloatBuffer projectionMatrix, viewMatrix;
	private float time;
	private int textureWidth, textureHeight, texture;
	private int lightAmount;
	private FloatBuffer lights;
	private FloatBuffer lightColors;

	public ShaderHandler() {
		shaders = new HashMap<>();
		shaderUse = new HashMap<>();
	}

	public ShaderProgram loadShader(String name) {
		shaderUse.put(name, getUseAmount(name) + 1);
		if (!shaders.containsKey(name)) {
			ShaderProgram shader = createShader(name);

			shaders.put(name, shader);

			viewMatrix.rewind();
			projectionMatrix.rewind();
			lights.rewind();
			lightColors.rewind();

			shader.start();
			shader.setLightAmount(lightAmount);
			shader.setLights(lights);
			shader.setLightColors(lightColors);
			shader.setTexture(texture);
			shader.setTextureTotalBounds(textureWidth, textureHeight);
			shader.setTime(time);
			shader.setViewMatrix(viewMatrix);
			shader.setProjectionMatrix(projectionMatrix);
			shader.stop();

			return shader;
		}
		return shaders.get(name);
	}

	public void unloadShader(String name) {
		shaderUse.put(name, getUseAmount(name) - 1);
		if (getUseAmount(name) <= 0) {
			ShaderProgram shader = shaders.get(name);

			shader.cleanUp();
			shaders.remove(name);
			shaderUse.remove(name);
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
		this.lights = BufferUtils.createFloatBuffer(lightArray.length * lightArray[0].length);
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
		this.lightColors = BufferUtils.createFloatBuffer(lightColorsArray.length * lightColorsArray[0].length);
		for (int i = 0; i < lightColorsArray.length; i++) {
			lightColors.put(lightColorsArray[i]);
		}

		shaders.values().forEach(shader -> {
			shader.start();
			lightColors.rewind();
			shader.setLights(lightColors);
		});
	}

	public ShaderProgram getShader(String name) {
		return shaders.get(name);
	}

	private int getUseAmount(String name) {
		if (shaderUse.containsKey(name)) return shaderUse.get(name);
		return 0;
	}

	private ShaderProgram createShader(String name) {
		if (name.equals("BasicShader")) return new BasicShader();
		if (name.equals("StaticShader")) return new StaticShader();

		ErrorUtil.printError("Unknown Shader: " + name);
		return null;
	}

	public void cleanUp() {
		while (!shaders.isEmpty()) {
			String name = (String) shaders.keySet().toArray()[0];
			shaders.get(name).cleanUp();
			shaders.remove(name);
		}
	}
}
