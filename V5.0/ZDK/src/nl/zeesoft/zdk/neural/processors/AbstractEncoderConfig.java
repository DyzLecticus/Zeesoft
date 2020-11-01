package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure an AbstractEncoder before initialization.
 * 
 * Configurable properties;
 * - sizeX, sizeY; Encoded output SDR dimensions 
 * - onBits; The number of on bits in the encoded output 
 */
public abstract class AbstractEncoderConfig extends SDRProcessorConfig{
	public static String 	DESCRIPTION 	=
		"Configurable properties;  \n" + 
		" * sizeX, sizeY; Encoded output SDR dimensions.  \n" + 
		" * onBits; The number of on bits in the encoded output.  \n" +
		"";

	public int	sizeX	= 16;
	public int	sizeY	= 16;
	public int	onBits	= 16;
}
