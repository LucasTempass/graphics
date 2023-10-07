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

		float[] rectangle = rectangle(0.0f, 0.0f, 0.5f, 0.5f);
		var triangleVAO = setupGeometryWithEBO(rectangle);

		shader.use();

		while (!glfwWindowShouldClose(window)) {
			// buscar eventos de input
			glfwPollEvents();

			glClearColor(0.3f, 0.2f, 0.3f, 1f);
			glClear(GL_COLOR_BUFFER_BIT);

			glLineWidth(5.0f);

			shader.setVec4("inputColor", 1.0f, 1.0f, 1.0f, 1.0f);

			triangleVAO.bind();

			// rotaciona 45 graus
			float[] rotate45 = {
					(float) Math.cos(Math.toRadians(45)), (float) Math.sin(Math.toRadians(45)), 0.0f, 0.0f,
					(float) -Math.sin(Math.toRadians(45)), (float) Math.cos(Math.toRadians(45)), 0.0f, 0.0f,
					0.0f, 0.0f, 1.0f, 0.0f,
					0f, 0f, 0f, 1f
			};

			// escala 1.5 vezes
			float[] scale15 = {
					1.25f, 0.0f, 0.0f, 0.0f,
					0.0f, 1.25f, 0.0f, 0.0f,
					0.0f, 0.0f, 1.25f, 0.0f,
					0f, 0f, 0f, 1f
			};

			float[] transform = multiplyMatrix(rotate45, scale15);

			shader.setTransform(transform);

			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);


			// desenha outro quadrado abaixo do primeiro
			shader.setTransform(new float[]{
					1.0f, 0.0f, 0.0f, 0.0f,
					0.0f, -1.0f, 0.0f, 0.0f,
					0.0f, 0.0f, -1.0f, 0.0f,
					0f, 0f, 0f, 1f
			});

			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

			triangleVAO.unbind();

			// troca o buffer ativo por aquele que acabamos de desenhar
			glfwSwapBuffers(window);
		}
	}

	float[] multiplyMatrix(float[] a, float[] b) {
		float[] result = new float[16];

		int tamanho = 4;

		for (int i = 0; i < tamanho; i++) { // linha
			for (int j = 0; j < tamanho; j++) { // coluna
				float sum = 0;
				for (int k = 0; k < tamanho; k++) {
					sum += a[i * tamanho + k] * b[k * tamanho + j];
				}
				result[i * tamanho + j] = sum;
			}
		}

		return result;
	}

}
