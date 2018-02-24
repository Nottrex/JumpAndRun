package game.gameobjects.gameobjects;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.AbstractGameObject;
import game.util.TextureHandler;
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
	private float x, y, size, drawingPriority;
	private boolean useCamera;
	private int letters, timer;
	private String text;
	private Color color;

	private float anchorX, anchorY;

	private boolean update;
	private float aspectRatio;

	private boolean hasAnimation;
	private int animationNumber = 0;
	private int drawTime;
	private boolean rising = true;

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

		drawTime = 0;
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
			updateBuffers(shader);
			update = false;
		}
		if (hasAnimation) {
			if (drawTime == 0) updateBuffers(shader);
			drawTime++;
			if (drawTime == 20) drawTime = 0;
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

	private void updateBuffers(TextShader shader) {
		//Create characters
		Map<HitBox, String> hitBoxList = new HashMap<>();

		for (int i = 65; i < 91; i++) {
			text = text.replaceAll(("<" + ((char) i) + ">"), String.valueOf((char) (i + 880)));
			text = text.replaceAll(("<" + ((char) (i + 32)) + ">"), String.valueOf((char) (i + 880)));
		}
		text = text.replace("<stick>", String.valueOf((char) 800));
		text = text.replace("<stick_left>", String.valueOf((char) 801));
		text = text.replace("<stick_right>", String.valueOf((char) 802));
		text = text.replace("<stick_up>", String.valueOf((char) 803));
		text = text.replace("<stick_down>", String.valueOf((char) 804));
		char[] chars = text.replaceAll("_", " ").toLowerCase().toCharArray();

		float fontHeight = size;
		float fontWidth =  fontHeight / Constants.FONT_ASPECT  / (useCamera ? 1 : aspectRatio);
		float fontSpacing = fontWidth * Constants.FONT_SPACING;

		float width = (chars.length-1) * fontSpacing + fontWidth;
		float height = fontHeight;

		float centeredX = x - width * anchorX;
		float centeredY = y - height * anchorY;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != ' ') {
				if (chars[i] > 900) {
					float keySize = 1.5f * fontHeight;
					hitBoxList.put(new HitBox(centeredX + i * fontSpacing - 0.25f * keySize, centeredY - 0.15f * keySize, keySize, keySize), "key");
					hitBoxList.put(new HitBox(centeredX + i * fontSpacing, centeredY, fontWidth, fontHeight), String.valueOf((char) (chars[i] - 848)));
				} else if (chars[i] > 800 && chars[i] < 805) {
					hasAnimation = true;
					float stickSize = 1.5f * fontHeight;
					String direction = "up";
					switch (chars[i]) {
						case 801: direction = "left"; break;
						case 802: direction = "right"; break;
						case 803: direction = "up"; break;
						case 804: direction = "down"; break;
					}
					hitBoxList.put(new HitBox(centeredX + i * fontSpacing - 0.25f * stickSize, centeredY - 0.15f * stickSize, stickSize, stickSize), "stick_" + direction + "_" + animationNumber);
					if (rising) animationNumber++;
					else  animationNumber--;
					if (animationNumber == 0) rising = true;
					else if (animationNumber == 3) rising = false;
				} else {
					hitBoxList.put(new HitBox(centeredX + i * fontSpacing, centeredY, fontWidth, fontHeight), String.valueOf(chars[i]));
				}
			}
		}

		//Load stuff into OpenGL
		letters = hitBoxList.size();

		FloatBuffer locations = BufferUtils.createFloatBuffer(letters * 2 * Constants.VERTEX_POS.length);
		FloatBuffer texLocations = BufferUtils.createFloatBuffer(letters * 2 * Constants.VERTEX_POS.length);
		IntBuffer indices = BufferUtils.createIntBuffer(letters * Constants.INDICES.length);

		int i = 0;
		java.util.List<HitBox> hitBoxList1 = hitBoxList.keySet().stream().sorted((o1, o2) -> Boolean.compare(hitBoxList.get(o2).equals("key"), hitBoxList.get(o1).equals("key"))).collect(Collectors.toList());
		for (HitBox hitBox: hitBoxList1) {
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
}

