package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;

public class ProcessorIO {
	public boolean				sequential					= false;
	public boolean				learn						= true;
	public int					timeoutMs					= 1000;
	public List<SDR>			inputs						= new ArrayList<SDR>();
	public List<SDR>			outputs						= null;
	public Str					error						= new Str();
	
	public ProcessorIO() {
		
	}
	
	public ProcessorIO(ProcessorIO io) {
		copyFrom(io);
	}
	
	public void copyFrom(ProcessorIO io) {
		this.sequential = io.sequential;
		this.learn = io.learn;
		this.timeoutMs = io.timeoutMs;
		for (SDR sdr: io.inputs) {
			if (sdr instanceof KeyValueSDR) {
				this.inputs.add(new KeyValueSDR(sdr));
			} else {
				this.inputs.add(new SDR(sdr));
			}
		}
		for (SDR sdr: io.outputs) {
			if (sdr instanceof KeyValueSDR) {
				this.outputs.add(new KeyValueSDR(sdr));
			} else {
				this.outputs.add(new SDR(sdr));
			}
		}
		this.error = new Str(io.error);
	}
	
	public List<Classification> getClassifications() {
		List<Classification> r = new ArrayList<Classification>();
		for (SDR output: outputs) {
			if (output instanceof KeyValueSDR) {
				KeyValueSDR kvSdr = (KeyValueSDR) output;
				List<String> valueKeys = kvSdr.getValueKeys();
				for (String key: valueKeys) {
					if (key.startsWith(Classifier.CLASSIFICATION_VALUE_KEY)) {
						Object value = kvSdr.get(key);
						if (value instanceof Classification) {
							r.add((Classification)value);
						}
					}
				}
			}
		}
		return r;
	}
	
	public Float getClassifierAccuracy(boolean trend) {
		Float r = null;
		for (SDR output: outputs) {
			if (output instanceof KeyValueSDR) {
				KeyValueSDR kvSdr = (KeyValueSDR) output;
				if (kvSdr.containsKey(Classifier.ACCURACY_VALUE_KEY)) {
					float accuracy = 0F;
					if (trend) {
						accuracy = (float) kvSdr.get(Classifier.ACCURACY_TREND_VALUE_KEY);
					} else {
						accuracy = (float) kvSdr.get(Classifier.ACCURACY_VALUE_KEY);
					}
					r = accuracy;
				}
			}
		}
		return r;
	}
}
