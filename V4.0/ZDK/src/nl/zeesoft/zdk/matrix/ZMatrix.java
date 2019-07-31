package nl.zeesoft.zdk.matrix;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.matrix.functions.StaticFunctions;
import nl.zeesoft.zdk.matrix.functions.ZRandomize;

public class ZMatrix {
	public int			rows	= 0;
	public int			cols	= 0;
	public float[][]	data	= null;
	
	public ZMatrix(int rows,int cols) {
		this.rows = rows;
		this.cols = cols;
		data = new float[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = 0F;
			}
		}
	}
	
	// Scalar
	public void add(float f) {
		applyFunction(StaticFunctions.ADD,f);
	}
	
	public void subtract(float f) {
		applyFunction(StaticFunctions.SUBTRACT,f);
	}
	
	public void multiply(float f) {
		applyFunction(StaticFunctions.MULTIPLY,f);
	}
	
	public void divide(float f) {
		applyFunction(StaticFunctions.DIVIDE,f);
	}
	
	public void set(float f) {
		applyFunction(StaticFunctions.SET,f);
	}
	
	// Element
	public void add(ZMatrix m) {
		applyFunction(StaticFunctions.ADD,m);
	}
	
	public void subtract(ZMatrix m) {
		applyFunction(StaticFunctions.SUBTRACT,m);
	}
	
	public void multiply(ZMatrix m) {
		applyFunction(StaticFunctions.MULTIPLY,m);
	}
	
	public void divide(ZMatrix m) {
		applyFunction(StaticFunctions.DIVIDE,m);
	}
	
	public void set(ZMatrix m) {
		applyFunction(StaticFunctions.SET,m);
	}
	
	public void randomize() {
		applyFunction(new ZRandomize());
	}
	
	public void randomize(float min, float max) {
		ZRandomize rand = new ZRandomize();
		rand.min = min;
		rand.max = max;
		applyFunction(rand);
	}
	
	// Matrix
	public static ZMatrix add(ZMatrix a,ZMatrix b) {
		return applyFunction(StaticFunctions.ADD,a,b);
	}

	public static ZMatrix subtract(ZMatrix a,ZMatrix b) {
		return applyFunction(StaticFunctions.SUBTRACT,a,b);
	}
	
	public static ZMatrix multiply(ZMatrix a,ZMatrix b) {
		return applyFunction(StaticFunctions.MULTIPLY,a,b);
	}
	
	public static ZMatrix divide(ZMatrix a,ZMatrix b) {
		return applyFunction(StaticFunctions.DIVIDE,a,b);
	}
	
	public void applyFunction(ZFunction function) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = function.applyFunction(data[i][j]);
			}
		}
	}
	
	public void applyFunction(ZParamFunction function,float p) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = function.applyFunction(data[i][j],p);
			}
		}
	}

	public void applyFunction(ZParamFunction function,ZMatrix p) {
		if (p.rows==rows && p.cols==cols) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					data[i][j] = function.applyFunction(data[i][j],p.data[i][j]);
				}
			}
		}
	}

	public static ZMatrix applyFunction(ZParamFunction function,ZMatrix a,ZMatrix b) {
		ZMatrix r = null;
		if (a.cols==b.rows) {
			r = new ZMatrix(a.rows,b.cols);
			for (int i = 0; i < r.rows; i++) {
				for (int j = 0; j < r.cols; j++) {
					float v = 0;
					for (int k = 0; k < a.cols; k++) {
						v = v + function.applyFunction(a.data[i][k],b.data[k][j]);
					}
					r.data[i][j] = v;
				}
			}
		}
		return r;
	}
	
	public static ZMatrix transpose(ZMatrix o) {
		ZMatrix r = new ZMatrix(o.cols,o.rows);
		for (int i = 0; i < r.rows; i++) {
			for (int j = 0; j < r.cols; j++) {
				r.data[i][j] = o.data[j][i];
			}
		}
		return r;
	}
	
	public static ZMatrix getFromArray(float [] a) {
		ZMatrix r = new ZMatrix(a.length,1);
		r.fromArray(a);
		return r;
	}

	public void fromArray(float [] a) {
		if (rows==a.length) {
			for (int i = 0; i < a.length; i++) {
				data[i][0] = a[i];
			}
		}
	}

	public float[] toArray() {
		float[] r = new float[cols];
		for (int i = 0; i < cols; i++) {
			r[i] = data[0][i];
		}
		return r;
	}
	
	public ZMatrix copy() {
		ZMatrix r = new ZMatrix(rows,cols);
		r.set(this);
		return r;
	}
	
	public ZStringBuilder getTable() {
		ZStringBuilder r = new ZStringBuilder();
		DecimalFormat df = new DecimalFormat("000.00");
		DecimalFormat dfn = new DecimalFormat("00.00");
		for (int i = 0; i < rows; i++) {
			if (r.length()>0) {
				r.append("\n");
				for (int j = 0; j < cols; j++) {
					if (j>0) {
						r.append("-+-");
					}
					r.append("------");
				}
				r.append("\n");
			}
			for (int j = 0; j < cols; j++) {
				if (j>0) {
					r.append(" | ");
				}
				if (data[i][j]>=0) {
					r.append(df.format(data[i][j]));
				} else {
					r.append(dfn.format(data[i][j]));
				}
			}
		}
		return r;
	}
}
