package game.gameObjects;

import game.HitBox;

import java.util.List;

public interface CollisionObject extends GameObject{
	List<HitBox> getCollisionBoxes();
	void collide(GameObject gameObject, HitBox.HitBoxDirection direction);
}
