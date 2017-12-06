package game;

import java.util.List;

public interface CollisionObject {
	public List<HitBox> getCollisionBoxes();
	public void collide(GameObject gameObject);
}
