package nl.zeesoft.zdk.blackbox;

import java.util.ArrayList;
import java.util.List;

public abstract class BoxRule {
	public List<Integer>	inputIndexes	= new ArrayList<Integer>();
	public int				outputIndex		= -1;
	
	public void apply(BoxElementValue[] inputs, BoxElementValue[] outputs) {
		List<Object> inputValues = getInputValues(inputs);
		if (inputValues.size()>0) {
			outputs[outputIndex].setValue(getOutputValue(inputValues));
		}
	}
	
	protected abstract Object getOutputValue(List<Object> inputValues);
	
	protected abstract boolean isAcceptedValue(Object value);
	
	protected List<Object> getInputValues(BoxElementValue[] inputs) {
		List<Object> inputValues = new ArrayList<Object>();
		for (Integer idx: inputIndexes) {
			Object value = inputs[idx].getValue();
			if (isAcceptedValue(value)) {
				inputValues.add(value);
			}
		}
		return inputValues;
	}
}
