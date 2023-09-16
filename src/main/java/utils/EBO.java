package utils;

import static org.lwjgl.opengl.GL15.*;

public class EBO {


	private final int pointer;

	public EBO() {
		this.pointer = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, pointer);
	}

	public void setElements(int[] data) {
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

}
