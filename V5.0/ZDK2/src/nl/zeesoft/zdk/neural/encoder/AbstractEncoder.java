package nl.zeesoft.zdk.neural.encoder;

import nl.zeesoft.zdk.neural.Sdr;

public abstract class AbstractEncoder {
	public abstract Sdr getEncodedValue(Object value);
}
