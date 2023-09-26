package game;

import enums.Colors;
import org.joml.Vector3f;

import static enums.Colors.*;

public enum Block {
	FOOD(RED), SNAKE(GREEN), SNAKE_HEAD(DARK_GREEN), WALL(BLACK);

	private final Colors color;

	Block(Colors color) {
		this.color = color;
	}

	public Vector3f getColor() {
		return color.getColor();
	}

}
