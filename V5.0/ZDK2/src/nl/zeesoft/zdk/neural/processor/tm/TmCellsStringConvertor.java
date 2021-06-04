package nl.zeesoft.zdk.neural.processor.tm;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.model.CellsStringConvertor;

public class TmCellsStringConvertor extends CellsStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return TmCells.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof TmCells) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public TmCells fromStringBuilder(StringBuilder str) {
		TmCells r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new TmCells(this, m);
		}
		return r;
	}
}
