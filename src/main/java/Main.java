import static geometry.Geometry.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;
import shaders.Shader;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Main {

	private long window;

	public void run() {
		init();

		loop();

		cleanUp();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Não foi possível iniciar o GLFW");
		}


		GLFW.glfwDefaultWindowHints();
		glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		window = GLFW.glfwCreateWindow(
				300, 300,
				"Lucas Tempass Cerveira",
				NULL, NULL
		);

		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// configura o callback de teclado
		GLFW.glfwSetKeyCallback(window, (currentWindows, key, scancode, action, mods) -> {
			if (key != GLFW.GLFW_KEY_ESCAPE || action != GLFW.GLFW_RELEASE) {
				return;
			}

			// quando a tecla ESC for pressionada, sinaliza para fechar a janela
			GLFW.glfwSetWindowShouldClose(currentWindows, true);
		});

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetFramebufferSize(window, pWidth, pHeight);
			glViewport(0, 0, pWidth.get(0), pHeight.get(0));
		} //the stack frame is popped automatically

		GLFW.glfwMakeContextCurrent(window);

		// habilita v-sync
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(window);
	}

	public void cleanUp() {
		Callbacks.glfwFreeCallbacks(window);

		GLFW.glfwDestroyWindow(window);

		GLFW.glfwTerminate();

		glfwSetErrorCallback(null).free();
	}

	public static void main(String[] args) {
		new Main().run();
	}


	private void loop() {
		var shader = new Shader("Vertex.vsh", "Fragment.fsh");

		int triangleVAO = setupGeometry(triangle(0, 0, 0.5f));

		shader.use();

		while (!GLFW.glfwWindowShouldClose(window)) {
			glfwPollEvents();

			glClearColor(0.3f, 0.2f, 0.3f, 1f);
			glClear(GL_COLOR_BUFFER_BIT);

			shader.setVec4("inputColor", 1.0f, 1.0f, 1.0f, 1.0f);

			glBindVertexArray(triangleVAO);
			glDrawArrays(GL_TRIANGLES, 0, 3);

			glBindVertexArray(0);

			// end render
			GLFW.glfwSwapBuffers(window);
		}
	}
}
