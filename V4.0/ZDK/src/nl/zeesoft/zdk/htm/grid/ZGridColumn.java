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
	
	protected ZGridRequest			request		= null;

	protected ZGridColumn(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(1);
	}
	
	protected String getId() {
		return getColumnId(row.index,index);
	}
	
	protected void setRequest(ZGridRequest request) {
		lockMe(this);
		this.request = request;
		unlockMe(this);
	}

	protected void destroy() {
		lockMe(this);
		row = null;
		encoder = null;
		processor = null;
		contexts.clear();
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		lockMe(this);
		if (request!=null) {
			List<SDR> outputs = null;
			if (encoder!=null) {
				SDR output = encoder.encodeRequestValue(index, request);
				if (output!=null) {
					outputs = new ArrayList<SDR>();
					outputs.add(output);
				}
			}
			if (processor!=null) {
				// Use previous row column output as input
				SDR input = request.getColumnOutput(getColumnId(row.index - 1,index),0);
				if (row.index==0) {
					if (input==null && outputs!=null) {
						// Use encoded output as input
						input = outputs.get(0);
					}
					if (input==null && 
						request.inputValues.length>index &&
						request.inputValues[index]!=null &&
						request.inputValues[index] instanceof SDR
						) {
						// Use input value SDR as input
						input = (SDR) request.inputValues[index];
					}
				}
				if (input!=null) {
					List<SDR> context = new ArrayList<SDR>();
					if (contexts.size()>0) {
						for (ZGridColumnContext ctx: contexts) {
							SDR contextSDR = request.getColumnOutput(getColumnId(ctx.sourceRow,ctx.sourceColumn),ctx.sourceIndex);
							if (contextSDR!=null) {
								context.add(contextSDR);
							}
						}
					}
					outputs = processor.getSDRsForInput(input,context,request.learn);
				}
			}
			request.setColumnOutput(getId(),outputs);
			request = null;
			row.processedColumn();
		}
		unlockMe(this);
	}
	
	protected static final String getColumnId(int rowIndex,int columnIndex) {
		return String.format("%02d",rowIndex) + "-" + String.format("%02d",columnIndex);
	}
}
