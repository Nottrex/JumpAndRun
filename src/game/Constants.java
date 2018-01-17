package game;

import game.window.Keyboard;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String WINDOW_NAME = "JumpAndRun";
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final float FOV = 90;
	public static final float NEAR = 0.01f;
	public static final float FAR = 1000f;
	public static final int TPS = 60;

	public static final String COLOR_VERTEX_FILE = "colorVertexShader";
	public static final String COLOR_FRAGMENT_FILE = "colorFragmentShader";
	public static final String BASIC_VERTEX_FILE = "basicVertexShader";
	public static final String BASIC_FRAGMENT_FILE = "basicFragmentShader";
	public static final String PARTICLE_VERTEX_FILE = "particleVertexShader";
	public static final String PARTICLE_FRAGMENT_FILE = "particleFragmentShader";
	public static final String STATIC_VERTEX_FILE = "staticVertexShader";
	public static final String STATIC_FRAGMENT_FILE = "staticFragmentShader";
	public static final String TEXT_VERTEX_FILE = "textVertexShader";
	public static final String TEXT_FRAGMENT_FILE = "textFragmentShader";

	public static final float JUMP_ACCELERATION = 0.3f;
	public static final float MAX_WALKING_SPEED = 0.175f;
	public static final float DOWN_ACCELERATION = 0.04f;
	public static final float MAX_DOWN_SPEED = 0.5f;
	public static final float GRAVITY_ACCELERATION = 0.04f;
	public static final float MAX_GRAVITY_SPEED = 0.3f;
	public static final int MAX_JUMP_TICKS = 10;
	public static final float MAX_SPEED = 0.5f;

	public static final Map<String, Integer> DEFAULT_CONTROLS = new HashMap<>();
	public static final float DEAD_ZONE = 0.2f;

	public static int PIXEL_PER_TILE = 8;
	public static final float FONT_WIDTH = 0.3125f;
	public static final float FONT_SPACING = 0.375f; //FONT_WIDTH * 1.2 looks preferably
	public static final int MAX_PARTICLES = 1000;
	public static final int[] INDICES = new int[]{0, 2, 1, 0, 3, 2};
	public static final float[][] VERTEX_POS = new float[][]{{0, 0}, {0, 1}, {1, 1}, {1, 0}};

	public static final PrintStream ERR_STREAM = System.err;
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

	public static final int TIME_FRAC = 25;
	public static final float MIN_AMP = 0.0001f;
	public static final float DECAY = 0.8f;

	public static final float MIN_CAMERA_ZOOM = 0.125f;
	public static final int FADE_TIME = 60;

	public static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator;
	public static final String DATA_FILE_PATH = DATA_PATH + "options.yml";

	static {
		DEFAULT_CONTROLS.put("UP0", Keyboard.KEY_W);
		DEFAULT_CONTROLS.put("DOWN0", Keyboard.KEY_S);
		DEFAULT_CONTROLS.put("RIGHT0", Keyboard.KEY_D);
		DEFAULT_CONTROLS.put("LEFT0", Keyboard.KEY_A);
		DEFAULT_CONTROLS.put("SHAKE0", Keyboard.KEY_X);
		DEFAULT_CONTROLS.put("PARTICLE0", Keyboard.KEY_Z);
		DEFAULT_CONTROLS.put("INTERACT0", Keyboard.KEY_SPACE);

		DEFAULT_CONTROLS.put("UP1", Keyboard.KEY_UP);
		DEFAULT_CONTROLS.put("DOWN1", Keyboard.KEY_DOWN);
		DEFAULT_CONTROLS.put("RIGHT1", Keyboard.KEY_RIGHT);
		DEFAULT_CONTROLS.put("LEFT1", Keyboard.KEY_LEFT);
		DEFAULT_CONTROLS.put("SHAKE1", Keyboard.KEY_COMMA);
		DEFAULT_CONTROLS.put("PARTICLE1", Keyboard.KEY_PERIOD);
		DEFAULT_CONTROLS.put("INTERACT1", Keyboard.KEY_KP_0);

		for (int i = 0; i < 16; i++) {
			DEFAULT_CONTROLS.put("UP" + (i+2), Keyboard.GAMEPAD_1_BUTTON_A + 30*i);
			DEFAULT_CONTROLS.put("DOWN" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_DOWN + 30*i);
			DEFAULT_CONTROLS.put("RIGHT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_RIGHT + 30*i);
			DEFAULT_CONTROLS.put("LEFT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_LEFT + 30*i);
			DEFAULT_CONTROLS.put("SHAKE" + (i+2), Keyboard.GAMEPAD_1_BUTTON_X + 30*i);
			DEFAULT_CONTROLS.put("PARTICLE" + (i+2), Keyboard.GAMEPAD_1_BUTTON_Y + 30*i);
			DEFAULT_CONTROLS.put("INTERACT" + (i+2), Keyboard.GAMEPAD_1_BUTTON_A + 30*i);
		}
	}
}
