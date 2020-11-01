package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure a Merger before initialization.
 */
public class MergerConfig extends SDRProcessorConfig{
	public static String 	DESCRIPTION 	=
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDR dimensions are not restricted).  \n" + 
		" * *concatenate*; Indicates the input SDRs should be concatenated.  \n" +
		" * *maxOnBits*; Optional maximum number of on bits in the merged output (uses sub sampling).  \n" +
		" * *pdistortion*; Optional on bit distortion to the output SDR.  \n" +
		"";

	public int		sizeX		= 768;
	public int		sizeY		= 48;
	public boolean	concatenate	= false;
	public int		maxOnBits	= 256;
	public float	distortion	= 0.0F;
}
