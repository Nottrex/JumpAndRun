package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Lever extends BasicStaticEntity {
	private static Sprite right = new Sprite(100, "lever_right");
	private static Sprite left = new Sprite(100, "lever_left");
	private static Sprite changingLeft = new Sprite(350, "lever_middle", "lever_left");
	private static Sprite changingRight = new Sprite(350, "lever_middle", "lever_right");

	private boolean activated;
	private boolean turning;
	private int startTick;

	private Tree tagValue;
	private String tag;

	public Lever(float x, float y, float drawingPriority, Tree tagValue) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		this.tagValue = tagValue;
		turning = false;
		startTick = 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (gameObject instanceof Player && ((Player) gameObject).isInteracting()) {
			if (!turning) {
				turning = true;
				startTick = game.getGameTick();
				setSprite(activated ? changingRight : changingLeft);
			}
		}
	}

	@Override
	public void init(Game game) {
		super.init(game);

		this.tag = (String) tagValue.get(game);
		activated = game.getValue(tag) > 0;
		setSprite(activated ? left : right);
	}

	@Override
	public void update(Game game) {
		if (turning) {
			if (game.getGameTick() - startTick > 40) {
				activated = !activated;
				turning = false;

				game.setValue(tag, game.getValue(tag) + (activated ? 1 : -1));
				setSprite(activated ? left : right);
			}
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
