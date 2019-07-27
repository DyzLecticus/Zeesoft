package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TestCycleSet implements Comparable<TestCycleSet>, JsAble {
	public List<TestCycle>				cycles				= new ArrayList<TestCycle>();
	
	public int							successes			= 0;
	public float						averageError		= 0.0F;
	public SortedMap<Integer,Integer>	cyclesPerLevel		= new TreeMap<Integer,Integer>();
	public SortedMap<Integer,Integer>	successesPerLevel	= new TreeMap<Integer,Integer>();
	
	public boolean isSuccess(int level) {
		boolean r = successes == cycles.size();
		if (level>0) {
			for (Integer key: cyclesPerLevel.keySet()) {
				if (successesPerLevel.get(key)!=null) {
					r = successesPerLevel.get(key) == cyclesPerLevel.get(key);
					if (!r) {
						break;
					} else if (key==level) {
						break;
					}
				}
			}
		}
		return r;
	}
	
	public List<Integer> getLevels() {
		List<Integer> r = new ArrayList<Integer>();
		for (TestCycle tc: cycles) {
			if (!r.contains(tc.level)) {
				int add = r.size();
				for (Integer l: r) {
					if (tc.level<l) {
						add = r.indexOf(l);
						break;
					}
				}
				r.add(add,tc.level);
			}
		}
		return r;
	}

	public void finalize() {
		float totalError = 0.0F;
		float total = 0.0F;
		
		for (TestCycle tc: cycles) {
			int cycPerLevel = 1;
			if (cyclesPerLevel.get(tc.level)!=null) {
				cycPerLevel = cyclesPerLevel.get(tc.level);
				cycPerLevel++;
			}
			cyclesPerLevel.put(tc.level,cycPerLevel);
			if (tc.success) {
				successes++;
				int sucPerLevel = 1;
				if (successesPerLevel.get(tc.level)!=null) {
					sucPerLevel = successesPerLevel.get(tc.level);
					sucPerLevel++;
				}
				successesPerLevel.put(tc.level,sucPerLevel);
			}
		}
		for (TestCycle tc: cycles) {
			for (int n = 0; n < tc.outputs.length; n++) {
				total += 1.0F;
				if (!tc.success) {
					if (tc.errors[n]!=0.0F) {
						if (tc.errors[n]>0.0F) {
							totalError += tc.errors[n];
						} else {
							totalError += (tc.errors[n] * -1);
						}
					}
				}
			}
		}
		if (total>0.0F) {
			averageError = totalError / total;
		}
	}
	
	@Override
	public int compareTo(TestCycleSet o) {
		int r = 0;
		if (this!=o && cyclesPerLevel.size()>0) {
			for (Integer key: cyclesPerLevel.keySet()) {
				int successes = 0;
				int compare = 0;
				if (successesPerLevel.get(key)!=null) {
					successes = successesPerLevel.get(key);
				}
				if (o.successesPerLevel.get(key)!=null) {
					compare = o.successesPerLevel.get(key);
				}
				if (successes>compare) {
					r = 1;
					break;
				} else if (successes<compare) {
					r = -1;
					break;
				}
			}
			if (r==0) {
				if (averageError < o.averageError) {
					r = 1;
				} else if (averageError > o.averageError) {
					r = -1;
				}
			}
		}
		return r;
	}

	public TestCycleSet copy() {
		TestCycleSet r = getCopyTestCycleSet();
		r.successes = successes;
		r.averageError = averageError;
		for (Integer key: cyclesPerLevel.keySet()) {
			if (cyclesPerLevel.get(key)!=null) {
				r.cyclesPerLevel.put(key,cyclesPerLevel.get(key));
			}
			if (successesPerLevel.get(key)!=null) {
				r.successesPerLevel.put(key,successesPerLevel.get(key));
			}
		}
		for (TestCycle tc: cycles) {
			TestCycle copyTc = new TestCycle();
			tc.copy(copyTc);
			r.cycles.add(copyTc);
		}
		return r;
	}
	
	public TestCycle addTestCycle(NN nn) {
		TestCycle r = new TestCycle();
		r.initialize(nn);
		cycles.add(r);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("successes","" + successes));
		json.rootElement.children.add(new JsElem("averageError","" + averageError));
		JsElem sPerLevel = new JsElem("successesPerLevel",true);
		json.rootElement.children.add(sPerLevel);
		for (Integer key: cyclesPerLevel.keySet()) {
			int s = 0;
			if (successesPerLevel.get(key)!=null) {
				s = successesPerLevel.get(key);
			}
			JsElem sucTotElem = new JsElem();
			sPerLevel.children.add(sucTotElem);
			sucTotElem.children.add(new JsElem("level","" + key));
			sucTotElem.children.add(new JsElem("successes","" + s));
			sucTotElem.children.add(new JsElem("total","" + cyclesPerLevel.get(key)));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			successes = json.rootElement.getChildInt("successes",successes);
			averageError = json.rootElement.getChildFloat("averageError",averageError);
			JsElem sPerLevel = json.rootElement.getChildByName("successesPerLevel");
			if (sPerLevel!=null) {
				for (JsElem sucTotElem: sPerLevel.children) {
					int l = sucTotElem.getChildInt("level");
					int s = sucTotElem.getChildInt("successes");
					int t = sucTotElem.getChildInt("total");
					cyclesPerLevel.put(l,t);
					successesPerLevel.put(l,s);
				}
			}
		}
	}

	public void toSystemOut() {
		int c = 0;
		for (TestCycle tc: cycles) {
			c++;
			System.out.println("Test cycle: " + c + ", level: " + tc.level + ", fired neurons: " + tc.firedNeurons.size() + ", fired links: " + tc.firedLinks.size() + ", success: " + tc.success);
			for (int n = 0; n < tc.outputs.length; n++) {
				if (tc.outputs[n]!=tc.expectedOutputs[n]) {
					System.out.println("  Output: " + n + ": " + tc.outputs[n] + ", expected: " + tc.expectedOutputs[n] + ", error: " + tc.errors[n]);
				}
			}
		}
		System.out.println("Test cycle set average error: " + averageError + ", successes: " + successes);
		if (cyclesPerLevel.size()>0) {
			for (Integer key: cyclesPerLevel.keySet()) {
				int successes = 0;
				if (successesPerLevel.get(key)!=null) {
					successes = successesPerLevel.get(key);
				}
				System.out.println("  Level: " + key + ", successes: " + successes + "/" + cyclesPerLevel.get(key));
			}
		}
	}
	
	protected TestCycleSet getCopyTestCycleSet() {
		return new TestCycleSet();
	}
}
