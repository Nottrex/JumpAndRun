package game.gameobjects.gameobjects;

import game.Constants;
import game.Game;
import game.gameobjects.AbstractGameObject;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.ColorShader;
import org.lwjgl.opengl.GL11;

public class Fade extends AbstractGameObject implements Drawable {

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

		if (currentTick - startTick >= Constants.FADE_TIME) game.removeGameObject(this);
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

		if (t <= Constants.FADE_TIME/2.0f) {
			shader.setColor(0, 0, 0,  t / (Constants.FADE_TIME/2.0f));
		} else {
			shader.setColor(0, 0, 0,  2 - (t / (Constants.FADE_TIME/2.0f)));
		}

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
