package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Memory {
	private		MemoryConfig							config					= null;

	protected	List<MemoryColumn>						columns					= new ArrayList<MemoryColumn>();
	protected	SortedMap<String,MemoryColumnGroup>		columnGroups			= new TreeMap<String,MemoryColumnGroup>();
	protected	MemoryColumnGroup						globalColumnGroup		= null;
	
	public Memory(MemoryConfig config) {
		this.config = config;
	}

	public void initialize() {
		globalColumnGroup = new MemoryColumnGroup();
		for (int i = 0; i < config.size; i++) {
			MemoryColumn mCol = new MemoryColumn(globalColumnGroup,i);
			globalColumnGroup.columns.add(mCol);
			columns.add(mCol);
		}
		initializeCells();
	}

	public void initialize(Pooler pooler) {
		globalColumnGroup = new MemoryColumnGroup();
		for (PoolerColumnGroup pcg: pooler.columnGroups.values()) {
			columnGroups.put(pcg.getId(),new MemoryColumnGroup());
		}
		for (PoolerColumn pCol: pooler.columns) {
			MemoryColumn mCol = new MemoryColumn(columnGroups.get(pCol.columnGroup.getId()),pCol.index);
			globalColumnGroup.columns.add(mCol);
			columns.add(mCol);
		}
		for (PoolerColumnGroup pcg: pooler.columnGroups.values()) {
			MemoryColumnGroup mcg = columnGroups.get(pcg.getId());
			for (PoolerColumn pCol: pcg.columns) {
				mcg.columns.add(columns.get(pCol.index));
			}
		}
		initializeCells();
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = config.getDescription();
		int cells = config.size * config.depth;
		int min = cells; 
		int max = 0;
		int avg = 0;
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				int con = 0;
				for (DistalLink lnk: cell.distLinks) {
					if (lnk.connection>=0) {
						con++;
						avg++;
					}
				}
				if (con<min) {
					min = con;
				}
				if (con>max) {
					max = con;
				}
			}
		}
		if (avg>0) {
			avg = avg / cells;
			r.append("\n");
			r.append("Average distal inputs per cell: ");
			r.append("" + avg);
			if (min!=avg || max!=avg) {
				r.append(" (min: ");
				r.append("" + min);
				r.append(", max: ");
				r.append("" + max);
				r.append(")");
			}
		}
		return r;
	}
	
	public void randomizeConnections() {
		for (MemoryColumn col: columns) {
			col.randomizeConnections();
		}
	}

	public SDR getSDRForInput(SDR input,boolean learn) {
		SDR r = new SDR(config.size);
		
		List<MemoryColumnCell> previouslyActiveCells = cycleActiveState();
		//System.out.println("---> Cycled active state");
		
		List<MemoryColumn> activeColumns = new ArrayList<MemoryColumn>();
		for (Integer onBit: input.getOnBits()) {
			activeColumns.add(columns.get(onBit));
		}
		//System.out.println("---> Active columns: " + activeColumns.size());
		
		activateColumnCells(activeColumns,learn,previouslyActiveCells,r);
		//System.out.println("---> Activated column cells, bursting: " + r.onBits());
		
		calculateOverlapScoresForActiveLinks();
		//System.out.println("---> Calculated overlap scores");
		
		Set<MemoryColumnCell> predictiveCells = selectPredictiveCells(activeColumns.size());
		//System.out.println("---> Selected predictive cells: " + predictiveCells.size());
		
		//for (MemoryColumnCell cell: predictiveCells) {
		//	System.out.println("     ---> Cell: " + cell.columnIndex + "/" + cell.posZ + ", overlap: " + cell.overlapScore);
		//}
		
		predictColumnCells(predictiveCells);
		///System.out.println("---> Set predictions");
		
		return r;
	}

	protected List<MemoryColumnCell> cycleActiveState() {
		List<MemoryColumnCell> r = new ArrayList<MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			col.cycleActiveState(r);
		}
		return r;
	}

	protected void activateColumnCells(List<MemoryColumn> activeColumns,boolean learn,List<MemoryColumnCell> previouslyActiveCells,SDR burstSDR) {
		for (MemoryColumn col: activeColumns) {
			if (col.activateColumnCells(learn,previouslyActiveCells)) {
				burstSDR.setBit(col.index,true);
			}
		}
	}
	
	protected void calculateOverlapScoresForActiveLinks() {
		for (MemoryColumn col: columns) {
			col.calculateOverlapScoresForActiveLinks();
		}
	}

	protected Set<MemoryColumnCell> selectPredictiveCells(int max) {
		Set<MemoryColumnCell> r = new HashSet<MemoryColumnCell>();
		SortedMap<Integer,List<MemoryColumnCell>> map = new TreeMap<Integer,List<MemoryColumnCell>>();
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				if (cell.overlapScore>0) {
					List<MemoryColumnCell> list = map.get(cell.overlapScore);
					if (list==null) {
						list = new ArrayList<MemoryColumnCell>();
						map.put(cell.overlapScore,list);
					}
					list.add(cell);
				}
			}
		}
		Object[] keys = map.keySet().toArray();
		for (int i = (map.size() - 1); i>=0; i--) {
			List<MemoryColumnCell> list = map.get(keys[i]);
			if (max - r.size() < list.size()) {
				for (int s = 0; s < max - r.size(); s++) {
					int sel = ZRandomize.getRandomInt(0,list.size() - 1);
					r.add(list.get(sel));
					list.remove(sel);
				}
			} else {
				for (MemoryColumnCell col: list) {
					r.add(col);
					if (r.size()>=max) {
						break;
					}
				}
			}
			if (r.size()>=max) {
				break;
			}
		}
		return r;
	}
	
	protected void predictColumnCells(Set<MemoryColumnCell> predictiveCells) {
		for (MemoryColumn col: columns) {
			col.predictColumnCells(predictiveCells);
		}
	}
	
	protected void initializeCells() {
		for (MemoryColumn col: columns) {
			for (int i = 0; i < config.depth; i++) {
				MemoryColumnGroup mcg = col.columnGroup;
				if (config.distalColumnGroupGlobal) {
					mcg = globalColumnGroup;
				}
				MemoryColumnCell cell = new MemoryColumnCell(config,mcg,col.index,i);
				col.cells.add(cell);
			}
		}
	}
}
