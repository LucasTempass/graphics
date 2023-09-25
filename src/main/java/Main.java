import enums.Colors;
import game.Block;
import game.Direction;
import game.Game;
import game.Timer;
import org.joml.Vector3f;
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

	private static final Game GAME = new Game(20, 20);

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
				800, 800,
				"Lucas Tempass Cerveira",
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

			float width = 1.0f / matrix.length;
			float height = 1.0f / matrix.length;

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					Block block = matrix[i][j];

					if (block == null) {
						// TODO
						continue;
					}

					Vector3f color = Colors.WHITE.getColor();

					if (block == Block.FOOD) {
						color = Colors.RED.getColor();
						shader.setVec4("inputColor", color.x, color.y, color.z, 1.0f);
					}

					if (block == Block.SNAKE_HEAD) {
						color = Colors.DARK_GREEN.getColor();
						shader.setVec4("inputColor", color.x, color.y, color.z, 1.0f);
					}

					if (block == Block.SNAKE) {
						color = Colors.GREEN.getColor();
						shader.setVec4("inputColor", color.x, color.y, color.z, 1.0f);
					}

					if (block == Block.WALL) {
						color = Colors.BLACK.getColor();
						shader.setVec4("inputColor", color.x, color.y, color.z, 1.0f);
					}

					float x = ((float) j / matrix.length) - 0.5f;
					float y = 0.5f - (float) i / matrix.length;

					var squareVAO = setupGeometryWithEBO(rectangle(x, y, width, height, color));

					squareVAO.bind();

					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

					squareVAO.unbind();
				}
			}

			timer.stop();

			var elapsed = timer.getDuration();

			// calcula o tempo que deve dormir para manter o FPS
			var sleepTime = calcSleepTime(5, elapsed);

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
