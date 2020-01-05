package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.Detector;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGridColumn extends Worker {
	protected ZGridRow					row			= null;
	protected int 						index		= 0;
	protected ZGridColumnEncoder		encoder		= null;
	protected ProcessorObject			processor	= null;
	protected List<ZGridColumnContext>	contexts	= new ArrayList<ZGridColumnContext>();
	
	protected ZGridResult				result		= null;

	protected ZGridColumn(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(0);
		setSleepNs(ZGrid.SLEEP_NS);
		setStopOnException(false);
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}
	
	protected String getId() {
		return getColumnId(row.index,index);
	}
	
	protected void setRequest(ZGridResult result) {
		setSleepNs(0);
		lockMe(this);
		this.result = result;
		unlockMe(this);
	}

	protected void destroy() {
		lockMe(this);
		if (processor!=null) {
			processor.destroy();
		}
		row = null;
		encoder = null;
		processor = null;
		contexts.clear();
		unlockMe(this);
	}
	
	protected boolean isActive() {
		return encoder!=null || processor!=null;
	}
	
	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		lockMe(this);
		if (result!=null) {
			result = null;
			row.processedColumn();
		}
		unlockMe(this);
	}

	@Override
	protected void whileWorking() {
		processRequestNoLock();
		setSleepNs(ZGrid.SLEEP_NS);
	}
	
	protected void processRequestNoLock() {
		if (result!=null) {
			List<SDR> outputs = null;
			if (encoder!=null) {
				SDR output = encoder.encodeRequestValue(index, result);
				if (output!=null) {
					outputs = new ArrayList<SDR>();
					outputs.add(output);
				}
				if (result.getRequest().inputValues.length>index && result.getRequest().inputValues[index]!=null) {
					if (outputs==null) {
						getMessenger().error(this,encoder.getClass().getSimpleName() + " at " + getId() + " returned null");
					} else if (outputs.size()==0) {
						getMessenger().error(this,encoder.getClass().getSimpleName() + " at " + getId() + " returned an empty list");
					}
				}
			} else if (processor!=null) {
				// Use previous row column output as input
				SDR input = null;
				for (int r = row.index - 1; r >= 0; r--) {
					input = result.getColumnOutput(getColumnId(r,index),0);
					if (input!=null) {
						break;
					}
				}
				if (input==null && 
					result.getRequest().inputValues.length>index &&
					result.getRequest().inputValues[index]!=null &&
					result.getRequest().inputValues[index] instanceof SDR
					) {
					// Use input value SDR as input
					input = (SDR) result.getRequest().inputValues[index];
				}
				List<SDR> context = new ArrayList<SDR>();
				if (contexts.size()>0) {
					for (ZGridColumnContext ctx: contexts) {
						SDR contextSDR = result.getColumnOutput(getColumnId(ctx.sourceRow,ctx.sourceColumn),ctx.sourceIndex);
						if (contextSDR!=null) {
							context.add(contextSDR);
						}
					}
				}
				if (input!=null || context.size()>0) {
					if (processor instanceof Classifier) {
						((Classifier)processor).setClassifyMaxSteps(result.getRequest().classifyMaxSteps);
					} else if (processor instanceof Detector) {
						((Detector)processor).setDetectAnomalies(result.getRequest().detectAnomalies);
					}
					if (context.size()==0 && (processor instanceof Classifier || processor instanceof Detector)) {
						getMessenger().error(this,processor.getClass().getSimpleName() + " at " + getId() + " did not recieve any context");
					}
					outputs = processor.getSDRsForInput(input,context,result.getRequest().learn);
					if (getMessenger()!=null) {
						if (outputs==null) {
							getMessenger().error(this,processor.getClass().getSimpleName() + " at " + getId() + " returned null");
						} else if (outputs.size()==0) {
							getMessenger().error(this,processor.getClass().getSimpleName() + " at " + getId() + " returned an empty list");
						}
					}
				} else if (getMessenger()!=null) {
					getMessenger().error(this,processor.getClass().getSimpleName() + " at " + getId() + " did not recieve any input");
				}
			}
			result.setColumnOutput(getId(),outputs);
			result = null;
			row.processedColumn();
		}
	}
	
	protected static final String getColumnId(int rowIndex,int columnIndex) {
		return String.format("%02d",rowIndex) + "-" + String.format("%02d",columnIndex);
	}
}
