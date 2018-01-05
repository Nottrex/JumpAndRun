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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	public static final int TPS = 60;

	private Window window;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private Player player;
	private ParticleSystem particleSystem;

	public Game(Window window) {
		this.window = window;

		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		player = new Player();
		particleSystem = new ParticleSystem();

		this.addGameObject(new Wall());
		this.addGameObject(player);
		this.addGameObject(particleSystem);
	}

	public void gameLoop() {
		boolean test = false;

		long time;
		while (window.isRunning()) {
			time = TimeUtil.getTime();
			Keyboard keyboard = window.getKeyboard();

			boolean test2 = keyboard.isPressed(Options.CONTROLS.get("PARTICLE"));
			if (!test && test2) {
				for (int i = 0; i < 1; i++) particleSystem.createParticle(ParticleType.EXPLOSION, player.getHitBox().getCenterX(), player.getHitBox().getCenterY(), (float)0, (float)0);
				//window.getLightHandler().setMinimumBrightnessSmooth((float) Math.random(), 1000);
				//window.getCamera().setRotationSmooth((float) Math.random() * (float) Math.PI * 2, 500);
			}
			test = test2;

			player.setJumping(keyboard.isPressed(Options.CONTROLS.get("UP")));
			player.setDown(keyboard.getPressed(Options.CONTROLS.get("DOWN")));
			player.setMx(keyboard.getPressed(Options.CONTROLS.get("RIGHT")) - keyboard.getPressed(Options.CONTROLS.get("LEFT")));
			if (keyboard.isPressed(Options.CONTROLS.get("SHAKE"))) {
				window.getCamera().addScreenshake(0.01f);
			}

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
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
