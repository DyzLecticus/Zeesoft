package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;

public class SpBoostFactorsStringConvertor extends MatrixStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return SpBoostFactors.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof SpBoostFactors) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public SpBoostFactors fromStringBuilder(StringBuilder str) {
		SpBoostFactors r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new SpBoostFactors(this, m);
		}
		return r;
	}
}
