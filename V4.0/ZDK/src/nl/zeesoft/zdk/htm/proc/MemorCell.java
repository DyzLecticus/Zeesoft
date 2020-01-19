package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

public class MemorCell {
	protected MemorConfig				config		= null;
	protected int						index		= 0;
	protected int						columnIndex	= 0;
	protected int						posX		= 0;
	protected int						posY		= 0;
	protected int						posZ		= 0;

	protected List<MemorDistalSegment>	segments	= new ArrayList<MemorDistalSegment>();
	
	protected List<MemorDistalSegment>	toSegments	= new ArrayList<MemorDistalSegment>(); // Redundant
	
	protected MemorCell(MemorConfig config,int index,int columnIndex,int posX, int posY, int posZ) {
		this.config = config;
		this.index = index;
		this.columnIndex = columnIndex;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public static String getId(int posX, int posY, int posZ) {
		return posX + "-" + posY + "-" + posZ;
	}
	
	protected void destroy() {
		this.config = null;
		segments.clear();
		toSegments.clear();
	}
	
	protected String getId() {
		return getId(posX,posY,posZ);
	}
}
