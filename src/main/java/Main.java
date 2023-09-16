import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import shaders.Shader;

import static geometry.Geometry.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

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
		glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		window = GLFW.glfwCreateWindow(
				800, 800,
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
			var pWidth = stack.mallocInt(1);
			var pHeight = stack.mallocInt(1);

			glfwGetFramebufferSize(window, pWidth, pHeight);
			glViewport(0, 0, pWidth.get(0), pHeight.get(0));
		}

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

		int count = 10;
		var pacmanVAO = setupGeometryWithEBO(pacman(0, 0, 0.5f, count, Colors.YELLOW.getColor()));

		float[] circleVertices = circle(0.075f, 0.25f, 0.05f, 0, Colors.YELLOW.getColor());
		var eye = setupGeometry(circleVertices);

		shader.use();

		while (!glfwWindowShouldClose(window)) {
			// buscar eventos de input
			glfwPollEvents();

			glClearColor(0.3f, 0.2f, 0.3f, 1f);
			glClear(GL_COLOR_BUFFER_BIT);

			glLineWidth(5.0f);

			shader.setVec4("inputColor", 1.0f, 1.0f, 1.0f, 1.0f);

			pacmanVAO.bind();

			glDrawArrays(GL_LINE_LOOP, 0, count);

			pacmanVAO.unbind();

			eye.bind();

			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

			glDrawArrays(GL_LINE_LOOP, 0, circleVertices.length / 6);

			eye.unbind();

			// troca o buffer ativo por aquele que acabamos de desenhar
			glfwSwapBuffers(window);
		}
	}
}
