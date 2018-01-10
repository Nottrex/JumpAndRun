package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Door extends BasicStaticEntity {
	private Sprite door = new Sprite(1, "door_side");

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
			game.setGameMap(targetMap);
		}
	}
}
