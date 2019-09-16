package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;

public class MemoryColumnCell {
	private		MemoryConfig		config				= null;
	private		MemoryColumnGroup	columnGroup			= null;	
	protected	int					columnIndex			= 0;
	protected	int					posZ				= 0;
	
	protected	List<DistalLink>	distLinks			= new ArrayList<DistalLink>();
	
	protected	int					overlapScore		= 0;
	
	protected	boolean				active				= false;
	protected	boolean				activePreviously	= false;
	protected	boolean				predictive			= false;
	
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
					distLinks.add(link);
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
	
	protected void cycleActiveState() {
		activePreviously = active;
		active = false;
	}

	protected void calculateOverlapScoresForActiveLinks() {
		overlapScore = 0;
		for (DistalLink link: distLinks) {
			if (link.cell.active && link.connection>config.connectionThreshold) {
				overlapScore++;
			}
		}
	}
	
	protected void addLinksToCells(List<MemoryColumnCell> toCells) {
		for (MemoryColumnCell toCell: toCells) {
			DistalLink link = new DistalLink();
			link.cell = toCell;
			link.connection = config.connectionThreshold + config.connectionDecrement;
			distLinks.add(link);
		}
	}
	
	protected void learnPreviouslyActiveLinks() {
		for (DistalLink link: distLinks) {
			if (link.cell.activePreviously) {
				link.connection += config.connectionIncrement;
				if (link.connection < 0) {
					link.connection = 0;
				}
			} else {
				link.connection -= config.connectionIncrement;
				if (link.connection > 1) {
					link.connection = 1;
				}
			}
		}
	}
	
	protected List<DistalLink> getPreviouslyActiveLinks() {
		List<DistalLink> r = new ArrayList<DistalLink>();
		for (DistalLink link: distLinks) {
			if (link.cell.activePreviously) {
				r.add(link);
			}
		}
		return r;
	}
}
