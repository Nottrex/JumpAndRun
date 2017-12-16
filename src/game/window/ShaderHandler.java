package game.window;

import com.joml.matrix.Matrix4f;
import game.util.ErrorUtil;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderHandler {
	private Map<String, ShaderProgram> shaders;
	private Map<String, Integer> shaderUse;

	private FloatBuffer projectionMatrix, viewMatrix;
	private float time;
	private int textureWidth, textureHeight, texture;

	public ShaderHandler() {
		shaders = new HashMap<>();
		shaderUse = new HashMap<>();
	}

	public void loadShader(String name) {
		if (!shaders.containsKey(name)) {
			ShaderProgram shader = createShader(name);

			shaders.put(name, shader);

			viewMatrix.flip();
			projectionMatrix.flip();
			shader.start();
			shader.setTexture(texture);
			shader.setTextureTotalBounds(textureWidth, textureHeight);
			shader.setTime(time);
			shader.setViewMatrix(viewMatrix);
			shader.setProjectionMatrix(projectionMatrix);
			shader.stop();

		}
		shaderUse.put(name, getUseAmount(name) + 1);
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
			buffer.flip();
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
			buffer.flip();
			shader.start();
			shader.setViewMatrix(buffer);
			shader.stop();
		});
	}

	public void setTime(float time) {
		this.time = time;
		shaders.values().forEach(shader -> shader.setTime(time));
	}

	public void setTextureTotalBounds(int textureWidth, int textureHeight) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		shaders.values().forEach(shader -> shader.setTextureTotalBounds(textureWidth, textureHeight));
	}

	public void setTexture(int texture) {
		this.texture = texture;
		shaders.values().forEach(shader -> shader.setTexture(texture));
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
