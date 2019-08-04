package nl.zeesoft.zdk.genetic;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.TestSet;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Evolver extends Locker {
	private WorkerUnion				union				= null;
	private int						maxHiddenLayers		= 1;
	private int						maxHiddenNeurons	= 2;
	private int						codePropertyStart	= 0;
	private TestSet					baseTestSet			= null;
	private List<EvolverWorker>		evolvers			= new ArrayList<EvolverWorker>();
	
	private boolean					debug				= false;
	private float					mutationRate		= 0.05F;
	private int						trainEpochs			= 5000;
	private int						trainEpochSize		= 10;
	private int						evolverSleepMs		= 10;
	
	private EvolverUnit				bestSoFar			= null;
	
	public Evolver(Messenger msgr, WorkerUnion uni, int maxHiddenLayers, int maxHiddenNeurons, int codePropertyStart, TestSet baseTestSet,int evolvers) {
		super(msgr);
		union = uni;
		initialize(maxHiddenLayers,maxHiddenNeurons,codePropertyStart,baseTestSet,evolvers);
	}
	
	public void setDebug(boolean debug) {
		lockMe(this);
		this.debug = debug;
		unlockMe(this);
	}
	
	public void setMutationRate(float mutationRate) {
		lockMe(this);
		this.mutationRate = mutationRate;
		unlockMe(this);
	}
	
	public void setTrainEpochs(int trainEpochs) {
		lockMe(this);
		this.trainEpochs = trainEpochs;
		unlockMe(this);
	}
	
	public void setTrainEpochSize(int trainEpochSize) {
		lockMe(this);
		this.trainEpochSize = trainEpochSize;
		unlockMe(this);
	}
	
	public void setEvolverSleepMs(int evolverSleepMs) {
		lockMe(this);
		this.evolverSleepMs = evolverSleepMs;
		unlockMe(this);
	}
	
	public void start() {
		for (EvolverWorker evolver: evolvers) {
			evolver.start();
		}
	}
	
	public void stop() {
		for (EvolverWorker evolver: evolvers) {
			evolver.stop();
		}
	}
	
	public EvolverUnit getBestSoFar() {
		EvolverUnit r = null;
		lockMe(this);
		r = bestSoFar.copy();
		unlockMe(this);
		return r;
	}
	
	protected EvolverUnit getNewEvolverUnit(EvolverWorker evolver) {
		EvolverUnit r = new EvolverUnit();
		r.geneticNN = new GeneticNN(baseTestSet.inputNeurons,maxHiddenLayers,maxHiddenNeurons,baseTestSet.outputNeurons,codePropertyStart);
		GeneticCode code = null;
		if (evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
			lockMe(this);
			if (bestSoFar!=null) {
				code = new GeneticCode();
				code.setCode(bestSoFar.geneticNN.code.getCode());
				code.mutate(mutationRate);
			}
			unlockMe(this);
		}
		r.geneticNN.generateNewNN(code);
		r.trainingProgram = getNewTrainingProgram(r.geneticNN.neuralNet,baseTestSet.copy());
		return r;
	}

	protected TrainingProgram getNewTrainingProgram(NeuralNet nn, TestSet baseTs) {
		return new TrainingProgram(nn,baseTs);
	}
	
	protected int getTrainEpochs() {
		int r = 0;
		lockMe(this);
		r = trainEpochs;
		unlockMe(this);
		return r;
	}
	
	protected int getTrainEpochSize() {
		int r = 0;
		lockMe(this);
		r = trainEpochSize;
		unlockMe(this);
		return r;
	}
	
	protected int getEvolverSleepMs() {
		int r = 0;
		lockMe(this);
		r = evolverSleepMs;
		unlockMe(this);
		return r;
	}
	
	protected void finishedTrainigProgram(EvolverWorker worker,EvolverUnit unit) {
		if (unit.trainingProgram.latestResults.success) {
			lockMe(this);
			if (bestSoFar==null || unit.compareTo(bestSoFar)>0) {
				bestSoFar = unit;
				if (debug && getMessenger()!=null) {
					getMessenger().debug(this,"Selected new best genetic neural net;\n" + unit.toStringBuilder());
				}
			}
			unlockMe(this);
		}
	}
	
	protected void initialize(int maxHiddenLayers, int maxHiddenNeurons,int codePropertyStart,TestSet baseTestSet,int evolvers) {
		if (maxHiddenLayers<1) {
			maxHiddenLayers = 1;
		}
		if (maxHiddenNeurons<2) {
			maxHiddenLayers = 2;
		}
		if (evolvers < 2) {
			evolvers = 2;
		}
		this.maxHiddenLayers = maxHiddenLayers;
		this.maxHiddenNeurons = maxHiddenNeurons;
		this.codePropertyStart = codePropertyStart;
		this.baseTestSet = baseTestSet;
		for (int i = 0; i < evolvers; i++) {
			this.evolvers.add(new EvolverWorker(getMessenger(),union,this));
		}
	}
}
