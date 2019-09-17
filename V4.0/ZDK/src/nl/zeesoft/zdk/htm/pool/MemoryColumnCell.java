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
		/*
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
		if (sel > config.maxDistalConnectionsPerCell) {
			sel = config.maxDistalConnectionsPerCell;
		}
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
		*/
	}
	
	protected void cycleActiveState() {
		activePreviously = active;
		active = false;
	}

	protected void calculateOverlapScoresForActiveLinks() {
		overlapScore = 0;
		float activation = 0;
		for (DistalLink link: distLinks) {
			if (link.cell.active && link.connection>config.connectionThreshold) {
				overlapScore++;
				activation += link.connection;
			}
		}
		overlapScore = (int) (overlapScore * activation);
	}
	
	protected void addLinksToCells(List<MemoryColumnCell> toCells) {
		int addMax = config.maxDistalConnectionsPerCell - distLinks.size();
		if (addMax>0) {
			toCells = new ArrayList<MemoryColumnCell>(toCells);
			toCells.remove(this);
			for (DistalLink link: distLinks) {
				int idx = toCells.indexOf(link.cell);
				if (idx>=0) {
					toCells.remove(idx);
				}
			}
			int added = 0;
			for (MemoryColumnCell toCell: toCells) {
				DistalLink link = new DistalLink();
				link.cell = toCell;
				link.connection = config.connectionThreshold;
				distLinks.add(link);
				added++;
				if (added>=addMax) {
					break;
				}
			}
		}
	}
	
	protected void learnPreviouslyActiveLinks() {
		if (active) {
			for (DistalLink link: distLinks) {
				if (link.cell.activePreviously) {
					link.connection += config.connectionIncrement;
					if (link.connection > 1) {
						link.connection = 1;
					}
				}
			}
		}
	}
	
	protected void unlearnPreviouslyActiveLinks() {
		if (predictive && !active) {
			for (int i = 0; i < distLinks.size(); i++) {
				DistalLink link = distLinks.get(i);
				if (link.cell.activePreviously) {
					link.connection -= config.connectionDecrement;
					if (link.connection<=0) {
						distLinks.remove(i);
					}
				}
			}
		}
	}
}
