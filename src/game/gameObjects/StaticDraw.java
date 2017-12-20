package game.gameObjects;

import game.HitBox;
import game.util.TextureHandler;
import game.window.StaticShader;
import game.window.Window;
import javafx.util.Pair;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public abstract class StaticDraw implements Drawable {
	private static final int INDICES = 6;
	private static final float[][] VERTEX_POS = new float[][] {
		{0,0}, {0,1}, {1,1}, {1,0}
	};

	private int rectangles;
	private int vao, vao2;
	private List<Pair<HitBox, String>> hitBoxList;

	private boolean update;

	public StaticDraw() {
		update = false;
	}

	@Override
	public void setup(Window window) {
		window.getShaderHandler().loadShader("StaticShader");
		vao = GL30.glGenVertexArrays();
		vao2 = GL30.glGenVertexArrays();
	}

	@Override
	public void draw(Window window, long time) {
		StaticShader shader = (StaticShader) window.getShaderHandler().getShader("StaticShader");

		shader.start();

		if (update) {
			updateBuffers(shader);
			update = false;
		}

		if (hitBoxList == null) return;

		GL30.glBindVertexArray(vao);

		GL11.glDrawElements(GL11.GL_TRIANGLES, rectangles * INDICES, GL11.GL_UNSIGNED_INT, 0);

		GL30.glBindVertexArray(vao2);
	}

	@Override
	public void cleanUp(Window window) {
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteVertexArrays(vao2);

		window.getShaderHandler().unloadShader("StaticShader");
	}

	protected void updateContent(List<Pair<HitBox, String>> hitBoxList) {
		this.hitBoxList = hitBoxList;
		update = true;
	}

	private void updateBuffers(StaticShader shader) {
		rectangles = hitBoxList.size();

		FloatBuffer locations = BufferUtils.createFloatBuffer(rectangles * 2 * VERTEX_POS.length);
		FloatBuffer texLocations = BufferUtils.createFloatBuffer(rectangles * 2 * VERTEX_POS.length);
		IntBuffer indices = BufferUtils.createIntBuffer(rectangles * INDICES);

		for (int i = 0; i < rectangles; i++) {
			HitBox hitBox = hitBoxList.get(i).getKey();
			Rectangle texture = TextureHandler.getSpriteSheetBounds("textures_" + hitBoxList.get(i).getValue());

			for (int v = 0; v < VERTEX_POS.length; v++) {
				locations.put(hitBox.x + VERTEX_POS[v][0] * hitBox.width);
				locations.put(hitBox.y + VERTEX_POS[v][1] * hitBox.height);

				texLocations.put(texture.x + VERTEX_POS[v][0] * texture.width);
				texLocations.put(texture.y + VERTEX_POS[v][1] * texture.height);
			}

			indices.put(i * VERTEX_POS.length);
			indices.put(i * VERTEX_POS.length + 2);
			indices.put(i * VERTEX_POS.length + 1);
			indices.put(i * VERTEX_POS.length);
			indices.put(i * VERTEX_POS.length + 3);
			indices.put(i * VERTEX_POS.length + 2);
		}

		locations.flip();
		texLocations.flip();
		indices.flip();

		int locationBuffer = GL15.glGenBuffers();
		int texLocationBuffer = GL15.glGenBuffers();
		int indicesBuffer =  GL15.glGenBuffers();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, locations, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texLocations, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao);

		GL20.glEnableVertexAttribArray(shader.getLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationBuffer);
		GL20.glVertexAttribPointer(shader.getLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL20.glEnableVertexAttribArray(shader.getTexLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationBuffer);
		GL20.glVertexAttribPointer(shader.getTexLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao2);


	}
}
