package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.gameobjects.gameobjects.wall.Wall;

/**
 * A barrel that explodes when hit
 */
public class ExplosiveBarrel extends BasicStaticEntity {
	private static Sprite stand = new Sprite(100, "ex_barrel_stand_s");
	private static Sprite ground = new Sprite(100, "ex_barrel_ground_s");
	private static Sprite explodeStand = new Sprite(300, "ex_barrel_stand_l", "ex_barrel_stand_e_0", "ex_barrel_stand_e_1");
	private static Sprite explodeGround = new Sprite(300, "ex_barrel_ground_l", "ex_barrel_ground_e_0", "ex_barrel_ground_e_1");

	private boolean onGround;
	private boolean exploding;
	private int startTick;

	public ExplosiveBarrel(float x, float y, float drawingPriority, boolean onGround) {
		super(new HitBox(x + 0.125f, y + 0.125f + 1f / 16, 0.75f, 0.625f), drawingPriority);

		hitBox.type = HitBox.HitBoxType.BLOCKING;

		this.onGround = onGround;
		setSprite((onGround ? stand : ground));
		exploding = false;
		startTick = 0;
	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (interactionType == InteractionType.ATTACK) {
			setSprite((onGround ? explodeStand : explodeGround));
			startTick = game.getGameTick();
			exploding = true;
		}
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {
		if (exploding && game.getGameTick() - startTick > 50) {
			game.getParticleSystem().createParticle(ParticleType.EXPLOSION, this.hitBox.x + 0.5f, this.hitBox.y + 0.5f, 0, 0);
			game.getCamera().addScreenshake(0.1f);

			HitBox attackHitBox = new HitBox(hitBox.getCenterX() + -1.5f, hitBox.getCenterY() - 1.5f, 3f,  3f);

			for (CollisionObject collisionObject : game.getCollisionObjects()) {
				if (collisionObject.equals(this)) continue;
				for (HitBox hitBox : collisionObject.getCollisionBoxes()) {
					if (hitBox.collides(attackHitBox)) {
						collisionObject.interact(this, attackHitBox, InteractionType.ATTACK);
						break;
					}
				}
			}

			game.removeGameObject(this);
		}
	}
}
