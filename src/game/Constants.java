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
	public static final float FOV = 90;					//field of view
	public static final float NEAR = 0.01f;					//minimum distance of objects to camera
	public static final float FAR = 1000f;					//maximum distance of objects to camera
	public static final int TPS = 60;					//ticks per second

	public static final float JUMP_ACCELERATION = 0.355f;
	public static final float MAX_WALKING_SPEED = 0.175f;
	public static final float DOWN_ACCELERATION = 0.04f;
	public static final float MAX_DOWN_SPEED = 0.5f;
	public static final float GRAVITY_ACCELERATION = 0.04f;
	public static final float GRAVITY_ACCELERATION_JUMPING = GRAVITY_ACCELERATION / 2;
	public static final float MAX_GRAVITY_SPEED = 0.3f;

	public static int PIXEL_PER_TILE = 8;
	public static final float FONT_ASPECT = 1.4f; 				//font height : font width
	public static final float FONT_SPACING = 1.2f; 				//1 + spacing : font width
	public static final int MAX_PARTICLES = 1000;				//maximum particle count displayed simultaneously
	public static final int[] INDICES = new int[]{0, 2, 1, 0, 3, 2};
	public static final float[][] VERTEX_POS = new float[][]{{0, 0}, {0, 1}, {1, 1}, {1, 0}};

	public static final PrintStream ERR_STREAM = System.err;
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

	public static final int TIME_FRAC = 25;					//time after which the amplitude reduces by DECAY
	public static final float DECAY = 0.8f;					//percentage of screenshake after TIME_FRAC
	public static final float MIN_AMP = 0.0001f;				//minimum amplitude for screenshakes before getting dumped 

	public static final float MIN_CAMERA_ZOOM = 0.125f;			//nearest camera distance
	public static final int FADE_TIME = 60;					//tick amount of camera fading

	public static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator;
	public static final String DATA_FILE_PATH = DATA_PATH + "options.yml";	//directory of settings file
	public static final String SYS_PREFIX = String.valueOf(Math.random());	//internal prefix for private data

	public static final Map<String, Integer> DEFAULT_CONTROLS = new HashMap<>();	//default controls for all 18 players
	public static final float DEAD_ZONE = 0.2f;					//dead zone for controller triggers

	static {
		DEFAULT_CONTROLS.put("UP0", Keyboard.KEY_W);
		DEFAULT_CONTROLS.put("DOWN0", Keyboard.KEY_S);
		DEFAULT_CONTROLS.put("RIGHT0", Keyboard.KEY_D);
		DEFAULT_CONTROLS.put("LEFT0", Keyboard.KEY_A);
		DEFAULT_CONTROLS.put("INTERACT0", Keyboard.KEY_E);
		DEFAULT_CONTROLS.put("ATTACK0", Keyboard.KEY_SPACE);

		DEFAULT_CONTROLS.put("UP1", Keyboard.KEY_UP);
		DEFAULT_CONTROLS.put("DOWN1", Keyboard.KEY_DOWN);
		DEFAULT_CONTROLS.put("RIGHT1", Keyboard.KEY_RIGHT);
		DEFAULT_CONTROLS.put("LEFT1", Keyboard.KEY_LEFT);
		DEFAULT_CONTROLS.put("INTERACT1", Keyboard.KEY_RIGHT_SHIFT);
		DEFAULT_CONTROLS.put("ATTACK1", Keyboard.KEY_RIGHT_CONTROL);

		for (int i = 0; i < 16; i++) {
			DEFAULT_CONTROLS.put("UP" + (i+2), Keyboard.GAMEPAD_1_BUTTON_A + 30*i);
			DEFAULT_CONTROLS.put("DOWN" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_DOWN + 30*i);
			DEFAULT_CONTROLS.put("RIGHT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_RIGHT + 30*i);
			DEFAULT_CONTROLS.put("LEFT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_LEFT + 30*i);
			DEFAULT_CONTROLS.put("INTERACT" + (i+2), Keyboard.GAMEPAD_1_BUTTON_Y + 30*i);
			DEFAULT_CONTROLS.put("ATTACK" + (i+2), Keyboard.GAMEPAD_1_BUTTON_B + 30*i);
		}
	}
}
