package nl.zeesoft.zdk.matrix;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class SizeStringConvertor extends ObjectStringConvertor {
	public String dimensionSeparator	= ",";
	
	@Override
	public Class<?> getObjectClass() {
		return Size.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Size) {
			Size size = (Size) obj;
			r.append(size.x);
			r.append(dimensionSeparator);
			r.append(size.y);
			r.append(dimensionSeparator);
			r.append(size.z);
		}
		return r;
	}

	@Override
	public Size fromStringBuilder(StringBuilder str) {
		Size r = null;
		List<StringBuilder> xyz = StrUtil.split(str, dimensionSeparator);
		if (xyz.size()==3) {
			r = new Size(
				Util.parseInt(xyz.get(0).toString()),
				Util.parseInt(xyz.get(1).toString()),
				Util.parseInt(xyz.get(2).toString())
			);
		}
		return r;
	}
}
