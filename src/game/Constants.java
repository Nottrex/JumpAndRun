package game;

import game.window.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Map<String, Integer> DEFAULT_CONTROLS = new HashMap<>();

	static {
		DEFAULT_CONTROLS.put("UP0", Keyboard.KEY_W);
		DEFAULT_CONTROLS.put("DOWN0", Keyboard.KEY_S);
		DEFAULT_CONTROLS.put("RIGHT0", Keyboard.KEY_D);
		DEFAULT_CONTROLS.put("LEFT0", Keyboard.KEY_A);
		DEFAULT_CONTROLS.put("SHAKE0", Keyboard.KEY_X);
		DEFAULT_CONTROLS.put("PARTICLE0", Keyboard.KEY_Z);

		DEFAULT_CONTROLS.put("UP1", Keyboard.KEY_UP);
		DEFAULT_CONTROLS.put("DOWN1", Keyboard.KEY_DOWN);
		DEFAULT_CONTROLS.put("RIGHT1", Keyboard.KEY_RIGHT);
		DEFAULT_CONTROLS.put("LEFT1", Keyboard.KEY_LEFT);
		DEFAULT_CONTROLS.put("SHAKE1", Keyboard.KEY_COMMA);
		DEFAULT_CONTROLS.put("PARTICLE1", Keyboard.KEY_PERIOD);

		for (int i = 0; i < 16; i++) {
			DEFAULT_CONTROLS.put("UP" + (i+2), Keyboard.GAMEPAD_1_BUTTON_A + 30*i);
			DEFAULT_CONTROLS.put("DOWN" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_DOWN + 30*i);
			DEFAULT_CONTROLS.put("RIGHT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_RIGHT + 30*i);
			DEFAULT_CONTROLS.put("LEFT" + (i+2), Keyboard.GAMEPAD_1_LEFT_AXIS_LEFT + 30*i);
			DEFAULT_CONTROLS.put("SHAKE" + (i+2), Keyboard.GAMEPAD_1_BUTTON_B + 30*i);
			DEFAULT_CONTROLS.put("PARTICLE" + (i+2), Keyboard.GAMEPAD_1_BUTTON_Y + 30*i);
		}
	}
}
