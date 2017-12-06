package game;

import java.util.List;

public interface CollisionObject extends GameObject{
	public List<HitBox> getCollisionBoxes();
	public void collide(GameObject gameObject);
}
