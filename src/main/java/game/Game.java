package game;

import static utils.Utils.isIntersecting;

public class Game {

	private final int width;
	private final int height;
	private final Snake snake;
	private int[] food;

	public Game(int width, int height, Snake snake) {
		this.food = new int[]{ width / 2, height / 2 };
		this.snake = snake;
		this.height = height;
		this.width = width;
	}

	public Game(int width, int height) {
		this.food = new Food(width, height, 1).getPosition();
		this.snake = new Snake(new int[]{ 1, 1 }, 1, 0);
		// adiciona +1 para as paredes
		this.height = height + 1;
		this.width = width + 1;
	}

	public void play() {
		var discardedTail = snake.move();

		if (isIntersecting(snake.getHead(), food)) {
			snake.grow(discardedTail);
			updateFood();
		}

		if (isDead(snake)) {
			throw new IllegalStateException("Game Over!");
		}
	}

	public void onCommand(Direction direction) {
		switch (direction) {
			case UP -> snake.up();
			case DOWN -> snake.down();
			case LEFT -> snake.left();
			case RIGHT -> snake.right();
		}
	}

	public boolean isDead(Snake snake) {
		var head = snake.getHead();

		if (head[0] <= 0 || head[0] >= height) {
			return true;
		}

		if (head[1] <= 0 || head[1] >= width) {
			return true;
		}

		for (int i = 1; i < snake.getBody().size(); i++) {
			if (isIntersecting(head, snake.getBody().get(i))) {
				return true;
			}
		}

		return false;
	}

	public void updateFood() {
		// para não gerar nas paredes
		food = new Food(width, height, 1).getPosition();

		for (int[] bodyPart : snake.getBody()) {
			if (isIntersecting(food, bodyPart)) {
				updateFood();
				break;
			}
		}
	}

	public Block[][] toMatrix() {
		var matrix = new Block[width][height];

		// adiciona o corpo
		for (int[] bodyPart : snake.getBody()) {
			matrix[bodyPart[0]][bodyPart[1]] = Block.SNAKE;
		}

		// adiciona a cabeça
		var head = snake.getHead();
		matrix[head[0]][head[1]] = Block.SNAKE_HEAD;

		// adiciona a comida
		matrix[food[0]][food[1]] = Block.FOOD;

		// adiciona as paredes
		for (int i = 0; i < width; i++) {
			matrix[i][0] = Block.WALL;
			matrix[i][height - 1] = Block.WALL;
		}

		// adiciona as paredes
		for (int i = 0; i < height; i++) {
			matrix[0][i] = Block.WALL;
			matrix[width - 1][i] = Block.WALL;
		}

		return matrix;
	}

}
