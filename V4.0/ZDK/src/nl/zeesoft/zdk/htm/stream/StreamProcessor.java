package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.proc.Stats;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class StreamProcessor extends Worker {
	private Stream				stream					= null;
	
	private ProcessorObject		processor				= null;
	private int					useOutputIndex			= -1;
	
	private boolean				learn					= true;
	private boolean				logStats				= true;
	
	private Queue<StreamResult>	queue					= new LinkedList<StreamResult>();
		
	protected StreamProcessor(Messenger msgr,WorkerUnion uni,Stream stream,ProcessorObject processor,int useOutputIndex) {
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
	
	protected void setLogStats(boolean logStats) {
		lockMe(this);
		this.logStats = logStats;
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
		boolean learn = true;
		lockMe(this);
		StreamResult result = queue.poll();
		if (result!=null) {
			input = result.inputSDR;
			if (useOutputIndex>=0 && useOutputIndex < result.outputSDRs.size()) {
				input = result.outputSDRs.get(useOutputIndex);
			}
		}
		learn = this.learn;
		processor.logStats = logStats;
		processor.statsLog.log.clear();
		unlockMe(this);
		if (input!=null) {
			List<SDR> context = new ArrayList<SDR>();
			context.add(result.inputSDR);
			for (SDR sdr: result.outputSDRs) {
				context.add(sdr);
			}
			List<SDR> outputSDRs = processor.getSDRsForInput(input,context,learn);
			for (SDR outputSDR: outputSDRs) {
				result.outputSDRs.add(outputSDR);
			}
			Stats stats = null;
			if (processor.statsLog.log.size()>0) {
				stats = processor.statsLog.log.get(0);
			}
			stream.processedResult(this,stats,result);
			setSleep(0);
		} else {
			setSleep(1);
		}
	}
}
