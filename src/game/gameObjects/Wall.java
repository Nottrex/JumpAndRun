package game.gameObjects;

import game.HitBox;

import java.util.LinkedList;
import java.util.List;

public class Wall implements CollisionObject {
	private List<HitBox> collisionBoxes;

	public Wall() {
		collisionBoxes = new LinkedList<>();
		collisionBoxes.add(new HitBox(-20, -5, 40, 1));
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return collisionBoxes;
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {

	}

	@Override
	public void update() {

	}
}
