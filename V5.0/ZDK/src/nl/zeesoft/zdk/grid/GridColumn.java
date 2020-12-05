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
	
	protected boolean	useLock	= true;
	
	private Object[]	values	= null;
	
	protected void initialize(int sizeZ, Object value) {
		values = new Object[sizeZ];
		if (value!=null) {
			for (int z = 0; z < sizeZ; z++) {
				values[z] = value;
			}
		}
	}
	
	@Override
	public String toString() {
		Str vals = new Str();
		if (useLock) {
			lock.lock(this);
		}
		for (int z = 0; z < values.length; z++) {
			if (vals.length()>0) {
				vals.sb().append(", ");
			}
			vals.sb().append(values[z]);
		}
		if (useLock) {
			lock.unlock(this);
		}
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
		if (useLock) {
			lock.lock(this);
			if (values!=null && posZ<values.length) {
				values[posZ] = value;
			}
			lock.unlock(this);
		} else {
			if (values!=null && posZ<values.length) {
				values[posZ] = value;
			}
		}
	}
	
	public Object getValue() {
		return getValue(0);
	}
	
	public Object getValue(int posZ) {
		Object r = null;
		if (useLock) {
			lock.lock(this);
			if (values!=null && posZ<values.length) {
				r = values[posZ];
			}
			lock.unlock(this);
		} else {
			if (values!=null && posZ<values.length) {
				r = values[posZ];
			}
		}
		return r;
	}
	
	public List<Position> getValuePositions(Object value) {
		List<Position> r = new ArrayList<Position>();
		if (useLock) {
			lock.lock(this);
		}
		for (int z = 0; z < values.length; z++) {
			if ((values[z]==null && value==null) ||
				(values[z]!=null && values[z]==value) ||
				(values[z]!=null && values[z].equals(value))
				) {
				r.add(new Position(posX,posY,z));
			}
		}
		if (useLock) {
			lock.unlock(this);
		}
		return r;
	}
	
	public void applyFunction(ColumnFunction function) {
		if (useLock) {
			lock.lock(this);
			Object[] currentValues = getCurrentValuesNoLock();
			lock.unlock(this);
			Object[] newValues = function.applyFunction(this, currentValues);
			lock.lock(this);
			values = newValues;
			lock.unlock(this);
		} else {
			values = function.applyFunction(this, values);
		}
	}
	
	private Object[] getCurrentValuesNoLock() {
		Object[] r = new Object[values.length];
		for (int z = 0; z < values.length; z++) {
			r[z] = values[z];
		}
		return r;
	}
}
