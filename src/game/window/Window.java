package game.window;

import com.joml.matrix.Matrix4f;
import com.joml.utils.CamMath;
import com.joml.vector.Vector3f;
import game.Constants;
import game.Options;
import game.audio.AudioHandler;
import game.util.ErrorUtil;
import game.util.TextureHandler;
import game.util.TimeUtil;
import game.window.light.LightHandler;
import game.window.shader.ShaderHandler;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Window {
	private float aspect = Constants.WINDOW_WIDTH / Constants.WINDOW_HEIGHT;
	private long window, audioDevice, audioContext;			//OpenGL IDs for the window and the audio context
	private Keyboard keyboard;
	private Camera camera;
	private LightHandler lightHandler;
	private ShaderHandler shaderHandler;
	private int texture;									//OpenGL ID for the texture of the spriteSheet
	private int width, height;								//size of the window
	private boolean running = true;							//false, if the window should be closed

	private List<Drawable> drawables;						//list of drawables, that are used to draw the content of the screen
	private Queue<Drawable> toRemove;						//list of drawables, that should be added to the window after the next draw
	private Queue<Drawable> toAdd;							//list of drawables, that should be removed from the windw after the next draw
	private Vector3f cameraPosition, dir, up, target, right;	//Vectors used to calculate the camera direction
	private Matrix4f viewMatrix;								//transformation and rotation matrix of the camera
	private Matrix4f projectionMatrix;							//transformation and rotation matrix of the screen

	public Window() {
		System.out.println(String.format("LWJGL Version %s", Version.getVersion()));

		initOpenAL();
		initGLFW();
		initOpenGL();
	}

	/**
	 * redraw loop
	 * while the game is running, this method updates the keyboard, the window and the drawables
	 */
	public void run() {
		while (running) {
			Options.applyOptions(this);

			while (!toAdd.isEmpty()) {
				Drawable drawable = toAdd.poll();
				drawables.add(drawable);
				drawable.setup(this);
			}

			while (!toRemove.isEmpty()) {
				Drawable drawable = toRemove.poll();
				drawables.remove(drawable);
				drawable.cleanUp(this);
			}

			if (lightHandler.update()) {
				shaderHandler.setLightAmount(lightHandler.getLightAmount());
				shaderHandler.setLights(lightHandler.getLights());
				shaderHandler.setLightColors(lightHandler.getLightColors());
				shaderHandler.setMinimumBrightness(lightHandler.getMinimumBrightness());
			}

			drawables.sort((o1, o2) -> Float.compare(o2.getDrawingPriority(), o1.getDrawingPriority()));

			draw();
			keyboard.update();

			running = !GLFW.glfwWindowShouldClose(window);
		}

		cleanUp();
	}

	/**
	 * Redraws the content on the screen
	 */
	private void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		updateViewMatrix();

		long time = TimeUtil.getTime();
		shaderHandler.setTime(time);

		drawables.forEach(drawable -> drawable.draw(this, time));

		GLFW.glfwSwapBuffers(window);
	}

	/**
	 * Calculates the matrix for the camera
	 */
	private void updateViewMatrix() {
		boolean b = camera.update();
		if (camera.getZoom() == Double.POSITIVE_INFINITY || camera.getZoom() == Double.NEGATIVE_INFINITY || camera.getZoom() == Double.NaN) {
			camera.setZoom(1);
			b = camera.update();
		}

		if (viewMatrix.determinant() == 0 || b) {
			right.set((float) Math.cos(camera.getRotation()), (float) -Math.sin(camera.getRotation()), 0);
			target.set(camera.getX(), camera.getY(), 0);
			cameraPosition.set(camera.getX(), camera.getY(), 1 / camera.getZoom());
			dir.set(cameraPosition.x - target.x, cameraPosition.y - target.y, cameraPosition.z - target.z);


			Vector3f.cross(dir, right, up);
			up.normalize();

			CamMath.lookAt(cameraPosition, target, up, viewMatrix);

			shaderHandler.setViewMatrix(viewMatrix);
		}
	}

	/**
	 * Calculates the matrix for the screen
	 */
	private void updateProjectionMatrix() {
		aspect = width * 1.0f / height;

		CamMath.perspective(Constants.FOV, aspect, Constants.NEAR, Constants.FAR, projectionMatrix);

		shaderHandler.setProjectionMatrix(projectionMatrix);

		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Adds a drawable to this window
	 * @param drawable the drawable to be added
	 */
	public void addDrawable(Drawable drawable) {
		this.toAdd.add(drawable);
	}

	/**
	 * Removes a drawable from this window
	 * @param drawable the drawable to be removed
	 */
	public void removeDrawable(Drawable drawable) {
		this.toRemove.add(drawable);
	}

	/**
	 * Removes all used OpenGL instances and closes the window
	 */
	private void cleanUp() {
		shaderHandler.cleanUp();

		GL11.glDeleteTextures(texture);

		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();

		AudioHandler.cleanUp();
		ALC10.alcDestroyContext(audioContext);
		ALC10.alcCloseDevice(audioDevice);
	}

	/**
	 * Sets up the audio system
	 */
	private void initOpenAL() {
		audioDevice = ALC10.alcOpenDevice((ByteBuffer) null);
		ALCCapabilities cap = ALC.createCapabilities(audioDevice);

		audioContext = ALC10.alcCreateContext(audioDevice, (IntBuffer) null);
		ALC10.alcMakeContextCurrent(audioContext);

		AL.createCapabilities(cap);

		System.out.println(String.format("OpenAL Version %s", AL10.alGetString(AL10.AL_VERSION)));

		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	/**
	 * Sets up the graphics system
	 */
	private void initOpenGL() {
		GL.createCapabilities();
		System.out.println(String.format("OpenGL Version %s", GL11.glGetString(GL11.GL_VERSION)));

		GL11.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glDepthFunc(GL11.GL_LEQUAL);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		camera = new Camera();
		shaderHandler = new ShaderHandler();

		viewMatrix = new Matrix4f();
		cameraPosition = new Vector3f();
		dir = new Vector3f();
		target = new Vector3f();
		up = new Vector3f();
		right = new Vector3f(1, 0, 0);
		projectionMatrix = new Matrix4f();

		drawables = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		BufferedImage image = TextureHandler.getImagePng("textures");
		texture = TextureHandler.createImage(image);

		shaderHandler.setTexture(texture);
		shaderHandler.setTextureTotalBounds(image.getWidth(), image.getHeight());

		lightHandler = new LightHandler();

		updateProjectionMatrix();
	}

	/**
	 * Sets up the window
	 */
	private void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) ErrorUtil.printError("Initializing GLFW");

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

		window = GLFW.glfwCreateWindow(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.WINDOW_NAME, MemoryUtil.NULL, MemoryUtil.NULL);

		if (window == MemoryUtil.NULL) ErrorUtil.printError("Creating window");

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (action == GLFW.GLFW_RELEASE)
				GLFW.glfwSetWindowShouldClose(window, true);
		});

		keyboard = new Keyboard(window);

		GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;

			if (projectionMatrix != null) updateProjectionMatrix();
		});

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			width = pWidth.get(0);
			height = pHeight.get(0);

			GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(
					window,
					(videoMode.width() - pWidth.get(0)) / 2,
					(videoMode.height() - pHeight.get(0)) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(window);
	}

	public LightHandler getLightHandler() {
		return lightHandler;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public ShaderHandler getShaderHandler() {
		return shaderHandler;
	}

	public Camera getCamera() {
		return camera;
	}

	public float getAspectRatio() {
		return aspect;
	}

	public boolean isRunning() {
		return running;
	}

	public void setFullscreen(boolean value) {
		/*if (value) {
			GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, mode.width(), mode.height(), mode.refreshRate());
		} else {
			GLFW.glfwSetWindowSize(window, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		}*/
	}
}
