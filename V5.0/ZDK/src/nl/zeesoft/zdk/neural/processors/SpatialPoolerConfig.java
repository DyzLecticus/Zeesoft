package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;

/**
 * This class can be used to configure a SpatialPooler before initialization.
 */
public class SpatialPoolerConfig extends SDRProcessorConfig{
	public static String 	DOCUMENTATION 		=
		"Please note that this spatial pooler implementation does not support local inhibition.  \n" +
	    "  \n" + 
		"Configurable properties;  \n" + 
		" * *inputSizeX*, *inputSizeY*; Input SDR dimensions.  \n" + 
		" * *outputSizeX*, *outputSizeY*; Output SDR dimensions (mini column dimensions).  \n" +
		" * *outputOnBits*; Maximum number of on bits in the output.  \n" +
		" * *potentialConnections*, *potentialRadius*; Number and optional radius of potential connections relative to the input space.  \n" +
		" * *permanenceThreshold*, *permanenceIncrement*, *permanenceDecrement*; Potential synapse adaptation control.  \n" +
		" * *activationHistorySize*; Historic column activation buffer size (used to calculate boost factors).  \n" +
		" * *boostFactorPeriod*; Boost factor recalculation period.  \n" +
		" * *boostStrength*; Boost strength.  \n" +
		"";

	public int		inputSizeX					= 16;
	public int		inputSizeY					= 16;
	
	public int		outputSizeX					= 48;
	public int		outputSizeY					= 48;
	public int		outputOnBits				= 46;
	
	public float	potentialConnections		= 0.85F;
	public int		potentialRadius				= 16;
	
	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;
	
	public int		activationHistorySize		= 1000;
	public int		boostFactorPeriod			= 10;
	public int		boostStrength				= 2;
	
	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		
		r.sb().append(": ");
		r.sb().append(inputSizeX);
		r.sb().append("*");
		r.sb().append(inputSizeY);
		r.sb().append("*1");

		r.sb().append("\n");
		r.sb().append("-> 0 = Active columns: ");
		r.sb().append(outputSizeX);
		r.sb().append("*");
		r.sb().append(outputSizeY);
		r.sb().append(", on bits: ");
		r.sb().append(outputOnBits);
		return r;
	}
}
