package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.Classification;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.thread.Lock;

public class NetworkIO implements StrAble {
	private Lock							lock			= new Lock();
	
	private SortedMap<String,Object>		values			= new TreeMap<String,Object>();
	private int								timeoutMs		= 1000;
	private SortedMap<String,ProcessorIO>	processorIO		= new TreeMap<String,ProcessorIO>();
	private List<Str>						errors			= new ArrayList<Str>();
	
	public NetworkIO() {
		
	}
	
	public NetworkIO(NetworkIO io) {
		copyFrom(io);
	}
	
	public void copyFrom(NetworkIO io) {
		io.lock.lock(this);
		lock.lock(this);
		values.clear();
		processorIO.clear();
		errors.clear();
		
		this.values.putAll(io.values);
		this.timeoutMs = io.timeoutMs;
		this.processorIO.putAll(io.processorIO);
		this.errors.addAll(io.errors);
		lock.unlock(this);
		io.lock.unlock(this);
	}
	
	public void setValue(String name, Object value) {
		if (name!=null && name.length()>0 && value!=null) {
			lock.lock(this);
			values.put(name, value);
			lock.unlock(this);
		}
	}
	
	public List<String> getValueNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(values.keySet());
		lock.unlock(this);
		return r;
	}
	
	public Object getValue(String name) {
		lock.lock(this);
		Object r = values.get(name);
		lock.unlock(this);
		return r;
	}

	public int getTimeoutMs() {
		lock.lock(this);
		int r = timeoutMs;
		lock.unlock(this);
		return r;
	}

	public void setTimeoutMs(int timeoutMs) {
		lock.lock(this);
		this.timeoutMs = timeoutMs;
		lock.unlock(this);
	}
	
	public List<String> getProcessorNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(processorIO.keySet());
		lock.unlock(this);
		return r;
	}
	
	public void setProcessorIO(String name, ProcessorIO io) {
		lock.lock(this);
		processorIO.put(name, io);
		lock.unlock(this);
	}
	
	public ProcessorIO getProcessorIO(String name) {
		lock.lock(this);
		ProcessorIO r = processorIO.get(name);
		lock.unlock(this);
		return r;
	}
	
	public void addError(Str err) {
		lock.lock(this);
		errors.add(err);
		lock.unlock(this);
	}
	
	public boolean hasErrors() {
		lock.lock(this);
		boolean r = errors.size() > 0;
		lock.unlock(this);
		return r;
	}
	
	public List<Str> getErrors() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(errors);
		lock.unlock(this);
		return r;
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		lock.lock(this);
		for (Entry<String,ProcessorIO> entry: processorIO.entrySet()) {
			for (SDR output: entry.getValue().outputs) {
				Str line = new Str(entry.getKey());
				line.sb().append(">");
				line.sb().append(output.toStr().sb());
				if (r.length()>0) {
					r.sb().append("\n");
				}
				r.sb().append(line.sb());
			}
		}
		lock.unlock(this);
		return r;
	}

	@Override
	public void fromStr(Str str) {
		lock.lock(this);
		List<Str> elems = str.split("\n");
		for (Str elem: elems) {
			List<Str> nameSDR = elem.split(">");
			if (nameSDR.size()==2) {
				String name = nameSDR.get(0).toString();
				ProcessorIO io = processorIO.get(nameSDR.get(0).toString());
				if (io==null) {
					io = new ProcessorIO();
					processorIO.put(name, io);
				}
				Str sdrStr = nameSDR.get(1);
				SDR sdr = null;
				if (sdrStr.startsWith("SDR")) {
					sdr = new KeyValueSDR();
				} else {
					sdr = new SDR();
				}
				sdr.fromStr(sdrStr);
				io.outputs.add(sdr);
			}
		}
		lock.unlock(this);
	}
	
	public List<Classification> getClassifications() {
		List<Classification> r = new ArrayList<Classification>();
		lock.lock(this);
		for (Entry<String,ProcessorIO> entry: processorIO.entrySet()) {
			r.addAll(entry.getValue().getClassifications());
		}
		lock.unlock(this);
		return r;
	}
	
	public SortedMap<String,Object> getClassificationValues(int step) {
		SortedMap<String,Object> r = new TreeMap<String,Object>();
		lock.lock(this);
		for (Entry<String,ProcessorIO> entry: processorIO.entrySet()) {
			for (Classification classification: entry.getValue().getClassifications()) {
				if (classification.step==step) {
					List<Object> values = classification.getMostCountedValues();
					Object value = null;
					if (values.size()==1) {
						value = values.get(0);
					} else if (values.size()>1) {
						value = values.get(Rand.getRandomInt(0, values.size() - 1));
					}
					r.put(entry.getKey(), value);
				}
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public SortedMap<String,Float> getClassifierAccuracies(boolean trend) {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		lock.lock(this);
		for (Entry<String,ProcessorIO> entry: processorIO.entrySet()) {
			Float accuracy = entry.getValue().getClassifierAccuracy(trend);
			if (accuracy!=null) {
				r.put(entry.getKey(), accuracy);
			}
		}
		lock.unlock(this);
		return r;
	}

	public boolean isAccurate() {
		return isAccurate(false,1.0F);
	}
	
	public boolean isAccurate(float minimumAverageAccuracy) {
		return isAccurate(true,minimumAverageAccuracy);
	}

	public boolean isAccurate(boolean average, float minimumAverageAccuracy) {
		boolean r = false;
		if (average) {
			r = getAverageClassifierAccuracy(false) >= minimumAverageAccuracy;
		} else {
			SortedMap<String,Float> accuracies = getClassifierAccuracies(false);
			if (accuracies.size()>0) {
				r = true;
				for (Float accuracy: accuracies.values()) {
					if (accuracy<minimumAverageAccuracy) {
						r = false;
						break;
					}
				}
			}
		}
		return r;
	}
	
	public float getAverageClassifierAccuracy(boolean trend) {
		float r = 0;
		SortedMap<String,Float> accuracies = getClassifierAccuracies(trend);
		if (accuracies.size()>0) {
			for (Float accuracy: accuracies.values()) {
				r += accuracy;
			}
			r = r / (float) accuracies.size();
		}
		return r;
	}
}
