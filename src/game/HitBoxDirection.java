package game;

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
