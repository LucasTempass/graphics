package game;

import static utils.Utils.isIntersecting;

public class Board {

	private final int width;
	private final int height;
	private final Snake snake;
	private int[] food;

	public Board(int width, int height, Snake snake) {
		this.food = new int[]{ width / 2, height / 2 };
		this.snake = snake;
		this.height = height;
		this.width = width;
	}

	public Board(int width, int height) {
		this.food = new int[]{ width / 2, height / 2 };
		this.snake = new Snake(new int[]{ 1, 1 }, 1, 0);
		this.height = height;
		this.width = width;
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
		food = new Food(width, height).getPosition();

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

		return matrix;
	}

}
