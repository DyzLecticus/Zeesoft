package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;

public class MemoryColumnCell {
	private		MemoryConfig		config			= null;
	private		MemoryColumnGroup	columnGroup		= null;	
	protected	int					columnIndex		= 0;
	protected	int					posZ			= 0;
	
	protected	Set<DistalLink>		distLinks		= new HashSet<DistalLink>();
	
	protected	int					overlapScore	= 0;
	
	protected	boolean				active			= false;
	protected	boolean				predictive		= false;
	
	protected MemoryColumnCell(MemoryConfig config,MemoryColumnGroup columnGroup,int columnIndex,int posZ) {
		this.config = config;
		this.columnGroup = columnGroup;
		this.columnIndex = columnIndex;
		this.posZ = posZ;
	}

	protected void randomizeConnections() {
		distLinks.clear();
		for (MemoryColumn col: columnGroup.columns) {
			for (int d = 0; d < config.depth; d++) {
				if (!(col.index == columnIndex && d==posZ)) {
					DistalLink link = new DistalLink();
					link.cell = col.cells.get(d);
				}
			}
		}
		List<DistalLink> availableLinks = new ArrayList<DistalLink>(distLinks);
		int sel = (int) ((float) availableLinks.size() * config.potentialDistalConnections);
		for (int i = 0; i < sel; i++) {
			DistalLink lnk = availableLinks.remove(ZRandomize.getRandomInt(0,availableLinks.size() - 1));
			if (ZRandomize.getRandomInt(0,1)==1) {
				lnk.connection = ZRandomize.getRandomFloat(0,config.connectionThreshold);
			} else {
				lnk.connection = ZRandomize.getRandomFloat(config.connectionThreshold,1.0F);
			}
		}
		for (DistalLink lnk: availableLinks) {
			distLinks.remove(lnk);
		}
	}

}
