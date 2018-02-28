package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * an implementation of StaticDraw that has no collisions and which fades out if a player is behind this
**/
public class Background extends StaticDraw {
	private List<HitBox> hitBoxes;		//All hitboxes for the fade out

	public Background(Map<HitBox, String> hitBoxList, float drawingPriority) {
		super(drawingPriority);

		super.updateContent(hitBoxList);

		hitBoxes = new ArrayList<>(hitBoxList.keySet());

		boolean merge;

		//optimize hitboxes -> Merge adjecent hitboxes of similar size into one
		do {
			merge = false;

			for (int i = 0; i < hitBoxes.size()-1; i++) {
				HitBox hitBox1 = hitBoxes.get(i);
				for (int j = i+1; j < hitBoxes.size(); j++) {
					HitBox hitBox2 = hitBoxes.get(j);

					if ((hitBox1.y == hitBox2.y && hitBox1.height == hitBox2.height && ((hitBox1.x + hitBox1.width) >= hitBox2.x && (hitBox2.x + hitBox2.width) >= hitBox1.x)) || (hitBox1.x == hitBox2.x && hitBox1.width == hitBox2.width && ((hitBox1.y + hitBox1.height) >= hitBox2.y && (hitBox2.y + hitBox2.height) >= hitBox1.y))) {
						float x = Math.min(hitBox1.x, hitBox2.x);
						float y = Math.min(hitBox1.y, hitBox2.y);

						HitBox merged = new HitBox(x, y, Math.max(hitBox2.x + hitBox2.width - x, hitBox1.x + hitBox1.width - x), Math.max(hitBox2.y + hitBox2.height - y, hitBox1.y + hitBox1.height - y), hitBox1.type);
						hitBoxes.remove(hitBox1);
						hitBoxes.remove(hitBox2);
						hitBoxes.add(merged);

						merge = true;
						i--;
						break;
					}
				}
			}
		} while (merge);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	private float alpha = 1;

	@Override
	public void update(Game game) {

		if (coversPlayer(game)) {
			setAlphaSmooth(0.25f);
		} else {
			setAlphaSmooth(1);
		}
	}

	private void setAlphaSmooth(float alpha) {
		if (alpha != this.alpha) {
			this.alpha = alpha;
			super.setAlphaSmooth(alpha, 500);
		}
	}

	/**
	 * checks if a player is behind this layer
	 * @param game the game instance containing this object
	 * @return if this covers a player
	**/
	private boolean coversPlayer(Game game) {
		for (Player player: game.getPlayers()) {
			if (player.getDrawingPriority() > this.getDrawingPriority()) {
				for (HitBox hitBox: hitBoxes) {
					if (hitBox.collides(player.getHitBox())) return true;
				}
			}
		}

		return false;
	}
}
