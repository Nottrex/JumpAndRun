package game;

import game.window.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Map<String, Integer> PRIMARY_CONTROLS = new HashMap<>();
	public static final Map<String, Integer> SECONDARY_CONTROLS = new HashMap<>();
	public static final Map<String, Integer> CONTROLLER_CONTROLS = new HashMap<>();

	static {
		PRIMARY_CONTROLS.put("UP", Keyboard.KEY_W);
		PRIMARY_CONTROLS.put("DOWN", Keyboard.KEY_S);
		PRIMARY_CONTROLS.put("RIGHT", Keyboard.KEY_D);
		PRIMARY_CONTROLS.put("LEFT", Keyboard.KEY_A);
		PRIMARY_CONTROLS.put("SHAKE", Keyboard.KEY_X);
		PRIMARY_CONTROLS.put("PARTICLE", Keyboard.KEY_Z);

		SECONDARY_CONTROLS.put("UP", Keyboard.KEY_UP);
		SECONDARY_CONTROLS.put("DOWN", Keyboard.KEY_DOWN);
		SECONDARY_CONTROLS.put("RIGHT", Keyboard.KEY_RIGHT);
		SECONDARY_CONTROLS.put("LEFT", Keyboard.KEY_LEFT);
		SECONDARY_CONTROLS.put("SHAKE", Keyboard.KEY_COMMA);
		SECONDARY_CONTROLS.put("PARTICLE", Keyboard.KEY_PERIOD);

		CONTROLLER_CONTROLS.put("UP", Keyboard.GAMEPAD_AXIS_LEFT_Y_RIGHT);
		CONTROLLER_CONTROLS.put("DOWN", Keyboard.GAMEPAD_AXIS_LEFT_Y_LEFT);
		CONTROLLER_CONTROLS.put("RIGHT", Keyboard.GAMEPAD_AXIS_LEFT_X_RIGHT);
		CONTROLLER_CONTROLS.put("LEFT", Keyboard.GAMEPAD_AXIS_LEFT_X_LEFT);
		CONTROLLER_CONTROLS.put("SHAKE", Keyboard.GAMEPAD_BUTTON_X);
		CONTROLLER_CONTROLS.put("PARTICLE", Keyboard.GAMEPAD_BUTTON_Y);
	}
}
