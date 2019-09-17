package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

public class PoolerColumnGroup {
	protected	int					posX				= 0;
	protected	int					posY				= 0;
	
	protected	List<PoolerColumn>	columns				= new ArrayList<PoolerColumn>();
	
	protected	float				averageActivity		= 0;
	
	protected PoolerColumnGroup(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	protected String getId() {
		return getId(posX,posY);
	}
	
	protected static String getId(int posX, int posY) {
		return posX + "-" + posY;
	}
	
	protected void calculateAverageActivity() {
		averageActivity = 0;
		for (PoolerColumn col: columns) {
			averageActivity += col.averageActivity;
		}
		if (averageActivity>0) {
			averageActivity = averageActivity / columns.size();
		}
	}
}
