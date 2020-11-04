package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;

/**
 * This class can be used to configure a Merger before initialization.
 */
public class MergerConfig extends SDRProcessorConfig{
	public static String 	DOCUMENTATION 	=
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDR dimensions are not restricted).  \n" + 
		" * *concatenate*; Indicates the input SDRs should be concatenated.  \n" +
		" * *maxOnBits*; Optional maximum number of on bits in the merged output (uses sub sampling).  \n" +
		" * *distortion*; Optional on bit distortion to the output SDR.  \n" +
		"";

	public int		sizeX		= 768;
	public int		sizeY		= 48;
	public boolean	concatenate	= false;
	public int		maxOnBits	= 256;
	public float	distortion	= 0.0F;

	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		
		r.sb().append(": ?");

		r.sb().append("\n");
		r.sb().append("-> Encoded value: ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);
		return r;
	}
}
