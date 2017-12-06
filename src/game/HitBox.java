package game;

public class HitBox {
	private float x1, y1, y2, x2;

	public HitBox(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		checkValues();
	}

	public void setX1(float x1) {
		this.x1 = x1;
		checkValues();
	}

	public void setX2(float x2) {
		this.x2 = x2;
		checkValues();
	}

	public void setY1(float y1) {
		this.y1 = y1;
		checkValues();
	}

	public void setY2(float y2) {
		this.y2 = y2;
		checkValues();
	}

	public void setFirstPoint(float x1, float y1) {
		this.x1 = x1;
		this.y1 = y1;
		checkValues();
	}

	public void setSecondPoint(float x2, float y2) {
		this.x2 = x2;
		this.y2 = y2;
		checkValues();
	}

	public void setPoints(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		checkValues();
	}

	public void move(float mx, float my) {
		this.x1 += mx;
		this.y1 += my;
		this.x2 += mx;
		this.y2 += my;
	}

	private void checkValues() {
		if (x1 > x2) {
			float temp = this.x1;
			this.x1 = this.x2;
			this.x2 = temp;
		}
		if (y1 > y2) {
			float temp = this.y1;
			this.y1 = this.y2;
			this.y2 = temp;
		}
	}

	public boolean collides(HitBox box2) {
		return (x2 > box2.x1 &&  box2.x2 > x1 &&  y2 > box2.y1 && box2.y2 > y1);
	}

	/**
	 * @param box2 the second box
	 * @return the direction of the given Box relative to this
	 *
	 * LUR
	 * L#R
	 * LDR
	 * (# - This box)
	 */
	public HitBoxDirection direction(HitBox box2) {
		if (collides(box2)) return HitBoxDirection.COLLIDE;

		if (x2 <= box2.x1) return HitBoxDirection.RIGHT;
		if (box2.x2 <= x1) return HitBoxDirection.LEFT;
		if (y2 <= box2.x1) return HitBoxDirection.DOWN;
		return HitBoxDirection.UP;
	}

	/**
	 *
	 * @param box2 the moveable box
	 * @param ax the x part of the direction where the object should be moved
	 * @param ay the y part of the direction where the object should be moved
	 * @return the amount of the given direction the second object has to be moved to avoid collision
	 */
	public float collisionDepth(HitBox box2, float ax, float ay) {
		if (!collides(box2)) return 0;

		float distance = Float.MAX_VALUE;

		if (ax != 0) {
			if (ax < 0) {
				distance = (box2.x2 - x1) / ax;
			} else {
				distance = (box2.x2 - x1) / ax;
			}
		}
		if (ay != 0) {
			if (ay < 0) {
				distance = Math.min((x2 - box2.x1) / ax, distance);
			} else {
				distance = Math.min((box2.x2 - x1) / ax, distance);
			}
		}

		return distance;
	}

	@Override
	protected Object clone() {
		return new HitBox(x1, y1, x2, y2);
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f), (%f, %f)]", x1, y1, x2, y2);
	}

	private enum HitBoxDirection {
		LEFT, UP, RIGHT, DOWN, COLLIDE,
	}
}
