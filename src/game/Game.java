package game;

import game.gameobjects.CollisionObject;
import game.window.Drawable;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.entities.Player;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.gameobjects.gameobjects.wall.Wall;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Keyboard;
import game.window.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	public static final int TPS = 60;

	private Window window;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Player> players;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private ParticleSystem particleSystem;

	public Game(Window window) {
		this.window = window;

		players = new ArrayList<>();
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		particleSystem = new ParticleSystem();

		this.addGameObject(new Wall());
		this.addGameObject(new Player());
		this.addGameObject(particleSystem);
	}

	public void gameLoop() {
		long time;
		while (window.isRunning()) {
			time = TimeUtil.getTime();
			Keyboard keyboard = window.getKeyboard();

			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);

				player.setJumping(keyboard.isPressed(Options.controls.get("UP" + i)));
				player.setMx(keyboard.getPressed(Options.controls.get("RIGHT" + i)) - keyboard.getPressed(Options.controls.get("LEFT" + i)));
				player.setDown(keyboard.getPressed(Options.controls.get("DOWN" + i)));
			}

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
				if (gameObject instanceof Player) players.add((Player) gameObject);
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
			}

			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(), o1.getPriority()));

			for (GameObject gameObject : gameObjects) {
				gameObject.update(this);
			}

			long newTime = TimeUtil.getTime();

			TimeUtil.sleep((int) (1000.0f / TPS - (newTime - time)));
		}

		cleanUp();
	}

	private void cleanUp() {
		Options.save();
	}

	public void addGameObject(GameObject gameObject) {
		toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		toRemove.add(gameObject);
	}

	public List<CollisionObject> getCollisionObjects() {
		return collisionObjects;
	}

	public Camera getCamera() {
		return window.getCamera();
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}
}
