package nl.zeesoft.zdk.htm.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.mdl.Cell;
import nl.zeesoft.zdk.htm.mdl.Column;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class TemporalMemory extends Model {
	private TemporalMemoryConfig			memoryConfig					= null;
	
	private List<Column>					activeColumns					= new ArrayList<Column>();
	private Set<Column>						previouslyActiveColumns			= new HashSet<Column>();

	private List<Cell>						winnerCells						= new ArrayList<Cell>();							
	private Set<Cell>						previousWinnerCells				= new HashSet<Cell>();							
	
	public TemporalMemory(Model model,TemporalMemoryConfig config) {
		super(model.config);
		model.copyTo(this,false,true);
		this.memoryConfig = config;
		config.initialized = true;
	}
	
	public SDR getOutputSDRForInputSDR(SDR input,boolean learn) {
		SDR r = null;
		
		List<Integer> onBits = input.getOnBits();
		activateColumns(onBits);
		
		
		
		return r;
	}
	
	protected void activateColumns(List<Integer> onBits) {
		previouslyActiveColumns.clear();
		for (Column column: activeColumns) {
			previouslyActiveColumns.add(column);
		}
		activeColumns.clear();
		for (Integer onBit: onBits) {
			activeColumns.add(columns.get(onBit));
		}
	}

	protected void selectWinnerCells(boolean learn) {
		previousWinnerCells.clear();
		for (Cell cell: winnerCells) {
			previousWinnerCells.add(cell);
		}
		winnerCells.clear();
		for (Column column: activeColumns) {
			Cell winner = null;
			for (Cell cell: column.cells) {
				if (previousWinnerCells.contains(cell)) {
					winner = cell;
					break;
				}
			}
			if (learn && winner==null) {
				burstColumn(column);
			}
			if (winner==null) {
				winner = column.cells.get(ZRandomize.getRandomInt(0,column.cells.size()));
			}
			winnerCells.add(winner);
		}
	}
	
	protected void burstColumn(Column column) {
		
	}
}
