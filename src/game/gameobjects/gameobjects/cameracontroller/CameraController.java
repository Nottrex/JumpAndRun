package game.gameobjects.gameobjects.cameracontroller;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Move the camera to show the player
 */
public class CameraController extends AbstractGameObject {
	private List<Area> cameraAreas;
	private float spawnX, spawnY;

	private float lastX, lastY, lastZoom;
	private boolean start;

	public CameraController() {
		cameraAreas = new ArrayList<>();

		spawnX = 0;
		spawnY = 0;

		lastX = 0;
		lastY = 0;
		lastZoom = 0.1f;
		start = true;
	}

	@Override
	public void update(Game game) {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;

		List<HitBox> visibleObjects = game.getPlayers().stream().map(Player::getHitBox).collect(Collectors.toList());

		List<HitBox> deadBodies = game.getDeadBodyHandler().getDeadBodies().entrySet().stream().filter(e -> e.getValue() >= game.getGameTick() - 60).map(e -> e.getKey().getHitBox()).collect(Collectors.toList());
		visibleObjects.addAll(deadBodies);

		boolean outside = false;

		if (visibleObjects.size() > 0) {
			for (HitBox h : visibleObjects) {
				Area area = getAreaAt(h.getCenterX(), h.getCenterY());

				if (area == null) {
					outside = true;
					if (minX > h.getCenterX())
						minX = h.getCenterX();
					if (minY > h.getCenterY())
						minY = h.getCenterY();

					if (maxX < h.getCenterX())
						maxX = h.getCenterX();
					if (maxY < h.getCenterY())
						maxY = h.getCenterY();
				} else {
					if (minX > area.getX1())
						minX = area.getX1();
					if (minY > area.getY1())
						minY = area.getY1();

					if (maxX < area.getX2())
						maxX = area.getX2();
					if (maxY < area.getY2())
						maxY = area.getY2();

				}
			}
		} else {
			Area area = getAreaAt(spawnX, spawnY);

			if (area == null) {
				outside = true;
				if (minX > spawnX)
					minX = spawnX;
				if (minY > spawnY)
					minY = spawnY;

				if (maxX < spawnX)
					maxX = spawnX;
				if (maxY < spawnY)
					maxY = spawnY;
			} else {
				if (minX > area.getX1())
					minX = area.getX1();
				if (minY > area.getY1())
					minY = area.getY1();

				if (maxX < area.getX2())
					maxX = area.getX2();
				if (maxY < area.getY2())
					maxY = area.getY2();
			}
		}

		float posX = (maxX + minX) / 2.0f;
		float posY = (maxY + minY) / 2.0f;

		float width = (maxX - minX) * 1.25f;
		float height = (maxY - minY) * 1.25f;

		//calculate zoom
		float zoom = Math.min(Math.min(2 / height, 2 * game.getAspectRatio() / width), Constants.MIN_CAMERA_ZOOM);

		//Update camera position
		if (posX != lastX || posY != lastY || zoom != lastZoom) {
			if (outside) {
				//if the player leaves the camera zones he should still be shown
				game.getCamera().setZoom(zoom);
				game.getCamera().setPosition(posX, posY);
			} else if (start) {
				//On level change the camera should be zoomed in the spawn
				game.getCamera().setPosition(spawnX, spawnY);
				game.getCamera().setZoom(0.2f);
				game.getCamera().setPositionSmooth(posX, posY, 1000);
				game.getCamera().setZoomSmooth(zoom, 1000);
				start = false;
			} else {
				//move the camera smooth into the new zone
				game.getCamera().setPositionSmooth(posX, posY, 500);
				game.getCamera().setZoomSmooth(zoom, 500);
			}

			lastX = posX;
			lastY = posY;
			lastZoom = zoom;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return the first area that contains the given coordinates
	 */
	private Area getAreaAt(float x, float y) {
		for (Area area : cameraAreas) {
			if (area.contains(x, y)) return area;
		}

		return null;
	}

	@Override
	public float getPriority() {
		return -1;
	}

	/**
	 * Adds a new camera area
	 * @param area the area to be added
	 */
	public void addCameraArea(Area area) {
		cameraAreas.add(area);
	}

	public void setSpawn(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}
}
