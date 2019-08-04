package nl.zeesoft.zdk.genetic;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.TestSet;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Evolver extends Locker implements JsAble {
	private WorkerUnion				union				= null;
	
	private TestSet					baseTestSet			= null;
	private List<EvolverWorker>		evolvers			= new ArrayList<EvolverWorker>();
	private GeneticNN				geneticNN			= null;
	
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

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		lockMe(this);
		json.rootElement.children.add(new JsElem("mutationRate","" + mutationRate));
		json.rootElement.children.add(new JsElem("trainEpochs","" + trainEpochs));
		json.rootElement.children.add(new JsElem("trainEpochSize","" + trainEpochSize));
		json.rootElement.children.add(new JsElem("evolverSleepMs","" + evolverSleepMs));
		if (bestSoFar!=null) {
			json.rootElement.children.add(new JsElem("bestCode",bestSoFar.code.getCode()));
			
			JsElem bestNNElem = new JsElem("bestNeuralNet",true);
			json.rootElement.children.add(bestNNElem);
			bestNNElem.children.add(bestSoFar.neuralNet.toJson().rootElement);
			
			JsElem bestTPElem = new JsElem("bestTrainingProgram",true);
			json.rootElement.children.add(bestTPElem);
			bestTPElem.children.add(bestSoFar.trainingProgram.toJson().rootElement);
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			mutationRate = json.rootElement.getChildFloat("mutationRate",mutationRate);
			trainEpochs = json.rootElement.getChildInt("trainEpochs",trainEpochs);
			trainEpochSize = json.rootElement.getChildInt("trainEpochSize",trainEpochSize);
			evolverSleepMs = json.rootElement.getChildInt("evolverSleepMs",evolverSleepMs);
			
			JsElem bestCodeElem = json.rootElement.getChildByName("bestCode");
			JsElem bestNNElem = json.rootElement.getChildByName("bestNeuralNet");
			JsElem bestTPElem = json.rootElement.getChildByName("bestTrainingProgram");
			if (bestCodeElem!=null && bestNNElem!=null && bestTPElem!=null) {
				EvolverUnit evolver = new EvolverUnit();
				evolver.code = new GeneticCode();
				evolver.code.setCode(bestCodeElem.value);
				
				JsFile js = new JsFile();
				js.rootElement = bestNNElem.children.get(0);
				evolver.neuralNet = new NeuralNet(js);
				
				js.rootElement = bestTPElem.children.get(0);
				evolver.trainingProgram = getNewTrainingProgram(evolver.neuralNet,baseTestSet.copy());
				evolver.trainingProgram.fromJson(js);
				
				bestSoFar = evolver;
			}
			unlockMe(this);
		}
	}
	
	protected EvolverUnit getNewEvolverUnit(EvolverWorker evolver) {
		EvolverUnit r = new EvolverUnit();
		GeneticCode code = null;
		if (evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
			lockMe(this);
			if (bestSoFar!=null) {
				code = new GeneticCode();
				code.setCode(bestSoFar.code.getCode());
				code.mutate(mutationRate);
			}
			unlockMe(this);
		}
		lockMe(this);
		geneticNN.generateNewNN(code);
		r.code = geneticNN.code;
		r.neuralNet = geneticNN.neuralNet;
		r.trainingProgram = getNewTrainingProgram(r.neuralNet,baseTestSet.copy());
		unlockMe(this);
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
		this.baseTestSet = baseTestSet;
		for (int i = 0; i < evolvers; i++) {
			this.evolvers.add(new EvolverWorker(getMessenger(),union,this));
		}
		geneticNN = new GeneticNN(baseTestSet.inputNeurons,maxHiddenLayers,maxHiddenNeurons,baseTestSet.outputNeurons,codePropertyStart);
	}
}
