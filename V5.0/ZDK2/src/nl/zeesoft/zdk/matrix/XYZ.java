package nl.zeesoft.zdk.matrix;

public abstract class XYZ {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other.getClass() == this.getClass()) {
			XYZ xyz = (XYZ) other;
			r = xyz.x == x && xyz.y == y && xyz.z == z;
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
}
