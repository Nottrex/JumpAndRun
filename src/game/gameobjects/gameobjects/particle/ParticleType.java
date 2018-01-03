package game.gameobjects.gameobjects.particle;

import game.data.Sprite;

public enum ParticleType {
	EXPLOSION(new Sprite(500, "player", "player2"), 60, 1f, 1f, false);

	private int lifeTime;
	private boolean gravity;
	private Sprite sprite;
	private float width, height;

	ParticleType(Sprite sprite, int lifeTime, float width, float height, boolean gravity) {
		this.lifeTime = lifeTime;
		this.gravity = gravity;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public boolean isGravity() {
		return gravity;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
}
