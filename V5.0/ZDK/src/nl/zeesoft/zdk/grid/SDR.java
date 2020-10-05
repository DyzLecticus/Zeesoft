package nl.zeesoft.zdk.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class SDR extends Grid {
	public SDR() {
		
	}
	
	public SDR(SDR sdr) {
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
	
	public Str toStr() {
		Str r = new Str();
		for (GridColumn column: getActiveColumns()) {
			if (r.length()>0) {
				r.sb().append(",");
			}
			r.sb().append(column.index);
		}
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeY());
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeX());
		return r;
	}
	
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
	}
	
	public int getOverlap(SDR sdr) {
		int r = 0;
		for (GridColumn column: getActiveColumns()) {
			GridColumn other = sdr.getColumn(column.index);
			if (other!=null) {
				Object value = other.getValue();
				if (value!=null && value instanceof Boolean && (boolean)value) {
					r++;
				}
			}
		}
		return r;
	}
}
