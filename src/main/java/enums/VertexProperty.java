package enums;

public enum VertexProperty {
	// coordenadas
	X(0), Y(1), Z(2),
	// cores
	RED(3), GREEN(4), BLUE(5),
	// textura
	U(6), V(7);

	private final int position;


	VertexProperty(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public static int getVertexSize() {
		return values().length;
	}

	public static int getVertexSizeInBytes() {
		return getVertexSize() * Float.BYTES;
	}

	public static long getColorOffset() {
		// primeira cor, depois coordenadas
		return RED.getPosition();
	}

	public static long getColorOffsetInBytes() {
		return getColorOffset() * Float.BYTES;
	}


}
