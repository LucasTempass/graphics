package shaders;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL30.*;

public class Shader {

	public final int id;

	public Shader(String vertex, String fragment) {
		var vertexSource = getSource(vertex);
		var fragmentSource = getSource(fragment);

		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);

		int program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

		this.id = program;
	}

	private static String getSource(String resource) {
		try {
			Path path = Path.of("src/main/resources/shaders/" + resource);
			return Files.readString(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void use() {
		glUseProgram(id);
	}

	public void setBool(String name, boolean value) {
		glUniform1i(glGetUniformLocation(id, name), value ? 1 : 0);
	}

	void setInt(String name, int value) {
		glUniform1i(glGetUniformLocation(id, name), value);
	}

	void setFloat(String name, float value) {
		glUniform1f(glGetUniformLocation(id, name), value);
	}

	void setVec3(String name, float x, float y, float z) {
		glUniform3f(glGetUniformLocation(id, name), x, y, z);
	}

	public void setVec4(String name, float x, float y, float z, float w) {
		glUniform4f(glGetUniformLocation(id, name), x, y, z, w);
	}
}
