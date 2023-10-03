package geometry;

import enums.Colors;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static utils.VertexUtils.addVertexAt;

public class Rectangle {

	private Rectangle() {
		// classe utilitária
	}

	private static final int LEFT_UPPER_CORNER = 0;
	private static final int RIGHT_UPPER_CORNER = 1;
	private static final int RIGHT_LOWER_CORNER = 2;
	private static final int LEFT_LOWER_CORNER = 3;


	public static float[] rectangle(float x, float y, float width, float height) {
		var vertices = new float[4 * 6];

		// adiciona um cor aleatória para cada vértice
		addVertexAt(vertices, LEFT_LOWER_CORNER, x, y, Colors.getRandom());
		addVertexAt(vertices, RIGHT_LOWER_CORNER, x + width, y, Colors.getRandom());
		addVertexAt(vertices, RIGHT_UPPER_CORNER, x + width, y + height, Colors.getRandom());
		addVertexAt(vertices, LEFT_UPPER_CORNER, x, y + height, Colors.getRandom());

		return vertices;
	}

	public static float[] rectangle(float x, float y, float width, float height, Vector3f color) {
		var vertices = new float[4 * 8];

		// adiciona um cor aleatória para cada vértice
		addVertexAt(vertices, LEFT_LOWER_CORNER, x, y, color, new Vector2f(0, 0));
		addVertexAt(vertices, RIGHT_LOWER_CORNER, x + width, y, color, new Vector2f(1, 0));
		addVertexAt(vertices, RIGHT_UPPER_CORNER, x + width, y + height, color, new Vector2f(1, 1));
		addVertexAt(vertices, LEFT_UPPER_CORNER, x, y + height, color, new Vector2f(0, 1));

		return vertices;
	}

}
