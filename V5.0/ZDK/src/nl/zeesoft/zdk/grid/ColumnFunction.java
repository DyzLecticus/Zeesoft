package nl.zeesoft.zdk.grid;

public class ColumnFunction {
	public Object[] applyFunction(GridColumn column, Object[] values) {
		for (int z = 0; z < values.length; z++) {
			values[z] = applyFunction(column,z,values[z]);
		}
		return values;
	}
	
	public Object applyFunction(GridColumn column, int posZ, Object value) {
		return value;
	}
}
