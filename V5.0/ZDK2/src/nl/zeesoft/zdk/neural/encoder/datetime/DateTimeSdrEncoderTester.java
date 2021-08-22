package nl.zeesoft.zdk.neural.encoder.datetime;

import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoderTester;
import nl.zeesoft.zdk.str.StrUtil;

public class DateTimeSdrEncoderTester {
	public ScalarSdrEncoderTester	tester	= new ScalarSdrEncoderTester();
	
	public StringBuilder testMinimalOverlap(DateTimeSdrEncoder encoder) {
		StringBuilder r = new StringBuilder();
		for (SeasonSdrEncoder enc: encoder.getEncoders()) {
			StringBuilder err = tester.testMinimalOverlap(enc);
			if (err.length()>0) {
				StrUtil.appendLine(r, err);
			}
		}
		return r;
	}
}
