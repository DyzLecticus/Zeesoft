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
	
	public Size(int length) {
		if (length>1) {
			x = (int)Math.sqrt(length);
			y = x;
			if (x * y < length) {
				x++;
			}
			if (x * y < length) {
				y++;
			}
		} else {
			x = 1;
			y = 1;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof Size) {
			Size size = (Size) other;
			r = size.x == x && size.y == y && size.z == z;
		}
		return r;
	}
	
	public Size copy() {
		return new Size(x, y, z);
	}
	
	public int volume() {
		return x * y * z;
	}

	public int getIndexForPosition(Position position) {
		int r = -1;
		r = position.x + (position.y * x);
		r = (r * z) + position.z;
		return r;
	}
	
	public Position getPositionForIndex(int index) {
		Position r = new Position(0,0,0);
		if (index>0) {
			if (z>1) {
				r.z = index % z;
				r.x = (index - r.z) / z;
				int tx = r.x;
				r.x = tx % x;
				r.y = (tx - r.x) / x;
			} else {
				r.x = index % x;
				r.y = (index - r.x) / x;
			}
		}
		return r;
	}
	
	public Position projectPositionOn(Position position, Size other) {
		Position r = new Position(0,0,0);
		r.x = Math.round(position.x * getFactor(x, other.x));
		r.y = Math.round(position.y * getFactor(y, other.y));
		r.z = Math.round(position.z * getFactor(z, other.z));
		return r;
	}
	
	private float getFactor(float fromSize, float toSize) {
		return (toSize - 1) / (fromSize - 1);
	}
}
