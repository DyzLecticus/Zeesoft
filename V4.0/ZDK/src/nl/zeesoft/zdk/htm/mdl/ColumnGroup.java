package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class ColumnGroup extends ModelObject {
	public int				minPosX				= 0;
	public int				minPosY				= 0;
	
	public List<Column>		columns				= new ArrayList<Column>();
	
	// Used for boosting by SpatialPooler
	public float			averageActivity		= 0;
	
	public ColumnGroup(int minPosX,int minPosY) {
		this.minPosX = minPosX;
		this.minPosY = minPosY;
	}
	
	public String getPosId() {
		return getPosId(minPosX,minPosY);
	}

	public static String getPosId(int minPosX, int minPosY) {
		return minPosX + "-" + minPosY;
	}

	@Override
	public ColumnGroup copy() {
		ColumnGroup copy = new ColumnGroup(minPosX,minPosY);
		copy.setId(getId());
		return copy;
	}
	
	public void calculateAverageActivity() {
		averageActivity = 0;
		for (Column column: columns) {
			averageActivity += column.averageActivity;
		}
		if (averageActivity>0) {
			averageActivity = averageActivity / columns.size();
		}
	}
}
