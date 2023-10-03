package utils;


import objects.EBO;
import objects.VAO;
import objects.VBO;

import static enums.VertexProperty.getColorOffsetInBytes;
import static enums.VertexProperty.getVertexSizeInBytes;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class GeometryUtils {

	private GeometryUtils() {
		// classe utilitária
	}

	private static final int POSITION_ATTRIBUTE = 0;
	private static final int COLOR_ATTRIBUTE = 1;
	private static final int TEXT_ATTRIBUTE = 2;

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

		vao.setAttribute(TEXT_ATTRIBUTE, 2, GL_FLOAT, getVertexSizeInBytes(), 6 * Float.BYTES);

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

		vao.setAttribute(TEXT_ATTRIBUTE, 2, GL_FLOAT, getVertexSizeInBytes(), 6 * Float.BYTES);

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

}
