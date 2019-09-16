package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.functions.ZRandomize;

public class MemoryColumn {
	protected	MemoryColumnGroup		columnGroup	= null;	
	protected	int						index		= 0;
	
	protected	List<MemoryColumnCell>	cells		= new ArrayList<MemoryColumnCell>();
	
	protected MemoryColumn(MemoryColumnGroup columnGroup,int index) {
		this.columnGroup = columnGroup;
		this.index = index;
	}
	
	protected void randomizeConnections() {
		for (MemoryColumnCell cell: cells) {
			cell.randomizeConnections();
		}
	}
	
	protected void cycleActiveState(List<MemoryColumnCell> previouslyActiveCells) {
		for (MemoryColumnCell cell: cells) {
			if (cell.active) {
				previouslyActiveCells.add(cell);
			}
			cell.cycleActiveState();
		}
	}
	
	protected boolean activateColumnCells(boolean learn,List<MemoryColumnCell> previouslyActiveCells) {
		boolean r = false;
		MemoryColumnCell winner = null;
		for (MemoryColumnCell cell: cells) {
			if (cell.predictive) {
				cell.active = true;
				winner = cell;
				break;
			}
		}
		if (winner==null) {
			r = true;
			SortedMap<Integer,List<MemoryColumnCell>> cellsByDistalLinks = new TreeMap<Integer,List<MemoryColumnCell>>();
			for (MemoryColumnCell cell: cells) {
				cell.active = true;
				
				int key = cell.distLinks.size();
				List<MemoryColumnCell> list = cellsByDistalLinks.get(key);
				if (list==null) {
					list = new ArrayList<MemoryColumnCell>();
					cellsByDistalLinks.put(key,list);
				}
				list.add(cell);
			}
			if (learn && previouslyActiveCells.size()>0) {
				List<MemoryColumnCell> list = cellsByDistalLinks.get(cellsByDistalLinks.firstKey());
				if (list.size()==1) {
					winner = list.get(0);
				} else {
					winner = list.get(ZRandomize.getRandomInt(0,list.size()-1));
				}
				//System.out.println("Growing: " + previouslyActiveCells.size() + " !!!");
				winner.addLinksToCells(previouslyActiveCells);
			} else {
				winner = cells.get(ZRandomize.getRandomInt(0,cells.size()-1));
			}
		}
		if (winner!=null && learn) {
			winner.learnPreviouslyActiveLinks();
		}
		return r;
	}
	
	protected void calculateOverlapScoresForActiveLinks() {
		for (MemoryColumnCell cell: cells) {
			cell.calculateOverlapScoresForActiveLinks();
		}
	}

	protected void predictColumnCells(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		for (MemoryColumnCell cell: cells) {
			if (learn && cell.predictive && !cell.active) {
				cell.unlearnPreviouslyActiveLinks();
			}
			cell.predictive = predictiveCells.contains(cell);
		}
	}
}
