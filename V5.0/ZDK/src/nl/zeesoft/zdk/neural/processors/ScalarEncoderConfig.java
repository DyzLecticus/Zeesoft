package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure a ScalarEncoder before initialization.
 * 
 * Configurable properties;
 * - minValue, maxValue; The value range this encoder will encode 
 * - periodic; Indicates the encoder should wrap the on bits over the value range 
 */
public class ScalarEncoderConfig extends AbstractEncoderConfig{
	public float	minValue	= 0;
	public float	maxValue	= 200;
	public boolean	periodic	= false;
}
