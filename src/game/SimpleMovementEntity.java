package game;

import java.util.List;

public abstract  class SimpleMovementEntity implements CollisionObject {
	@Override
	public void update(Game game) {

	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return null;
	}

	@Override
	public abstract void collide(GameObject gameObject);

}
