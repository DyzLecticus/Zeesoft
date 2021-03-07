package nl.zeesoft.zdk.matrix;

public class Position {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof Position) {
			Position pos = (Position) other;
			r = pos.x == x && pos.y == y && pos.z == z;
		}
		return r;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(x);
		sb.append(",");
		sb.append(y);
		sb.append(",");
		sb.append(z);
		return sb.toString();
	}
	
	/**
	 * Assumes all coordinates are >= 0
	 */
	public float getDistance(Position other) {
		float r = 0;
		float len1 = 0;
		if (other.x > x) {
			len1 = other.x - x;
		} else {
			len1 = x - other.x;
		}
		float len2 = 0;
		if (other.y > y) {
			len2 = other.y - y;
		} else {
			len2 = y - other.y;
		}
		if (len1==0) {
			r = len2;
		} else if (len2==0) {
			r = len1;
		} else {
			r = getHypotenuse(len1, len2);
		}
		if (other.z!=z) {
			len1 = r;
			if (other.z > z) {
				len2 = other.z - z;
			} else {
				len2 = z - other.z;
			}
			if (len1==0) {
				r = len2;
			} else {
				r = getHypotenuse(len1, len2);
			}
		}
		return r;
	}
	
	private static float getHypotenuse(float len1, float len2) {
		return (float) Math.sqrt((len1 * len1) + (len2 * len2));
	}
}
