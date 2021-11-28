package nl.zeesoft.zdk.blackbox.rules;

import java.util.List;

public class AverageRule extends CopyAddRule {
	@Override
	protected Object getOutputValue(List<Object> inputValues) {
		Object r = super.getOutputValue(inputValues);
		if (r!=null) {
			float total = (float) r;
			float avg = total;
			if (inputValues.size()>1) {
				avg = total / (float)inputValues.size();
			}
			r = avg;
		}
		return r;
	}
}
