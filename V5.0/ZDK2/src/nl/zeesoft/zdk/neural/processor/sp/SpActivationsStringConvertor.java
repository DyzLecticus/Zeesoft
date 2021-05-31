package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;

public class SpActivationsStringConvertor extends MatrixStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return SpActivations.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof SpActivations) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public SpActivations fromStringBuilder(StringBuilder str) {
		SpActivations r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new SpActivations(this, m);
		}
		return r;
	}
}
