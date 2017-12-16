package game.gameObjects.entities;

import game.Sprite;
import game.gameObjects.GameObject;
import game.HitBox;

public class Player extends BasicWalkingEntity {

	public Player() {
		super(new HitBox(0,0,1,1));
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {
		super.collide(gameObject, direction);

	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getDrawingPriority() {
		return 0;
	}

	@Override
	public Sprite getCurrentSprite() {
		return null;
	}
}
