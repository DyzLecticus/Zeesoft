package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public abstract class ProcessorObject {
	protected List<Processable>				processors		= new ArrayList<Processable>();
	protected List<ProcessorListener>		listeners		= new ArrayList<ProcessorListener>();
	
	protected SDRSet						inputSDRSet		= null;
	protected boolean						learn			= true;
	
	public ProcessorObject(Processable processable) {
		processors.add(processable);
	}
	
	public List<Processable> getProcessors() {
		return processors;
	}
	
	public List<ProcessorListener> getListeners() {
		return listeners;
	}
	
	public void setLearn(boolean learn) {
		this.learn = learn;
	}
	
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		this.inputSDRSet = inputSDRSet;
	}
	
	public void process() {
		process(0,inputSDRSet.size());
	}

	public void process(int start,int num) {
		if (inputSDRSet!=null) {
			int stop = start + num;
			if (stop > inputSDRSet.size()) {
				stop = inputSDRSet.size();
			}
			for (int i = start; i < stop; i++) {
				processSDR(inputSDRSet.get(i));
			}
		}
	}
	
	protected void processSDR(SDR inputSDR) {
		List<SDR> outputSDRs = new ArrayList<SDR>();
		SDR inSDR = inputSDR;
		for (Processable processor: processors) {
			SDR outputSDR = processor.getSDRForInput(inSDR,learn);
			outputSDRs.add(outputSDR);
			if (processor instanceof ProcessableSecondaryOutput) {
				((ProcessableSecondaryOutput) processor).addSecondarySDRs(outputSDRs);
			}
			inSDR = outputSDR;
		}
		processedSDR(inputSDR,outputSDRs);
		for (ProcessorListener listener: listeners) {
			listener.processedSDR(this, inputSDR, outputSDRs);
		}
	}
	
	protected void processedSDR(SDR inputSDR, List<SDR> outputSDRs) {
		// Override to implement
	}
}
