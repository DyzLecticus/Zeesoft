package nl.zeesoft.zdk.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class GridColumn {
	private Lock		lock	= new Lock();			
	
	protected int		index	= 0;
	protected int		posX	= 0;
	protected int		posY	= 0;
	
	private Object[]	values	= null;
	
	protected void initialize(int sizeZ, Object value) {
		values = new Object[sizeZ];
		for (int z = 0; z < sizeZ; z++) {
			values[z] = value;
		}
	}
	
	@Override
	public String toString() {
		Str vals = new Str();
		lock.lock(this);
		for (int z = 0; z < values.length; z++) {
			if (vals.length()>0) {
				vals.sb().append(", ");
			}
			vals.sb().append(values[z]);
		}
		lock.unlock(this);
		vals.sb().insert(0,"[");
		vals.sb().append("]");
		return String.format("%02d", posX) + "," + String.format("%02d", posY) + "=" + vals;
	}
	
	public int index() {
		return index;
	}
	
	public int posX() {
		return posX;
	}
	
	public int posY() {
		return posY;
	}
	
	public void setValue(Object value) {
		setValue(0, value);
	}
	
	public void setValue(int posZ, Object value) {
		lock.lock(this);
		if (values!=null && posZ<values.length) {
			values[posZ] = value;
		}
		lock.unlock(this);
	}
	
	public Object getValue() {
		return getValue(0);
	}
	
	public Object getValue(int posZ) {
		Object r = null;
		lock.lock(this);
		if (values!=null && posZ<values.length) {
			r = values[posZ];
		}
		lock.unlock(this);
		return r;
	}
	
	public float getAverageValue() {
		float r = 0;
		lock.lock(this);
		if (values!=null && values.length>1) {
			for (int z = 0; z < values.length; z++) {
				if (values[z]!=null) {
					if (values[z] instanceof Float) {
						r = r + (float)values[z];
					} else if (values[z] instanceof Integer) {
						r = r + (int)values[z];
					} else if (values[z] instanceof Boolean) {
						if ((boolean)values[z]) {
							r = r + 1F;
						}
					}
				}
			}
			if (r>0) {
				r = r / (float) values.length;
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public void applyFunction(ColumnFunction function) {
		lock.lock(this);
		Object[] currentValues = new Object[values.length];
		for (int z = 0; z < values.length; z++) {
			currentValues[z] = values[z];
		}
		lock.unlock(this);
		Object[] newValues = function.applyFunction(this, currentValues);
		lock.lock(this);
		values = newValues;
		lock.unlock(this);
	}
	
	public List<Position> getValuePositions(Object value) {
		List<Position> r = new ArrayList<Position>();
		lock.lock(this);
		for (int z = 0; z < values.length; z++) {
			if ((values[z]==null && value==null) ||
				(values[z]!=null && values[z]==value) ||
				(values[z]!=null && values[z].equals(value))
				) {
				r.add(new Position(posX,posY,z));
			}
		}
		lock.unlock(this);
		return r;
	}
}
