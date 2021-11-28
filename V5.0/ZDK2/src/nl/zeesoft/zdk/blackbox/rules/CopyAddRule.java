package nl.zeesoft.zdk.blackbox.rules;

import java.util.List;

public class CopyAddRule extends FloatRule {
	@Override
	protected Object getOutputValue(List<Object> inputValues) {
		Object r = null;
		if (inputValues.size()>0) {
			float total = 0F;
			for (Object value: inputValues) {
				total += (float) value;
			}
			r = total;
		}
		return r;
	}
}
