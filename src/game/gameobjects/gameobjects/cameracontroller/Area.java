package game.gameobjects.gameobjects.cameracontroller;

/**
 * Rectangle
 */
public class Area {
	private float x1, y1, x2, y2; 		//First and second Point

	public Area(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		if (this.x2 < this.x1) {
			float c = this.x2;
			this.x2 = this.x1;
			this.x1 = c;
		}

		if (this.y2 < this.y1) {
			float c = this.y2;
			this.y2 = y1;
			this.y1 = c;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return if the given coordinates are within this area
	 */
	public boolean contains(float x, float y) {
		return x1 <= x && y1 <= y && x2 > x && y2 > y;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}
}
