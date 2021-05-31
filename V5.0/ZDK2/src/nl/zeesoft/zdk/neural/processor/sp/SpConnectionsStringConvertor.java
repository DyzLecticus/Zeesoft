package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;

public class SpConnectionsStringConvertor extends MatrixStringConvertor {
	public SpConnectionsStringConvertor() {
		dataSeparator = "$";
		positionSeparator = "%";
	}
	
	@Override
	public Class<?> getObjectClass() {
		return SpConnections.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof SpConnections) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public SpConnections fromStringBuilder(StringBuilder str) {
		SpConnections r = null;
		Matrix m = super.fromStringBuilder(str);
		if (m!=null) {
			r = new SpConnections(this, m);
		}
		return r;
	}
}
