package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.script.Tree;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.util.MathUtil;

public class Slider extends BasicMovingEntity {
	private Sprite idle = new Sprite(100, "box");

	private Tree onRelocate;
	private float minX, maxX;
	private float currentX;
	private float y;

	public Slider(float minX, float maxX, float y, float drawingPriority, Tree onRelocate) {
		super(new HitBox(minX, y, 1, 1), drawingPriority);

		this.y = y;
		this.minX = minX;
		this.maxX = maxX;
		this.currentX = minX;
		this.onRelocate = onRelocate;

		setSprite(idle);
	}

	public float getSliderValue() {
		return (hitBox.x - minX) / (maxX - minX);
	}

	public void setOnRelocate(Tree onRelocate) {
		this.onRelocate = onRelocate;
	}

	@Override
	public void update(Game game) {
		super.update(game);
		hitBox.y = y;
		hitBox.x = MathUtil.clamp(hitBox.x, minX, maxX);
		if (hitBox.x != currentX && onRelocate != null) onRelocate.get(game);
		currentX = hitBox.x;
	}

	@Override
	protected boolean fallThroughBlock() {
		return false;
	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public float getPriority() {
		return 0;
	}
}
