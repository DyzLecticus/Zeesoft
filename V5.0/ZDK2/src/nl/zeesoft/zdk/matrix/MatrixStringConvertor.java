package nl.zeesoft.zdk.matrix;

import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class MatrixStringConvertor extends ObjectStringConvertor {
	public SizeStringConvertor		sizeConvertor		= (SizeStringConvertor) ObjectStringConvertors.getConvertor(Size.class);
	public PositionStringConvertor	positionConvertor	= (PositionStringConvertor) ObjectStringConvertors.getConvertor(Position.class);
	public String					dataSeparator		= "#";
	public String					positionSeparator	= ";";
	
	@Override
	public Class<?> getObjectClass() {
		return Matrix.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Matrix) {
			Matrix m = (Matrix) obj;
			if (m.size!=null) {
				r.append(sizeConvertor.toStringBuilder(m.size));
				appendData(r, m);
			}
		}
		return r;
	}

	@Override
	public Matrix fromStringBuilder(StringBuilder str) {
		Matrix r = null;
		Class<?> dataType = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		int i = 0;
		for (StringBuilder dat: data) {
			if (parseBreak(i, r, dataType)) {
				break;
			}
			if (i==0) {
				r = parseSize(dat);
			} else if (i==1) {
				dataType = Instantiator.getClassForName(dat.toString());
			} else {
				parseData(r, dataType, dat);
			}
			i++;
		}
		return r;
	}

	protected void appendData(StringBuilder str, Matrix m) {
		Class<?> type = getDataType(m);
		if (type!=null) {
			str.append(dataSeparator);
			str.append(type.getName());
			m.applyFunction(this, getStringFunction(str));
		}
	}
	
	protected Class<?> getDataType(Matrix m) {
		Class<?> r = null;
		for (int x = 0; x < m.size.x; x++) {
			for (int y = 0; y < m.size.y; y++) {
				for (int z = 0; z < m.size.z; z++) {
					if (m.data[x][y][z]!=null) {
						r = m.data[x][y][z].getClass();
						break;
					}
				}
				if (r!=null) {
					break;
				}
			}
			if (r!=null) {
				break;
			}
		}
		return r;
	}
	
	protected Function getStringFunction(StringBuilder str) {
		return new Function() {
			@Override
			protected Object exec() {
				appendData(str, (Position)param1, param2);
				return param2;
			}
		};
	}
	
	protected void appendData(StringBuilder str, Position pos, Object value) {
		str.append(dataSeparator);
		str.append(positionConvertor.toStringBuilder(pos));
		str.append(positionSeparator);
		str.append(getStringBuilderForObject(value));
	}
	
	protected boolean parseBreak(int i, Matrix m, Class<?> dataType) {
		boolean r = false;
		if (i==1 && m==null) {
			r = true;
		} else if (i==2 && dataType==null) {
			r = true;
		}
		return r;
	}
	
	protected Matrix parseSize(StringBuilder dat) {
		Matrix r = null;
		Size size = sizeConvertor.fromStringBuilder(dat);
		if (size!=null) {
			r = new Matrix();
			r.initialize(size);
		}
		return r;
	}
	
	protected void parseData(Matrix m, Class<?> dataType, StringBuilder dat) {
		List<StringBuilder> posVal = StrUtil.split(dat, positionSeparator);
		Position pos = (Position) positionConvertor.fromStringBuilder(posVal.get(0));
		if (pos!=null && !StrUtil.equals(posVal.get(1), StrUtil.NULL)) {
			m.setValue(pos, getObjectForStringBuilder(dataType, posVal.get(1)));
		}
	}
}
