package game;

import java.util.Random;

public class Food {

	private static final Random random = new Random();

	private final int[] position;


	public Food(int width, int height) {
		this.position = new int[]{ random.nextInt(width), random.nextInt(height) };
	}

	public int[] getPosition() {
		return position;
	}

}
