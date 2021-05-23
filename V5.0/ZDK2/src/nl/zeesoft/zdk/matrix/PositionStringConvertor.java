package nl.zeesoft.zdk.matrix;

public class PositionStringConvertor extends XYZStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return Position.class;
	}

	@Override
	public Position fromStringBuilder(StringBuilder str) {
		return (Position) super.fromStringBuilder(str);
	}
	
	@Override
	protected Position getNewInstance(int x, int y, int z) {
		return new Position(x, y, z);
	}
}
