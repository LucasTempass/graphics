package game;

import java.util.Random;

public class Food {

	private static final Random random = new Random();

	private final int[] position;


	public Food(int width, int height, int padding) {
		// duplica para representar a padding em ambos os lados
		int y = random.nextInt(height - padding * 2) + padding;
		int x = random.nextInt(width - padding * 2) + padding;
		this.position = new int[]{ y, x };
	}

	public int[] getPosition() {
		return position;
	}

}
