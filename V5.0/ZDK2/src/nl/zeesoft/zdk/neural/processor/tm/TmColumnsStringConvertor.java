package nl.zeesoft.zdk.neural.processor.tm;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;

public class TmColumnsStringConvertor extends MatrixStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return TmColumns.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof TmColumns) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public TmColumns fromStringBuilder(StringBuilder str) {
		TmColumns r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new TmColumns(this, m);
		}
		return r;
	}
}
