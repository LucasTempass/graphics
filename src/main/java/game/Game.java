package game;

import java.util.Arrays;

public class Game {

	private Game() {
		// classe estática
	}

	public static void main(String[] args) {
		var game = new Board(20, 20);

		for (int i = 0; i < 30; i++) {
			// TODO ler input do usuário
			game.onCommand(i % 2 == 0 ? Direction.DOWN : Direction.RIGHT);

			game.play();

			Block[][] matrix = game.toMatrix();

			for (Block[] row : matrix) {
				System.out.println(Arrays.toString(row));
			}

			System.out.println("=====================================");
		}
	}

}
