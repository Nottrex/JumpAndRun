package game.gameobjects.gameobjects.wall;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sign extends StaticDraw {
	private List<HitBox> hitBoxes;
	private Background background;



	public Sign(float x, float y, String text, float drawingPriority) {
		super(drawingPriority);

		float fontWidth = Constants.FONT_WIDTH;
		float fontHeight = fontWidth * 1.4f;
		float fontSpacing = Constants.FONT_SPACING;

		Map<HitBox, String> hitBoxList = new HashMap<>();
		char[] chars = text.replaceAll("-_", " ").toLowerCase().toCharArray();
		float centeredX = x - chars.length * fontSpacing / 2;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != ' ') {
				hitBoxList.put(new HitBox(centeredX + i * fontSpacing, y, fontWidth, fontHeight), String.valueOf(chars[i]));
			}
		}

		float backgroundHeight = fontHeight * 13 / 7;
		float backgroundTailWidth = backgroundHeight * 2 / 13;
		float yOffset = (backgroundHeight - fontHeight) / 2;


		Map<HitBox, String> backgroundHitboxList = new HashMap<>();
		backgroundHitboxList.put(new HitBox(centeredX - backgroundTailWidth, y - yOffset, backgroundTailWidth, backgroundHeight), "sign_left");
		backgroundHitboxList.put(new HitBox(centeredX + chars.length * fontSpacing, y - yOffset, backgroundTailWidth, backgroundHeight), "sign_right");
		for (int i = 0; i < chars.length; i++) {
			backgroundHitboxList.put(new HitBox(centeredX + i * fontSpacing, y - yOffset, fontSpacing, backgroundHeight), "sign_middle");
		}
		background = new Background(backgroundHitboxList,getDrawingPriority() + 0.1f);

		super.updateContent(hitBoxList);

		hitBoxes = new ArrayList<>(hitBoxList.keySet());

		boolean merge;

		do {
			merge = false;

			for (int i = 0; i < hitBoxes.size()-1; i++) {
				HitBox hitBox1 = hitBoxes.get(i);
				for (int j = i+1; j < hitBoxes.size(); j++) {
					HitBox hitBox2 = hitBoxes.get(j);

					if ((hitBox1.y == hitBox2.y && hitBox1.height == hitBox2.height && ((hitBox1.x + hitBox1.width) >= hitBox2.x && (hitBox2.x + hitBox2.width) >= hitBox1.x)) || (hitBox1.x == hitBox2.x && hitBox1.width == hitBox2.width && ((hitBox1.y + hitBox1.height) >= hitBox2.y && (hitBox2.y + hitBox2.height) >= hitBox1.y))) {
						float xPos = Math.min(hitBox1.x, hitBox2.x);
						float yPos = Math.min(hitBox1.y, hitBox2.y);

						HitBox merged = new HitBox(xPos, yPos, Math.max(hitBox2.x + hitBox2.width - xPos, hitBox1.x + hitBox1.width - xPos), Math.max(hitBox2.y + hitBox2.height - yPos, hitBox1.y + hitBox1.height - yPos), hitBox1.type);
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

		game.addGameObject(background);
	}

	private void setAlphaSmooth(float alpha) {
		if (alpha != this.alpha) {
			this.alpha = alpha;
			super.setAlphaSmooth(alpha, 500);
		}
	}

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

