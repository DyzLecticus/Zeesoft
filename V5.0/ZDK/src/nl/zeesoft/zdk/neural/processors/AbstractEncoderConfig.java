package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;

/**
 * This class can be used to configure an AbstractEncoder before initialization.
 * 
 * Configurable properties;
 * - sizeX, sizeY; Encoded output SDR dimensions 
 * - onBits; The number of on bits in the encoded output 
 */
public abstract class AbstractEncoderConfig extends SDRProcessorConfig{
	public static String 	DOCUMENTATION 	=
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*; Encoded output SDR dimensions.  \n" + 
		" * *onBits*; The number of on bits in the encoded output.  \n" +
		"";

	public int	sizeX	= 16;
	public int	sizeY	= 16;
	public int	onBits	= 16;

	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		
		r.sb().append(":");

		r.sb().append("\n");
		r.sb().append("-> 0 = SDR: ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);
		r.sb().append(", on bits: ");
		r.sb().append(onBits);
		
		r.sb().append("\n");
		r.sb().append("-> 1 = value: 1*1");
		return r;
	}
}
