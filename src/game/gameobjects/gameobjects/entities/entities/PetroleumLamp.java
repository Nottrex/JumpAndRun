package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;
import game.window.light.Light;

import java.awt.*;

public class PetroleumLamp extends BasicStaticEntity implements Light {
	public enum PetroleumColor{
		YELLOW(new Sprite(100, "petroleum_yellow"), new Color(255, 255, 0)),
		ORANGE(new Sprite(100, "petroleum_orange"), new Color(255, 153, 0)),
		RED(new Sprite(100, "petroleum_red"), new Color(255, 0, 0)),
		DARK_RED(new Sprite(100, "petroleum_darkRed"), new Color(102, 0, 0));

		private Sprite sprite;
		private Color color;

		PetroleumColor(Sprite sprite, Color color) {
			this.sprite = sprite;
			this.color = color;
		}

		public Sprite getSprite() {
			return sprite;
		}

		public Color getColor() {
			return color;
		}
	}

	private PetroleumColor color;

	public PetroleumLamp(float x, float y, float drawingPriority, PetroleumColor color) {
		super(new HitBox(x, y, 0.375f, 0.625f), drawingPriority);

		this.color = color;
		setSprite(color.getSprite());
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);

		window.getLightHandler().removeLight(this);
	}

	@Override
	public float getPriority() {
		return 1;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 1.3f*color.getColor().getRed()/255.0f;
		values[1] = 1.3f*color.getColor().getGreen()/255.0f;
		values[2] = 1.3f*color.getColor().getBlue()/255.0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.getCenterX();
		values[1] = hitBox.getCenterY();
		values[2] = 0.7f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}
}
