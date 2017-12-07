package game;

import game.util.ErrorUtil;
import game.util.TextureHandler;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Window {
	private static final int WIDTH = 800, HEIGHT = 600;
	private static final String WINDOW_NAME = "JumpAndRun";

	private long window;

	public Window() {
		System.out.println(String.format("LWJGL Version %s", Version.getVersion()));

		initGLFW();
		initOpenGL();

		run();
	}

	public void run() {
		while (!GLFW.glfwWindowShouldClose(window)) {
			draw();

			try {
				Thread.sleep(1);
			} catch (Exception e) {}
		}

		cleanUp();
	}

	private void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}

	private void cleanUp() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	private void initOpenGL() {
		GL.createCapabilities();
		System.out.println(String.format("OpenGL Version %s", GL11.glGetString(GL11.GL_VERSION)));

		GL11.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
	}

	private void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) ErrorUtil.printError("Initializing GLFW");

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,  GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,  GLFW.GLFW_TRUE);

		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, WINDOW_NAME, MemoryUtil.NULL, MemoryUtil.NULL);

		if (window == MemoryUtil.NULL) ErrorUtil.printError("Creating window");

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(window);
	}
}
