package nl.zeesoft.zdk.htm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.mdl.Cell;
import nl.zeesoft.zdk.htm.mdl.Column;
import nl.zeesoft.zdk.htm.mdl.DistalDendrite;
import nl.zeesoft.zdk.htm.mdl.DistalSynapse;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class TemporalMemory extends Model {
	private TemporalMemoryConfig				memoryConfig					= null;

	private HashMap<Cell,List<DistalDendrite>>	connectedDendritesPerSourceCell	= new HashMap<Cell,List<DistalDendrite>>();
	private HashMap<DistalDendrite,Float>		dendriteActivity				= new HashMap<DistalDendrite,Float>();

	private List<Column>						activeColumns					= new ArrayList<Column>();
	private Set<Column>							previouslyActiveColumns			= new HashSet<Column>();

	private List<Cell>							winnerCells						= new ArrayList<Cell>();							
	private Set<Cell>							previousWinnerCells				= new HashSet<Cell>();
	
	private List<Cell>							predictiveCells					= new ArrayList<Cell>();							
	private Set<Cell>							previouslyPredictiveCells		= new HashSet<Cell>();
	
	public TemporalMemory(Model model,TemporalMemoryConfig config) {
		super(model.config);
		model.copyTo(this,false,true);
		this.memoryConfig = config;
		config.initialized = true;
		initializeConnectedSynapsesPerSourceCell();
	}
	
	public SDR getOutputSDRForInputSDR(SDR input,boolean learn) {
		SDR r = null;
		
		List<Integer> onBits = input.getOnBits();
		activateColumns(onBits);
		
		selectWinnerCells(learn);
		
		calculateDendriteActivity(winnerCells);
		
		return r;
	}
	

	@Override
	public void clearDistalDendrites() {
		super.clearDistalDendrites();
		connectedDendritesPerSourceCell.clear();
	}
	
	
	protected void initializeConnectedSynapsesPerSourceCell() {
		connectedDendritesPerSourceCell.clear();
		for (Column column: columns) {
			for (Cell cell: column.cells) {
				for (DistalDendrite dendrite: cell.distalDendrites) {
					for (DistalSynapse synapse: dendrite.synapses) {
						if (synapse.permanence>memoryConfig.permanenceThreshold) {
							Cell sourceCell = cellsById.get(synapse.sourceCellId);
							List<DistalDendrite> list = connectedDendritesPerSourceCell.get(sourceCell);
							if (list==null) {
								list = new ArrayList<DistalDendrite>();
								connectedDendritesPerSourceCell.put(sourceCell,list);
							}
							list.add(dendrite);
						}
					}
				}
			}
		}
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
				burstColumn(column,learn);
			}
			if (winner==null) {
				winner = column.cells.get(ZRandomize.getRandomInt(0,column.cells.size()));
			}
			winnerCells.add(winner);
		}
	}
	
	protected void burstColumn(Column column,boolean learn) {
		// TODO: Burst; select 'almost' winner dendrite
		// If not; select least connected dendrite
		// If selected dendrite is 'full'
	}
	
	protected void calculateDendriteActivity(List<Cell> winnerCells) {
		previouslyPredictiveCells.clear();
		for (Cell cell: predictiveCells) {
			previouslyPredictiveCells.add(cell);
		}
		predictiveCells.clear();
		dendriteActivity.clear();
		for (Cell cell: winnerCells) {
			List<DistalDendrite> targetDendrites = connectedDendritesPerSourceCell.get(cell);
			for (DistalDendrite target: targetDendrites) {
				if (!dendriteActivity.containsKey(target)) {
					float activity = 0;
					for (DistalSynapse synapse: target.synapses) {
						Cell sourceCell = cellsById.get(synapse.sourceCellId);
						if (winnerCells.contains(sourceCell) && synapse.permanence>memoryConfig.permanenceThreshold) {
							activity += synapse.permanence - memoryConfig.permanenceThreshold; 
						}
					}
					activity = memoryConfig.distalActivator.applyFunction(activity);
					dendriteActivity.put(target,activity);
					if (activity>memoryConfig.distalActivityThreshold) {
						if (predictiveCells.contains(cell)) {
							predictiveCells.add(cell);
						}
					}
				}
			}
		}
	}
}
