package nl.zeesoft.zdk.test.function;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;

public class MockFunction extends Function {
	@Override
	protected Object exec() {
		int value = Integer.parseInt("" + param1);
		Logger.debug(caller,"Integer.parseInt(\"" + param1 + "\") = " + value);
		return value;
	}
}
