package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGridColumn extends Worker {
	protected ZGridRow				row			= null;
	protected int 					index		= 0;
	protected ZGridColumnEncoder	encoder		= null;
	protected ProcessorObject		processor	= null;
	
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
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		lockMe(this);
		if (request!=null) {
			SDR output = null;
			if (encoder!=null) {
				output = encoder.encodeRequestValue(index, request);
			}
			if (processor!=null) {
				SDR input = request.getColumnOutput(getColumnId(row.index - 1,index));
				if (input!=null) {
					List<SDR> context = new ArrayList<SDR>();
					// TODO: Add context
					List<SDR> outputSDRs = processor.getSDRsForInput(input,context,request.learn);
					output = outputSDRs.get(0);
				}
			}
			request.setColumnOutput(getId(),output);
			request = null;
			row.processedColumn();
		}
		unlockMe(this);
	}
	
	protected static final String getColumnId(int rowIndex,int columnIndex) {
		return String.format("%02d",rowIndex) + "-" + String.format("%02d",columnIndex);
	}
}
