package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

public class MemoryColumnCell {
	private		MemoryConfig		config				= null;
	protected	int					columnIndex			= 0;
	protected	int					posZ				= 0;
	
	protected	List<DistalLink>	distLinks			= new ArrayList<DistalLink>();
	
	protected	int					activity			= 0;
	
	protected	boolean				active				= false;
	protected	boolean				activePreviously	= false;
	protected	boolean				predictive			= false;
	
	protected MemoryColumnCell(MemoryConfig config,int columnIndex,int posZ) {
		this.config = config;
		this.columnIndex = columnIndex;
		this.posZ = posZ;
	}
	
	protected void cycleActiveState() {
		activePreviously = active;
		active = false;
	}

	protected void calculateActivity() {
		activity = 0;
		float activation = 0;
		for (DistalLink link: distLinks) {
			if (link.cell.active && link.connection>config.connectionThreshold) {
				activity++;
				activation += link.connection;
			}
		}
		if (activity>0) {
			activation = (activation / activity) * (1F / config.connectionThreshold);
		}
		activity = (int) (activation * activity);
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
				link.connection = config.connectionThreshold + config.connectionIncrement;
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
