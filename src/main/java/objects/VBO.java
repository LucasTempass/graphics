package objects;

import static org.lwjgl.opengl.GL15.*;

public class VBO {

	private final int pointer;

	public VBO() {
		this.pointer = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, pointer);
	}

	public void setData(float[] data, int usage) {
		glBufferData(GL_ARRAY_BUFFER, data, usage);
	}

	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public int getPointer() {
		return pointer;
	}

}
