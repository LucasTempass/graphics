package objects;

import static org.lwjgl.opengl.GL30.*;

public class VAO {

	private final int pointer;

	public VAO() {
		this.pointer = glGenVertexArrays();
	}

	public void setAttribute(int index, int size, int type, int stride, long offset) {
		glVertexAttribPointer(index, size, type, false, stride, offset);
		glEnableVertexAttribArray(index);
	}

	public int getPointer() {
		return pointer;
	}

	public void bind() {
		glBindVertexArray(pointer);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteVertexArrays(pointer);
	}

}
