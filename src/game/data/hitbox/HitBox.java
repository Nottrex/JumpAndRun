package game.data.hitbox;

public class HitBox {
	public float x, y, width, height;
	public HitBoxType type;

	public HitBox(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = HitBoxType.BLOCKING;
	}

	public HitBox(float x, float y, float width, float height, HitBoxType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	/**
	 * moves this hitbox
	 * @param mx the x movement
	 * @param my the y movement
	 */
	public void move(float mx, float my) {
		this.x += mx;
		this.y += my;
	}

	/**
	 * checks for collision with a second hitbox
	 * @param box2 the second hitbox
	 * @return if they collide
	 */
	public boolean collides(HitBox box2) {
		return ((x + width) > box2.x && (box2.x + box2.width) > x && (y + height) > box2.y && (box2.y + box2.height) > y);
	}

	/**
	 * @param box2 the second box
	 * @return the direction of the given Box relative to this
	 * <p>
	 * LUR
	 * L#R
	 * LDR
	 * (# - This box)
	 */
	public HitBoxDirection direction(HitBox box2) {
		if (collides(box2)) return HitBoxDirection.COLLIDE;

		if ((x + width) <= box2.x) return HitBoxDirection.RIGHT;
		if ((box2.x + box2.width) <= x) return HitBoxDirection.LEFT;
		if ((y + height) <= box2.y) return HitBoxDirection.UP;
		return HitBoxDirection.DOWN;
	}

	/**
	 * @param box2 the movable box
	 * @param ax   the x part of the direction where the object should be moved
	 * @param ay   the y part of the direction where the object should be moved
	 * @return the amount of the given direction the second object has to be moved to avoid collision
	 */
	public float collisionDepth(HitBox box2, float ax, float ay) {
		if (!collides(box2)) return 0;

		float distance = Float.MAX_VALUE;

		if (ax != 0) {
			if (ax < 0) {
				distance = ((x + width) - box2.x) / (-ax);
			} else {
				distance = ((box2.x + box2.width) - x) / ax;
			}
		}
		if (ay != 0) {
			if (ay < 0) {
				distance = Math.min(((y + height) - box2.y) / (-ay), distance);
			} else {
				distance = Math.min(((box2.y + box2.height) - y) / ay, distance);
			}
		}

		return distance;
	}

	/**
	 * Calculates the distance of the center of a second hitbox to this center
	 * @param hitBox2 the second hitbox
	 * @return the distance
	 */
	public float distance(HitBox hitBox2) {
		return (float) Math.sqrt(Math.pow(hitBox2.getCenterX() - getCenterX(), 2) + Math.pow(hitBox2.getCenterY() - getCenterY(), 2));
	}

	/**
	 * @return the center of the x value of this hitbox
	 */
	public float getCenterX() {
		return x + width / 2;
	}

	/**
	 * @return the center of the y value of this hitbox
	 */
	public float getCenterY() {
		return y + height / 2;
	}

	/**
	 * @return a second hitbox with the same position and size
	 */
	@Override
	public HitBox clone() {
		return new HitBox(x, y, width, height);
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f), (%f, %f)]", x, y, x + width, y + height);
	}

	public enum HitBoxType {
		BLOCKING, HALF_BLOCKING, NOT_BLOCKING

	}
}
