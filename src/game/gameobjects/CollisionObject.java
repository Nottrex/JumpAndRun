package game.gameobjects;

import game.data.HitBox;
import game.data.HitBoxDirection;

import java.util.List;

public interface CollisionObject extends GameObject {
	List<HitBox> getCollisionBoxes();

	void collide(GameObject gameObject, HitBoxDirection direction, float velocity);
}
