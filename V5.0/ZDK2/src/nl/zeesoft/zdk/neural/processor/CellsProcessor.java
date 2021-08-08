package nl.zeesoft.zdk.neural.processor;

import nl.zeesoft.zdk.neural.model.Cells;

public interface CellsProcessor {
	public Cells getCells();
	public boolean isCellModel();
}
