package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.proc.Stats;
import nl.zeesoft.zdk.htm.proc.StatsLog;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A stream provides a threaded processor sequence where the output SDR of each processor is used as input for the next processor
 */
public class Stream extends Worker {
	private StreamEncoder					encoder				= null;
	private List<StreamProcessor>			processors			= new ArrayList<StreamProcessor>();
	private List<StreamListener>			listeners			= new ArrayList<StreamListener>();
	
	private boolean							streaming			= false;
	
	private boolean							logStats			= false;						
	private StatsLog						statsLog			= new StatsLog(this);
	private List<StatsLog>					processorStatsLogs	= new ArrayList<StatsLog>();
	
	private	StreamResults					results				= null;
	
	public Stream() {
		super(null,null);
		initialize(null,new StreamEncoder());
	}
	
	public Stream(Messenger msgr,WorkerUnion uni) {
		super(msgr,uni);
		initialize(msgr,new StreamEncoder());
	}

	public Stream(Messenger msgr,WorkerUnion uni,StreamEncoder encoder) {
		super(msgr,uni);
		initialize(msgr,encoder);
	}

	@Override
	protected Messenger getMessenger() {
		return super.getMessenger();
	}

	@Override
	protected WorkerUnion getUnion() {
		return super.getUnion();
	}

	public void addInputProcessor(ProcessorObject processor) {
		addNextProcessor(processor,-1);
	}

	public void addNextProcessor(ProcessorObject processor,int useOutputIndex) {
		if (!isWorking() && !isStreaming()) {
			lockMe(this);
			StreamProcessor sp = new StreamProcessor(getMessenger(),getUnion(),this,processor,useOutputIndex);
			processors.add(sp);
			processorStatsLogs.add(new StatsLog(processor));
			unlockMe(this);
		}
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

	public void setLogStats(boolean logStats) {
		lockMe(this);
		if (logStats!=this.logStats) {
			statsLog.log.clear();
			for (StatsLog statsLog: processorStatsLogs) {
				statsLog.log.clear();
			}
			this.logStats = logStats;
			for (StreamProcessor processor: processors) {
				processor.setLogStats(logStats);
			}
		}
		unlockMe(this);
	}
	
	public List<StatsLog> getStats() {
		List<StatsLog> r = new ArrayList<StatsLog>();
		lockMe(this);
		r.add(statsLog.copy());
		unlockMe(this);
		for (StatsLog statsLog: processorStatsLogs) {
			lockMe(this);
			r.add(statsLog.copy());
			unlockMe(this);
		}
		return r;
	}

	public long addValue(int value) {
		return addSDRtoStream(encoder.getSDRForValue(value));
	}
	
	public long addValue(float value) {
		return addSDRtoStream(encoder.getSDRForValue(value));
	}
	
	public long addValue(long value) {
		return addSDRtoStream(encoder.getSDRForValue(value));
	}
	
	public long addValue(int value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(value,label));
	}
	
	public long addValue(float value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(value,label));
	}
	
	public long addValue(long value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(value,label));
	}

	public long addValue(long dateTime,int value) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value));
	}
	
	public long addValue(long dateTime,float value) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value));
	}
	
	public long addValue(long dateTime,long value) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value));
	}
	
	public long addValue(long dateTime,int value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value,label));
	}
	
	public long addValue(long dateTime,float value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value,label));
	}
	
	public long addValue(long dateTime,long value,String label) {
		return addSDRtoStream(encoder.getSDRForValue(dateTime,value,label));
	}

	public long addSDR(SDR sdr) {
		return addSDRtoStream(encoder.getSDRForSDR(sdr));
	}

	public long addSDR(SDR sdr,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(sdr,label));
	}

	public long addSDR(SDR sdr,int value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(sdr,value,label));
	}

	public long addSDR(SDR sdr,float value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(sdr,value,label));
	}

	public long addSDR(SDR sdr,long value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(sdr,value,label));
	}

	public long addSDR(long dateTime,SDR sdr) {
		return addSDRtoStream(encoder.getSDRForSDR(dateTime,sdr));
	}

	public long addSDR(long dateTime,SDR sdr,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(dateTime,sdr,label));
	}

	public long addSDR(long dateTime,SDR sdr,int value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(dateTime,sdr,value,label));
	}

	public long addSDR(long dateTime,SDR sdr,float value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(dateTime,sdr,value,label));
	}

	public long addSDR(long dateTime,SDR sdr,long value,String label) {
		return addSDRtoStream(encoder.getSDRForSDR(dateTime,sdr,value,label));
	}
	
	public boolean isStreaming() {
		boolean r = false;
		lockMe(this);
		r = streaming;
		unlockMe(this);
		return r;
	}

	@Override
	public void start() {
		boolean r = !isWorking();
		if (r) {
			lockMe(this);
			r = !streaming;
			if (r) {
				streaming = r;
			}
			unlockMe(this);
			if (r) {
				for (StreamProcessor processor: processors) {
					processor.start();
				}
				super.start();
			}
		}
	}
	
	public void waitForStart() {
		while (!isWorking()) {
			try {
				Thread.sleep(1);
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
		boolean r = isWorking();
		if (r) {
			lockMe(this);
			r = streaming;
			if (r) {
				streaming = false;
			}
			unlockMe(this);
			if (r) {
				for (StreamProcessor processor: processors) {
					processor.stop();
				}
				super.stop();
				flushOutput();
			}
		}
	}
	
	public void waitForStop() {
		for (StreamProcessor processor: processors) {
			whileStopping(processor); 
		}
		whileStopping(this);
	}
	
	public void destroy() {
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

	protected long addSDRtoStream(DateTimeSDR inputSDR) {
		StreamResult result = results.getNewResult(inputSDR);
		lockMe(this);
		processors.get(0).addResultToQueue(result);
		unlockMe(this);
		return result.id;
	}

	protected void processedResult(StreamProcessor processor,Stats pStats,StreamResult result) {
		lockMe(this);
		int index = processors.indexOf(processor);
		if (logStats && pStats!=null) {
			processorStatsLogs.get(index).addStats(pStats);
		}
		int nextIndex = index + 1;
		if (nextIndex<processors.size()) {
			processors.get(nextIndex).addResultToQueue(result);
		} else {
			results.addResult(result);
			if (logStats) {
				Stats stats = new Stats();
				stats.setValue("total",System.nanoTime() - result.added);
				statsLog.addStats(stats);
			}
		}
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		flushOutput();
	}
	
	protected void flushOutput() {
		lockMe(this);
		List<StreamListener> list = new ArrayList<StreamListener>(listeners);
		unlockMe(this);
		List<StreamResult> res = results.flush();
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
	
	protected void initialize(Messenger msgr,StreamEncoder encoder) {
		this.encoder = encoder;
		results = new StreamResults(msgr);
		setSleep(10);
	}
}
