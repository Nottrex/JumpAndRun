package game.gameobjects.gameobjects.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicMovingEntity extends BasicDrawingEntity implements CollisionObject {
	private List<HitBox> hitBoxList;
	protected float vx, vy;
	protected float kx, ky;


	public BasicMovingEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);
		this.hitBoxList = new ArrayList<>();
		hitBoxList.add(hitBox);

		vx = 0;
		vy = 0;
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	@Override
	public void update(Game game) {
		float vx_ = vx;
		vx = 0;
		vy += ky;
		move(game);
		vx += vx_;
		vy -= ky;

		float vy_ = vy;
		vy = 0;
		vx += kx;
		move(game);
		vy += vy_;
		vx -= kx;

		kx *= 0.95f;
		ky *= 0.95f;
	}

	private void move(Game game) {
		List<CollisionObject> collides = new ArrayList<>();
		List<HitBoxDirection> directions = new ArrayList<>();
		List<Float> velocities = new ArrayList<>();
		HitBox targetLocation = hitBox.clone();
		targetLocation.move(vx, vy);

		boolean fallThroughBlock = fallThroughBlock();

		boolean collision;
		do {
			collision = false;
			for (CollisionObject collisionObject : game.getCollisionObjects()) {
				if (collisionObject == this) continue;
				for (HitBox hitBox2 : collisionObject.getCollisionBoxes()) {
					if (hitBox2.collides(targetLocation)) {
						HitBoxDirection direction = hitBox.direction(hitBox2);

						collides.add(collisionObject);
						directions.add(direction);

						if (direction == HitBoxDirection.COLLIDE || hitBox2.type == HitBox.HitBoxType.NOT_BLOCKING || (hitBox2.type == HitBox.HitBoxType.HALF_BLOCKING && (direction != HitBoxDirection.DOWN || fallThroughBlock))) {
							velocities.add(0f);
							continue;
						}
						collision = true;

						float ax = direction.getXDirection();
						float ay = direction.getYDirection();

						float distance = hitBox2.collisionDepth(targetLocation, ax, ay);

						ax *= distance;
						ay *= distance;

						velocities.add((float) Math.sqrt(vx * vx + vy * vy));

						vx -= ax;
						vy -= ay;
						targetLocation = hitBox.clone();
						targetLocation.move(vx, vy);
					}
				}
			}
		} while (collision);

		hitBox.move(vx, vy);

		for (int i = 0; i < directions.size(); i++) {
			HitBoxDirection direction = directions.get(i);
			if (velocities.get(i) == 0) continue;

			if (direction == HitBoxDirection.LEFT || direction == HitBoxDirection.RIGHT) {
				vx = 0;
				ky *= 0.75f;
				kx = 0;
			} else if (direction == HitBoxDirection.UP ||direction == HitBoxDirection.DOWN) {
				vy = 0;
				kx *= 0.75f;
				ky = 0;
			}
		}

		for (int i = 0; i < collides.size(); i++) {
			CollisionObject collisionObject = collides.get(i);
			HitBoxDirection direction = directions.get(i);
			float velocity = velocities.get(i);

			collide(collisionObject, direction, velocity, true);
			collisionObject.collide(this, direction.invert(), velocity, false);
		}
	}

	private int lastAttackKnockBack = -MIN_TIME_BETWEEN_ATTACK_KNOCK_BACK;
	private static final int MIN_TIME_BETWEEN_ATTACK_KNOCK_BACK = 30;
	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (game.getGameTick() - lastAttackKnockBack > MIN_TIME_BETWEEN_ATTACK_KNOCK_BACK && gameObject instanceof Player && interactionType == InteractionType.ATTACK) {
			float dx = (this.hitBox.getCenterX() - hitBox.getCenterX());
			float dy = (this.hitBox.getCenterY() - hitBox.getCenterY());
			double l = Math.sqrt(dx*dx+dy*dy);
			dx /= l;
			dy /= l;
			addKnockBack(0.4f*dx, 0.4f*dy);
			lastAttackKnockBack = game.getGameTick();
		}
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (source && this.hitBox.type == HitBox.HitBoxType.BLOCKING && gameObject instanceof BasicMovingEntity) {
			((BasicMovingEntity) gameObject).addKnockBack(direction.getXDirection() * velocity / 20, direction.getYDirection() * velocity / 4);
		}
	}

	protected abstract boolean fallThroughBlock();

	@Override
	public final List<HitBox> getCollisionBoxes() {
		return hitBoxList;
	}

	public void addKnockBack(float kx, float ky) {
		this.kx += kx;
		this.ky += ky;
	}


}
