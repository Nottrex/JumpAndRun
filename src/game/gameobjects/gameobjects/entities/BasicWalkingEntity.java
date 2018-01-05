package game.gameobjects.gameobjects.entities;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	private static final float SPEED = 0.175f;
	private static final float JUMP_ACCELERATION = 0.3f;
	private static final float DOWN_ACCELERATION = 0.04f;
	private static final float MAX_DOWN_SPEED = 0.5f;
	private static final float GRAVITY_ACCELERATION = 0.04f;
	private static final float MAX_GRAVITY_SPEED = 0.3f;
	private static final int MAX_JUMP_TICKS = 10;

	private boolean onGround;
	private int jumpTicks;
	private boolean jumpingLastTick;

	private float mx;
	private boolean jumping;
	private float down;

	public BasicWalkingEntity(HitBox hitBox) {
		super(hitBox);

		mx = 0;
		jumping = false;
		down = 0;

		jumpTicks = 0;
		jumpingLastTick = false;
		onGround = false;
	}

	@Override
	public void update(Game game) {
		vx = mx * SPEED;
		if (-vy < MAX_GRAVITY_SPEED) vy = Math.max(vy - GRAVITY_ACCELERATION, -MAX_GRAVITY_SPEED);
		if (-vy < MAX_DOWN_SPEED) vy = Math.max(vy - down * DOWN_ACCELERATION, -MAX_DOWN_SPEED);

		if (((onGround && !jumpingLastTick) || (jumpTicks < MAX_JUMP_TICKS && jumpTicks > 0)) && jumping) {
			vy = JUMP_ACCELERATION;
			jumpTicks++;
		} else {
			jumpTicks = 0;
		}

		jumpingLastTick = jumping;

		onGround = false;
		super.update(game);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (direction == HitBoxDirection.DOWN) {
			if (velocity > MAX_GRAVITY_SPEED) {
				game.getCamera().addScreenshake(velocity / 15);

				game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.y, 0, 0);
			}

			onGround = true;
		}
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setDown(float down) {
		this.down = MathUtil.clamp(down, 0, 1);
	}
}
