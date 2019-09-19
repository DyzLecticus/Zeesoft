package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Processable;
import nl.zeesoft.zdk.htm.proc.StatsObject;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A stream provides a threaded processor sequence where the output SDR of each processor is used as input for the next processor
 */
public class Stream extends Worker {
	private List<StreamProcessor>	processors		= new ArrayList<StreamProcessor>();

	private List<StreamListener>	listeners		= new ArrayList<StreamListener>();
	
	private StreamStats				stats			= new StreamStats();
	private StatsObject[]			processorStats	= null;
	private boolean[]				resetStats		= null;
	
	private	StreamResults			results			= null;
	
	public Stream(Messenger msgr,WorkerUnion uni) {
		super(msgr,uni);
		results = new StreamResults(msgr);
		setSleep(10);
	}

	public void addInputProcessor(Processable processor) {
		addNextProcessor(processor,-1);
	}

	public void addNextProcessor(Processable processor,int useOutputIndex) {
		lockMe(this);
		StreamProcessor sp = new StreamProcessor(getMessenger(),getUnion(),this,processor,useOutputIndex);
		processors.add(sp);
		processorStats = new StatsObject[processors.size()];
		resetStats = new boolean[processors.size()];
		for (int i = 0; i < processors.size(); i++) {
			resetStats[i] = false;
		}
		unlockMe(this);
	}
	
	public void addListener(StreamListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void setLearn(boolean learn) {
		lockMe(this);
		for (StreamProcessor processor: processors) {
			processor.setLearn(learn);
		}
		unlockMe(this);
	}
	
	public long addSDR(SDR inputSDR) {
		if (!(inputSDR instanceof DateTimeSDR)) {
			DateTimeSDR dts = new DateTimeSDR(inputSDR);
			dts.dateTime = System.currentTimeMillis();
			inputSDR = dts;
		}
		StreamResult result = results.getNewResult(inputSDR);
		lockMe(this);
		processors.get(0).addResultToQueue(result);
		unlockMe(this);
		return result.id;
	}
	
	public void resetStats() {
		lockMe(this);
		stats = new StreamStats();
		for (int i = 0; i < processors.size(); i++) {
			resetStats[i] = true;
		}
		unlockMe(this);
	}
	
	public List<StatsObject> getStats() {
		List<StatsObject> r = new ArrayList<StatsObject>();
		lockMe(this);
		r.add(stats.copy());
		for (int i = 0; i < processorStats.length; i++) {
			if (processorStats[i]!=null) {
				r.add(processorStats[i]);
			}
		}
		unlockMe(this);
		return r;
	}
	
	@Override
	public void start() {
		if (!isWorking()) {
			for (StreamProcessor processor: processors) {
				processor.start();
			}
			super.start();
		}
	}
	
	public void waitForStart() {
		while (!isWorking()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Exception while starting stream",e);
				} else {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void stop() {
		for (StreamProcessor processor: processors) {
			processor.stop();
		}
		super.stop();
		flushOutput();
	}
	
	public void waitForStop() {
		for (StreamProcessor processor: processors) {
			whileStopping(processor); 
		}
		whileStopping(this);
		for (StreamProcessor processor: processors) {
			processor.destroy();
		}
	}
	
	protected void whileStopping(Worker worker) {
		while (worker.isWorking()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Exception while stopping stream",e);
				} else {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected boolean processedResult(StreamProcessor processor,StatsObject pStats,StreamResult result) {
		boolean r = false;
		lockMe(this);
		int index = processors.indexOf(processor);
		processorStats[index] = pStats;
		int nextIndex = index + 1;
		if (nextIndex<processors.size()) {
			processors.get(nextIndex).addResultToQueue(result);
		} else {
			results.addResult(result);
			stats.total++;
			stats.totalNs += System.nanoTime() - result.added;
		}
		r = resetStats[index];
		unlockMe(this);
		return r;
	}
	
	@Override
	protected void whileWorking() {
		flushOutput();
	}
	
	protected void flushOutput() {
		lockMe(this);
		List<StreamResult> res = results.flush();
		List<StreamListener> list = new ArrayList<StreamListener>(listeners);
		unlockMe(this);
		for (StreamResult result: res) {
			for (StreamListener listener: list) {
				try {
					listener.processedResult(this,result);
				} catch (Exception e) {
					if (getMessenger()!=null) {
						getMessenger().error(this,"Exception while sending result to listener: " + listener,e);
					} else {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
