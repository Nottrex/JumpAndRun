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
	private static Sprite changingLeft = new Sprite(350, "lever_right_2", "lever_middle", "lever_left_2", "lever_left");
	private static Sprite changingRight = new Sprite(350, "lever_left_2","lever_middle","lever_right_2", "lever_right");

	private boolean activated;
	private boolean turning;
	private int startTick;

	private Tree onActivate, onDeactivate, enabled;

	public Lever(float x, float y, float drawingPriority, boolean activated, Tree onActivate, Tree onDeactivate, Tree enabled) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		this.activated = activated;
		setSprite(activated ? left : right);

		this.onActivate = onActivate;
		this.onDeactivate = onDeactivate;
		this.enabled = enabled;
		turning = false;
		startTick = 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (enabled != null && !(boolean)enabled.get(game)) return;

		if (gameObject instanceof Player) {
			if (!turning) {
				turning = true;
				startTick = game.getGameTick();
				setSprite(activated ? changingRight : changingLeft);
			}
		}
	}

	@Override
	public void update(Game game) {
		if (turning) {
			if (game.getGameTick() - startTick > 40) {
				activated = !activated;
				turning = false;

				if(activated && onActivate!=null) onActivate.get(game);
				if(!activated && onDeactivate!=null) onDeactivate.get(game);

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

	public void setEnabled(Tree enabled) {
		this.enabled = enabled;
	}

	public void setOnActivate(Tree onActivate) {
		this.onActivate = onActivate;
	}

	public void setOnDeactivate(Tree onDeactivate) {
		this.onDeactivate = onDeactivate;
	}
}
