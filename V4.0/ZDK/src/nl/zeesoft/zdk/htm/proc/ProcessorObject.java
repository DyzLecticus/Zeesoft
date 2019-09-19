package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

/**
 * A processor provides basic sequential processor chaining where the output SDR of each processor is used as input for the next processor
 */
public abstract class ProcessorObject {
	protected List<Processable>				processors		= new ArrayList<Processable>();
	protected List<ProcessorListener>		listeners		= new ArrayList<ProcessorListener>();
	
	protected SDRMap						inputSDRMap		= null;
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
	
	public void setIntputSDRMap(SDRMap inputSDRMap) {
		this.inputSDRMap = inputSDRMap;
	}
	
	public void process() {
		process(0,inputSDRMap.size());
	}

	public void process(int start,int num) {
		if (inputSDRMap!=null) {
			int stop = start + num;
			if (stop > inputSDRMap.size()) {
				stop = inputSDRMap.size();
			}
			for (int i = start; i < stop; i++) {
				processSDR(inputSDRMap.getSDR(i));
			}
		}
	}
	
	protected void processSDR(SDR inputSDR) {
		List<SDR> outputSDRs = new ArrayList<SDR>();
		SDR inSDR = inputSDR;
		for (Processable processor: processors) {
			if (processor instanceof ProcessableContextInput) {
				List<SDR> contextSDRs = new ArrayList<SDR>();
				contextSDRs.add(inputSDR);
				((ProcessableContextInput) processor).setContextSDRs(contextSDRs);
			}
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
