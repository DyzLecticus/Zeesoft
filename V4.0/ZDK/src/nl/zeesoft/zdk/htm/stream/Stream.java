package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.proc.Stats;
import nl.zeesoft.zdk.htm.proc.StatsLog;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A stream provides a threaded processor sequence where the output SDR of each processor is used as input for the next processor
 */
public class Stream extends Worker implements JsAble {
	private StreamEncoder					encoder				= null;
	private List<ProcessorObject>			processors			= new ArrayList<ProcessorObject>();
	private List<StreamProcessor>			streamProcessors	= new ArrayList<StreamProcessor>();
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
		if (!isWorking()) {
			lockMe(this);
			if (!streaming) {
				processors.add(processor);
				StreamProcessor sp = new StreamProcessor(getMessenger(),getUnion(),this,processor,useOutputIndex);
				streamProcessors.add(sp);
				processorStatsLogs.add(new StatsLog(processor));
			}
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
		for (StreamProcessor processor: streamProcessors) {
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
			for (StreamProcessor processor: streamProcessors) {
				processor.setLogStats(logStats);
			}
		}
		unlockMe(this);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		if (!isWorking()) {
			lockMe(this);
			if (!streaming) {
				json.rootElement = new JsElem();
				json.rootElement.children.add(new JsElem("streamClassName",this.getClass().getName(),true));
				json.rootElement.children.add(new JsElem("encoderClassName",encoder.getClass().getName(),true));
				json.rootElement.children.add(new JsElem("encoderData",encoder.toStringBuilder(),true));
				JsElem procsElem = new JsElem("processors",true);
				json.rootElement.children.add(procsElem);
				for (ProcessorObject processor: processors) {
					JsElem procElem = new JsElem();
					procsElem.children.add(procElem);
					procElem.children.add(new JsElem("processorClassName",processor.getClass().getName(),true));
					procElem.children.add(new JsElem("processorData",processor.toStringBuilder(),true));
				}
			}
			unlockMe(this);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null && !isWorking()) {
			lockMe(this);
			if (!streaming) {
				String streamClassName = json.rootElement.getChildString("streamClassName");
				String encoderClassName = json.rootElement.getChildString("encoderClassName");
				if (this.getClass().getName().equals(streamClassName) && encoder.getClass().getName().equals(encoderClassName)) {
					ZStringBuilder encoderData = json.rootElement.getChildZStringBuilder("encoderData");
					if (encoderData.length()>0) {
						encoder.fromStringBuilder(encoderData);
					}
					JsElem procsElem = json.rootElement.getChildByName("processors");
					if (procsElem!=null) {
						int i = 0;
						for (JsElem procElem: procsElem.children) {
							ProcessorObject processor = processors.get(i);
							if (processor!=null) {
								String processorClassName = procElem.getChildString("processorClassName");
								ZStringBuilder processorData = procElem.getChildZStringBuilder("processorData");
								if (processor.getClass().getName().equals(processorClassName) && processorData.length()>0) {
									processor.fromStringBuilder(processorData);
								}
							}
							i++;
						}
					}
				}
			}
			unlockMe(this);
		}
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
				for (StreamProcessor processor: streamProcessors) {
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
				for (StreamProcessor processor: streamProcessors) {
					processor.stop();
				}
				super.stop();
				flushOutput();
			}
		}
	}
	
	public void waitForStop() {
		for (StreamProcessor processor: streamProcessors) {
			whileStopping(processor); 
		}
		whileStopping(this);
	}
	
	public void destroy() {
		for (StreamProcessor processor: streamProcessors) {
			processor.destroy();
		}
		for (ProcessorObject processor: processors) {
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
		streamProcessors.get(0).addResultToQueue(result);
		unlockMe(this);
		return result.id;
	}

	protected void processedResult(StreamProcessor processor,Stats pStats,StreamResult result) {
		lockMe(this);
		int index = streamProcessors.indexOf(processor);
		if (logStats && pStats!=null) {
			processorStatsLogs.get(index).addStats(pStats);
		}
		int nextIndex = index + 1;
		if (nextIndex<streamProcessors.size()) {
			streamProcessors.get(nextIndex).addResultToQueue(result);
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
