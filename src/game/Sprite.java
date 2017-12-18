package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite {
	private List<String> textures;
	private int time;

	public Sprite(int time, String... textures) {
		this.textures = new ArrayList<>(Arrays.asList(textures));
		this.time = time;
	}

	public String getTexture(long currentTime) {
		return textures.get((int) ((currentTime / time) % textures.size()));
	}

}
