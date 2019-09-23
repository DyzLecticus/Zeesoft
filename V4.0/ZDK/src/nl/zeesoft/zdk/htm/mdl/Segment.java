package nl.zeesoft.zdk.htm.mdl;

public class Segment implements ModelObject {
	public String			cellId			= "";
	public int				index			= 0;
	
	public Segment(String cellId, int index) {
		this.cellId = cellId;
		this.index = index;
	}
	
	@Override
	public String getId() {
		return getClass().getSimpleName() + ":" + cellId + "-" + index;
	}
}
