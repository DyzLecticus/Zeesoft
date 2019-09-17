package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

public class MemoryColumnCell {
	private		MemoryConfig		config				= null;
	protected	int					posX				= 0;
	protected	int					posY				= 0;
	protected	int					posZ				= 0;
	
	protected	List<DistalLink>	distLinks			= new ArrayList<DistalLink>();
	protected	List<DistalLink>	activeLinks			= new ArrayList<DistalLink>();
	
	protected	int					activity			= 0;
	
	protected	boolean				active				= false;
	protected	boolean				activePreviously	= false;
	protected	boolean				predictive			= false;
	
	protected MemoryColumnCell(MemoryConfig config,int posX,int posY,int posZ) {
		this.config = config;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	protected void cycleActiveState() {
		activePreviously = active;
		active = false;
	}

	protected void calculateActivity() {
		activity = 0;
		float activation = 0;
		for (DistalLink link: activeLinks) {
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
				int dist = 0;
				if (toCell.posX>posX) {
					dist = toCell.posX - posX;
				} else {
					dist = posX - toCell.posX;
				}
				if (toCell.posY>posY) {
					dist += toCell.posY - posY;
				} else {
					dist += posY - toCell.posY;
				}
				if (toCell.posZ>posZ) {
					dist += toCell.posZ - posZ;
				} else {
					dist += posZ - toCell.posZ;
				}
				
				DistalLink link = new DistalLink();
				link.cell = toCell;
				link.connection = config.connectionThreshold + (config.connectionIncrement / 2F);
				if (dist<=config.initialDistalConnectedRadius) {
					link.connection += config.connectionIncrement;
					if (dist<=(config.initialDistalConnectedRadius / 2)) {
						link.connection += config.connectionIncrement;
					}
				}
				if (link.connection > 1) {
					link.connection = 1;
				}
				distLinks.add(link);
				activeLinks.add(link);
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
					if (link.connection <= config.connectionThreshold && link.connection + config.connectionIncrement > config.connectionThreshold) {
						activeLinks.add(link);
					}
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
					if (link.connection > config.connectionThreshold && link.connection - config.connectionDecrement <= config.connectionThreshold) {
						activeLinks.remove(link);
					}
					link.connection -= config.connectionDecrement;
					if (link.connection<=0) {
						distLinks.remove(i);
					}
				}
			}
		}
	}
}
