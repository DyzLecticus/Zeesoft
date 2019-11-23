package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.htm.util.SDR;

public abstract class GridColumnEncoder {
	protected abstract SDR encodeRequestValues(int columnIndex,GridRequest request);
}
