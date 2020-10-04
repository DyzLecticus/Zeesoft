package nl.zeesoft.zdk.neural;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.HistoricalGrid;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class SpatialPooler {
	public Logger				logger						= new Logger();
	
	public int					inputSizeX					= 16;
	public int					inputSizeY					= 16;
	
	public int					outputSizeX					= 48;
	public int					outputSizeY					= 48;
	public int					outputOnBits				= 46;
	
	public float				potentialConnections		= 0.85F;
	public float				potentialRadius				= 500;
	
	public float				connectionThreshold			= 0.1F;
	public float				permanenceIncrement			= 0.05F;
	public float				permanenceDecrement			= 0.008F;
	
	public int					activationHistorySize		= 1000;
	public int					boostStrength				= 10;
	
	protected SDR				input						= null;
	protected List<GridColumn>	activeInputColumns			= null;
	protected SDR				output						= null;
	protected Grid				connections					= null;
	protected Grid				activations					= null;
	protected HistoricalGrid	activationHistory			= null;
	protected float				averageGlobalActivation	= 0.0F;
	protected Grid				boostFactors				= null;
		
	public Str getDimensions() {
		Str r = new Str();
		r.sb().append(inputSizeX + "*" + inputSizeY);
		r.sb().append(" > ");
		r.sb().append(outputSizeX + "*" + outputSizeY);
		r.sb().append("...");
		return r;
	}
	
	public void initialize() {
		Str msg = new Str("Initializing spatial pooler; ");
		msg.sb().append(inputSizeX + "*" + inputSizeY);
		msg.sb().append(" > ");
		msg.sb().append(outputSizeX + "*" + outputSizeY);
		msg.sb().append("...");
		logger.debug(this, msg);
		initialize(null);
		logger.debug(this, new Str("Initialized spatial pooler"));
	}

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
		
		input = new SDR();
		input.initialize(inputSizeX, inputSizeY);
		
		output = new SDR();
		output.initialize(outputSizeX, outputSizeY);
		
		connections = new Grid(output);
		
		activations = new Grid();
		activations.initialize(output.sizeX(),output.sizeY(), 1, 0F);
		
		activationHistory = new HistoricalGrid();
		activationHistory.initialize(output.sizeX(), output.sizeY(), activationHistorySize);
		
		boostFactors = new Grid();
		boostFactors.initialize(output.sizeX(),output.sizeY(), 1, 1F);
		
		final int inX = input.sizeX();
		final int inY = input.sizeY();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
				Grid g = new Grid();
				g.initialize(inX, inY, 1, 0F);
				return g;
			}
		};
		connections.applyFunction(function, runnerList);
	}
	
	public void randomizeConnections() {
		logger.debug(this, new Str("Randomizing spatial pooler connections ..."));
		randomizeConnections(null);
		logger.debug(this, new Str("Randomized spatial pooler connections"));
	}
	
	public void randomizeConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object[] applyFunction(GridColumn column, Object[] values) {
				randomConnectColumn(column);
				return values;
			}
		};
		connections.applyFunction(function, runnerList);
	}

	public void setInput(SDR sdr) {
		input.copyValuesFrom(sdr.getColumns());
		activeInputColumns = input.getActiveColumns();
		activations.setValue(0F);
		output.setValue(false);
	}

	public CodeRunnerChain getProcessorChain(boolean learn) {
		CodeRunnerChain r = new CodeRunnerChain();
		
		CodeRunnerList activateColumns = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
				Grid inputPermanences = (Grid) connections.getColumn(column.index()).getValue();
				float newValue = (float) value;
				for (GridColumn inputColumn: activeInputColumns) {
					float permanence = (float) inputPermanences.getValue(inputColumn.posX(), inputColumn.posY());
					if (permanence>connectionThreshold) {
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
		
		CodeRunnerList selectWinners = new CodeRunnerList();
		selectWinners.add(new RunCode() {
			@Override
			protected boolean run() {
				List<GridColumn> winners = activations.getColumnsByValue(false,outputOnBits);
				for (GridColumn winner: winners) {
					output.setValue(winner.posX(), winner.posY(), true);
				}
				return true;
			}
		});
		
		CodeRunnerList adjustPermanences = new CodeRunnerList();
		if (learn) {
			function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, Object value) {
					if ((boolean)output.getValue(column.posX(), column.posY())) {
						Grid winnerPermanences = (Grid) value;
						for (GridColumn pColumn: winnerPermanences.getColumns()) {
							float permanence = (float)pColumn.getValue();
							if (permanence>=0) {
								if ((boolean)input.getValue(pColumn.posX(), pColumn.posY())) {
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
		
		CodeRunnerList cycleHistory = new CodeRunnerList();
		activationHistory.cycle(cycleHistory);
		
		CodeRunnerList updateHistory = new CodeRunnerList();
		activationHistory.update(output, updateHistory);
		
		CodeRunnerList calculateHistoricActivation = new CodeRunnerList();
		calculateHistoricActivation.add(new RunCode() {
			@Override
			protected boolean run() {
				averageGlobalActivation = activationHistory.getAverageValue();
				return true;
			}
		});
		
		
		CodeRunnerList updateBoostFactors = new CodeRunnerList();
		function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
				float average = column.getAverageValue();
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
		
		r.add(activateColumns);
		r.add(selectWinners);
		if (learn) {
			r.add(adjustPermanences);
		}
		r.add(cycleHistory);
		r.add(updateHistory);
		r.add(calculateHistoricActivation);
		r.add(updateBoostFactors);
		
		return r;
	}
	
	public SDR getOutput() {
		return new SDR(output);
	}
	

	protected void randomConnectColumn(GridColumn column) {
		Grid inputs = (Grid) column.getValue();
		int posXonInput = connections.getPosXOn(inputs,column.posX()); 
		int posYonInput = connections.getPosYOn(inputs,column.posY()); 
		for (GridColumn inputColumn: inputs.getColumns()) {
			boolean inReach = true;
			if (potentialRadius < input.sizeX() * input.sizeY()) {
				int distance = Grid.getDistance(inputColumn.posX(), inputColumn.posY(), posXonInput, posYonInput);
				inReach = distance < potentialRadius;
			}
			if (inReach && Rand.getRandomFloat(0, 1) < potentialConnections) {
				if (Rand.getRandomInt(0, 1) ==1) {
					inputColumn.setValue(Rand.getRandomFloat(connectionThreshold, 1));
				} else {
					inputColumn.setValue(Rand.getRandomFloat(0, connectionThreshold));
				}
			} else {
				inputColumn.setValue(-1F);
			}
		}
	}
}
