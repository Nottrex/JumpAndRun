package game.gameobjects.gameobjects;

import game.Constants;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.AbstractGameObject;
import game.util.TextureHandler;
import game.util.TimeUtil;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.TextShader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Text extends AbstractGameObject implements Drawable {
	public static Sprite coin = new Sprite(100, "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle1_0", "coin_idle1_1", "coin_idle1_2", "coin_idle1_3", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle2_0", "coin_idle2_1", "coin_idle2_0");
	private Sprite stick_up = new Sprite(400, "stick_up_0", "stick_up_1", "stick_up_2", "stick_up_3", "stick_up_2", "stick_up_1");
	private Sprite stick_down = new Sprite(400, "stick_down_0", "stick_down_1", "stick_down_2", "stick_down_3", "stick_down_2", "stick_down_1");
	private Sprite stick_left = new Sprite(400, "stick_left_0", "stick_left_1", "stick_left_2", "stick_left_3", "stick_left_2", "stick_left_1");
	private Sprite stick_right = new Sprite(400, "stick_right_0", "stick_right_1", "stick_right_2", "stick_right_3", "stick_right_2", "stick_right_1");
	private Sprite stick_vertical = new Sprite(400, "stick_up_0", "stick_up_1", "stick_up_2", "stick_up_3", "stick_up_2", "stick_up_1", "stick_down_0", "stick_down_1", "stick_down_2", "stick_down_3", "stick_down_2", "stick_down_1");
	private Sprite stick_horizontal = new Sprite(400, "stick_left_0", "stick_left_1", "stick_left_2", "stick_left_3", "stick_left_2", "stick_left_1", "stick_right_0", "stick_right_1", "stick_right_2", "stick_right_3", "stick_right_2", "stick_right_1");
	private Sprite stick = new Sprite(400, "stick_up_0", "stick_up_1", "stick_up_2", "stick_up_3", "stick_up_2", "stick_up_1", "stick_down_0", "stick_down_1", "stick_down_2", "stick_down_3", "stick_down_2", "stick_down_1");
	private Sprite buttonA = new Sprite(100, "button_a");
	private Sprite buttonB = new Sprite(100, "button_b");
	private Sprite buttonX = new Sprite(100, "button_x");
	private Sprite buttonY = new Sprite(100, "button_y");
	private Sprite key = new Sprite(100, "key");

	private float x, y, size, drawingPriority;
	private boolean useCamera;
	private int letters, timer;
	private String text;
	private Color color;

	private float anchorX, anchorY;

	private boolean update;
	private float aspectRatio;

	private Map<Integer, Sprite> animations;
	private long startTime;

	private int vao, vao2;
	private int locationVBO, texLocationVBO, indicesVBO;


	public Text(float drawingPriority, String text, float x, float y, float size, boolean useCamera) {
		this.drawingPriority = drawingPriority;
		this.x = x;
		this.y = y;
		this.size = size;
		this.useCamera = useCamera;
		update = false;
		letters = 0;
		anchorY = 0;
		anchorX = 0f;
		color = Color.WHITE;
		timer = -1;
		setText(text);

		startTime = TimeUtil.getTime();
		animations = new HashMap<>();
	}

	public Text(float drawingPriority, String text, float x, float y, float size, boolean useCamera, Color c) {
		this(drawingPriority, text, x, y, size, useCamera);
		color = c;
	}

	public Text(float drawingPriority, String text, float x, float y, float size, boolean useCamera, float anchorX, float anchorY) {
		this(drawingPriority, text, x, y, size, useCamera);

		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	public Text(float drawingPriority, String text, float x, float y, float size, boolean useCamera, float anchorX, float anchorY, Color c) {
		this(drawingPriority, text, x, y, size, useCamera, anchorX, anchorY);
		color = c;
	}

	public void setText(String text) {
		this.text = text;
		this.update = true;
	}

	@Override
	public void setup(Window window) {
		vao = GL30.glGenVertexArrays();
		vao2 = GL30.glGenVertexArrays();

		locationVBO = GL15.glGenBuffers();
		texLocationVBO = GL15.glGenBuffers();
		indicesVBO = GL15.glGenBuffers();
	}

	@Override
	public void draw(Window window, long time) {
		TextShader shader = (TextShader) window.getShaderHandler().getShader(ShaderType.TEXT_SHADER);

		shader.start();

		if (!useCamera && text != null && window.getAspectRatio() != aspectRatio) {
			update = true;
		}

		if (update) {
			aspectRatio = window.getAspectRatio();
			updateBuffers(shader, time);
			update = false;
		}
		if (animations.size() > 0) {
			updateBuffers(shader, time);
		}

		if (letters == 0) return;

		shader.setUseCameraLocation(useCamera);
		shader.setColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);

		GL30.glBindVertexArray(vao);

		GL11.glDrawElements(GL11.GL_TRIANGLES, letters * Constants.INDICES.length, GL11.GL_UNSIGNED_INT, 0);

		GL30.glBindVertexArray(vao2);
	}

	@Override
	public void cleanUp(Window window) {
		if (indicesVBO != 0) {
			GL15.glDeleteBuffers(indicesVBO);
		}

		if (locationVBO != 0) {
			GL15.glDeleteBuffers(locationVBO);
		}

		if (texLocationVBO != 0) {
			GL15.glDeleteBuffers(texLocationVBO);
		}

		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteVertexArrays(vao2);
	}

	private void updateBuffers(TextShader shader, long currentTime) {
		//Create characters
		Map<HitBox, String> hitBoxList = new HashMap<>();

		char[] chars = trimText();

		float fontHeight = size;
		float fontWidth =  fontHeight / Constants.FONT_ASPECT  / (useCamera ? 1 : aspectRatio);
		float fontSpacing = fontWidth * Constants.FONT_SPACING;

		float width = (chars.length-1) * fontSpacing + fontWidth;
		float height = fontHeight;

		float centeredX = x - width * anchorX;
		float centeredY = y - height * anchorY;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != ' ') hitBoxList.put(new HitBox(centeredX + i * fontSpacing, centeredY, fontWidth, fontHeight), String.valueOf(chars[i]));
		}

		//Load stuff into OpenGL
		letters = hitBoxList.size() + animations.size();

		FloatBuffer locations = BufferUtils.createFloatBuffer(letters * 2 * Constants.VERTEX_POS.length);
		FloatBuffer texLocations = BufferUtils.createFloatBuffer(letters * 2 * Constants.VERTEX_POS.length);
		IntBuffer indices = BufferUtils.createIntBuffer(letters * Constants.INDICES.length);

		int i = 0;
		for (int index: animations.keySet()) {
			Rectangle texture = animations.get(index).getTexture(startTime, currentTime);
			float size = 1.5f * fontHeight;
			float x = centeredX + index * fontSpacing - 0.25f * size;
			float y = centeredY - 0.15f * size;

			for (float[] v : Constants.VERTEX_POS) {
				locations.put(x + v[0] * (size / (useCamera ? 1 : aspectRatio)));
				locations.put(y + v[1] * size);

				texLocations.put(texture.x + v[0] * texture.width);
				texLocations.put(texture.y + (1 - v[1]) * texture.height);
			}

			for (int ind : Constants.INDICES) {
				indices.put(i * Constants.VERTEX_POS.length + ind);
			}

			i++;
		}
		for (HitBox hitBox: hitBoxList.keySet()) {
			Rectangle texture = TextureHandler.getSpriteSheetBounds("textures_" + hitBoxList.get(hitBox));

			for (float[] v : Constants.VERTEX_POS) {
				locations.put(hitBox.x + v[0] * hitBox.width);
				locations.put(hitBox.y + v[1] * hitBox.height);

				texLocations.put(texture.x + v[0] * texture.width);
				texLocations.put(texture.y + (1 - v[1]) * texture.height);
			}

			for (int ind : Constants.INDICES) {
				indices.put(i * Constants.VERTEX_POS.length + ind);
			}

			i++;
		}

		locations.flip();
		texLocations.flip();
		indices.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, locations, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texLocations, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao);

		GL20.glEnableVertexAttribArray(shader.getLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationVBO);
		GL20.glVertexAttribPointer(shader.getLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL20.glEnableVertexAttribArray(shader.getTexLocationLocation());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texLocationVBO);
		GL20.glVertexAttribPointer(shader.getTexLocationLocation(), 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(vao2);
	}

	@Override
	public float getDrawingPriority() {
		return drawingPriority;
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void init(Game game) {
		super.init(game);
	}

	@Override
	public void update(Game game) {
		if (timer == 0) game.removeGameObject(this);
		else if (timer > 0) timer--;
	}

	public void setPosition(float x, float y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;

		if (text != null) update = true;
	}

	public void setUseCamera(boolean useCamera) {
		this.useCamera = useCamera;
	}

	public void setAnchorPoint(float anchorX, float anchorY) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;

		if (text != null) update = true;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setTimer(int ticks) {
		this.timer = ticks;
	}

	public static float getWidth(String text, float size) {
		char[] chars = text.replaceAll("_", " ").toLowerCase().toCharArray();


		float fontHeight = size;
		float fontWidth =  fontHeight / Constants.FONT_ASPECT;
		float fontSpacing = fontWidth * Constants.FONT_SPACING;

		return (chars.length-1) * fontSpacing + fontWidth;
	}

	public static float getWidth(String text, float size, float aspectRatio) {
		char[] chars = text.replaceAll("_", " ").toLowerCase().toCharArray();


		float fontHeight = size;
		float fontWidth =  fontHeight / Constants.FONT_ASPECT / aspectRatio;
		float fontSpacing = fontWidth * Constants.FONT_SPACING;

		return (chars.length-1) * fontSpacing + fontWidth;
	}

	public static float getHeight(String text, float size) {
		return size;
	}

	private char[] trimText() {
		String printText = text.toLowerCase();

		animations.clear();
		while (printText.contains("<") && printText.contains(">") && printText.indexOf("<") < printText.indexOf(">")) {
			String object = printText.substring(printText.indexOf("<") + 1, printText.indexOf(">"));
			String replacement = " ";
			int index = printText.indexOf("<");

			switch (object) {
				case "stick":
					animations.put(index, stick);
					break;
				case "stick_up":
					animations.put(index, stick_up);
					break;
				case "stick_down":
					animations.put(index, stick_down);
					break;
				case "stick_left":
					animations.put(index, stick_left);
					break;
				case "stick_right":
					animations.put(index, stick_right);
					break;
				case "stick_horizontal":
					animations.put(index, stick_horizontal);
					break;
				case "stick_vertical":
					animations.put(index, stick_vertical);
					break;
				case "button_a":
					animations.put(index, buttonA);
					break;
				case "button_b":
					animations.put(index, buttonB);
					break;
				case "button_x":
					animations.put(index, buttonX);
					break;
				case "button_y":
					animations.put(index, buttonY);
					break;
				case "coin":
					animations.put(index, coin);
					break;
				case "key_up":
					animations.put(index, key);
					replacement = "↑";
					break;
				case "key_down":
					animations.put(index, key);
					replacement = "↓";
					break;
				case "key_left":
					animations.put(index, key);
					replacement = "←";
					break;
				case "key_right":
					animations.put(index, key);
					replacement = "→";
					break;
				default:
					if (Character.isLetter(object.charAt(0)) && object.length() == 1) {
						replacement = String.valueOf(object.charAt(0));
						animations.put(index, key);
					} else if (object.startsWith("#")) {
						replacement = String.valueOf(game.getValue(object.substring(1)));
					}
			}
			printText = printText.replaceFirst("<" + object + ">", replacement);
		}

		return printText.replaceAll("_", " ").toCharArray();
	}
}

