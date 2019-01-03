package nl.zeesoft.zsc.analyze;

import java.util.ArrayList;
import java.util.List;

/**
 * Compressed multidimensional vectors are used to store wide layered vector information which contains mostly zero values.
 */
public class Vector {
	public List<VectorValue>	values		= new ArrayList<VectorValue>();

	public int getIntegerValue(int dimension,int column) {
		int r = 0;
		VectorValue v = getValue(dimension,column);
		if (v!=null) {
			r = v.count;
		}
		return r;
	}

	public VectorValue getValue(int dimension,int column) {
		VectorValue r = null;
		for (VectorValue value: values) {
			if (value.dimension==dimension && value.column==column) {
				r = value;
				break;
			}
		}
		return r;
	}
	
	public VectorValue removeValue(int dimension,int column) {
		VectorValue r = getValue(dimension,column);
		if (r!=null) {
			values.remove(r);
		}
		return r;
	}
	
	public VectorValue setValue(int dimension,int column,int count) {
		VectorValue r = getValue(dimension,column);
		if (r==null) {
			r = new VectorValue();
			r.dimension = dimension;
			r.column = column;
			values.add(r);
		}
		r.count = count;
		return r;
	}
}
