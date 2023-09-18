package geometry;

import enums.Colors;

import static utils.VertexUtils.addVertex;

public class Triangle {

	private Triangle() {
		// classe utilitária
	}

	public static float[] triangleEquilateral(float centerX, float centerY, float radius) {
		return Polygon.polygon(centerX, centerY, radius, 3, 90);
	}

	public static float[] triangleIsosceles(float centerX, float centerY, float radius, float angle, float rotation) {
		float[] vertices = new float[3 * 6];

		float smallAngle = 180f - (angle * 2f);

		// centro
		addVertex(vertices, 0, centerX, centerY, Colors.getRandom());

		float thetaAlpha = smallAngle + rotation;

		float cosAlpha = (float) Math.cos(Math.toRadians(thetaAlpha));
		float sinAlpha = (float) Math.sin(Math.toRadians(thetaAlpha));

		float xAlpha = centerX + (radius * cosAlpha);
		float yAlpha = centerY + (radius * sinAlpha);

		addVertex(vertices, 6, xAlpha, yAlpha, Colors.getRandom());

		float thetaBeta = (smallAngle * 2) + rotation;

		float cosBeta = (float) Math.cos(Math.toRadians(thetaBeta));
		float sinBeta = (float) Math.sin(Math.toRadians(thetaBeta));

		float xBeta = centerX + (radius * cosBeta);
		float yBeta = centerY + (radius * sinBeta);

		addVertex(vertices, 6 * 2, xBeta, yBeta, Colors.getRandom());

		return vertices;
	}

}
