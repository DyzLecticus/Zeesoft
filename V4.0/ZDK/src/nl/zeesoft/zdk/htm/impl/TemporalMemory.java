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
	private HashMap<Cell,List<DistalDendrite>>	cellActivity					= new HashMap<Cell,List<DistalDendrite>>();

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
				List<DistalDendrite> availableDendrites = getAvailableActiveDendrites(column);
				if (availableDendrites.size()==0) {
					availableDendrites = getAvailableDendrites(column);
				}
				if (availableDendrites.size()>0) {
					targetDendrite = availableDendrites.get(ZRandomize.getRandomInt(0,availableDendrites.size() - 1));
					winner = cellsById.get(targetDendrite.cellId);
				}
			}
			if (winner==null) {
				winner = column.cells.get(ZRandomize.getRandomInt(0,column.cells.size() - 1));
			}
			winnerCells.add(winner);
			
			if (learn) {
				for (Cell sourceCell: previousWinnerCells) {
					int distance = sourceCell.getDistanceToCell(winner);
					if (distance <= memoryConfig.localDistalConnectionRadius) {
						DistalDendrite synapseDendrite = targetDendrite;
						DistalSynapse synapse = null;
						for (DistalDendrite dendrite: winner.distalDendrites) {
							for (DistalSynapse syn: dendrite.synapses) {
								if (syn.sourceCellId.equals(sourceCell.getId())) {
									synapse = syn;
									synapseDendrite = distalDendritesById.get(syn.dendriteId);
									break;
								}
							}
						}
						if (synapseDendrite==null && winner.distalDendrites.size()<memoryConfig.maxDistalDendritesPerCell) {
							synapseDendrite = new DistalDendrite(winner.getId());
							putObject(synapseDendrite);
							winner.distalDendrites.add(synapseDendrite);
							if (targetDendrite==null) {
								targetDendrite = synapseDendrite;
							}
						}
						if (synapseDendrite!=null) {
							if (synapse==null) {
								synapse = new DistalSynapse(synapseDendrite.getId(),sourceCell.getId());
								putObject(synapse);
								synapseDendrite.synapses.add(synapse);
								synapse.permanence = memoryConfig.permanenceThreshold;
								synapse.permanence += ZRandomize.getRandomFloat(memoryConfig.permanenceIncrement / 2F,memoryConfig.permanenceIncrement);
								addConnectedDendriteForSourceCell(sourceCell,synapseDendrite);
							} else {
								if (synapse.permanence<memoryConfig.permanenceThreshold && synapse.permanence>memoryConfig.permanenceThreshold - memoryConfig.permanenceIncrement) {
									addConnectedDendriteForSourceCell(sourceCell,synapseDendrite);
								}
								synapse.permanence += memoryConfig.permanenceIncrement;
							}
							if (synapse.permanence > 1) {
								synapse.permanence = 1;
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
		list.add(dendrite);
	}

	protected DistalDendrite getDendriteForSourceCell(Column column) {
		DistalDendrite r = null;
		return r;
	}

	protected List<DistalDendrite> getAvailableActiveDendrites(Column column) {
		List<DistalDendrite> r = new ArrayList<DistalDendrite>();
		int min = Integer.MAX_VALUE;
		for (Cell cell: column.cells) {
			List<DistalDendrite> activeDendrites = cellActivity.get(cell);
			if (activeDendrites!=null) {
				for (DistalDendrite dendrite: activeDendrites) {
					Float activity = dendriteActivity.get(dendrite);
					if (activity!=null && activity <= memoryConfig.distalActivityThreshold && dendrite.synapses.size()<memoryConfig.maxDistalSynapsesPerDendrite) {
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

	protected List<DistalDendrite> getAvailableDendrites(Column column) {
		List<DistalDendrite> r = new ArrayList<DistalDendrite>();
		int min = Integer.MAX_VALUE;
		for (Cell cell: column.cells) {
			for (DistalDendrite dendrite: cell.distalDendrites) {
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
		predictiveCells.clear();
		dendriteActivity.clear();
		cellActivity.clear();
		for (Cell cell: winnerCells) {
			List<DistalDendrite> targetDendrites = connectedDendritesPerSourceCell.get(cell);
			if (targetDendrites!=null) {
				for (DistalDendrite target: targetDendrites) {
					if (!dendriteActivity.containsKey(target)) {
						float activity = 0;
						for (DistalSynapse synapse: target.synapses) {
							Cell sourceCell = cellsById.get(synapse.sourceCellId);
							if (winnerCells.contains(sourceCell) && synapse.permanence>memoryConfig.permanenceThreshold) {
								activity += synapse.permanence - memoryConfig.permanenceThreshold; 
							}
						}
						if (activity>0) {
							activity = memoryConfig.distalActivator.applyFunction(activity);
							dendriteActivity.put(target,activity);
		
							Cell targetCell = cellsById.get(target.cellId);
							List<DistalDendrite> list = cellActivity.get(targetCell);
							if (list==null) {
								list = new ArrayList<DistalDendrite>();
								cellActivity.put(targetCell,list);
							}
							list.add(target);
		
							if (activity>memoryConfig.distalActivityThreshold) {
								if (!predictiveCells.contains(cell)) {
									predictiveCells.add(cell);
								}
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
