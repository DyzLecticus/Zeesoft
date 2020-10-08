package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

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

public class SpatialPooler extends SDRProcessor {
	public int					inputSizeX					= 16;
	public int					inputSizeY					= 16;
	
	public int					outputSizeX					= 48;
	public int					outputSizeY					= 48;
	public int					outputOnBits				= 46;
	
	public float				potentialConnections		= 0.85F;
	public float				potentialRadius				= 500;
	
	public float				permanenceThreshold			= 0.1F;
	public float				permanenceIncrement			= 0.05F;
	public float				permanenceDecrement			= 0.008F;
	
	public int					activationHistorySize		= 1000;
	public int					boostStrength				= 10;
	
	protected List<GridColumn>	activeInputColumns			= new ArrayList<GridColumn>();
	protected Grid				connections					= null;
	protected Grid				activations					= null;
	protected HistoricalGrid	activationHistory			= null;
	protected float				averageGlobalActivation		= 0.0F;
	protected Grid				boostFactors				= null;
		
	@Override
	public Str getDescription() {
		Str r = new Str();
		r.sb().append(" (");
		r.sb().append(inputSizeX + "*" + inputSizeY);
		r.sb().append(" > ");
		r.sb().append(outputSizeX + "*" + outputSizeY);
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
		
		final int inSizeX = input.sizeX();
		final int inSizeY = input.sizeY();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Grid inputPermanences = new Grid();
				inputPermanences.initialize(inSizeX, inSizeY, 1, 0F);
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
	public void setInput(SDR sdr) {
		super.setInput(sdr);
		activeInputColumns = input.getActiveColumns();
		activations.setValue(0F);
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		CodeRunnerList activateColumns = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Grid inputPermanences = (Grid) connections.getColumn(column.index()).getValue();
				float newValue = (float) value;
				for (GridColumn inputColumn: activeInputColumns) {
					float permanence = (float) inputPermanences.getValue(inputColumn.posX(), inputColumn.posY());
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
					output.setValue(winner.posX(), winner.posY(), true);
				}
				return true;
			}
		});
		
		CodeRunnerList adjustPermanences = new CodeRunnerList();
		if (learn) {
			function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, int posZ, Object value) {
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
		
		CodeRunnerList calculateHistoricActivation = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				averageGlobalActivation = activationHistory.getAverageValue();
				return true;
			}
		});
		
		CodeRunnerList updateBoostFactors = new CodeRunnerList();
		function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
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
		
		runnerChain.add(activateColumns);
		runnerChain.add(selectWinners);
		if (learn) {
			runnerChain.add(adjustPermanences);
		}
		runnerChain.add(cycleHistory);
		runnerChain.add(updateHistory);
		runnerChain.add(calculateHistoricActivation);
		runnerChain.add(updateBoostFactors);
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
