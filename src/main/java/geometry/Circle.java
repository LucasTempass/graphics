package geometry;

import enums.Colors;
import utils.VertexUtils;

public class Circle {

	private Circle() {
		// classe utilitária
	}

	public static float[] circle(float centerX, float centerY, float radius, int angles, float rotation) {
		// (centro + pontos) * 6 floats por ponto
		float[] vertices = new float[(angles + 1) * 6];

		// centro
		VertexUtils.addVertex(vertices, 0, centerX, centerY, Colors.getRandom());

		for (int i = 1; i <= angles; i++) {
			float theta = i + rotation;

			float cos = (float) Math.cos(Math.toRadians(theta));
			float sin = (float) Math.sin(Math.toRadians(theta));

			float x = centerX + (radius * cos);
			float y = centerY + (radius * sin);

			VertexUtils.addVertex(vertices, i * 6, x, y, Colors.getRandom());
		}

		return vertices;
	}

}