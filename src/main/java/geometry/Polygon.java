package geometry;

import enums.Colors;
import utils.VertexUtils;

public class Polygon {

	private Polygon() {
		// classe utilitária
	}

	public static float[] polygon(float centerX, float centerY, float radius, int points, float rotation) {
		// (centro + pontos) * 6 floats por ponto
		float[] vertices = new float[(points + 1) * 6];

		float angulo = 360.0f / points;

		// centro
		VertexUtils.addVertex(vertices, 0, centerX, centerY, Colors.getRandom());

		for (int i = 1; i <= points; i++) {
			float theta = (angulo * i) + rotation;

			float cos = (float) Math.cos(Math.toRadians(theta));
			float sin = (float) Math.sin(Math.toRadians(theta));

			float x = centerX + (radius * cos);
			float y = centerY + (radius * sin);

			VertexUtils.addVertex(vertices, i * 6, x, y, Colors.getRandom());
		}

		return vertices;
	}
}