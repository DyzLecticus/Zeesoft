package nl.zeesoft.zdk.htm.proc;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class BufferedPredictor extends Predictor implements Processable, ProcessableContextInput {
	private SDRSet										outputBuffer		= null;
	private SortedMap<ZStringBuilder,DateTimeSDR>		inputByOutput		= new TreeMap<ZStringBuilder,DateTimeSDR>();
	private	int											maxBufferSize		= 1000;
	
	private DateTimeSDR									dateTimeSDR			= null;
	
	public BufferedPredictor(MemoryConfig config) {
		super(config);
		outputBuffer = new SDRSet(config.size);
	}
	
	public DateTimeSDR getDateTimeSDR() {
		return dateTimeSDR;
	}

	@Override
	public void setContextSDRs(List<SDR> contextSDRs) {
		SDR inputSDR = contextSDRs.get(0);
		SDR outputSDR = contextSDRs.get(1);
		if (inputSDR instanceof DateTimeSDR) {
			inputByOutput.put(outputSDR.toStringBuilder(),(DateTimeSDR)inputSDR);
			outputBuffer.add(outputSDR);
			if (outputBuffer.size()>maxBufferSize) {
				SDR removed = outputBuffer.remove(0);
				inputByOutput.remove(removed.toStringBuilder());
			}
		}
	}

	@Override
	public SDR getSDRForInput(SDR input, boolean learn) {
		dateTimeSDR = null;
		SDR burstSDR = super.getSDRForInput(input, learn);
		SDR predictionSDR = getPredictionSDR();
		if (predictionSDR!=null && outputBuffer.size()>1) {
			SortedMap<Integer,List<SDR>> inputs = outputBuffer.getMatches(predictionSDR);
			if (inputs.size()>0) {
				SDR winner = null;
				List<SDR> winners = inputs.get(inputs.lastKey());
				if (winners.size()==1) {
					winner = winners.get(0);
				} else {
					winner = winners.get(ZRandomize.getRandomInt(0,winners.size() - 1));
				}
				dateTimeSDR = inputByOutput.get(winner.toStringBuilder());
			}
		}
		if (dateTimeSDR==null) {
			dateTimeSDR = new DateTimeSDR(config.size);
		}
		return burstSDR;
	}

	@Override
	public void addSecondarySDRs(List<SDR> outputSDRs) {
		super.addSecondarySDRs(outputSDRs);
		outputSDRs.add(dateTimeSDR);
	}
}
