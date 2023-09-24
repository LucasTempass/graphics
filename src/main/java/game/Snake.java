package game;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;

public class Snake {

	private final List<int[]> body;
	private int dirX;
	private int dirY;

	public Snake(int[] startingCoordinates, int dirX, int dirY) {
		this.body = new ArrayList<>(of(startingCoordinates));
		this.dirX = dirX;
		this.dirY = dirY;
	}

	public int[] getHead() {
		return body.get(0);
	}

	public int[] getTail() {
		return body.get(body.size() - 1);
	}

	public List<int[]> getBody() {
		return body;
	}

	public int[] move() {
		// armazena a cauda, para adicionar um novo pedaço caso a cobra coma
		int[] oldTail = getTail();

		// move o corpo para seguir a cabeça
		for (int i = 1; i < body.size(); i++) {
			body.set(i, body.get(i - 1));
		}

		int[] head = getHead();

		int[] newHead = new int[]{ head[0] + dirY, head[1] + dirX };

		// move a cabeça
		body.set(0, newHead);

		return oldTail;
	}

	public void grow(int[] tail) {
		body.add(tail);
	}

	public void down() {
		if (dirY == 1) {
			return;
		}

		dirX = 0;
		dirY = +1;
	}

	public void up() {
		if (dirY == -1) {
			return;
		}

		dirX = 0;
		dirY = -1;
	}

	public void left() {
		if (dirX == 1) {
			return;
		}

		dirX = -1;
		dirY = 0;
	}

	public void right() {
		if (dirX == -1) {
			return;
		}

		dirX = 1;
		dirY = 0;
	}

}
