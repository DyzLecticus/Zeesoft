package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;

public class MemoryColumnCell {
	private		MemoryConfig		config				= null;
	protected	int					columnIndex			= 0;
	protected	int					posX				= 0;
	protected	int					posY				= 0;
	protected	int					posZ				= 0;
	
	protected	List<DistalLink>	distLinks			= new ArrayList<DistalLink>();
	
	protected	List<DistalLink>	forwardLinks		= new ArrayList<DistalLink>();
	
	protected	float				activity			= 0;
	
	protected	boolean				active				= false;
	protected	boolean				activePreviously	= false;
	protected	boolean				predictive			= false;
	
	protected MemoryColumnCell(MemoryConfig config,int columnIndex,int posX,int posY,int posZ) {
		this.config = config;
		this.columnIndex = columnIndex;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	protected void cycleActiveState() {
		activePreviously = active;
		active = false;
	}
	
	protected void calculateActivity() {
		if (active) {
			for (DistalLink link: forwardLinks) {
				if (link.cell.active && link.connection>config.connectionThreshold) {
					link.origin.activity += link.connection - config.connectionThreshold;
				}
			}
		}
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
				int distX = 0;
				int distY = 0;
				int distZ = 0;
				int dist = 0;
				if (toCell.posX>posX) {
					distX = toCell.posX - posX;
				} else {
					distX = posX - toCell.posX;
				}
				if (toCell.posY>posY) {
					distY += toCell.posY - posY;
				} else {
					distY += posY - toCell.posY;
				}
				if (toCell.posZ>posZ) {
					distZ += toCell.posZ - posZ;
				} else {
					distZ += posZ - toCell.posZ;
				}
				dist = (int) Math.sqrt((distX * distX) + (distY * distY));
				dist = (int) Math.sqrt((dist * dist) + (distZ * distZ));
				DistalLink link = new DistalLink();
				link.origin = this;
				link.cell = toCell;
				toCell.forwardLinks.add(link);
				link.connection = config.connectionThreshold;
				link.connection += ZRandomize.getRandomFloat(config.connectionIncrement / 2F,config.connectionIncrement);
				if (dist<=config.localDistalConnectedRadius) {
					link.connection += ZRandomize.getRandomFloat(config.connectionIncrement,config.connectionIncrement * 2F);
				}
				if (link.connection > 1) {
					link.connection = 1;
				}
				distLinks.add(link);
				added++;
				if (added>=addMax) {
					break;
				}
			}
		}
	}
	
	protected int getAlmostActiveLinks() {
		int r = 0;
		for (DistalLink link: distLinks) {
			if (link.cell.activePreviously && link.connection<=config.connectionThreshold && link.connection>config.connectionThreshold - config.connectionIncrement) {
				r++;
			}
		}
		return r;
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
						link.cell.forwardLinks.remove(link);
					}
				}
			}
		}
	}
}
