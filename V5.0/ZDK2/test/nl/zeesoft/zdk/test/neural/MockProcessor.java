package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.Processor;
import nl.zeesoft.zdk.neural.ProcessorIO;

public class MockProcessor extends Processor {
	@Override
	protected void processValidIO(ProcessorIO io) {
		addOutput(io, new ArrayList<Position>());
	}

	@Override
	protected String getInputName(int index) {
		return "InputName";
	}

	@Override
	protected String getOutputName(int index) {
		return "OutputName";
	}
}
