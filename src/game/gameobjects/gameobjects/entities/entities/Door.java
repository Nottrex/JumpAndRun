package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Door extends BasicStaticEntity {
	private Sprite door = new Sprite(1, "door_side");
	private Sprite doorOpen = new Sprite(100, "door_side_open_0", "door_side_open_1", "door_side_open_2", "door_side_open_2", "door_side_open_2", "door_side_open_2");

	private String targetMap;

	public Door(float x, float y, float drawingPriority, String targetMap) {
		super(new HitBox(x, y, 0.75f, 1), drawingPriority);

		this.targetMap = targetMap;

		setSprite(door);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (gameObject instanceof Player) {
			if (game.setGameMap(targetMap, true)) {
				setSprite(doorOpen);
			}
		}
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}
}
