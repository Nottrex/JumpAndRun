package game.gameObjects;

import game.HitBox;
import game.HitBoxDirection;

import java.util.List;

public interface CollisionObject extends GameObject{
	List<HitBox> getCollisionBoxes();
	void collide(GameObject gameObject, Enum<HitBoxDirection> direction, float velocity);
}
