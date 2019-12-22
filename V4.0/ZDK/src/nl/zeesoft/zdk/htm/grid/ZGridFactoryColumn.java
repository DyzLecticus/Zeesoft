package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.ProcessorConfigObject;

public class ZGridFactoryColumn {
	protected String					columnId		= "";
	protected int						rowIndex		= 0;
	protected int						columnIndex		= 0;
	protected ZGridColumnEncoder		encoder			= null;
	protected ProcessorConfigObject		processorConfig	= null;
	protected List<ZGridColumnContext>	contexts		= new ArrayList<ZGridColumnContext>();
	
	protected ZGridFactoryColumn() {
		
	}
}
