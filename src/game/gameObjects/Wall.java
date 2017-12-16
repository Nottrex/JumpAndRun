package game.gameObjects;

import game.HitBox;
import game.Sprite;
import game.gameObjects.entities.BasicMovingEntity;

public class Wall extends BasicMovingEntity {
	private Sprite sprite = new Sprite(1, "wall");

	public Wall() {
		super(new HitBox(-2, -5, 10, 1));
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {

	}

	@Override
	public void update() {

	}

	@Override
	public float getDrawingPriority() {
		return 0;
	}

	@Override
	public Sprite getCurrentSprite() {
		return sprite;
	}
}
