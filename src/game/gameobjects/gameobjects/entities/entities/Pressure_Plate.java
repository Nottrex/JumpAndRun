package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Pressure_Plate extends BasicStaticEntity {
	private static Sprite up = new Sprite(100, "pressureplate");
	private static Sprite down = new Sprite(100, "pressureplate_pressed");

	private Tree onActivate, onDeactivate;
	private int timeActivated, timeDeactivated;
	private boolean pressed;

	public Pressure_Plate(float x, float y, float drawingPriority, Tree onActivate, Tree onDeactivate) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		pressed = false;
		setSprite(up);

		this.onActivate = onActivate;
		this.onDeactivate = onDeactivate;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
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

		if (timeActivated > 60 && onActivate != null) onActivate.get(game);
		else if (timeActivated > 60 && onDeactivate != null) onDeactivate.get(game);

		setSprite(pressed ? down : up);
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
