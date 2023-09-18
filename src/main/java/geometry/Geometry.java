package geometry;


import enums.Colors;
import objects.EBO;
import objects.VAO;
import objects.VBO;

import static enums.VertexProperty.getColorOffsetInBytes;
import static enums.VertexProperty.getVertexSizeInBytes;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static utils.VertexUtils.addVertex;

public class Geometry {

	private static final int POSITION_ATTRIBUTE = 0;
	private static final int COLOR_ATTRIBUTE = 1;

	public static VAO setupGeometry(float[] vertices) {
		var vao = new VAO();
		vao.bind();

		var vbo = new VBO();
		vbo.bind();

		// associa os dados do vertices ao VBO
		vbo.setData(vertices, GL_STATIC_DRAW);

		// usa os 3 primeiros floats para a posição
		vao.setAttribute(POSITION_ATTRIBUTE, 3, GL_FLOAT, getVertexSizeInBytes(), 0);

		// usa os 3 ultimos floats para a cor
		vao.setAttribute(COLOR_ATTRIBUTE, 3, GL_FLOAT, getVertexSizeInBytes(), getColorOffsetInBytes());

		vbo.unbind();
		vao.unbind();

		return vao;
	}


	public static VAO setupGeometryWithEBO(float[] vertices) {
		var vao = new VAO();
		vao.bind();

		var vbo = new VBO();
		vbo.bind();

		// associa os dados do vertices ao VBO
		vbo.setData(vertices, GL_STATIC_DRAW);

		var ebo = new EBO();
		ebo.bind();

		// associa os dados dos elementos ao EBO
		var elements = getElementsFrom(vertices);
		ebo.setElements(elements);

		// offset é 0, pois são as primeiras propriedades
		vao.setAttribute(POSITION_ATTRIBUTE, 3, GL_FLOAT, getVertexSizeInBytes(), 0);

		vao.setAttribute(COLOR_ATTRIBUTE, 3, GL_FLOAT, getVertexSizeInBytes(), getColorOffsetInBytes());

		vbo.unbind();
		vao.unbind();
		ebo.unbind();

		return vao;
	}

	private static int[] getElementsFrom(float[] vertices) {
		// quantidade de vertices menos o centro
		var amount = (vertices.length / 6) - 1;

		// cada triangulo tem 3 vertices
		int verticesPerElement = 3;

		var elements = new int[amount * verticesPerElement];

		for (int i = 0; i < amount; i++) {
			var elementIndex = i * verticesPerElement;
			// quando ultrapassa o tamanho do array, volta para o primeiro vertice sem ser o centro
			elements[elementIndex + 2] = ((i + 1) % amount) + 1;
			elements[elementIndex + 1] = i + 1;
			// sempre utiliza o centro como vertice
			elements[elementIndex] = 0;
		}

		return elements;
	}


	public static float[] triangleEquilateral(float centerX, float centerY, float radius) {
		return polygon(centerX, centerY, radius, 3, 90);
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

	public static float[] polygon(float centerX, float centerY, float radius, int points, float rotation) {
		// (centro + pontos) * 6 floats por ponto
		float[] vertices = new float[(points + 1) * 6];

		float angulo = 360.0f / points;

		// centro
		addVertex(vertices, 0, centerX, centerY, Colors.getRandom());

		for (int i = 1; i <= points; i++) {
			float theta = (angulo * i) + rotation;

			float cos = (float) Math.cos(Math.toRadians(theta));
			float sin = (float) Math.sin(Math.toRadians(theta));

			float x = centerX + (radius * cos);
			float y = centerY + (radius * sin);

			addVertex(vertices, i * 6, x, y, Colors.getRandom());
		}

		return vertices;
	}

	public static float[] circle(float centerX, float centerY, float radius, int angles, float rotation) {
		// (centro + pontos) * 6 floats por ponto
		float[] vertices = new float[(angles + 1) * 6];

		// centro
		addVertex(vertices, 0, centerX, centerY, Colors.getRandom());

		for (int i = 1; i <= angles; i++) {
			float theta = i + rotation;

			float cos = (float) Math.cos(Math.toRadians(theta));
			float sin = (float) Math.sin(Math.toRadians(theta));

			float x = centerX + (radius * cos);
			float y = centerY + (radius * sin);

			addVertex(vertices, i * 6, x, y, Colors.getRandom());
		}

		return vertices;
	}

}
