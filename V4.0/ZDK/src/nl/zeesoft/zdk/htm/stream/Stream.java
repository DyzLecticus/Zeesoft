package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.proc.Stats;
import nl.zeesoft.zdk.htm.proc.StatsLog;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A Stream provides a threaded processor chain where the output SDR of each processor is used as input for the next processor.
 * It uses a StreamEncoder to encode values or SDRs that are added to the stream.
 * The learned state of the processors in the stream can be converted to and from JSON when the stream is not actively streaming SDRs.
 */
public class Stream extends Worker implements JsAble {
	public static final String				STATE_STOPPED		= "STOPPED";
	public static final String				STATE_STARTING		= "STARTING";
	public static final String				STATE_STARTED		= "STARTED";
	public static final String				STATE_STOPPING		= "STOPPING";
	
	private StreamEncoder					encoder				= null;
	private List<ProcessorObject>			processors			= new ArrayList<ProcessorObject>();
	private List<StreamProcessor>			streamProcessors	= new ArrayList<StreamProcessor>();
	private List<StreamListenerWorker>		listeners			= new ArrayList<StreamListenerWorker>();
	
	private String							state				= STATE_STOPPED;
	
	private boolean							logStats			= false;						
	private StatsLog						statsLog			= new StatsLog(this);
	private List<StatsLog>					processorStatsLogs	= new ArrayList<StatsLog>();
	
	private	StreamResults					results				= null;
	
	public Stream() {
		super(null,null);
		initialize(null,new StreamEncoder());
	}
	
	public Stream(StreamEncoder encoder) {
		super(null,null);
		initialize(null,encoder);
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

	/**
	 * Adds the input processor to the stream.
	 * The input processor gets its input SDR directly from the stream encoder.
	 * 
	 * @param processor The processor to add
	 */
	public void addInputProcessor(ProcessorObject processor) {
		addNextProcessor(processor,-1);
	}

	/**
	 * Adds the next processor to the stream.
	 * This will only work while the stream state equals STOPPED
	 * 
	 * @param processor The processor to add
	 * @param useOutputIndex The stream result output list index of the SDR to use as input for this processor
	 */
	public void addNextProcessor(ProcessorObject processor,int useOutputIndex) {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			processors.add(processor);
			StreamProcessor sp = new StreamProcessor(getMessenger(),getUnion(),this,processor,useOutputIndex);
			streamProcessors.add(sp);
			processorStatsLogs.add(new StatsLog(processor));
		}
		unlockMe(this);
	}
	
	/**
	 * Adds a stream listener to this stream.
	 * The stream is isolated from listeners by delegating the work of pushing results to the listeners to separate workers for each listener.
	 * If the listener throws an exception, the worker will keep working and the stream itself will not be impacted.
	 * 
	 * @param listener The listener to add
	 */
	public void addListener(StreamListener listener) {
		lockMe(this);
		StreamListenerWorker listenerWorker = new StreamListenerWorker(getMessenger(),getUnion(),this,listener);
		listeners.add(listenerWorker);
		if (state.equals(STATE_STARTED)) {
			listenerWorker.start();
		}
		unlockMe(this);
	}

	/**
	 * Indicates the processors in the stream should learn from their input.
	 * 
	 * @param learn Indicates the processors in the stream should learn from their input
	 */
	public void setLearn(boolean learn) {
		lockMe(this);
		for (StreamProcessor processor: streamProcessors) {
			processor.setLearn(learn);
		}
		unlockMe(this);
	}

	/**
	 * Indicates the stream and its processors should log performance statistics.
	 * 
	 * @param logStats Indicates the stream and its processors should log performance statistics
	 */
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
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			json.rootElement = new JsElem();
			json.rootElement.children.add(new JsElem("streamClassName",this.getClass().getName(),true));
			json.rootElement.children.add(new JsElem("encoderClassName",encoder.getClass().getName(),true));
			json.rootElement.children.add(new JsElem("encoderData",encoder.toStringBuilder(),true));
			json.rootElement.children.add(new JsElem("uid","" + results.uid,true));
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
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			if (state.equals(STATE_STOPPED)) {
				String streamClassName = json.rootElement.getChildString("streamClassName");
				String encoderClassName = json.rootElement.getChildString("encoderClassName");
				if (this.getClass().getName().equals(streamClassName) && encoder.getClass().getName().equals(encoderClassName)) {
					ZStringBuilder encoderData = json.rootElement.getChildZStringBuilder("encoderData");
					if (encoderData.length()>0) {
						encoder.fromStringBuilder(encoderData);
					}
					results.uid = json.rootElement.getChildLong("uid",results.uid);
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
	
	/**
	 * Returns a list of statistics logs for the stream and its processors.
	 * 
	 * @return A list of statistics logs for the stream and its processors
	 */
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
	
	/**
	 * Returns the state of the stream.
	 * 
	 * @return The state of the stream
	 */
	public String getState() {
		String r = "";
		lockMe(this);
		r = state;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns true if the stream state does not equal STOPPED.
	 * 
	 * @return True if the stream state does not equals STOPPED
	 */
	public boolean isActive() {
		boolean r = false;
		lockMe(this);
		r = !state.equals(STATE_STOPPED);
		unlockMe(this);
		return r;
	}

	@Override
	public void start() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STOPPED);
		if (r) {
			state = STATE_STARTING;
		}
		unlockMe(this);
		if (r) {
			for (StreamProcessor processor: streamProcessors) {
				processor.start();
			}
			for (StreamListenerWorker listener: listeners) {
				listener.start();
			}
			super.start();
		}
	}
	
	@Override
	public void stop() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STARTED);
		if (r) {
			state = STATE_STOPPING;
		}
		unlockMe(this);
		if (r) {
			if (r) {
				for (StreamProcessor processor: streamProcessors) {
					processor.stop();
				}
				for (StreamListenerWorker listener: listeners) {
					listener.stop();
				}
				super.stop();
			}
		}
	}
	
	/**
	 * Destroys the stream, its processors and its listener workers.
	 */
	public void destroy() {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			for (StreamProcessor processor: streamProcessors) {
				processor.destroy();
			}
			streamProcessors.clear();
			for (ProcessorObject processor: processors) {
				processor.destroy();
			}
			processors.clear();
			for (StreamListenerWorker listener: listeners) {
				listener.destroy();
			}
			listeners.clear();
			results.flush();
		}
		unlockMe(this);
	}
	
	@Override
	protected void startedWorking() {
		lockMe(this);
		state = STATE_STARTED;
		unlockMe(this);
	}
	
	@Override
	protected void stoppedWorking() {
		lockMe(this);
		state = STATE_STOPPED;
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		flushOutput();
	}
	
	protected void flushOutput() {
		lockMe(this);
		List<StreamListenerWorker> list = new ArrayList<StreamListenerWorker>(listeners);
		unlockMe(this);
		List<StreamResult> res = results.flush();
		for (StreamListenerWorker listener: list) {
			listener.addResults(res);
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
	
	protected void initialize(Messenger msgr,StreamEncoder encoder) {
		this.encoder = encoder;
		results = new StreamResults(msgr);
		setSleep(10);
	}
}
