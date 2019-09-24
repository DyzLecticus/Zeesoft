package nl.zeesoft.zdk.htm.impl;

import nl.zeesoft.zdk.htm.mdl.Column;
import nl.zeesoft.zdk.htm.mdl.ColumnGroup;

public class SpatialColumnGroup extends ColumnGroup {
	public float			averageActivity		= 0;
	
	public SpatialColumnGroup(ColumnGroup columnGroup) {
		super(columnGroup.minPosX,columnGroup.minPosY);
		setId(columnGroup.getId());
	}

	@Override
	public SpatialColumnGroup copy() {
		return new SpatialColumnGroup(this);
	}
	
	public void calculateAverageActivity() {
		averageActivity = 0;
		for (Column column: columns) {
			if (column instanceof SpatialColumn) {
				averageActivity += ((SpatialColumn)column).averageActivity;
			}
		}
		if (averageActivity>0) {
			averageActivity = averageActivity / columns.size();
		}
	}
}
