package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.AbstractProcessor;
import nl.zeesoft.zdk.neural.ProcessorIO;

public class MockProcessor extends AbstractProcessor {
	@Override
	protected void processValidIO(ProcessorIO io) {
		addOutput(io, new ArrayList<Position>());
	}
}
