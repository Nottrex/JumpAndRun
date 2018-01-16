package game.gameobjects.gameobjects.entities;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.entities.Ladder;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	protected boolean onGround, onLadder;
	private int jumpTicks;
	private boolean jumpingLastTick;

	protected float lastMX;
	protected float mx;
	protected boolean jumping;
	protected boolean down;

	public BasicWalkingEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);

		lastMX = -1;
		mx = 0;
		jumping = false;
		down = false;
		jumpingLastTick = false;

		jumpTicks = 0;
		onGround = false;
		onLadder = false;
	}

	@Override
	public void update(Game game) {
		vx = mx * Constants.MAX_WALKING_SPEED;
		if(Math.abs(mx) >= 0.2f) lastMX = mx;
		if (-vy < Constants.MAX_GRAVITY_SPEED) vy = Math.max(vy - Constants.GRAVITY_ACCELERATION, -Constants.MAX_GRAVITY_SPEED);
		if (down && -vy < Constants.MAX_DOWN_SPEED) vy = Math.max(vy - Constants.DOWN_ACCELERATION, -Constants.MAX_DOWN_SPEED);

		if (((onGround && !jumpingLastTick) || (jumpTicks < Constants.MAX_JUMP_TICKS && jumpTicks > 0)) && jumping) {
			vy = Constants.JUMP_ACCELERATION;
			jumpTicks++;
		} else {
			jumpTicks = 0;
		}
		if (onLadder) {
			vy = (jumping? 0.1f: -0.1f);
		}

		jumpingLastTick = jumping;

		onGround = false;
		onLadder = false;
		super.update(game);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (direction == HitBoxDirection.DOWN && velocity != 0) {
			if (velocity > Constants.MAX_GRAVITY_SPEED + 0.01f) {
				game.getCamera().addScreenshake(velocity / 15);

				game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.y, 0, 0);
			}

			onGround = true;
		}

		if (gameObject instanceof Ladder) {
			onLadder = true;
		}
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	@Override
	protected boolean fallThroughBlock() {
		return down;
	}
}
