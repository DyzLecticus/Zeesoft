package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private NN 							baseNN				= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							method				= 0;
	private int							minSuccessLevel		= 0;
	private int							trainCycles			= 1000;
	private float						minLearningRate		= 0.1F;
	private float						maxLearningRate		= 0.5F;
	
	private int							trainedCycles		= 0;
	private TestCycleSet				initialResults		= null;
	private TestCycleSet				finalResults		= null;

	public TrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		this.baseNN = baseNN;
		this.baseTestCycleSet = baseTestCycleSet;
	}

	public TrainingProgram copy(NN baseNN, TestCycleSet baseTestCycleSet) {
		TrainingProgram r = getCopyTrainingProgram(baseNN,baseTestCycleSet);
		r.setMethod(method);
		r.setMinSuccessLevel(minSuccessLevel);
		r.setTrainCycles(trainCycles);
		r.setMinMaxLearningRate(minLearningRate,maxLearningRate);
		return r;
	}
	
	public void setMethod(int method) {
		this.method = method;
	}

	public void setMinSuccessLevel(int minSuccessLevel) {
		this.minSuccessLevel = minSuccessLevel;
	}
	
	public void setTrainCycles(int trainCycles) {
		this.trainCycles = trainCycles;
	}

	public void setMinMaxLearningRate(float min,float max) {
		this.minLearningRate = min;
		this.maxLearningRate = max;
	}

	public NN runProgram() {
		NN r = baseNN.copy();
		trainedCycles = 0;
		TestCycleSet bestResults = baseTestCycleSet.copy();
		r.runTestCycleSet(bestResults);
		initialResults = bestResults;
		if (!bestResults.isSuccess(minSuccessLevel)) {
			for (int c = 0; c < trainCycles; c++) {
				trainedCycles++;
				
				NN variation = r.copy();
				TestCycleSet tcs = baseTestCycleSet.copy();
				float err = bestResults.averageError;
				if (err<0.001) {
					err = err * 1000.0F;
				} if (err<0.01) {
					err = err * 100.0F;
				} if (err<0.1) {
					err = err * 10.0F;
				}
				float learningRate = minLearningRate + (err * 2.0F);
				if (learningRate>maxLearningRate) {
					learningRate = maxLearningRate;
				}
				trainVariation(variation,method,tcs,learningRate);
				
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess(minSuccessLevel)) {
					bestResults = tcs;
					r = variation;
					break;
				} else if (tcs.compareTo(bestResults) > 0) {
					bestResults = tcs;
					r = variation;
				}
			}
		}
		finalResults = bestResults;
		return r;
	}
	
	public int getTrainedCycles() {
		return trainedCycles;
	}
	
	public TestCycleSet getInitialResults() {
		return initialResults;
	}
	
	public TestCycleSet getFinalResults() {
		return finalResults;
	}
	
	public boolean isSuccess() {
		return finalResults.isSuccess(minSuccessLevel);
	}
	
	protected void trainVariation(NN nn, int method, TestCycleSet tcs, float learningRate) {
		if (method==0) {
			trainVariationRandomUseAllPaths(nn,tcs,learningRate);
		} else {
			if (trainedCycles % 5 == 0) {
				trainVariationRandom(nn,learningRate);
			} else if (trainedCycles % 3 == 0) {
				trainVariationRandomUseTestResults(nn,tcs,learningRate);
			} else {
				trainVariationRandomUsePaths(nn,tcs,learningRate);
			}
		}
	}

	protected TrainingProgram getCopyTrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		return new TrainingProgram(baseNN,baseTestCycleSet);
	}

	protected float getRandomFloat(float max) {
		return random.nextFloat() * max;
	}
	
	protected boolean getRandomBoolean() {
		return random.nextFloat() > 0.5F;
	}
	
	protected List<NeuronLayer> getLayers(NN nn) {
		return nn.getLayers();
	}

	private void trainVariationRandomUseAllPaths(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			
			List<TrainingPath> allPaths = null;
			for (Integer l: tcs.getLevels()) {
				allPaths = getAllPaths(nn,tcs,allPaths,l);
			}
			
			for (TrainingPath path: allPaths) {
				Neuron current = path.inputNeuron;
				if (path.error>0.0F && current.value<current.threshold) {
					current.threshold = current.value;
				}
				Neuron next = null;
				for (int i = 0; i <= path.middleNeurons.size(); i++) {
					boolean flip = false;
					if (i==path.middleNeurons.size()) {
						next = path.outputNeuron;
						flip = true;
					} else {
						next = path.middleNeurons.get(i);
					}
					if (
						(path.error>0.0F && next.value<=next.threshold) ||
						(path.error<0.0F && next.value>=next.threshold)
						) {
						for (NeuronLink link: current.targets) {
							if (link.target==next) {
								if (path.error>0.0F) {
									if (link.weight<0) {
										link.weight = link.weight * -1;
									} else {
										link.weight += getRandomFloat(learningRate);
									}
								} else {
									if (flip && link.weight>0) {
										link.weight = link.weight * -1;
									} else {
										link.weight -= getRandomFloat(learningRate);
									}
								}
							}
						}
					}
					current = next;
				}
			}
		}
	}

	private void trainVariationRandomUsePaths(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			for (TestCycle tc: tcs.cycles) {
				nn.runCycle(tc);
				if (!tc.success) {
					List<TrainingPath> positivePaths = getPositivePaths(nn,tc,null);
					List<TrainingPath> negativePaths = getNegativePaths(nn,tc,positivePaths);
					List<TrainingPath> paths = new ArrayList<TrainingPath>(positivePaths);
					for (TrainingPath tp: negativePaths) {
						paths.add(tp);
					}
					for (TrainingPath path: paths) {
						Neuron current = path.inputNeuron;
						if (path.error>0.0F && current.value<current.threshold) {
							current.threshold = current.value;
						}
						Neuron next = null;
						for (int i = 0; i <= path.middleNeurons.size(); i++) {
							if (i==path.middleNeurons.size()) {
								next = path.outputNeuron;
							} else {
								next = path.middleNeurons.get(i);
							}
							if (
								(path.error>0.0F && next.value<=next.threshold) ||
								(path.error<0.0F && next.value>=next.threshold)
								) {
								for (NeuronLink link: current.targets) {
									if (link.target==next) {
										if (path.error>0.0F) {
											link.weight += getRandomFloat(learningRate);
										} else {
											link.weight -= getRandomFloat(learningRate);
										}
									}
								}
							}
							current = next;
						}
					}
				}
			}
		}
	}

	private void trainVariationRandomUseTestResults(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			for (TestCycle tc: tcs.cycles) {
				nn.runCycle(tc);
				if (!tc.success) {
					for (int i = 0; i < tc.errors.length; i++) {
						if (tc.errors[i]!=0.0F) {
							for (NeuronLink link: tc.firedLinks) {
								if (getRandomBoolean()) {
									float diff = tc.errors[i];
									if (diff<0.0F) {
										diff = diff * -1.0F;
									}
									float rate = (learningRate + diff) / 2.0F;
									if (rate<minLearningRate) {
										rate = minLearningRate;
									}
									if (tc.errors[i]>0.0F) {
										link.weight += getRandomFloat(rate);
									} else {
										link.weight -= getRandomFloat(rate);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void trainVariationRandom(NN nn, float learningRate) {
		if (learningRate>0.0F) {
			List<NeuronLayer> layers = getLayers(nn);
			for (NeuronLayer layer: layers) {
				for (Neuron neuron: layer.neurons) {
					if (getRandomBoolean()) {
						float threshold = neuron.threshold;
						float diff = neuron.threshold - neuron.value;
						if (diff<0.0F) {
							diff = diff * -1.0F;
						}
						float rate = (learningRate + diff) / 2.0F;
						if (getRandomBoolean()) {
							threshold -= getRandomFloat(rate);
						} else {
							threshold += getRandomFloat(rate);
						}
						if (threshold<0.0F) {
							threshold = 0.0F;
						} else if (threshold>1.0F) {
							threshold = 1.0F;
						}
						neuron.threshold = threshold;
					}
					for (NeuronLink link: neuron.sources) {
						//if (getRandomBoolean()) {
							float weight = link.weight;
							if (getRandomBoolean()) {
								weight -= getRandomFloat(learningRate);
							} else {
								weight += getRandomFloat(learningRate);
							}
							link.weight = weight;
						//}
					}
				}
			}
		}
	}
	
	private List<TrainingPath> getAllPaths(NN nn,TestCycleSet tcs,List<TrainingPath> exclude,int focusLevel) {
		List<TrainingPath> r = new ArrayList<TrainingPath>();
		if (exclude!=null) {
			for (TrainingPath tp: exclude) {
				r.add(tp);
			}
		}
		for (TestCycle tc: tcs.cycles) {
			if (tc.level==focusLevel) {
				nn.runCycle(tc);
			}
		}
		for (TestCycle tc: tcs.cycles) {
			if (tc.level==focusLevel && !tc.success) {
				List<TrainingPath> pPaths = getPositivePaths(nn,tc,r);
				List<TrainingPath> nPaths = getNegativePaths(nn,tc,r);
				for (TrainingPath tp: pPaths) {
					r.add(tp);
				}
				for (TrainingPath tp: nPaths) {
					r.add(tp);
				}
			}
		}
		return r;
	}
	
	private List<TrainingPath> getPositivePaths(NN nn,TestCycle tc,List<TrainingPath> exclude) {
		return getPaths(nn,tc,exclude,true);
	}
	
	private List<TrainingPath> getNegativePaths(NN nn,TestCycle tc,List<TrainingPath> exclude) {
		return getPaths(nn,tc,exclude,false);
	}
	
	private List<TrainingPath> getPaths(NN nn,TestCycle tc,List<TrainingPath> exclude,boolean positive) {
		List<TrainingPath> r = new ArrayList<TrainingPath>();
		for (int e = 0; e < tc.outputs.length; e++) {
			if (
				(positive && tc.errors[e]>0.0F) ||
				(!positive && tc.errors[e]<0.0F)
				) {
				for (int in = 0; in < tc.inputs.length; in++) {
					if (tc.inputs[in]>0.0F) {
						TrainingPath path = new TrainingPath();
						r.add(path);
						path.inputNeuron = nn.getInputLayer().neurons.get(in);
						path.outputNeuron = nn.getOutputLayer().neurons.get(e);
						path.error = tc.errors[e];
						findPath(nn,path,exclude);
					}
				}
			}
		}
		return r;
	}
	
	private void findPath(NN nn,TrainingPath path,List<TrainingPath> exclude) {
		findPath(nn,path,path.inputNeuron,exclude);
	}
	
	private void findPath(NN nn,TrainingPath path,Neuron workingNeuron,List<TrainingPath> exclude) {
		List<NeuronLink> candidates = new ArrayList<NeuronLink>();
		for (NeuronLink link: workingNeuron.targets) {
			if (link.target==path.outputNeuron) {
				candidates.clear();
				break;
			} else if (
				link.target.posX > workingNeuron.posX - 2 &&
				link.target.posX < workingNeuron.posX + 2
				) {
				boolean add = true;
				if (exclude!=null) {
					for (TrainingPath exPath: exclude) {
						if (exPath.middleNeurons.contains(link.target)) {
							add = false;
							break;
						}
					}
				}
				if (add) {
					candidates.add(link);
				}
			}
		}
		if (candidates.size()>0) {
			if (candidates.size()>1) {
				int index = random.nextInt() % candidates.size();
				if (index<0) {
					index = index * -1;
				}
				workingNeuron = candidates.get(index).target;
			} else {
				workingNeuron = candidates.get(0).target;
			}
			path.middleNeurons.add(workingNeuron);
			findPath(nn,path,workingNeuron,exclude);
		}
	}
}
