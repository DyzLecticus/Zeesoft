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
}
