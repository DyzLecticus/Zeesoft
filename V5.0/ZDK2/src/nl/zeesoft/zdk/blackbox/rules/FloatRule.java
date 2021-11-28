package nl.zeesoft.zdk.blackbox.rules;

import nl.zeesoft.zdk.blackbox.BoxRule;

public abstract class FloatRule extends BoxRule {
	@Override
	protected boolean isAcceptedValue(Object value) {
		return value!=null && value instanceof Float && (Float)value!=Float.NaN && Float.isFinite((Float)value);
	}
}
