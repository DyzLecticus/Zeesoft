package nl.zeesoft.zdk.test.neural.processor;

import java.util.ArrayList;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class MockProcessor extends LearningProcessor {
	@Override
	public void reset() {
		
	}
	
	@Override
	protected void processValidIO(ProcessorIO io) {
		addOutput(io, new ArrayList<Position>());
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		r.addInput("InputName", Integer.MAX_VALUE);
		r.addOutput("OutputName", new Size(1,1,1));
		return r;
	}
}
