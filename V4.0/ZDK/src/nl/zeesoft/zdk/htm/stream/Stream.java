package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Processable;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Stream extends Worker {
	private List<StreamProcessor>	processors	= new ArrayList<StreamProcessor>();

	private List<StreamListener>	listeners	= new ArrayList<StreamListener>();
	
	private	StreamResults			results		= null;
	
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
	
	public void addSDR(SDR inputSDR) {
		StreamResult result = results.getNewResult(inputSDR);
		lockMe(this);
		processors.get(0).addResultToQueue(result);
		unlockMe(this);
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
	
	@Override
	public void stop() {
		for (StreamProcessor processor: processors) {
			processor.stop();
		}
		super.stop();
	}
	
	public void waitForStop() {
		for (StreamProcessor processor: processors) {
			whileStopping(processor); 
		}
		whileStopping(this); 
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
	
	protected void processedResult(StreamProcessor processor,StreamResult result) {
		lockMe(this);
		int nextIndex = processors.indexOf(processor) + 1;
		if (nextIndex<processors.size()) {
			processors.get(nextIndex).addResultToQueue(result);
		} else {
			results.addResult(result);
		}
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
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
