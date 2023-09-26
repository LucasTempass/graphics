import game.Block;
import game.Direction;
import game.Game;
import game.Timer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import shaders.Shader;

import static geometry.Rectangle.rectangle;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static utils.GeometryUtils.setupGeometryWithEBO;

public class Main {

	private static final Game GAME = new Game(30, 30);

	private static final Timer timer = new Timer();

	private static long window;

	public static void main(String[] args) {
		init();

		loop();

		cleanUp();
	}

	private static void init() {
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
				1080, 1080,
				"The Snake Game",
				NULL, NULL
		);

		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// configura o callback de teclado
		GLFW.glfwSetKeyCallback(window, (currentWindows, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				// quando a tecla ESC for pressionada, sinaliza para fechar a janela
				GLFW.glfwSetWindowShouldClose(currentWindows, true);
				return;
			}

			if (key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
				GAME.onCommand(Direction.LEFT);
			}

			if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
				GAME.onCommand(Direction.RIGHT);
			}

			if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
				GAME.onCommand(Direction.UP);
			}

			if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
				GAME.onCommand(Direction.DOWN);
			}
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

	public static void cleanUp() {
		Callbacks.glfwFreeCallbacks(window);

		GLFW.glfwDestroyWindow(window);

		GLFW.glfwTerminate();

		glfwSetErrorCallback(null).free();
	}


	private static void loop() {
		var shader = new Shader("Vertex.vsh", "Fragment.fsh");

		shader.use();

		while (!glfwWindowShouldClose(window)) {
			// buscar eventos de input
			glfwPollEvents();

			timer.start();

			glClearColor(1f, 1f, 1f, 1f);
			glClear(GL_COLOR_BUFFER_BIT);

			glLineWidth(5.0f);

			GAME.play();

			Block[][] matrix = GAME.toMatrix();
			float expansionFactor = 1.5f;

			float size = (1.0f / matrix.length) * expansionFactor;

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					Block block = matrix[i][j];

					if (block == null) {
						continue;
					}

					var color = block.getColor();

					shader.setVec4("inputColor", color.x, color.y, color.z, 1.0f);

					// como iniciamos com 1, precisamos subtrair 1 para que o primeiro elemento seja 0
					float padding = (1 - (2 - expansionFactor) / 2) + size;

					// inicia com 1 pois quando chegar no último elemento, o valor será 1
					float x = (((float) (j + 1) / matrix.length) * expansionFactor) - padding;
					float y = padding - (((float) (i + 1) / matrix.length) * expansionFactor);

					var squareVAO = setupGeometryWithEBO(rectangle(x, y, size, size, color));

					squareVAO.bind();

					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

					squareVAO.unbind();
				}
			}

			timer.stop();

			var elapsed = timer.getDuration();

			// calcula o tempo que deve dormir para manter o FPS
			var sleepTime = calcSleepTime(8, elapsed);

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
