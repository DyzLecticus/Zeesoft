package nl.zeesoft.zdk.neural.processors;

public class EncoderConfig extends SDRProcessorConfig{
	public int				sizeX			= 16;
	public int				sizeY			= 16;
	public int				onBits			= 16;
	
	public float			minValue		= 0;
	public float			maxValue		= 200;
	public boolean			periodic		= false;
}
