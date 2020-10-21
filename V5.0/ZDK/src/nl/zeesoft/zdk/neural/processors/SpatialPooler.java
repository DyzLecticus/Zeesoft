package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.model.ProximalSegment;
import nl.zeesoft.zdk.neural.model.Synapse;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class SpatialPooler extends CellGridProcessor {
	public static final int		ACTIVE_COLUMNS_OUTPUT		= 0;
	
	// Configuration
	protected int				inputSizeX					= 16;
	protected int				inputSizeY					= 16;
	
	protected int				outputSizeX					= 48;
	protected int				outputSizeY					= 48;
	protected int				outputOnBits				= 46;
	
	protected float				potentialConnections		= 0.85F;
	protected int				potentialRadius				= 500;
	
	protected float				permanenceThreshold			= 0.1F;
	protected float				permanenceIncrement			= 0.05F;
	protected float				permanenceDecrement			= 0.008F;
	
	protected int				activationHistorySize		= 1000;
	protected int				boostStrength				= 2;
	
	// State
	protected List<Position>	activeInputPositions		= new ArrayList<Position>();
	protected Grid				connections					= null;
	protected Grid				activations					= null;
	protected SDRHistory		activationHistory			= null;
	protected float				averageGlobalActivation		= 0.0F;
	protected Grid				boostFactors				= null;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof SpatialPoolerConfig) {
			SpatialPoolerConfig cfg = (SpatialPoolerConfig) config;
			
			this.inputSizeX = cfg.inputSizeX;
			this.inputSizeY = cfg.inputSizeY;
			
			this.outputSizeX = cfg.outputSizeX;
			this.outputSizeY = cfg.outputSizeY;
			this.outputOnBits = cfg.outputOnBits;
			
			this.potentialConnections = cfg.potentialConnections;
			this.potentialRadius = cfg.potentialRadius;
			
			this.permanenceThreshold = cfg.permanenceThreshold;
			this.permanenceIncrement = cfg.permanenceIncrement;
			this.permanenceDecrement = cfg.permanenceDecrement;

			this.activationHistorySize = cfg.activationHistorySize;
			this.boostStrength = cfg.boostStrength;
			
			if (potentialRadius > inputSizeX * inputSizeY) {
				potentialRadius = 0;
			}
		}
	}
		
	@Override
	public Str getDescription() {
		Str r = new Str();
		r.sb().append(" (");
		r.sb().append(inputSizeX);
		r.sb().append("*");
		r.sb().append(inputSizeY);
		r.sb().append(" > ");
		r.sb().append(outputSizeX);
		r.sb().append("*");
		r.sb().append(outputSizeY);
		r.sb().append(")");
		return r;
	}
	
	@Override
	public void initialize(CodeRunnerList runnerList) {
		if (inputSizeX < 2) {
			inputSizeX = 2;
		}
		if (inputSizeY < 2) {
			inputSizeY = 2;
		}
		if (outputSizeX < 4) {
			outputSizeX = 4;
		}
		if (outputSizeY < 4) {
			outputSizeY = 4;
		}
		
		connections = new Grid();
		connections.initialize(outputSizeX, outputSizeY);
		
		activations = new Grid();
		activations.initialize(outputSizeX, outputSizeY, 1, 0F);
		
		activationHistory = new SDRHistory(outputSizeX, outputSizeY, activationHistorySize);
		
		boostFactors = new Grid();
		boostFactors.initialize(outputSizeX, outputSizeY, 1, 1F);
		
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Grid inputPermanences = new Grid();
				inputPermanences.initialize(inputSizeX, inputSizeY, 1, -1F);
				return inputPermanences;
			}
		};
		connections.applyFunction(function, runnerList);
	}
	
	@Override
	public void resetConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object[] applyFunction(GridColumn column, Object[] values) {
				randomConnectColumn(column);
				return values;
			}
		};
		connections.applyFunction(function, runnerList);
	}
	
	@Override
	public void setInput(SDR... sdrs) {
		if (sdrs.length>0) {
			SDR input = sdrs[0];
			if (input.sizeY()==1) {
				input.square();
			}
			if (input.sizeX()==inputSizeX && input.sizeY()==inputSizeY) {
				super.setInput(sdrs);
				activeInputPositions = input.toPositions();
				activations.setValue(0F);
			} else {
				Str msg = new Str("Input dimensions do not match expectation: ");
				msg.sb().append(input.sizeX());
				msg.sb().append("*");
				msg.sb().append(input.sizeY());
				msg.sb().append(" <> ");
				msg.sb().append(inputSizeX);
				msg.sb().append("*");
				msg.sb().append(inputSizeY);
				msg.sb().append("*1");
				Logger.err(this, msg);
			}
		} else {
			Logger.err(this, new Str("At least one input SDR is required"));
		}
		
		outputs.add(new SDR(outputSizeX, outputSizeY));
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		CodeRunnerList activateColumns = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Grid inputPermanences = (Grid) connections.getColumn(column.index()).getValue();
				float newValue = (float) value;
				for (Position pos: activeInputPositions) {
					float permanence = (float) inputPermanences.getValue(pos.x, pos.y);
					if (permanence>permanenceThreshold) {
						newValue = newValue + 1F; 
					}
				}
				float factor = (float)boostFactors.getValue(column.posX(),column.posY());
				if (factor!=1F) {
					newValue = newValue * factor;
				}
				return newValue;
			}
		};
		activations.applyFunction(function, activateColumns);
		
		CodeRunnerList selectWinners = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				List<GridColumn> winners = activations.getColumnsByValue(false,outputOnBits);
				for (GridColumn winner: winners) {
					outputs.get(ACTIVE_COLUMNS_OUTPUT).setBit(winner.posX(), winner.posY(), true);
				}
				return true;
			}
		});
		
		CodeRunnerList adjustPermanences = new CodeRunnerList();
		if (learn) {
			function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, int posZ, Object value) {
					if (outputs.get(ACTIVE_COLUMNS_OUTPUT).getBit(column.posX(), column.posY())) {
						Grid winnerPermanences = (Grid) value;
						for (GridColumn pColumn: winnerPermanences.getColumns()) {
							float permanence = (float)pColumn.getValue();
							if (permanence>=0) {
								if (inputs.get(0).getBit(pColumn.posX(), pColumn.posY())) {
									pColumn.setValue(permanence + permanenceIncrement);
									if ((float)pColumn.getValue()>1F) {
										pColumn.setValue(1F);
									}
								} else {
									pColumn.setValue(permanence - permanenceDecrement);
									if ((float)pColumn.getValue()<0F) {
										pColumn.setValue(0F);
									}
								}
							}
						}
					}
					return value;
				}
			};
			connections.applyFunction(function, adjustPermanences);
		}
		
		CodeRunnerList updateHistory = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				activationHistory.addSDR(outputs.get(ACTIVE_COLUMNS_OUTPUT));
				return true;
			}
		});
		
		CodeRunnerList calculateHistoricActivation = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				averageGlobalActivation = activationHistory.getTotalAverage();
				return true;
			}
		});
		
		CodeRunnerList updateBoostFactors = new CodeRunnerList();
		function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				float average = activationHistory.getAverage(column.posX(),column.posY());
				float factor = 0F;
				if (average!=averageGlobalActivation && boostStrength>1) {
					factor = (float) Math.exp((float)boostStrength * - 1 * (average - averageGlobalActivation));
				} else {
					factor = 1;
				}
				return factor;
			}
		};
		boostFactors.applyFunction(function, updateBoostFactors);
		
		runnerChain.add(activateColumns);
		runnerChain.add(selectWinners);
		if (learn) {
			runnerChain.add(adjustPermanences);
		}
		runnerChain.add(updateHistory);
		runnerChain.add(calculateHistoricActivation);
		runnerChain.add(updateBoostFactors);
		addIncrementProcessedToProcessorChain(runnerChain);
	}
	
	public void fromCellGrid(CellGrid cellGrid, CodeRunnerList runnerList) {
		if (cellGrid.sizeX()==outputSizeX &&
			cellGrid.sizeY()==outputSizeY
			) {
			ColumnFunction function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, int posZ, Object value) {
					Cell cell = (Cell) value;
					Grid inputPermanences = (Grid) connections.getValue(column.posX(), column.posY(), posZ);
					for (ProximalSegment segment: cell.proximalSegments) {
						for (Synapse synapse: segment.synapses) {
							inputPermanences.setValue(synapse.connectTo.x, synapse.connectTo.y, 0, synapse.permanence);
						}
					}
					return value;
				}
			};
			cellGrid.applyFunction(function, runnerList);
		}
	}
	
	public CellGrid toCellGrid(CodeRunnerList runnerList) {
		CellGrid cellGrid = new CellGrid();
		cellGrid.initialize(outputSizeX, outputSizeY, 1);
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = new Cell(new Position(column.posX(), column.posY(), posZ));
				cellGrid.setValue(column.posX(), column.posY(), posZ, cell);
				ProximalSegment segment = new ProximalSegment();
				cell.proximalSegments.add(segment);
				Grid inputPermanences = (Grid) value;
				for (int y = 0; y < inputPermanences.sizeY(); y++) {
					for (int x = 0; x < inputPermanences.sizeX(); x++) {
						float permanence = (float) inputPermanences.getValue(x, y);
						if (permanence >= 0F) {
							Synapse synapse = new Synapse();
							synapse.connectTo.x = x;
							synapse.connectTo.y = y;
							synapse.connectTo.z = 0;
							synapse.permanence = permanence;
							segment.synapses.add(synapse);
						}
					}
				}
				return value;
			}
		};
		connections.applyFunction(function, runnerList);
		return cellGrid;
	}
	
	public void setActivationHistory(SDRHistory activationHistory) {
		if (activationHistory.sizeX()==outputSizeX &&
			activationHistory.sizeY()==outputSizeY
			) {
			this.activationHistory = activationHistory;
		}
	}
	
	public SDRHistory getActivationHistory() {
		return activationHistory;
	}

	@Override
	public Str toStr() {
		Str r = super.toStr();
		r.sb().append(processed);
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(toCellGrid(null).toStr().sb());
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(getActivationHistory().toStr().sb());
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> objects = str.split(OBJECT_SEPARATOR);
		if (objects.size()>=3) {
			processed = Integer.parseInt(objects.get(0).toString());
			CellGrid cellGrid = new CellGrid();
			cellGrid.fromStr(objects.get(1));
			fromCellGrid(cellGrid,null);
			SDRHistory hist = new SDRHistory();
			hist.fromStr(objects.get(2));
			setActivationHistory(hist);
		}
	}

	protected void randomConnectColumn(GridColumn column) {
		Grid inputs = (Grid) column.getValue();
		int posXonInput = connections.getPosXOn(inputs,column.posX()); 
		int posYonInput = connections.getPosYOn(inputs,column.posY()); 
		for (GridColumn inputColumn: inputs.getColumns()) {
			boolean inReach = true;
			if (potentialRadius> 0 && potentialRadius < inputSizeX * inputSizeY) {
				int distance = Position.getDistance(inputColumn.posX(), inputColumn.posY(), posXonInput, posYonInput);
				inReach = distance < potentialRadius;
			}
			if (inReach && Rand.getRandomFloat(0, 1) < potentialConnections) {
				if (Rand.getRandomInt(0, 1) == 1) {
					inputColumn.setValue(Rand.getRandomFloat(permanenceThreshold, 1));
				} else {
					inputColumn.setValue(Rand.getRandomFloat(0, permanenceThreshold));
				}
			} else {
				inputColumn.setValue(-1F);
			}
		}
	}
}
