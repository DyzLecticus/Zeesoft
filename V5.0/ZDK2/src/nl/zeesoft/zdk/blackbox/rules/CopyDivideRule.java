package nl.zeesoft.zdk.blackbox.rules;

import java.util.List;

public class CopyDivideRule extends FloatRule {
	@Override
	protected Object getOutputValue(List<Object> inputValues) {
		Object r = null;
		if (inputValues.size()>0) {
			float total = (float)inputValues.remove(0);
			for (Object value: inputValues) {
				if ((float) value!=0F) {
					total /= (float) value;
				}
			}
			r = total;
		}
		return r;
	}
}
