package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.ColorShader;
import org.lwjgl.opengl.GL11;

public class Fade extends AbstractGameObject implements Drawable {
	private static final int FADE_TIME = 60;

	private int startTick = 0;
	private int currentTick = 0;

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void init(Game game) {
		super.init(game);

		startTick = game.getGameTick();
	}

	@Override
	public void update(Game game) {
		currentTick = game.getGameTick();

		if (currentTick - startTick > FADE_TIME) game.removeGameObject(this);
	}

	@Override
	public void setup(Window window) {

	}

	@Override
	public void draw(Window window, long time) {
		ColorShader shader = (ColorShader) window.getShaderHandler().getShader(ShaderType.COLOR_SHADER);

		int t = (currentTick - startTick);

		shader.start();
		shader.setBounds(-1000, -1000, 2000, 2000);
		shader.setColor(0f, 0f, 0f, t < FADE_TIME/2.0f ? t / (FADE_TIME/2.0f) : 2 - (t / (FADE_TIME/2.0f)));

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public float getDrawingPriority() {
		return -10;
	}

	@Override
	public void cleanUp(Window window) {

	}
}
