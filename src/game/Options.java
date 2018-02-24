package game;

import game.util.ErrorUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Options {

	public static Map<String, Integer> controls = Constants.DEFAULT_CONTROLS;
	public static boolean startWithTutorial = true;
	public static boolean fullscreen = false;
	public static float effectVolume = 1.0f;
	public static float musicVolume = 1.0f;

	static {
		new File(Constants.DATA_PATH).mkdirs();
	}

	public static void save() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);

		Map<String, Object> data = new HashMap<>();
		data.put("controls", controls);
		data.put("startWithTutorial", false);
		data.put("fullscreen", fullscreen);
		data.put("effectVolume", effectVolume);
		data.put("musicVolume", musicVolume);

		try {
			yaml.dump(data, new FileWriter(new File(Constants.DATA_FILE_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);
		if (new File(Constants.DATA_FILE_PATH).exists()) {
			try {
				Map<String, Object> data = (Map<String, Object>) yaml.load(new FileInputStream(new File(Constants.DATA_FILE_PATH)));

				if (data.containsKey("CONTROLS")) {
					Map<String, Integer> con = (Map<String, Integer>) data.get("CONTROLS");
					for (String s: con.keySet()) {
						controls.put(s, con.get(s));
					}
				}
				if (data.containsKey("startWithTutorial")) startWithTutorial = (Boolean) data.get("startWithTutorial");
				if (data.containsKey("fullscreen")) fullscreen = (Boolean) data.get("fullscreen");
				if (data.containsKey("effectVolume")) effectVolume = (float) ((double) data.get("effectVolume"));
				if (data.containsKey("musicVolume")) musicVolume = (float) ((double) data.get("musicVolume"));
			
			} catch (FileNotFoundException e) {
				ErrorUtil.printError("Loading options: " + e.toString());
			}
		}
	}
	
	public static void applyOptions(Game g) {
		/*g.getAudioPlayer().setEffectVolume(effectVolume);
		g.getAudioPlayer().setMusicVolume(musicVolume);*/
		g.getWindow().setFullscreen(fullscreen);
	}
}
