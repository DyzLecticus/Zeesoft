package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;

public class SDRGrid extends Grid implements StrAble {
	public SDRGrid() {
		
	}
	
	public SDRGrid(SDRGrid sdr) {
		super(sdr);
	}
	
	public List<GridColumn> getActiveColumns() {
		List<GridColumn> r = new ArrayList<GridColumn>();
		for (GridColumn column: columns) {
			Object value = column.getValue();
			if (value!=null && value instanceof Boolean && (boolean)value) {
				r.add(column);
			}
		}
		return r;
	}
	
	@Override
	public String toString() {
		return toStr().toString();
	}
	
	@Override
	public Str toStr() {
		Str r = new Str();
		for (GridColumn column: getActiveColumns()) {
			if (r.length()>0) {
				r.sb().append(",");
			}
			r.sb().append(column.index());
		}
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeY());
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeX());
		return r;
	}
	
	@Override
	public void fromStr(Str str) {
		List<Str> split = str.split(";");
		if (split.size()>2) {
			int sizeX = Integer.parseInt(split.get(0).toString());
			int sizeY = Integer.parseInt(split.get(1).toString());
			initialize(sizeX,sizeY,1,false);
			split = split.get(2).split(",");
			for (Str idx: split) {
				int index = Integer.parseInt(idx.toString());
				if (index < columns.size()) {
					columns.get(index).setValue(true);
				}
			}
		}
	}

	@Override
	public void initialize(int sizeX, int sizeY, int sizeZ, Object value) {
		if (sizeZ != 1) {
			sizeZ = 1;
		}
		if (value==null || !(value instanceof Boolean)) {
			value = false;
		}
		super.initialize(sizeX, sizeY, sizeZ, value);
		setUseLock(false);
	}
}
