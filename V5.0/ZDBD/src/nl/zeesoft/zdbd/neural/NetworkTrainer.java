package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;

public class NetworkTrainer implements Waitable {
	private Lock				lock						= new Lock();
	private Busy				busy						= new Busy(this);
	
	protected PatternSequence	sequence					= PatternFactory.getFourOnFloorInstrumentPatternSequence();
	protected int				startTrainTemporalMemory	= 4;
	protected int				startTrainClassifiers		= 8;
	protected int				maxTrainCycles				= 16;
	protected float				minimumAverageAccuracy		= 0.950F;
	protected float				minimumClassifierAccuracy	= 0.990F;
	
	protected NetworkIO			lastIO						= null;

	public void copyFrom(NetworkTrainer trainer) {
		lock.lock(this);
		this.sequence = trainer.sequence.copy();
		this.startTrainTemporalMemory = trainer.startTrainTemporalMemory;
		this.startTrainClassifiers = trainer.startTrainClassifiers;
		this.maxTrainCycles = trainer.maxTrainCycles;
		this.minimumAverageAccuracy = trainer.minimumAverageAccuracy;
		this.minimumClassifierAccuracy = trainer.minimumClassifierAccuracy;
		this.lastIO = new NetworkIO(trainer.lastIO);
		lock.unlock(this);
	}
	
	public void setSequence(PatternSequence sequence) {
		lock.lock(this);
		this.sequence = sequence.copy();
		lock.unlock(this);
	}

	public void setStartTrainTemporalMemory(int startTrainTemporalMemory) {
		lock.lock(this);
		this.startTrainTemporalMemory = startTrainTemporalMemory;
		lock.unlock(this);
	}

	public void setStartTrainClassifiers(int startTrainClassifiers) {
		lock.lock(this);
		this.startTrainClassifiers = startTrainClassifiers;
		lock.unlock(this);
	}

	public void setMaxTrainCycles(int maxTrainCycles) {
		lock.lock(this);
		this.maxTrainCycles = maxTrainCycles;
		lock.unlock(this);
	}

	public void setMinimumAverageAccuracy(float minimumAverageAccuracy) {
		lock.lock(this);
		this.minimumAverageAccuracy = minimumAverageAccuracy;
		lock.unlock(this);
	}

	public void setMinimumClassifierAccuracy(float minimumClassifierAccuracy) {
		lock.lock(this);
		this.minimumClassifierAccuracy = minimumClassifierAccuracy;
		lock.unlock(this);
	}
	
	public RunCode getTrainNetworkRunCode(Network network) {
		return new RunCode() {
			@Override
			protected boolean run() {
				trainNetwork(network);
				return true;
			}
		};
	}
	
	public List<NetworkIO> trainNetwork(Network network) {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		lock.lock(this);
		if (!busy.isBusy()) {
			busy.setBusy(true);
			this.lastIO = null;
			
			if (maxTrainCycles < 4) {
				maxTrainCycles = 4;
			}
			if (startTrainClassifiers >= maxTrainCycles) {
				startTrainClassifiers = (maxTrainCycles - 1);
			}
			if (startTrainTemporalMemory >= startTrainClassifiers) {
				startTrainTemporalMemory = (startTrainClassifiers - 1);
			}
						
			List<NetworkIO> trainingSet = sequence.getNetworkIO();
			
			network.setProcessorLearn("*", false);
			network.setLayerLearn(NetworkConfigFactory.POOLER_LAYER, true);
			network.setLayerProperty(NetworkConfigFactory.CLASSIFIER_LAYER, "logPredictionAccuracy", true);
			
			long start = System.currentTimeMillis();
			
			NetworkIO lastIO = null;
			Str err = new Str();
			for (int i = 1; i <= maxTrainCycles; i++) {
				Str msg = new Str("Training cycle ");
				msg.sb().append(i);
				msg.sb().append("/");
				msg.sb().append(maxTrainCycles);
				msg.sb().append(" ...");
				Logger.dbg(this, msg);
				if (i == startTrainTemporalMemory) {
					Logger.dbg(this, new Str("(Training memory)"));
					network.setLayerLearn(NetworkConfigFactory.MEMORY_LAYER, true);
				}
				if (i == startTrainClassifiers) {
					Logger.dbg(this, new Str("(Training classifiers)"));
					network.setLayerLearn(NetworkConfigFactory.CLASSIFIER_LAYER, true);
				}
				for (NetworkIO io: trainingSet) {
					NetworkIO result = new NetworkIO(io);
					network.processIO(result);
					r.add(result);
					if (result.hasErrors()) {
						err = result.getErrors().get(0);
						Logger.err(this, err);
						break;
					}
				}
				if (err.length()>0) {
					break;
				}
				
				lastIO = r.get(r.size() - 1);
				if (lastIO.isAccurate(false, minimumAverageAccuracy) && lastIO.isAccurate(true, minimumClassifierAccuracy)) {
					this.lastIO = new NetworkIO(lastIO);
					break;
				}
				SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
				if (accuracies.size()>0) {
					SortedMap<String,Float> accuracyTrends = lastIO.getClassifierAccuracies(true);
					msg = new Str();
					msg.sb().append("Accuracies (average) / trends: "); 
					msg.sb().append(accuracies.values()); 
					msg.sb().append(" (");
					msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
					msg.sb().append(") / "); 
					msg.sb().append(accuracyTrends.values()); 
					Logger.dbg(this, msg);
				}
			}
			
			if (lastIO!=null) {
				SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
				if (accuracies.size()>0) {
					Str msg = new Str();
					msg.sb().append("Training network took; ");
					msg.sb().append((System.currentTimeMillis() - start));
					msg.sb().append(" ms, accuracies;\n");
					for (Entry<String,Float> entry: accuracies.entrySet()) {
						msg.sb().append("- "); 
						msg.sb().append(entry.getKey()); 
						msg.sb().append(": "); 
						msg.sb().append(entry.getValue()); 
						msg.sb().append("\n"); 
					}
					msg.sb().append("- Average: ");
					msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
					Logger.dbg(this, msg);
				}
			}
			
			network.setProcessorLearn("*", false);
			network.setLayerProperty(NetworkConfigFactory.CLASSIFIER_LAYER, "logPredictionAccuracy", false);
		}
		lock.unlock(this);
		return r;
	}
	
	public RunCode getFromFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				fromFile(path);
				return true;
			}
		};
	}
	
	public void fromFile(String path) {
		NetworkTrainer trainer = (NetworkTrainer) PersistableCollection.fromFile(path);
		if (trainer!=null) {
			copyFrom(trainer);
		}
	}
	
	public RunCode getToFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				toFile(path);
				return true;
			}
		};
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
	}

	@Override
	public boolean isBusy() {
		return busy.isBusy();
	}

	public PatternSequence getSequence() {
		lock.lock(this);
		PatternSequence r = sequence.copy();
		lock.unlock(this);
		return r;
	}

	public NetworkIO getLastIO() {
		lock.lock(this);
		NetworkIO r = new NetworkIO(lastIO);
		lock.unlock(this);
		return r;
	}
}
