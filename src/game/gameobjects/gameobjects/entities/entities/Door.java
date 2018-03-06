package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

/**
 * A door that opens and closes depending on a condition;
 */
public class Door extends BasicStaticEntity {
	private static Sprite opened = new Sprite(100, "door_6");
	private static Sprite closed = new Sprite(100, "door_1");
	private static Sprite open = new Sprite(150, "door_2", "door_3", "door_4", "door_5", "door_6");
	private static Sprite close = new Sprite(150, "door_5", "door_4", "door_3", "door_2", "door_1");

	private boolean isOpen;				//Is open
	private boolean turning;			//Is currently opening or closing
	private int startTick;				//The first tick of the opening or closing

	private Tree condition;

	public Door(float x, float y, float drawingPriority, Tree condition) {
		super(new HitBox(x + 0.375f, y, 0.25f, 1f), drawingPriority);

		this.condition = condition;
		turning = false;
		startTick = 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void init(Game game) {
		super.init(game);

		this.isOpen = (boolean) condition.get(game);
		setSprite(isOpen ? opened : closed);
		hitBox.type = isOpen ? HitBox.HitBoxType.NOT_BLOCKING : HitBox.HitBoxType.BLOCKING;
	}

	@Override
	public void update(Game game) {
		if (turning) {
			if (game.getGameTick() - startTick == 40) {
				isOpen = !isOpen;
				setSprite(isOpen ? opened : closed);
				hitBox.type = isOpen ? HitBox.HitBoxType.NOT_BLOCKING : HitBox.HitBoxType.BLOCKING;
				turning = false;
			}

			game.getCamera().addScreenshake(0.004f);
		} else if ((boolean) condition.get(game) != isOpen) {
			turning = true;
			startTick = game.getGameTick();
			setSprite(isOpen ? close : open);
		}
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -5;
	}
}
