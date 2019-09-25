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
	
	private Set<DistalDendrite>					activeDendrites					= new HashSet<DistalDendrite>();
	private Set<DistalDendrite>					matchingDendrites				= new HashSet<DistalDendrite>();
	private HashMap<DistalDendrite,Float>		dendriteActivity				= new HashMap<DistalDendrite,Float>();

	private List<Cell>							winnerCells						= new ArrayList<Cell>();							
	private Set<Cell>							previousWinnerCells				= new HashSet<Cell>();
	private List<Cell>							predictiveCells					= new ArrayList<Cell>();							
	
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
		List<Column> activeColumns = getActiveColumns(onBits);
		
		//System.out.println("Predictive cells before selecting winners: " + predictiveCells.size());
		List<Integer> burstingColumnIndices = selectWinnerCells(activeColumns,learn);
		//System.out.println("Bursting columns: " + burstingColumnIndices.size());
		
		calculateDendriteActivity(learn);
		//System.out.println("Predictive cells after calculating activity: " + predictiveCells.size());
		
		r = recordBurstingColumnsInSDR(burstingColumnIndices);
		
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
							addConnectedDendriteForSourceCell(sourceCell,dendrite);
						}
					}
				}
			}
		}
	}
	
	protected List<Column> getActiveColumns(List<Integer> onBits) {
		List<Column> r = new ArrayList<Column>();
		for (Integer onBit: onBits) {
			r.add(columns.get(onBit));
		}
		return r;
	}

	protected List<Integer> selectWinnerCells(List<Column> activeColumns,boolean learn) {
		List<Integer> r = new ArrayList<Integer>();
		previousWinnerCells.clear();
		for (Cell cell: winnerCells) {
			previousWinnerCells.add(cell);
		}
		winnerCells.clear();
		for (Column column: activeColumns) {
			Cell winner = null;
			for (Cell cell: column.cells) {
				if (predictiveCells.contains(cell)) {
					winner = cell;
					break;
				}
			}
			DistalDendrite targetDendrite = null;
			if (learn && winner==null) {
				r.add(column.index);
				List<DistalDendrite> availableDendrites = getAvailableDendrites(column,matchingDendrites);
				if (availableDendrites.size()==0) {
					availableDendrites = getAvailableDendrites(column,matchingDendrites);
				}
				if (availableDendrites.size()==0) {
					availableDendrites = getAvailableDendrites(column,null);
				}
				if (availableDendrites.size()>0) {
					targetDendrite = availableDendrites.get(ZRandomize.getRandomInt(0,availableDendrites.size() - 1));
					winner = cellsById.get(targetDendrite.cellId);
				} else {
					winner = column.cells.get(ZRandomize.getRandomInt(0,column.cells.size() - 1));
					targetDendrite = new DistalDendrite(winner.getId());
					putObject(targetDendrite);
					winner.distalDendrites.add(targetDendrite);
				}
			}
			if (winner==null) {
				winner = column.cells.get(ZRandomize.getRandomInt(0,column.cells.size() - 1));
			}
			winnerCells.add(winner);
			
			if (learn) {
				for (Cell sourceCell: previousWinnerCells) {
					if (targetDendrite!=null) {
						int distance = sourceCell.getDistanceToCell(winner);
						if (distance <= memoryConfig.localDistalConnectionRadius) {
							DistalSynapse synapse = new DistalSynapse(targetDendrite.getId(),sourceCell.getId());
							putObject(synapse);
							targetDendrite.synapses.add(synapse);
							synapse.permanence = memoryConfig.permanenceThreshold;
							synapse.permanence += ZRandomize.getRandomFloat(memoryConfig.permanenceIncrement / 2F,memoryConfig.permanenceIncrement);
							addConnectedDendriteForSourceCell(sourceCell,targetDendrite);
						}
					} else {
						// Increment existing connections between sources and winner
						for (DistalDendrite dendrite: winner.distalDendrites) {
							for (DistalSynapse synapse: dendrite.synapses) {
								if (synapse.sourceCellId.equals(sourceCell.getId())) {
									if (synapse.permanence<memoryConfig.permanenceThreshold && synapse.permanence>memoryConfig.permanenceThreshold - memoryConfig.permanenceIncrement) {
										addConnectedDendriteForSourceCell(sourceCell,dendrite);
									}
									synapse.permanence += memoryConfig.permanenceIncrement;
									if (synapse.permanence > 1) {
										synapse.permanence = 1;
									}
								}
							}
						}
					}
				}
			}
		}
		return r;
	}

	protected void addConnectedDendriteForSourceCell(Cell sourceCell, DistalDendrite dendrite) {
		List<DistalDendrite> list = connectedDendritesPerSourceCell.get(sourceCell);
		if (list==null) {
			list = new ArrayList<DistalDendrite>();
			connectedDendritesPerSourceCell.put(sourceCell,list);
		}
		if (!list.contains(dendrite)) {
			list.add(dendrite);
		}
	}

	protected DistalDendrite getDendriteForSourceCell(Column column) {
		DistalDendrite r = null;
		return r;
	}

	protected List<DistalDendrite> getAvailableDendrites(Column column,Set<DistalDendrite> list) {
		List<DistalDendrite> r = new ArrayList<DistalDendrite>();
		int min = Integer.MAX_VALUE;
		for (Cell cell: column.cells) {
			for (DistalDendrite dendrite: cell.distalDendrites) {
				if (list==null || list.size()==0 || list.contains(dendrite)) {
					if (dendrite.synapses.size()<memoryConfig.maxDistalSynapsesPerDendrite) {
						if (dendrite.synapses.size()<min) {
							r.clear();
							min = dendrite.synapses.size();
						}
						if (dendrite.synapses.size()==min) {
							r.add(dendrite);
						}
					}
				}
			}
		}
		return r;
	}

	protected void calculateDendriteActivity(boolean learn) {
		if (learn) {
			for (Cell cell: predictiveCells) {
				if (!winnerCells.contains(cell)) {
					for (DistalDendrite dendrite: cell.distalDendrites) {
						if (dendriteActivity.containsKey(dendrite)) {
							List<DistalSynapse> remove = new ArrayList<DistalSynapse>();
							for (DistalSynapse synapse: dendrite.synapses) {
								if (synapse.permanence>memoryConfig.permanenceThreshold && synapse.permanence - memoryConfig.permanenceDecrement<=memoryConfig.permanenceThreshold) {
									Cell sourceCell = cellsById.get(synapse.sourceCellId);
									List<DistalDendrite> list = connectedDendritesPerSourceCell.get(sourceCell);
									if (list!=null) {
										list.remove(dendrite);
										if (list.size()==0) {
											connectedDendritesPerSourceCell.remove(sourceCell);
										}
									}
								}
								synapse.permanence -= memoryConfig.permanenceDecrement;
								if (synapse.permanence<=0) {
									remove.add(synapse);
								}
							}
							for (DistalSynapse synapse: remove) {
								dendrite.synapses.remove(synapse);
								removeObject(synapse);
							}
						}
					}
				}
			}
		}
		
		activeDendrites.clear();
		matchingDendrites.clear();
		dendriteActivity.clear();

		predictiveCells.clear();
		for (Cell cell: winnerCells) {
			List<DistalDendrite> targetDendrites = connectedDendritesPerSourceCell.get(cell);
			if (targetDendrites!=null) {
				for (DistalDendrite target: targetDendrites) {
					if (!dendriteActivity.containsKey(target)) {
						float activity = 0;
						int num = 0;
						for (DistalSynapse synapse: target.synapses) {
							Cell sourceCell = cellsById.get(synapse.sourceCellId);
							if (winnerCells.contains(sourceCell) && synapse.permanence>memoryConfig.permanenceThreshold) {
								activity += synapse.permanence - memoryConfig.permanenceThreshold;
								num++;
							}
						}
						if (activity>0) {
							dendriteActivity.put(target,activity);
							if (num>=memoryConfig.minActiveSynapses) {
								activeDendrites.add(target);
								if (!predictiveCells.contains(cell)) {
									predictiveCells.add(cell);
								}
							}
							if (num>=memoryConfig.minMatchingSynapses) {
								matchingDendrites.add(target);
							}
						}
					}
				}
			}
		}
	}

	protected SDR recordBurstingColumnsInSDR(List<Integer> burstingColumnIndices) {
		SDR r = config.getNewSDR();
		for (Integer onBit: burstingColumnIndices) {
			r.setBit(onBit,true);
		}
		return r;
	}
}
