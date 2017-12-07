package game;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

public class Window {
	private static final int WIDTH = 800, HEIGHT = 600;
	private static final String WINDOW_NAME = "JumpAndRun";

	private long window;

	public Window() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) throw new RuntimeException("Error while initializing GLFW");

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,  GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,  GLFW.GLFW_TRUE);

		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, WINDOW_NAME, MemoryUtil.NULL, MemoryUtil.NULL);

		if (window == MemoryUtil.NULL) throw new RuntimeException("Error while creating window");

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1); //Enable v-sync

		GLFW.glfwShowWindow(window);

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}

		cleanUp();
	}

	private void draw() {

	}

	private void cleanUp() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
}
