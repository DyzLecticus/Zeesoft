package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;

public class CellsStringConvertor extends MatrixStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return Cells.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Cells) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public Cells fromStringBuilder(StringBuilder str) {
		Cells r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new Cells(this, m);
		}
		return r;
	}
}
