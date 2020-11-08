package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure a ScalarEncoder before initialization.
 */
public class ScalarEncoderConfig extends AbstractEncoderConfig{
	public static String 	DOCUMENTATION 	=
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*; Encoded output SDR dimensions.  \n" + 
		" * *onBits*; The number of on bits in the encoded output.  \n" +
		" * *minValue*, *maxValue*; The value range this encoder will encode.  \n" +
		" * *resolution*; The value resolution this encoder will encode.  \n" +
		" * *periodic*; Indicates the encoder should wrap the on bits over the value range.  \n" +
		"";
	
	public float	minValue	= 0;
	public float	maxValue	= 200;
	public float	resolution	= 1;
	public boolean	periodic	= false;
}
