package nl.zeesoft.zdk.matrix;

public class SizeStringConvertor extends XYZStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return Size.class;
	}

	@Override
	public Size fromStringBuilder(StringBuilder str) {
		return (Size) super.fromStringBuilder(str);
	}
	
	@Override
	protected Size getNewInstance(int x, int y, int z) {
		return new Size(x, y, z);
	}
}
