package game.gameObjects.entities;

import game.gameObjects.CollisionObject;
import game.Game;
import game.HitBox;
import game.gameObjects.Drawable;
import game.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicMovingEntity extends BasicDrawingEntity implements CollisionObject {
	private static final float MAX_SPEED = 0.25f;

	private List<HitBox> hitBoxList;
	protected float vx, vy;


	public BasicMovingEntity(HitBox hitBox) {
		super(hitBox);
		this.hitBoxList = new ArrayList<>();
		hitBoxList.add(hitBox);

		vx = 0;
		vy = 0;
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	@Override
	public void update() {
		vx = MathUtil.clamp(vx, -MAX_SPEED, MAX_SPEED);
		vy = MathUtil.clamp(vy, -MAX_SPEED, MAX_SPEED);

		move();
	}

	private void move() {
		List<CollisionObject> collides = new ArrayList<>();
		List<HitBox.HitBoxDirection> directions = new ArrayList<>();
		HitBox targetLocation = hitBox.clone();
		targetLocation.move(vx, vy);

		boolean collision;
		do {
			collision = false;
			for (CollisionObject collisionObject: Game.getInstance().getCollisionObjects()) {
				for (HitBox hitBox2: collisionObject.getCollisionBoxes()) {
					if (hitBox2.collides(targetLocation)) {
						HitBox.HitBoxDirection direction = hitBox.direction(hitBox2);

						collides.add(collisionObject);
						directions.add(direction);

						if (direction == HitBox.HitBoxDirection.COLLIDE) continue;
						collision = true;

						float ax = direction.getXDirection();
						float ay = direction.getYDirection();

						float distance = hitBox2.collisionDepth(targetLocation, ax, ay);

						ax *= distance;
						ay *= distance;

						vx -= ax;
						vy -= ay;
						targetLocation.move(-ax, -ay);
					}
				}
			}
		} while (collision);

		hitBox.move(vx, vy);

		for (int i = 0; i < collides.size(); i++) {
			CollisionObject collisionObject = collides.get(i);
			HitBox.HitBoxDirection direction = directions.get(i);

			collide(collisionObject, direction);
			collisionObject.collide(this, direction.invert());
		}
	}

	@Override
	public final List<HitBox> getCollisionBoxes() {
		return hitBoxList;
	}
}
