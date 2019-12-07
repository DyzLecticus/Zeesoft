package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

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
		setSleep(1);
	}
	
	protected String getId() {
		return getColumnId(row.index,index);
	}
	
	protected void setSleepMs(int sleep) {
		setSleep(sleep);
	}
	
	protected void setRequest(ZGridResult result) {
		setSleep(0);
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
	protected void whileWorking() {
		lockMe(this);
		if (result!=null) {
			List<SDR> outputs = null;
			if (encoder!=null) {
				SDR output = encoder.encodeRequestValue(index, result);
				if (output!=null) {
					outputs = new ArrayList<SDR>();
					outputs.add(output);
				}
			} else if (processor!=null) {
				// Use previous row column output as input
				SDR input = result.getColumnOutput(getColumnId(row.index - 1,index),0);
				if (input==null && 
					result.getRequest().inputValues.length>index &&
					result.getRequest().inputValues[index]!=null &&
					result.getRequest().inputValues[index] instanceof SDR
					) {
					// Use input value SDR as input
					input = (SDR) result.getRequest().inputValues[index];
				}
				if (input!=null) {
					List<SDR> context = new ArrayList<SDR>();
					if (contexts.size()>0) {
						for (ZGridColumnContext ctx: contexts) {
							SDR contextSDR = result.getColumnOutput(getColumnId(ctx.sourceRow,ctx.sourceColumn),ctx.sourceIndex);
							if (contextSDR!=null) {
								context.add(contextSDR);
							}
						}
					}
					outputs = processor.getSDRsForInput(input,context,result.getRequest().learn);
				}
			}
			result.setColumnOutput(getId(),outputs);
			result = null;
			row.processedColumn();
		}
		unlockMe(this);
		setSleep(1);
	}
	
	protected static final String getColumnId(int rowIndex,int columnIndex) {
		return String.format("%02d",rowIndex) + "-" + String.format("%02d",columnIndex);
	}
}
