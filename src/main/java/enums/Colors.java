package enums;

import org.joml.Vector3f;

public enum Colors {
	WHITE(0xFFFFFF),
	BLACK(0x000000),
	PINK(0xFF72AD),
	PURPLE(0xAD72FF),
	BLUE(0x72ADFF),
	CYAN(0x72FFAD),
	GREEN(0x72FF72),
	YELLOW(0xFFFF72),
	ORANGE(0xFFAD72),
	RED(0xFF7272);


	private final Vector3f color;

	Colors(int hex) {
		float r = ((hex >> 16) & 0xFF) / 255f;
		float g = ((hex >> 8) & 0xFF) / 255f;
		float b = (hex & 0xFF) / 255f;
		this.color = new Vector3f(r, g, b);
	}

	public static Vector3f getRandom() {
		var values = values();

		int index = (int) (Math.random() * values.length);

		return values[index].getColor();
	}


	public Vector3f getColor() {
		return color;
	}

}
