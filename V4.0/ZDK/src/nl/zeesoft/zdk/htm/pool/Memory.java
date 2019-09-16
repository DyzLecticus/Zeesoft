package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class Memory {
	private		MemoryConfig							config				= null;

	protected	List<MemoryColumn>						columns				= new ArrayList<MemoryColumn>();
	protected	SortedMap<String,MemoryColumnGroup>		columnGroups		= new TreeMap<String,MemoryColumnGroup>();
	protected	MemoryColumnGroup						globalColumnGroup	= null;
	
	public Memory(MemoryConfig config) {
		this.config = config;
	}
	
	public void randomizeConnections() {
		for (MemoryColumn col: columns) {
			col.randomizeConnections();
		}
	}

	public SDR getSDRForInput(SDR sdr,boolean learn) {
		SDR r = new SDR(config.size);
		
		List<MemoryColumn> activeColumns = new ArrayList<MemoryColumn>();
		for (Integer onBit: sdr.getOnBits()) {
			activeColumns.add(columns.get(onBit));
		}
		
		activateColumnCells(activeColumns,r);
		
		return r;
	}
	
	protected void activateColumnCells(List<MemoryColumn> activeColumns,SDR burstSDR) {
		for (MemoryColumn col: activeColumns) {
			if (col.activateColumnCells()) {
				burstSDR.setBit(col.index,true);
			}
		}
	}

	public void initialize() {
		globalColumnGroup = new MemoryColumnGroup();
		for (int i = 0; i < config.size; i++) {
			MemoryColumn mCol = new MemoryColumn(config,globalColumnGroup,i);
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
			MemoryColumn mCol = new MemoryColumn(config,columnGroups.get(pCol.columnGroup.getId()),pCol.index);
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
