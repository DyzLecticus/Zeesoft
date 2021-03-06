package nl.zeesoft.zdk.matrix;

public class Size {
	public int x = 1;
	public int y = 1;
	public int z = 1;
	
	public Size(int x, int y) {
		if (x < 1) {
			x = 1;
		}
		if (y < 1) {
			y = 1;
		}
		this.x = x;
		this.y = y;
		this.z = 1;
	}
	
	public Size(int x, int y, int z) {
		if (x < 1) {
			x = 1;
		}
		if (y < 1) {
			y = 1;
		}
		if (z < 1) {
			z = 1;
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Size copy() {
		return new Size(x, y, z);
	}
}
