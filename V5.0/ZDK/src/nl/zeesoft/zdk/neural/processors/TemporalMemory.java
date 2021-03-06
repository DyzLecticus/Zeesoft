package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRGrid;
import nl.zeesoft.zdk.neural.model.ApicalSegment;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.model.DistalSegment;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class TemporalMemory extends CellGridProcessor {
	public static final int		ACTIVE_CELLS_OUTPUT				= 0;
	public static final int		BURSTING_COLUMNS_OUTPUT			= 1;
	public static final int		PREDICTIVE_CELLS_OUTPUT			= 2;
	public static final int		WINNER_CELLS_OUTPUT				= 3;
	
	protected Lock				lock							= new Lock();
	
	// Configuration
	protected int				sizeX							= 48;
	protected int				sizeY							= 48;
	protected int				sizeZ							= 16;
	
	protected int				maxSegmentsPerCell				= 256;
	protected int				maxSynapsesPerSegment			= 256;
	
	protected float				initialPermanence				= 0.21F;

	protected float				segmentCreationSubsample		= 0.9F;
	
	protected float				distalSegmentDecrement			= 0.2F;
	protected float				apicalSegmentDecrement			= 0.2F;

	protected int				distalPotentialRadius			= 512;
	protected int				apicalPotentialRadius			= 512;

	protected int				activationThreshold				= 13;
	protected int				matchingThreshold				= 10;
	protected int				maxNewSynapseCount				= 20;

	// State
	protected List<Position>	activeInputColumnPositions		= new ArrayList<Position>();
	protected SDRGrid			burstingColumns					= null;
	protected CellGrid			cellGrid						= null;
	protected List<Position>	activeCellPositions				= new ArrayList<Position>();
	protected List<Position>	winnerCellPositions				= new ArrayList<Position>();
	protected List<Position>	prevActiveCellPositions			= new ArrayList<Position>();
	protected List<Position>	prevWinnerCellPositions			= new ArrayList<Position>();
	protected List<Position>	predictiveCellPositions			= new ArrayList<Position>();

	protected List<Position>	activeApicalCellPositions		= new ArrayList<Position>();
	protected List<Position>	prevActiveApicalCellPositions	= new ArrayList<Position>();
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof TemporalMemoryConfig) {
			TemporalMemoryConfig cfg = (TemporalMemoryConfig) config;
			
			this.sizeX = cfg.sizeX;
			this.sizeY = cfg.sizeY;
			this.sizeZ = cfg.sizeZ;
			
			this.maxSegmentsPerCell = cfg.maxSegmentsPerCell;
			this.maxSynapsesPerSegment = cfg.maxSynapsesPerSegment;
			
			this.initialPermanence = cfg.initialPermanence;
			this.permanenceThreshold = cfg.permanenceThreshold;
			this.permanenceIncrement = cfg.permanenceIncrement;
			this.permanenceDecrement = cfg.permanenceDecrement;

			this.segmentCreationSubsample = cfg.segmentCreationSubsample;

			this.distalSegmentDecrement = cfg.distalSegmentDecrement;
			this.apicalSegmentDecrement	= cfg.apicalSegmentDecrement;

			this.distalPotentialRadius  = cfg.distalPotentialRadius;
			this.apicalPotentialRadius  = cfg.apicalPotentialRadius;
			
			this.activationThreshold = cfg.activationThreshold;
			this.matchingThreshold = cfg.matchingThreshold;
			this.maxNewSynapseCount = cfg.maxNewSynapseCount;
			
			if (distalPotentialRadius > sizeX * sizeY) {
				distalPotentialRadius = 0;
			}
			if (apicalPotentialRadius > sizeX * sizeY) {
				apicalPotentialRadius = 0;
			}
		}
	}
	
	@Override
	public void setProperty(String property, Object value) {
		if (property.equals("initialPermanence") && value instanceof Float) {
			initialPermanence = (Float) value;
		} else if (property.equals("permanenceThreshold") && value instanceof Float) {
			permanenceThreshold = (Float) value;
		} else if (property.equals("permanenceIncrement") && value instanceof Float) {
			permanenceIncrement = (Float) value;
		} else if (property.equals("permanenceDecrement") && value instanceof Float) {
			permanenceDecrement = (Float) value;
		} else if (property.equals("segmentCreationSubsample") && value instanceof Float) {
			segmentCreationSubsample = (Float) value;
		} else if (property.equals("distalSegmentDecrement") && value instanceof Float) {
			distalSegmentDecrement = (Float) value;
		} else if (property.equals("apicalSegmentDecrement") && value instanceof Float) {
			apicalSegmentDecrement = (Float) value;
		} else {
			super.setProperty(property, value);
		}
	}
	
	@Override
	public void initialize(CodeRunnerList runnerList) {
		if (sizeX < 4) {
			sizeX = 4;
		}
		if (sizeY < 4) {
			sizeY = 4;
		}
		if (sizeZ < 4) {
			sizeZ = 4;
		}
		
		burstingColumns = new SDRGrid();
		burstingColumns.initialize(sizeX, sizeY);
		
		resetLocalState();
		
		cellGrid = new CellGrid();
		cellGrid.initialize(sizeX, sizeY, sizeZ, runnerList);
	}
	
	@Override
	public void resetConnections(CodeRunnerList runnerList) {
		cellGrid.resetConnections(runnerList, ProcessorFactory.THREADS);
	}
	
	public void resetState(CodeRunnerList runnerList) {
		cellGrid.resetState(runnerList, ProcessorFactory.THREADS);
		if (runnerList!=null) {
			runnerList.add(new RunCode() {
				@Override
				protected boolean run() {
					resetLocalState();
					return true;
				}
			});
		} else {
			resetLocalState();
		}
	}
	
	@Override
	public Str setInput(SDR... sdrs) {
		Str err = new Str();
		outputs.clear();
		
		if (sdrs.length>0) {
			SDR input = sdrs[0];
			if (input.sizeY()==1) {
				input.square();
			}
			if (input.sizeX()==sizeX && input.sizeY()==sizeY) {
				super.setInput(sdrs);
				activeInputColumnPositions = inputs.get(0).toPositions();
				burstingColumns.setValue(false);
								
				prevActiveCellPositions = new ArrayList<Position>(activeCellPositions);
				prevWinnerCellPositions = new ArrayList<Position>(winnerCellPositions);
				activeCellPositions.clear();
				winnerCellPositions.clear();
				
				prevActiveApicalCellPositions = new ArrayList<Position>(activeApicalCellPositions);
				activeApicalCellPositions.clear();
				if (sdrs.length>1) {
					activeApicalCellPositions = sdrs[1].toPositions();
				}
				
				Position.randomizeList(predictiveCellPositions);
			} else {
				err.sb().append("Input dimensions do not match expectation: ");
				err.sb().append(input.sizeX());
				err.sb().append("*");
				err.sb().append(input.sizeY());
				err.sb().append(" <> ");
				err.sb().append(sizeX);
				err.sb().append("*");
				err.sb().append(sizeY);
			}
		} else {
			err.sb().append("At least one input SDR is required");
		}
		
		outputs.add(new SDR(sizeX * sizeZ, sizeY));
		outputs.add(new SDR(sizeX, sizeY));
		outputs.add(new SDR(sizeX * sizeZ, sizeY));
		outputs.add(new SDR(sizeX * sizeZ, sizeY));
		return err;
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, int threads) {
		runnerChain.add(getActivateColumnsRunnerList(threads));
		runnerChain.add(getAdaptColumnsRunnerList(threads));
		runnerChain.add(getClearPredictionRunnerList());
		runnerChain.add(getPredictActiveCellsRunnerList(threads));
		runnerChain.add(getGenerateOutputsRunnerList(threads));
		addIncrementProcessedToProcessorChain(runnerChain);
	}
	
	public void setCellGrid(CellGrid cellGrid) {
		if (cellGrid.sizeX()==sizeX &&
			cellGrid.sizeY()==sizeY &&
			cellGrid.sizeZ()==sizeZ
			) {
			this.cellGrid = cellGrid;
		}
	}
	
	public CellGrid getCellGrid() {
		return cellGrid;
	}

	@Override
	public Str toStr() {
		Str r = super.toStr();
		r.sb().append(learn);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(processed);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(initialPermanence);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(permanenceThreshold);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(permanenceIncrement);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(permanenceDecrement);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(segmentCreationSubsample);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(distalSegmentDecrement);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(apicalSegmentDecrement);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(getCellGrid().toStr().sb());
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> objects = str.split(OBJECT_SEPARATOR);
		if (objects.size()>=10) {
			learn = Boolean.parseBoolean(objects.get(0).toString());
			processed = Integer.parseInt(objects.get(1).toString());
			initialPermanence = Float.parseFloat(objects.get(2).toString());
			permanenceThreshold = Float.parseFloat(objects.get(3).toString());
			permanenceIncrement = Float.parseFloat(objects.get(4).toString());
			permanenceDecrement = Float.parseFloat(objects.get(5).toString());
			segmentCreationSubsample = Float.parseFloat(objects.get(6).toString());
			distalSegmentDecrement = Float.parseFloat(objects.get(7).toString());
			apicalSegmentDecrement = Float.parseFloat(objects.get(8).toString());
			CellGrid cellGrid = new CellGrid();
			cellGrid.fromStr(objects.get(9));
			setCellGrid(cellGrid);
		}
	}

	protected CodeRunnerList getActivateColumnsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				if (Position.posXYIsInList(column.posX(), column.posY(), activeInputColumnPositions)) {
					boolean predicted = false;
					for (Position pos: predictiveCellPositions) {
						if (pos.x==column.posX() && pos.y==column.posY()) {
							activatePredictedColumn(pos);
							predicted = true;
							break;
						}
					}
					if (!predicted) {
						value = true;
						burstColumn(column);
					}
				}
				return value;
			}
		};
		burstingColumns.applyFunction(function, r, threads);
		return r;
	}

	protected CodeRunnerList getAdaptColumnsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object[] applyFunction(GridColumn column, Object[] values) {
				if (learn) {
					if (Position.posXYIsInList(column.posX(), column.posY(), activeCellPositions)) {
						adaptColumn(column);
					} else if (
						(distalSegmentDecrement > 0F || apicalSegmentDecrement > 0F) &&
						Position.posXYIsInList(column.posX(), column.posY(), predictiveCellPositions)
						) {
						if (distalSegmentDecrement > 0F) {
							punishPredictedColumn(column, distalSegmentDecrement, false);
						}
						if (apicalSegmentDecrement > 0F) {
							punishPredictedColumn(column, apicalSegmentDecrement, true);
						}
					}
				}
				return values;
			}
		};
		cellGrid.applyFunction(function, r, threads);
		return r;
	}

	protected CodeRunnerList getClearPredictionRunnerList() {
		CodeRunnerList r = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				predictiveCellPositions.clear();
				return true;
			}
		});
		return r;
	}
	
	protected CodeRunnerList getPredictActiveCellsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				if (cell.distalSegments.size()>0) {
					if (activeCellPositions.size()>0) {
						cell.calculateDistalSegmentActivity(activeCellPositions, permanenceThreshold);
						cell.calculateApicalSegmentActivity(activeApicalCellPositions, permanenceThreshold);
						cell.classifySegmentActivity(activationThreshold, matchingThreshold);
						if ((activeApicalCellPositions.size()==0 && cell.activeDistalSegments.size()>0) ||
							(activeApicalCellPositions.size()>0 && cell.activeDistalSegments.size()>0 && cell.activeApicalSegments.size()>0)
							) {
							lock.lock(this);
							predictiveCellPositions.add(new Position(column.posX(),column.posY(),posZ));
							lock.unlock(this);
						}
					} else {
						cell.reset();
					}
				}
				return value;
			}
		};
		cellGrid.applyFunction(function, r, threads);
		return r;
	}

	protected CodeRunnerList getGenerateOutputsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		if (threads>=4) {
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(ACTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, activeCellPositions));
					return true;
				}
			});
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(BURSTING_COLUMNS_OUTPUT).fromPositions(burstingColumns.getValuePositions(true));
					return true;
				}
			});
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(PREDICTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, predictiveCellPositions));
					return true;
				}
			});
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(WINNER_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, winnerCellPositions));
					return true;
				}
			});
		} else if (threads>=2) {
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(ACTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, activeCellPositions));
					outputs.get(BURSTING_COLUMNS_OUTPUT).fromPositions(burstingColumns.getValuePositions(true));
					return true;
				}
			});
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(PREDICTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, predictiveCellPositions));
					outputs.get(WINNER_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, winnerCellPositions));
					return true;
				}
			});
		} else {
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					outputs.get(ACTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, activeCellPositions));
					outputs.get(BURSTING_COLUMNS_OUTPUT).fromPositions(burstingColumns.getValuePositions(true));
					outputs.get(PREDICTIVE_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, predictiveCellPositions));
					outputs.get(WINNER_CELLS_OUTPUT).fromPositions(Position.flattenTo2D(sizeZ, winnerCellPositions));
					return true;
				}
			});
		}
		return r;
	}
	
	protected void activatePredictedColumn(Position pos) {
		lock.lock(this);
		activeCellPositions.add(pos);
		winnerCellPositions.add(pos);
		lock.unlock(this);
	}
	
	protected void burstColumn(GridColumn column) {
		lock.lock(this);
		activeCellPositions.addAll(cellGrid.getColumnPositions(column));
		lock.unlock(this);
		Cell winnerCell = null;
		// Find potential match
		SortedMap<Integer,List<Cell>> cellsByPotential = new TreeMap<Integer,List<Cell>>();
		for (int z = 0; z < sizeZ; z++) {
			Cell cell = (Cell) cellGrid.getValue(column.posX(), column.posY(), z);
			int potential = 0;
			if (cell.matchingDistalSegment!=null) {
				potential = cell.matchingDistalSegment.potentialSynapses.size();
			}
			if (cell.matchingApicalSegment!=null) {
				potential = potential + cell.matchingApicalSegment.potentialSynapses.size();
			}
			if (potential>0) {
				List<Cell> cells = cellsByPotential.get(potential);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					cellsByPotential.put(potential, cells);
				}
				cells.add(cell);
			}
		}
		if (cellsByPotential.size()>0) {
			List<Cell> potentialCells = cellsByPotential.get(cellsByPotential.lastKey());
			winnerCell = potentialCells.get(Rand.getRandomInt(0, (potentialCells.size() -1)));
		} else {
			// Find least connected cell
			SortedMap<Integer,List<Cell>> cellsBySegments = new TreeMap<Integer,List<Cell>>();
			for (int z = 0; z < sizeZ; z++) {
				Cell cell = (Cell) cellGrid.getValue(column.posX(), column.posY(), z);
				int segments = cell.distalSegments.size() + cell.apicalSegments.size();
				List<Cell> cells = cellsBySegments.get(segments);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					cellsBySegments.put(segments, cells);
				}
				cells.add(cell);
			}
			List<Cell> leastConnectedCells = cellsBySegments.get(cellsBySegments.firstKey());
			winnerCell = leastConnectedCells.get(Rand.getRandomInt(0, (leastConnectedCells.size() -1)));
		}
		if (winnerCell!=null) {
			lock.lock(this);
			winnerCellPositions.add(winnerCell.position);
			lock.unlock(this);
		}
	}

	protected void adaptColumn(GridColumn column) {
		boolean bursting = (boolean) burstingColumns.getValue(column.posX(), column.posY());
		if (prevWinnerCellPositions.size()>0) {
			for (int z = 0; z < sizeZ; z++) {
				if (bursting) {
					if (Position.posIsInList(column.posX(), column.posY(), z, winnerCellPositions)) {
						Cell winnerCell = (Cell) cellGrid.getValue(column.posX(), column.posY(), z);
						if (winnerCell.matchingDistalSegment!=null) {
							winnerCell.adaptMatchingSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
								initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSynapsesPerSegment,
								distalPotentialRadius, apicalPotentialRadius);
						} else {
							winnerCell.createSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
								initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSegmentsPerCell, maxSynapsesPerSegment,
								distalPotentialRadius, apicalPotentialRadius,
								segmentCreationSubsample);
						}
					}
				} else {
					Cell cell = (Cell) cellGrid.getValue(column.posX(), column.posY(), z);
					cell.adaptActiveSegments(
						prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
						initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSynapsesPerSegment,
						distalPotentialRadius, apicalPotentialRadius);
				}
			}
		}
	}

	protected void punishPredictedColumn(GridColumn column, float segmentDecrement, boolean apical) {
		for (int z = 0; z < sizeZ; z++) {
			Cell cell = (Cell) cellGrid.getValue(column.posX(), column.posY(), z);
			if (apical) {
				for (ApicalSegment segment: cell.matchingApicalSegments) {
					segment.adaptSynapses(prevActiveApicalCellPositions, segmentDecrement * -1F, 0F);
				}
			} else {
				for (DistalSegment segment: cell.matchingDistalSegments) {
					segment.adaptSynapses(prevActiveCellPositions, segmentDecrement * -1F, 0F);
				}
			}
		}
	}
	
	protected void resetLocalState() {
		predictiveCellPositions.clear();
		prevActiveCellPositions.clear();
		prevWinnerCellPositions.clear();
		activeCellPositions.clear();
		winnerCellPositions.clear();

		activeApicalCellPositions.clear();
		prevActiveApicalCellPositions.clear();
	}
}
