package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class ColumnGroup implements ModelObject {
	public int				minPosX				= 0;
	public int				minPosY				= 0;
	
	public List<Column>		columns				= new ArrayList<Column>();
	
	public ColumnGroup(int minPosX,int minPosY) {
		this.minPosX = minPosX;
		this.minPosY = minPosY;
	}
	
	@Override
	public String getId() {
		return getId(minPosX,minPosY);
	}

	public static String getId(int minPosX, int minPosY) {
		return ColumnGroup.class.getSimpleName() + ":" + minPosX + "-" + minPosY;
	}
}
