package game.gameobjects.gameobjects.cameracontroller;

import game.Constants;
import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.Player;
import game.window.Window;

import java.util.ArrayList;
import java.util.List;

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
		float minX = 10000000;
		float minY = 10000000;
		float maxX = -10000000;
		float maxY = -10000000;

		List<Player> players = game.getPlayers();

		boolean outside = false;

		if (players.size() > 0) {
			for (Player p: players) {
				Area area = getAreaAt(p.getHitBox().getCenterX(), p.getHitBox().getCenterY());

				if (area == null) {
					outside = true;
					if (minX > p.getHitBox().getCenterX())
						minX = p.getHitBox().getCenterX();
					if (minY > p.getHitBox().getCenterY())
						minY = p.getHitBox().getCenterY();

					if (maxX < p.getHitBox().getCenterX())
						maxX = p.getHitBox().getCenterX();
					if (maxY < p.getHitBox().getCenterY())
						maxY = p.getHitBox().getCenterY();
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

		float zoom = Math.min(Math.min(2 / height, 2 * Window.aspect / width), Constants.MIN_CAMERA_ZOOM);

		if (posX != lastX || posY != lastY || zoom != lastZoom) {
			if (outside) {
				game.getCamera().setZoom(zoom);
				game.getCamera().setPosition(posX, posY);
			} else if (start) {
				game.getCamera().setPosition(spawnX, spawnY);
				game.getCamera().setZoom(0.2f);
				game.getCamera().setPositionSmooth(posX, posY, 1000);
				game.getCamera().setZoomSmooth(zoom, 1000);
				start = false;
			} else {
				game.getCamera().setPositionSmooth(posX, posY, 500);
				game.getCamera().setZoomSmooth(zoom, 500);
			}

			lastX = posX;
			lastY = posY;
			lastZoom = zoom;
		}
	}

	private Area getAreaAt(float x, float y) {
		for (Area area: cameraAreas) {
			if (area.contains(x, y)) return area;
		}

		return null;
	}

	@Override
	public float getPriority() {
		return -1;
	}

	public void addCameraArea(Area area) {
		cameraAreas.add(area);
	}

	public void setSpawn(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}
}