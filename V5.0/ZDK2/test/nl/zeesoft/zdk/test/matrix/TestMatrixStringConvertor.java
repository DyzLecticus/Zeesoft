package nl.zeesoft.zdk.test.matrix;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixStringConvertor;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class TestMatrixStringConvertor {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		MatrixStringConvertor msc = (MatrixStringConvertor) ObjectStringConvertors.getConvertor(Matrix.class);
		assert ObjectStringConvertors.getConvertor(Matrix.class) == msc;
		msc = new MatrixStringConvertor();
		assert ObjectStringConvertors.getConvertor(Matrix.class) != msc;
		
		Size size = new Size(2,3,4);
		Matrix m = new Matrix();
		m.initialize(size);
		testMatrix(m);
		
		m.data[0][0][0] = 1.92362F;
		m.data[0][0][2] = -1.28625F;
		testMatrix(m);

		m.data[0][0][0] = new Position(8,8,8);
		m.data[0][0][2] = new Position(9,9,9);
		testMatrix(m);
		
		assert msc.toStringBuilder(msc).length() == 0;
		assert msc.toStringBuilder(new Matrix()).length() == 0;
		assert msc.fromStringBuilder(new StringBuilder()) == null;
		assert msc.fromStringBuilder(new StringBuilder(",")) == null;
		assert msc.fromStringBuilder(new StringBuilder(",#")) == null;
		assert msc.fromStringBuilder(new StringBuilder("2,3,4#")) != null;
		assert msc.fromStringBuilder(new StringBuilder("2,3,4#Pizza#")) != null;
		m = msc.fromStringBuilder(new StringBuilder("2,3,4#java.lang.Float#0,0;1.92362"));
		assert m.data[0][0][0] == null;
	}
	
	public static void testMatrix(Matrix m) {
		MatrixStringConvertor msc = (MatrixStringConvertor) ObjectStringConvertors.getConvertor(Matrix.class);
		StringBuilder str = msc.toStringBuilder(m);
		
		Matrix m2 = msc.fromStringBuilder(str);
		StringBuilder str2 = msc.toStringBuilder(m2);

		assert StrUtil.equals(str2, str);
	}
}
