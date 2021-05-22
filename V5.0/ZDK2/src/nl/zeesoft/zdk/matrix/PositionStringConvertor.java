package nl.zeesoft.zdk.matrix;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class PositionStringConvertor extends ObjectStringConvertor {
	public String dimensionSeparator	= ",";
	
	@Override
	public Class<?> getObjectClass() {
		return Position.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Position) {
			Position pos = (Position) obj;
			r.append(pos.x);
			r.append(dimensionSeparator);
			r.append(pos.y);
			r.append(dimensionSeparator);
			r.append(pos.z);
		}
		return r;
	}

	@Override
	public Position fromStringBuilder(StringBuilder str) {
		Position r = null;
		List<StringBuilder> xyz = StrUtil.split(str, dimensionSeparator);
		if (xyz.size()==3) {
			r = new Position(
				Util.parseInt(xyz.get(0).toString()),
				Util.parseInt(xyz.get(1).toString()),
				Util.parseInt(xyz.get(2).toString())
			);
		}
		return r;
	}
}
