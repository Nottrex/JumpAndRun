package game.gameObjects;

import game.Game;
import game.util.TimeUtil;
import game.window.ParticleShader;
import game.window.ShaderType;
import game.window.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParticleSystem extends AbstractGameObject implements Drawable {
	public static final int MAX_PARTICLES = 1000;
	private static final int[] INDICES = new int[]{
			0, 2, 1, 0, 3, 2
	};
	private static final float[][] VERTEX_POS = new float[][]{
			{0, 0}, {0, 1}, {1, 1}, {1, 0}
	};
	private final List<Particle> particles = new ArrayList<>();
	private int vao, vao2;
	private int locationVBO, texLocationVBO, indicesVBO;
	private FloatBuffer locationBuffer, texLocationBuffer;
	private IntBuffer indicesBuffer;
	private List<Particle> toRemove;

	public ParticleSystem() {
		toRemove = new LinkedList<>();
	}

	public void createParticle(ParticleType particle, float x, float y, float vx, float vy) {
		if (particles.size() < MAX_PARTICLES)
			particles.add(new Particle(particle, x, y, vx, vy, TimeUtil.getTime()));
	}

	@Override
	public void update(Game game) {
		synchronized (particles) {
			for (Particle particle : particles) {
				if (particle.update())
					toRemove.add(particle);
			}

			particles.removeAll(toRemove);
		}
		toRemove.clear();
	}

	@Override
	public void setup(Window window) {
		ParticleShader shader = (ParticleShader) window.getShaderHandler().loadShader(ShaderType.PARTICLE_SHADER);
		vao = GL30.glGenVertexArrays();
		vao2 = GL30.glGenVertexArrays();

		locationVBO = GL15.glGenBuffers();
		texLocationVBO = GL15.glGenBuffers();
		indicesVBO = GL15.glGenBuffers();

		locationBuffer = BufferUtils.createFloatBuffer(MAX_PARTICLES * VERTEX_POS.length * 2);
		texLocationBuffer = BufferUtils.createFloatBuffer(MAX_PARTICLES * VERTEX_POS.length * 2);
		indicesBuffer = BufferUtils.createIntBuffer(MAX_PARTICLES * INDICES.length);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, locationBuffer, GL15.GL_DYNAMIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texLocationBuffer, GL15.GL_DYNAMIC_DRAW);

		GL30.glBindVertexArray(vao);

		GL20.glEnableVertexAttribArray(shader.getLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL20.glVertexAttribPointer(shader.getLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL20.glEnableVertexAttribArray(shader.getTexLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL20.glVertexAttribPointer(shader.getTexLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);

		GL30.glBindVertexArray(vao2);
	}

	@Override
	public void draw(Window window, long time) {
		ParticleShader shader = (ParticleShader) window.getShaderHandler().getShader(ShaderType.PARTICLE_SHADER);

		shader.start();

		updateBuffers(time);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, locationBuffer);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, texLocationBuffer);

		GL30.glBindVertexArray(vao);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
		GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indicesBuffer);

		GL11.glDrawElements(GL11.GL_TRIANGLES, Math.min(MAX_PARTICLES, particles.size()) * INDICES.length, GL11.GL_UNSIGNED_INT, 0);

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

		window.getShaderHandler().unloadShader(ShaderType.PARTICLE_SHADER);
	}

	@Override
	public float getDrawingPriority() {
		return -1;
	}

	@Override
	public float getPriority() {
		return -1;
	}

	private void updateBuffers(long time) {
		synchronized (particles) {
			locationBuffer.clear();
			texLocationBuffer.clear();
			indicesBuffer.clear();

			for (int i = 0; i < MAX_PARTICLES && i < particles.size(); i++) {
				Particle particle = particles.get(i);

				Rectangle texBounds = particle.type.getSprite().getTexture(particle.startTime, time);

				for (float[] v : VERTEX_POS) {
					locationBuffer.put(v[0] * particle.type.getWidth() + particle.x);
					locationBuffer.put(v[1] * particle.type.getHeight() + particle.y);

					texLocationBuffer.put((1 - v[0]) * texBounds.width + texBounds.x);
					texLocationBuffer.put((1 - v[1]) * texBounds.height + texBounds.y);
				}

				for (int ind : INDICES) {
					indicesBuffer.put(i * VERTEX_POS.length + ind);
				}
			}

			locationBuffer.flip();
			texLocationBuffer.flip();
			indicesBuffer.flip();
		}
	}

	private class Particle {
		private static final float GRAVITY_ACCELERATION = 0.003f;
		private static final float MAX_GRAVITY_SPEED = 0.1f;

		private float x, y;
		private float vx, vy;
		private ParticleType type;
		private long startTime;
		private int time;

		private Particle(ParticleType type, float x, float y, float vx, float vy, long startTime) {
			this.type = type;
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.startTime = startTime;

			time = 0;
		}

		private boolean update() {
			time++;
			if (time > type.getLifeTime())
				return true;

			if (type.isGravity())
				vy = Math.max(vy - GRAVITY_ACCELERATION, -MAX_GRAVITY_SPEED);

			x += vx;
			y += vy;

			return false;
		}
	}
}
