package utils;

import org.joml.Vector3f;

import static enums.VertexProperty.*;

public class VertexUtils {

	// 3 floats para a posição e 3 floats para a cor
	public static final int PROPS_VERTEX = 6;

	private VertexUtils() {
		// classe utilitária
	}

	public static void addVertex(float[] vertices, int offset, float x, float y, Vector3f color) {
		// inicia no indice 0, com 3 floats para a coordenadas
		vertices[offset + X.getPosition()] = x;
		vertices[offset + Y.getPosition()] = y;
		vertices[offset + Z.getPosition()] = 0.0f;
		// últimos 3 indices para a cor
		vertices[offset + RED.getPosition()] = color.x;
		vertices[offset + GREEN.getPosition()] = color.y;
		vertices[offset + BLUE.getPosition()] = color.z;
	}

	public static void addVertexAt(float[] vertices, int index, float x, float y, Vector3f color) {
		var indexOffset = index * PROPS_VERTEX;
		addVertex(vertices, indexOffset, x, y, color);
	}

	public static float[] mergeVertices(float[] v0, float[] v1) {
		float[] vertices = new float[v0.length + v1.length];
		System.arraycopy(v0, 0, vertices, 0, v0.length);
		System.arraycopy(v1, 0, vertices, v0.length, v1.length);
		return vertices;
	}

}