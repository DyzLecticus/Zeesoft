package nl.zeesoft.zdk.htm.stream;

import java.util.LinkedList;
import java.util.Queue;

import nl.zeesoft.zdk.htm.proc.Processable;
import nl.zeesoft.zdk.htm.proc.ProcessableSecondaryOutput;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class StreamProcessor extends Worker {
	private Stream				stream					= null;
	
	private Processable			processor				= null;
	private int					useOutputIndex			= -1;
	
	private boolean				learn					= true;
	
	private Queue<StreamResult>	queue					= new LinkedList<StreamResult>();
		
	protected StreamProcessor(Messenger msgr,WorkerUnion uni,Stream stream,Processable processor,int useOutputIndex) {
		super(msgr,uni);
		this.stream = stream;
		this.processor = processor;
		this.useOutputIndex = useOutputIndex;
		setSleep(1);
	}
	
	protected void setLearn(boolean learn) {
		lockMe(this);
		this.learn = learn;
		unlockMe(this);
	}
	
	protected void addResultToQueue(StreamResult result) {
		lockMe(this);
		queue.add(result);
		unlockMe(this);
	}
	
	protected void destroy() {
		lockMe(this);
		stream = null;
		processor = null;
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		SDR input = null;
		lockMe(this);
		StreamResult result = queue.poll();
		if (result!=null) {
			input = result.inputSDR;
			if (useOutputIndex>=0 && useOutputIndex < result.outputSDRs.size()) {
				input = result.outputSDRs.get(useOutputIndex);
			}
		}
		unlockMe(this);
		if (input!=null) {
			SDR outputSDR = processor.getSDRForInput(input, learn);
			result.outputSDRs.add(outputSDR);
			if (processor instanceof ProcessableSecondaryOutput) {
				((ProcessableSecondaryOutput) processor).addSecondarySDRs(result.outputSDRs);
			}
			stream.processedResult(this,result);
			setSleep(0);
		} else {
			setSleep(1);
		}
	}
}
