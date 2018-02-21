package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Exit extends BasicStaticEntity {
	private static Sprite door = new Sprite(1, "door_side");
	private static Sprite doorOpen = new Sprite(100, "door_side_open_0", "door_side_open_1", "door_side_open_2", "door_side_open_2", "door_side_open_2", "door_side_open_2");

	private String targetMap;
	private Tree onEntrance;

	public Exit(float x, float y, float drawingPriority, String targetMap, Tree onEntrance) {
		super(new HitBox(x+0.125f, y, 0.75f, 1), drawingPriority);

		this.targetMap = targetMap;
		this.onEntrance = onEntrance;

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

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (gameObject instanceof Player && interactionType == InteractionType.INTERACT) {
			if (game.setGameMap(targetMap, true)) {
				setSprite(doorOpen);
				if (onEntrance != null) onEntrance.get(game);
			}
		}
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}

	public String getTargetMap() {
		return targetMap;
	}

	public void setTargetMap(String targetMap) {
		this.targetMap = targetMap;
	}
}
