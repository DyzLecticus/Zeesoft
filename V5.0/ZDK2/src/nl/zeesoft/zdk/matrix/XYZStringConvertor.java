package nl.zeesoft.zdk.matrix;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public abstract class XYZStringConvertor extends ObjectStringConvertor {
	public String dimensionSeparator	= ",";
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof XYZ) {
			XYZ xyz = (XYZ) obj;
			r.append(xyz.x);
			r.append(dimensionSeparator);
			r.append(xyz.y);
			r.append(dimensionSeparator);
			r.append(xyz.z);
		}
		return r;
	}

	@Override
	public XYZ fromStringBuilder(StringBuilder str) {
		XYZ r = null;
		List<StringBuilder> xyz = StrUtil.split(str, dimensionSeparator);
		if (xyz.size()==3) {
			r = getNewInstance(
				Util.parseInt(xyz.get(0).toString()),
				Util.parseInt(xyz.get(1).toString()),
				Util.parseInt(xyz.get(2).toString())
			);
		}
		return r;
	}
	
	protected abstract XYZ getNewInstance(int x, int y, int z);
}
