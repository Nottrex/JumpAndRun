package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wall extends StaticDraw implements CollisionObject {
	private List<HitBox> hitBoxes;

	public Wall(Map<HitBox, String> hitBoxList, float priority) {
		super(priority);

		super.updateContent(hitBoxList);

		hitBoxes = new ArrayList<>(hitBoxList.keySet());

		boolean merge;

		do {
			merge = false;

			for (int i = 0; i < hitBoxes.size()-1; i++) {
				HitBox hitBox1 = hitBoxes.get(i);
				for (int j = i+1; j < hitBoxes.size(); j++) {
					HitBox hitBox2 = hitBoxes.get(j);

					if (hitBox1.type == hitBox2.type && (hitBox1.y == hitBox2.y && hitBox1.height == hitBox2.height && ((hitBox1.x + hitBox1.width) >= hitBox2.x && (hitBox2.x + hitBox2.width) >= hitBox1.x)) || (hitBox1.type != HitBox.HitBoxType.HALF_BLOCKING && hitBox1.x == hitBox2.x && hitBox1.width == hitBox2.width && ((hitBox1.y + hitBox1.height) >= hitBox2.y && (hitBox2.y + hitBox2.height) >= hitBox1.y))) {
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

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getCollisionPriority() {
		return 10;
	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return hitBoxes;
	}
}
