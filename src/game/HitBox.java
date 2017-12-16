package game;

public class HitBox {
	public float x, y, width, height;

	public HitBox(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void move(float mx, float my) {
		this.x += mx;
		this.y += my;
	}

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
		if ((y + height) <= box2.x) return HitBoxDirection.DOWN;
		return HitBoxDirection.UP;
	}

	/**
	 * @param box2 the moveable box
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

	@Override
	public HitBox clone() {
		return new HitBox(x, y, width, height);
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f), (%f, %f)]", x, y, x + width, y + height);
	}

	public enum HitBoxDirection {
		LEFT(-1, 0), UP(0, 1), RIGHT(1, 0), DOWN(0, -1), COLLIDE(0, 0);

		private float ax, ay;

		HitBoxDirection(float ax, float ay) {
			this.ax = ax;
			this.ay = ay;
		}

		public HitBoxDirection invert() {
			for (HitBoxDirection direction: HitBoxDirection.values()) {
				if (direction.ax == -ax && direction.ay == -ay) return direction;
			}
			return COLLIDE;
		}

		public float getXDirection() {
			if (ax == 0 && ay == 0) return ((float) Math.random()) * 2 - 1;
			return ax;
		}

		public float getYDirection() {
			if (ax == 0 && ay == 0) return ((float) Math.random()) * 2 - 1;
			return ay;
		}
	}
}
