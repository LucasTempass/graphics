import enums.Colors;
import game.Timer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import shaders.Shader;

import static geometry.Rectangle.rectangle;
import static geometry.Triangle.triangleIsosceles;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static utils.GeometryUtils.setupGeometryWithEBO;

public class Main {

	private long window;

	private static final Timer timer = new Timer();

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

		var triangleVAO = setupGeometryWithEBO(triangleIsosceles(0, 0, 0.5f, 80, 60));

		shader.use();

		while (!glfwWindowShouldClose(window)) {
			// buscar eventos de input
			glfwPollEvents();

			glClearColor(0.3f, 0.2f, 0.3f, 1f);
			glClear(GL_COLOR_BUFFER_BIT);

			glLineWidth(5.0f);

			shader.setVec4("inputColor", 1.0f, 1.0f, 1.0f, 1.0f);

			int length = 20;

			float size = 2.0f / length;

			timer.start();

			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					var x = -1.0f + i * size;

					var y = -1.0f + j * size;

					float[] rectangle = rectangle(x, y, size, size, Colors.getRandom());
					var squareVAO = setupGeometryWithEBO(rectangle);

					squareVAO.bind();

					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

					squareVAO.unbind();
				}
			}

			timer.stop();

			var elapsed = timer.getDuration();

			// calcula o tempo que deve dormir para manter o FPS
			var sleepTime = calcSleepTime(1, elapsed);

			if (sleepTime > 0) {
				try {
					Thread.sleep((long) sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// troca o buffer ativo por aquele que acabamos de desenhar
			glfwSwapBuffers(window);
		}
	}

	private static double calcSleepTime(int fps, double elapsedTime) {
		// 1 segundo em milisegundos
		return (1000 / (double) fps) - elapsedTime;
	}
}
