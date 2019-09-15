package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;

public class PoolerColumnCell {
	private		PoolerConfig		config		= null;
	private		PoolerColumnGroup	columnGroup	= null;	
	protected	int					posX		= 0;
	protected	int					posY		= 0;
	protected	int					posZ		= 0;
	
	protected	Set<DistalLink>		distLinks	= new HashSet<DistalLink>();
	
	protected	boolean				active		= false;
	protected	boolean				predictive	= false;
	
	protected PoolerColumnCell(PoolerConfig config,PoolerColumnGroup columnGroup,int posX,int posY,int posZ) {
		this.config = config;
		this.columnGroup = columnGroup;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	protected void randomizeConnections() {
		distLinks.clear();
		for (PoolerColumn col: columnGroup.columns) {
			for (int d = 0; d < config.outputDepth; d++) {
				if (!(col.posX == posX && col.posY == posY && d==posZ)) {
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
