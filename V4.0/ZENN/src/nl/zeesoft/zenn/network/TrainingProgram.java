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
	private float						minLearningRate		= 0.01F;
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
				if (err<0.0001) {
					err = err * 1000.0F;
				} else if (err<0.001) {
					err = err * 100.0F;
				} else if (err<0.01) {
					err = err * 10.0F;
				}
				float learningRate = minLearningRate + err;
				if (learningRate>maxLearningRate) {
					learningRate = maxLearningRate;
				}
				trainVariation(variation,method,tcs.copy(),learningRate);
				
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
		trainVariationBackPropagate(nn,tcs,learningRate);
		/*
		if (method==0) {
			if (trainedCycles % 10 == 0) {
				trainVariationRandomUseFiredLinks(nn,tcs,learningRate);
			} else {
				trainVariationRandomUseAllPaths(nn,tcs,learningRate);
			}
		} else {
			if (trainedCycles % 5 == 0) {
				trainVariationRandom(nn,learningRate);
			} else if (trainedCycles % 3 == 0) {
				trainVariationRandomUseFiredLinks(nn,tcs,learningRate);
			} else {
				trainVariationRandomUsePaths(nn,tcs,learningRate);
			}
		}
		*/
	}

	protected TrainingProgram getCopyTrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		return new TrainingProgram(baseNN,baseTestCycleSet);
	}

	protected float getRandomFloorFloat(float max) {
		max = max / 2.0F;
		return max + (random.nextFloat() * max);
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

	private void trainVariationBackPropagate(NN nn, TestCycleSet tcs, float learningRate) {
		//trainCycles = 10000;
		trainCycles = 1;
		learningRate = 0.1F;
		//System.out.println(learningRate);
		List<NeuronLayer> layers = getLayers(nn);
		List<TestCycle> cycles = tcs.getCycles();
		for (TestCycle tc: cycles) {
			nn.runCycle(tc);
			if (!tc.success) {
				System.out.println("BEFORE " + tc.getSummary(1));
				
				NeuronLayer pLayer = layers.get(layers.size()-2);
				for (int n = 0; n < tc.errors.length; n++) {
					Neuron neuron = nn.getOutputLayer().neurons.get(n);
					if (tc.errors[n]!=0.0F) {
						for (Neuron pNeuron: pLayer.neurons) {
							if (pNeuron.value>pNeuron.bias) {
								List<NeuronLink> sources = new ArrayList<NeuronLink>();
								for (NeuronLink source: neuron.sources) {
									if (source.source==pNeuron) {
										sources.add(source);
									}
								}
								for (NeuronLink source: sources) {
									float size = sources.size();
									//System.out.println("< error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
									if (tc.errors[n]>0.0F) {
										if (source.weight<0.0F) {
											source.weight = (source.weight * -1.0F) + (tc.errors[n]  / size) + learningRate;
										} else {
											source.weight = source.weight + (tc.errors[n] / size) + learningRate;
										}
									} else if (tc.errors[n]<0.0F) {
										if (source.weight>0.0F) {
											//System.out.println("< error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
											source.weight = (source.weight + ((tc.errors[n] * -1.0F) / size) + learningRate) * -1.0F;
											//source.weight = source.weight * -1F;
											//System.out.println("> error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
										} else {
											System.out.println("< error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
											source.weight = ((source.weight * -1.0F) + ((tc.errors[n] * -1.0F) / size) + learningRate) * -1.0F;
											System.out.println("> error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
										}
									}
									source.weight = NN.tanh(source.weight);
									//System.out.println("> error: " + tc.errors[n] + ", weight: " + source.weight + ", sources: " + sources.size());
								}
							}
						}
					}
				}
				
				TestCycle copy = new TestCycle();
				copy.initialize(nn);
				tc.copy(copy);
				nn.runCycle(copy);
				System.out.println("AFTER " + copy.getSummary(1));
				System.out.println();
				if (!copy.success) {
					break;
				}
			}
		}	
	}

	/*
	private void trainVariationRandomUseAllPaths(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			List<TrainingPath> allPaths = null;
			for (Integer l: tcs.getLevels()) {
				allPaths = getAllPaths(nn,tcs,allPaths,l);
			}
			
			for (TrainingPath path: allPaths) {
				Neuron current = path.inputNeuron;
				if (path.error>0.0F && current.value<=current.bias) {
					current.bias = NN.tanh(current.value);
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
						(path.error>0.0F && next.value<=next.bias) ||
						(path.error<0.0F && next.value>=next.bias)
						) {
						float diff = path.error;
						if (diff<0.0F) {
							diff = diff * -1.0F;
						}
						float rate = learningRate + diff;
						for (NeuronLink link: current.targets) {
							if (link.target==next) {
								if (path.error>0.0F) {
									if (link.weight<0) {
										link.weight = link.weight * -1;
									} else {
										link.weight = NN.tanh(link.weight + getRandomFloorFloat(rate));
									}
								} else {
									if (flip && link.weight>0) {
										link.weight = link.weight * -1;
									} else {
										link.weight = NN.tanh(link.weight - getRandomFloorFloat(rate));
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
						if (path.error>0.0F && current.value<current.bias) {
							current.bias = current.value;
						}
						Neuron next = null;
						for (int i = 0; i <= path.middleNeurons.size(); i++) {
							if (i==path.middleNeurons.size()) {
								next = path.outputNeuron;
							} else {
								next = path.middleNeurons.get(i);
							}
							if (
								(path.error>0.0F && next.value<=next.bias) ||
								(path.error<0.0F && next.value>=next.bias)
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

	private void trainVariationRandomUseFiredLinks(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			for (TestCycle tc: tcs.cycles) {
				if (tc.level<=minSuccessLevel) {
					nn.runCycle(tc);
					if (!tc.success) {
						for (int i = 0; i < tc.errors.length; i++) {
							if (tc.errors[i]>0.0F) {
								for (NeuronLink link: tc.firedLinks) {
									float diff = tc.errors[i];
									if (diff<0.0F) {
										diff = diff * -1.0F;
									}
									float rate = learningRate + diff;
									link.weight = NN.tanh(link.weight + getRandomFloorFloat(rate));
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
						float bias = neuron.bias;
						float diff = neuron.bias - neuron.value;
						if (diff<0.0F) {
							diff = diff * -1.0F;
						}
						float rate = (learningRate + diff) / 2.0F;
						if (getRandomBoolean()) {
							bias -= getRandomFloat(rate);
						} else {
							bias += getRandomFloat(rate);
						}
						if (bias<0.0F) {
							bias = 0.0F;
						} else if (bias>1.0F) {
							bias = 1.0F;
						}
						neuron.bias = bias;
					}
					for (NeuronLink link: neuron.sources) {
						float weight = link.weight;
						if (getRandomBoolean()) {
							weight -= getRandomFloat(learningRate);
						} else {
							weight += getRandomFloat(learningRate);
						}
						link.weight = weight;
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
				List<TrainingPath> paths = getPositivePaths(nn,tc,r);
				for (TrainingPath tp: paths) {
					r.add(tp);
				}
				paths = getNegativePaths(nn,tc,r);
				for (TrainingPath tp: paths) {
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
						path.inputNeuron = nn.getInputLayer().neurons.get(in);
						path.outputNeuron = nn.getOutputLayer().neurons.get(e);
						path.error = tc.errors[e];
						if (findPath(nn,path,exclude)) {
							r.add(path);
						}
					}
				}
			}
		}
		return r;
	}
	
	private boolean findPath(NN nn,TrainingPath path,List<TrainingPath> exclude) {
		return findPath(nn,path,path.inputNeuron,exclude);
	}
	
	private boolean findPath(NN nn,TrainingPath path,Neuron workingNeuron,List<TrainingPath> exclude) {
		boolean r = true;
		List<NeuronLink> candidates = new ArrayList<NeuronLink>();
		for (NeuronLink link: workingNeuron.targets) {
			if (link.target==path.outputNeuron) {
				candidates.clear();
				break;
			} else if (
				link.target.posX > workingNeuron.posX - 3 &&
				link.target.posX < workingNeuron.posX + 3
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
			if (workingNeuron!=path.outputNeuron) {
				path.middleNeurons.add(workingNeuron);
				findPath(nn,path,workingNeuron,exclude);
			}
		} else {
			r = false;
		}
		return r;
	}
	*/
}
