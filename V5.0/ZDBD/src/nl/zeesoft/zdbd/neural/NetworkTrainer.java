package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;

public class NetworkTrainer implements Waitable {
	public static String		TRAINING_SEQUENCE			= "Training sequence";
	
	private Lock				lock						= new Lock();
	private Busy				busy						= new Busy(this);
	
	protected PatternSequence	sequence					= new PatternSequence();
	protected int				startTrainTemporalMemory	= 4;
	protected int				startTrainClassifiers		= 8;
	protected int				maxTrainCycles				= 16;
	protected float				minimumAverageAccuracy		= 0.950F;
	protected float				minimumClassifierAccuracy	= 0.990F;
	
	protected long				changedSequence				= System.currentTimeMillis();
	protected long				trainedNetwork				= 0;
	
	protected List<NetworkIO>	trainingSet					= null;
	protected boolean			trainingIsDone				= false;
	protected long				trainingStarted				= 0;

	protected NetworkIO			lastIO						= null;
	
	public void copyFrom(NetworkTrainer trainer) {
		lock.lock(this);
		this.sequence = trainer.sequence.copy();
		this.startTrainTemporalMemory = trainer.startTrainTemporalMemory;
		this.startTrainClassifiers = trainer.startTrainClassifiers;
		this.maxTrainCycles = trainer.maxTrainCycles;
		this.minimumAverageAccuracy = trainer.minimumAverageAccuracy;
		this.minimumClassifierAccuracy = trainer.minimumClassifierAccuracy;
		this.changedSequence = trainer.changedSequence;
		this.trainedNetwork = trainer.trainedNetwork;
		if (trainer.lastIO!=null) {
			this.lastIO = new NetworkIO(trainer.lastIO);
		} else {
			this.lastIO = null;
		}
		lock.unlock(this);
	}
	
	public void setSequence(PatternSequence sequence) {
		lock.lock(this);
		this.sequence = sequence.copy();
		this.changedSequence = System.currentTimeMillis();
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

	public CodeRunnerChain getTrainNetworkChain(Network network) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		List<NetworkIO> trainingSet = sequence.getNetworkIO();
		if (!busy.isBusy() && trainingSet.size()>0) {
			this.trainingSet = trainingSet;
			trainingIsDone = false;
			busy.setBusy(true);
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					lock.lock(this);
					trainingStarted = System.currentTimeMillis();
					network.setProcessorLearn("*", false);
					network.setLayerLearn(NetworkConfigFactory.POOLER_LAYER, true);
					network.setLayerProperty(NetworkConfigFactory.CLASSIFIER_LAYER, "logPredictionAccuracy", true);
					lock.unlock(this);
					return true;
				}
			});
			for (int c = 1; c<=maxTrainCycles; c++) {
				RunCode code = new RunCode() {
					@Override
					protected boolean run() {
						int cycle = (int) params[0];
						trainCycle(network,cycle);
						return true;
					}
				};
				code.params[0] = c;
				r.add(code);
			}
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					lock.lock(this);
					trainingIsDone = true;
					trainedNetwork = System.currentTimeMillis();
					NetworkIO io = lastIO;
					long start = trainingStarted;
					busy.setBusy(false);
					lock.unlock(this);
					if (io!=null) {
						SortedMap<String,Float> accuracies = io.getClassifierAccuracies(false);
						if (accuracies.size()>0) {
							Str msg = new Str();
							msg.sb().append("Training network took; ");
							msg.sb().append((System.currentTimeMillis() - start));
							msg.sb().append(" ms, accuracies;\n");
							for (Entry<String,Float> entry: accuracies.entrySet()) {
								String name = entry.getKey();
								while (name.length() < 22) {
									name += " ";
								}
								msg.sb().append("- "); 
								msg.sb().append(name); 
								msg.sb().append(": "); 
								msg.sb().append(entry.getValue()); 
								msg.sb().append("\n"); 
							}
							msg.sb().append("------------------------------------\n");
							msg.sb().append("- Average               : ");
							msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
							Logger.dbg(this, msg);
							msg = new Str();
							msg.sb().append("Statistics;\n");
							msg.sb().append(network.getStatistics().getDebugLogStr());
							Logger.dbg(this, msg);
						}
					}
					return true;
				}
			});
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

	public long getChangedSequence() {
		lock.lock(this);
		long r = changedSequence;
		lock.unlock(this);
		return r;
	}

	public boolean changedSequenceSinceTraining() {
		lock.lock(this);
		boolean r = trainedNetwork < changedSequence;
		lock.unlock(this);
		return r;
	}

	public long getTrainedNetwork() {
		lock.lock(this);
		long r = trainedNetwork;
		lock.unlock(this);
		return r;
	}

	public NetworkIO getLastIO() {
		NetworkIO r = null;
		lock.lock(this);
		if (lastIO!=null) {
			r = new NetworkIO(lastIO);
		}
		lock.unlock(this);
		return r;
	}
	
	protected void trainCycle(Network network, int cycle) {
		lock.lock(this);
		boolean train = !trainingIsDone;
		if (train) {
			Str msg = new Str("Training cycle ");
			msg.sb().append(cycle);
			msg.sb().append("/");
			msg.sb().append(maxTrainCycles);
			msg.sb().append(" ...");
			Logger.dbg(this, msg);
			if (cycle == startTrainTemporalMemory) {
				Logger.dbg(this, new Str("(Training memory)"));
				network.setLayerLearn(NetworkConfigFactory.MEMORY_LAYER, true);
			}
			if (cycle == startTrainClassifiers) {
				Logger.dbg(this, new Str("(Training classifiers)"));
				network.setLayerLearn(NetworkConfigFactory.CLASSIFIER_LAYER, true);
			}
		}
		List<NetworkIO> trainingSet = new ArrayList<NetworkIO>(this.trainingSet);
		NetworkIO lastIO = this.lastIO;
		lock.unlock(this);
		if (train) {
			Str err = new Str();
			for (NetworkIO io: trainingSet) {
				NetworkIO result = new NetworkIO(io);
				network.processIO(result);
				lastIO = result;
				if (result.hasErrors()) {
					err = result.getErrors().get(0);
					Logger.err(this, err);
					break;
				}
			}
			
			if (lastIO!=null) {
				SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
				if (accuracies.size()>0) {
					SortedMap<String,Float> accuracyTrends = lastIO.getClassifierAccuracies(true);
					Str msg = new Str();
					msg.sb().append("Accuracies (average) / trends: "); 
					msg.sb().append(accuracies.values()); 
					msg.sb().append(" (");
					msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
					msg.sb().append(") / "); 
					msg.sb().append(accuracyTrends.values()); 
					Logger.dbg(this, msg);
				}
			}
			
			lock.lock(this);
			this.lastIO = lastIO;
			if (lastIO!=null &&
				lastIO.isAccurate(false, minimumAverageAccuracy) &&
				lastIO.isAccurate(true, minimumClassifierAccuracy)
				) {
				trainingIsDone = true;
			}
			lock.unlock(this);
		}
	}
}
