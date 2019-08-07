package nl.zeesoft.zenn.animal;

public class AnimalConstants {
	public static final int			LEFT_RED		= 0;
	public static final int			LEFT_GREEN		= 1;
	public static final int			LEFT_BLUE		= 2;
	
	public static final int			FRONT_RED		= 3;
	public static final int			FRONT_GREEN		= 4;
	public static final int			FRONT_BLUE		= 5;
	
	public static final int			RIGHT_RED		= 6;
	public static final int			RIGHT_GREEN		= 7;
	public static final int			RIGHT_BLUE		= 8;
	
	public static final int			OUT_LEFT		= 0;
	public static final int			OUT_FRONT		= 1;
	public static final int			OUT_BACK		= 2;
	public static final int			OUT_RIGHT		= 3;
	
	public static final float[]		INTENSITIES		= {1.00F,0.75F,0.50F,0.25F};

	public static final int			MAX_LAYERS		= 3;
	public static final int			MAX_NEURONS		= 12;
	public static final int			INPUT_NEURONS	= RIGHT_BLUE + 1;
	public static final int			OUTPUT_NEURONS	= OUT_RIGHT + 1;
}
