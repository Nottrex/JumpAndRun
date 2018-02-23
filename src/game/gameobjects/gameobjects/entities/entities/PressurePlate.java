package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class PressurePlate extends BasicStaticEntity {
	private static Sprite up = new Sprite(100, "pressureplate");
	private static Sprite down = new Sprite(100, "pressureplate_pressed");

	private Tree onActivate, onDeactivate;
	private int timeActivated, timeDeactivated;
	private boolean pressed;
	private boolean pressedREAL;

	public PressurePlate(float x, float y, float drawingPriority, Tree onActivate, Tree onDeactivate) {
		super(new HitBox(x, y, 1f, 0.25f), drawingPriority);

		pressed = false;
		pressedREAL = pressed;
		setSprite(up);

		this.onActivate = onActivate;
		this.onDeactivate = onDeactivate;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject instanceof Player) pressed = true;
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void update(Game game) {
		if (pressed) {
			timeActivated++;
			timeDeactivated = 0;
		} else {
			timeDeactivated++;
			timeActivated = 0;
		}

		if (timeActivated == 10 && onActivate != null) {
			onActivate.get(game);
			pressedREAL = true;
		}
		else if (timeDeactivated == 10 && onDeactivate != null) {
			onDeactivate.get(game);
			pressedREAL = false;
		}

		setSprite(pressedREAL ? down : up);
		pressed = false;
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -5;
	}

	public void setOnActivate(Tree onActivate) {
		this.onActivate = onActivate;
	}

	public void setOnDeactivate(Tree onDeactivate) {
		this.onDeactivate = onDeactivate;
	}
}
