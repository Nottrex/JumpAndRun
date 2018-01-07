package game.gameobjects.gameobjects.wall;

import game.Constants;
import game.data.HitBox;
import game.gameobjects.AbstractGameObject;
import game.window.Drawable;
import game.util.TextureHandler;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.StaticShader;
import javafx.util.Pair;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public abstract class StaticDraw extends AbstractGameObject implements Drawable {

	private int rectangles;
	private int vao, vao2;
	private int locationVBO, texLocationVBO, indicesVBO;
	private List<Pair<HitBox, String>> hitBoxList;

	private boolean update;

	public StaticDraw() {
		update = false;
	}

	@Override
	public void setup(Window window) {
		window.getShaderHandler().loadShader(ShaderType.STATIC_SHADER);

		vao = GL30.glGenVertexArrays();
		vao2 = GL30.glGenVertexArrays();

		locationVBO = GL15.glGenBuffers();
		texLocationVBO = GL15.glGenBuffers();
		indicesVBO = GL15.glGenBuffers();
	}

	@Override
	public void draw(Window window, long time) {
		StaticShader shader = (StaticShader) window.getShaderHandler().getShader(ShaderType.STATIC_SHADER);

		shader.start();

		if (update) {
			updateBuffers(shader);
			update = false;
		}

		if (hitBoxList == null) return;

		GL30.glBindVertexArray(vao);

		GL11.glDrawElements(GL11.GL_TRIANGLES, rectangles * Constants.INDICES.length, GL11.GL_UNSIGNED_INT, 0);

		GL30.glBindVertexArray(vao2);
	}

	@Override
	public void cleanUp(Window window) {
		if (indicesVBO != 0) {
			GL15.glDeleteBuffers(indicesVBO);
		}

		if (locationVBO != 0) {
			GL15.glDeleteBuffers(locationVBO);
		}

		if (texLocationVBO != 0) {
			GL15.glDeleteBuffers(texLocationVBO);
		}

		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteVertexArrays(vao2);

		window.getShaderHandler().unloadShader(ShaderType.STATIC_SHADER);
	}

	protected void updateContent(List<Pair<HitBox, String>> hitBoxList) {
		this.hitBoxList = hitBoxList;
		update = true;
	}

	private void updateBuffers(StaticShader shader) {
		rectangles = hitBoxList.size();

		FloatBuffer locations = BufferUtils.createFloatBuffer(rectangles * 2 * Constants.VERTEX_POS.length);
		FloatBuffer texLocations = BufferUtils.createFloatBuffer(rectangles * 2 * Constants.VERTEX_POS.length);
		IntBuffer indices = BufferUtils.createIntBuffer(rectangles * Constants.INDICES.length);

		for (int i = 0; i < rectangles; i++) {
			HitBox hitBox = hitBoxList.get(i).getKey();
			Rectangle texture = TextureHandler.getSpriteSheetBounds("textures_" + hitBoxList.get(i).getValue());

			for (float[] v : Constants.VERTEX_POS) {
				locations.put(hitBox.x + v[0] * hitBox.width);
				locations.put(hitBox.y + v[1] * hitBox.height);

				texLocations.put(texture.x + v[0] * texture.width);
				texLocations.put(texture.y + (1 - v[1]) * texture.height);
			}

			for (int ind : Constants.INDICES) {
				indices.put(i * Constants.VERTEX_POS.length + ind);
			}
		}

		locations.flip();
		texLocations.flip();
		indices.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, locations, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texLocations, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao);

		GL20.glEnableVertexAttribArray(shader.getLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL20.glVertexAttribPointer(shader.getLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL20.glEnableVertexAttribArray(shader.getTexLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL20.glVertexAttribPointer(shader.getTexLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao2);
	}
}
