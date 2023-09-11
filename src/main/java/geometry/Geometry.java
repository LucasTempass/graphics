package geometry;


import org.joml.Vector3f;
import utils.VAO;
import utils.VBO;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Geometry {

	public static final int FLOAT_SIZE = 4;

	private static final int POSITION_ATTRIBUTE = 0;
	private static final int COLOR_ATTRIBUTE = 1;
	private static final long COLORS_OFFSET = 3L;

	private static final int X_POSITION = 0;
	private static final int Y_POSITION = 1;
	private static final int Z_POSITION = 2;
	private static final int RED_POSITION = 3;
	private static final int GREEN_POSITION = 4;
	private static final int BLUE_POSITION = 5;

	public static VAO setupGeometry(float[] vertices) {
		var vao = new VAO();
		vao.bind();

		var vbo = new VBO();
		vbo.bind();

		// associa os dados do vertices ao VBO
		vbo.setData(vertices, GL_STATIC_DRAW);

		// usa os 3 primieros floats para a posição
		vao.setAttribute(POSITION_ATTRIBUTE, 3, GL_FLOAT, 6 * FLOAT_SIZE, 0);

		// usa os 3 ultimos floats para a cor
		vao.setAttribute(COLOR_ATTRIBUTE, 3, GL_FLOAT, 6 * FLOAT_SIZE, COLORS_OFFSET * FLOAT_SIZE);

		vbo.unbind();
		vao.unbind();

		return vao;
	}


	public static void addVertex(float[] vertices, int offset, float x, float y, Vector3f color) {
		// inicia no indice 0, com 3 floats para a coordenadas
		vertices[offset + X_POSITION] = x;
		vertices[offset + Y_POSITION] = y;
		vertices[offset + Z_POSITION] = 0.0f;
		// últimos 3 indices para a cor
		vertices[offset + RED_POSITION] = color.x;
		vertices[offset + GREEN_POSITION] = color.y;
		vertices[offset + BLUE_POSITION] = color.z;
	}

	public static float[] mergeVertices(float[] v0, float[] v1) {
		float[] vertices = new float[v0.length + v1.length];
		System.arraycopy(v0, 0, vertices, 0, v0.length);
		System.arraycopy(v1, 0, vertices, v0.length, v1.length);
		return vertices;
	}

	public static float[] triangle(float centerX, float centerY, float radius) {
		float[] vertices = new float[3 * 6];

		//angle between points
		float angle = 360.0f / 3;

		for (int i = 0; i < 3; i++) {
			//current angle
			float theta = angle * i + 90f;

			//calculate xy
			float x = centerX + radius * (float) Math.cos(Math.toRadians(theta));
			float y = centerY + radius * (float) Math.sin(Math.toRadians(theta));

			//add point
			addVertex(vertices, i * 6, x, y, Colors.get());
		}

		return vertices;
	}

	public static float[] circle(float centerX, float centerY, float radius, int points, float rotation) {
		float[] vertices = new float[points * 6];
		addVertex(vertices, 0, centerX, centerY, Colors.get());

		//angle between points
		float angle = 360.0f / (points - 2);

		for (int i = 0; i < points - 1; i++) {
			//current angle
			float theta = angle * i + rotation;

			//calculate xy
			float x = centerX + radius * (float) Math.cos(Math.toRadians(theta));
			float y = centerY + radius * (float) Math.sin(Math.toRadians(theta));

			//add point
			addVertex(vertices, (i + 1) * 6, x, y, Colors.get());
		}

		return vertices;
	}

	public static float[] spiral(float centerX, float centerY, int points, float loops, float radius, float rotation) {
		float[] vertices = new float[points * 6];

		addVertex(vertices, 0, centerX, centerY, Colors.get());

		//angle between points
		float ratio = loops / (points - 1);

		for (int i = 1; i < points; i++) {
			//current angle
			float j = i * ratio;
			float angle = j * (float) (Math.PI * 2 + Math.toRadians(rotation));
			float magnitude = j * radius / loops;

			//calculate xy
			float x = centerX + (float) Math.cos(angle) * magnitude;
			float y = centerY + (float) Math.sin(angle) * magnitude;

			//add point
			addVertex(vertices, i * 6, x, y, Colors.get());
		}

		return vertices;
	}

	public enum Colors {
		PINK(0xFF72AD),
		PURPLE(0xAD72FF),
		BLUE(0x72ADFF),
		CYAN(0x72FFAD),
		GREEN(0x72FF72),
		YELLOW(0xFFFF72),
		ORANGE(0xFFAD72),
		RED(0xFF7272);

		private static int last = (int) Math.floor(Math.random() * values().length);

		private final Vector3f color;

		Colors(int hex) {
			float r = ((hex >> 16) & 0xFF) / 255f;
			float g = ((hex >> 8) & 0xFF) / 255f;
			float b = (hex & 0xFF) / 255f;
			this.color = new Vector3f(r, g, b);
		}

		public static Vector3f get() {
			Colors[] colors = values();
			last++;
			last %= colors.length;
			return colors[last].color;
		}

		public static Vector3f random() {
			Colors[] colors = values();
			return colors[(int) (Math.random() * colors.length)].color;
		}
	}
}
