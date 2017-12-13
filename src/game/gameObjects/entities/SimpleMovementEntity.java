package game.gameObjects.entities;

import game.gameObjects.CollisionObject;
import game.Game;
import game.HitBox;
import game.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleMovementEntity implements CollisionObject {
	private static final float SPEED = 0.05f;
	private static final float GRAVITY_ACCELERATION = 0.01f;
	private static final float JUMP_ACCELERATION = 0.3f;
	private static final float MAX_SPEED = 0.5f;

	private HitBox hitBox;
	private List<HitBox> hitBoxList;
	private float vx, vy;

	private boolean onGround;

	private float mx;
	private float jumping;

	public SimpleMovementEntity(HitBox hitBox) {
		this.hitBox = hitBox;
		this.hitBoxList = new ArrayList<>();
		hitBoxList.add(hitBox);

		vx = 0;
		vy = 0;
		mx = 0;
		jumping = 0;
		onGround = false;
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(float jumping) {
		this.jumping = MathUtil.clamp(jumping, 0, 1);
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	@Override
	public void update() {
		vx = mx * SPEED;
		vy -= GRAVITY_ACCELERATION;
		if (onGround) vy += jumping * JUMP_ACCELERATION;

		vx = MathUtil.clamp(vx, -MAX_SPEED, MAX_SPEED);
		vy = MathUtil.clamp(vy, -MAX_SPEED, MAX_SPEED);

		move();
	}

	private void move() {
		List<CollisionObject> collides = new ArrayList<>();
		HitBox targetLocation = hitBox.clone();
		targetLocation.move(vx, vy);

		boolean collision;
		do {
			collision = false;
			for (CollisionObject collisionObject: Game.getInstance().getCollisionObjects()) {
				for (HitBox hitBox2: collisionObject.getCollisionBoxes()) {
					if (hitBox2.collides(targetLocation)) {
						collision = true;
						if (!collides.contains(collisionObject)) collides.add(collisionObject);

						HitBox.HitBoxDirection direction = hitBox2.direction(hitBox);
						if (direction == HitBox.HitBoxDirection.COLLIDE) continue;
						if (direction == HitBox.HitBoxDirection.UP) onGround = true;

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

		for (CollisionObject collisionObject: collides) {
			collide(collisionObject);
			collisionObject.collide(this);
		}
	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return hitBoxList;
	}
}
