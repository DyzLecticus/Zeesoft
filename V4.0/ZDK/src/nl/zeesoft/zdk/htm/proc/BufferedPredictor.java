package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.sdr.SDRMapElement;

public class BufferedPredictor extends Predictor implements Processable, ProcessableContextInput {
	private SDRMap			buffer				= null;
	private	int				maxBufferSize		= 1000;
	
	private DateTimeSDR		dateTimeSDR			= null;
	
	public BufferedPredictor(MemoryConfig config) {
		super(config);
		buffer = new SDRMap(config.size,config.bits);
	}

	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public DateTimeSDR getDateTimeSDR() {
		return dateTimeSDR;
	}

	@Override
	public void setContextSDRs(List<SDR> contextSDRs) {
		SDR inputSDR = contextSDRs.get(0);
		SDR outputSDR = contextSDRs.get(1);
		if (inputSDR instanceof DateTimeSDR) {
			buffer.addSDR(outputSDR,inputSDR);
			if (buffer.size()>maxBufferSize) {
				buffer.remove(0);
			}
		}
	}

	@Override
	public SDR getSDRForInput(SDR input, boolean learn) {
		dateTimeSDR = null;
		SDR burstSDR = super.getSDRForInput(input, learn);
		SDR predictionSDR = getPredictionSDR();
		if (predictionSDR!=null && buffer.size()>1) {
			SDRMapElement element = buffer.getRandomClosestMatch(predictionSDR);
			if (element!=null && element.value instanceof DateTimeSDR) {
				dateTimeSDR = (DateTimeSDR) element.value;
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
