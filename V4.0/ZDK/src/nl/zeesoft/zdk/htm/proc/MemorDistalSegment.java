package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

public class MemorDistalSegment {
	protected String				cellId			= "";
	protected int					index			= 0;
	protected List<MemorDistalLink>	links			= new ArrayList<MemorDistalLink>();
	
	protected MemorDistalSegment(String cellId,int index) {
		this.cellId = cellId;
		this.index = index;
	}
	
	protected String getId() {
		return cellId + "/" + index;
	}
	
	protected static String getCellId(String segmentId) {
		String r = "";
		if (segmentId.contains("/")) {
			r = segmentId.split("/")[0];
		}
		return r;
	}
}
