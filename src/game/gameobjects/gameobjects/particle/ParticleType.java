package game.gameobjects.gameobjects.particle;

import game.data.Sprite;

public enum ParticleType {
	EXPLOSION(new Sprite(55, "explosion_1", "explosion_2", "explosion_3", "explosion_4", "explosion_5", "explosion_6", "explosion_7", "explosion_8", "explosion_9"), 30, 2f, 2f, false),
	CLOUD(new Sprite(100, "cloud_0", "cloud_1", "cloud_2", "cloud_3", "cloud_4"), 29, 1.9375f, 1.5f, false),
	RED(new Sprite(100, "particle_red"), 15, 0.125f, 0.125f, false),
	LIGHT_GREEN(new Sprite(100, "particle_light_green"), 15, 0.125f, 0.125f, false),
	GREEN(new Sprite(100, "particle_green"), 15, 0.125f, 0.125f, false),
	DARK_GREEN(new Sprite(100, "particle_dark_green"), 15, 0.125f, 0.125f, false),
	GRAY(new Sprite(100, "particle_gray"), 15, 0.125f, 0.125f, false),
	DARK_GRAY(new Sprite(100, "particle_dark_gray"), 15, 0.125f, 0.125f, false),
	LIGHT_GRAY(new Sprite(100, "particle_light_gray"), 15, 0.125f, 0.125f, false),
	WHITE(new Sprite(100, "particle_white"), 15, 0.125f, 0.125f, false),
	LIGHT_BLUE(new Sprite(100, "particle_light_blue"), 15, 0.125f, 0.125f, false),
	BLUE(new Sprite(100, "particle_blue"), 15, 0.125f, 0.125f, false),
	DARK_RED(new Sprite(100, "particle_dark_red"), 15, 0.125f, 0.125f, false),
	ORANGE(new Sprite(100, "particle_orange"), 15, 0.125f, 0.125f, false),
	YELLOW(new Sprite(100, "particle_yellow"), 15, 0.125f, 0.125f, false),
	LIGHT_LIGHT_BROWN(new Sprite(100, "particle_light_light_brown"), 15, 0.125f, 0.125f, false),
	LIGHT_BROWN(new Sprite(100, "particle_light_brown"), 15, 0.125f, 0.125f, false),
	BROWN(new Sprite(100, "particle_brown"), 15, 0.125f, 0.125f, false),
	DARK_BROWN(new Sprite(100, "particle_dark_brown"), 15, 0.125f, 0.125f, false);

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
