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
}
