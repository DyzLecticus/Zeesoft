package nl.zeesoft.zenn.animal;

public class AnimalConstants {
	public static final int			IN_RANDOM			= 0;

	public static final int			IN_LEFT_RED			= 1;
	public static final int			IN_LEFT_GREEN		= 2;
	public static final int			IN_LEFT_BLUE		= 3;
	
	public static final int			IN_FRONT_RED		= 4;
	public static final int			IN_FRONT_GREEN		= 5;
	public static final int			IN_FRONT_BLUE		= 6;
	
	public static final int			IN_RIGHT_RED		= 7;
	public static final int			IN_RIGHT_GREEN		= 8;
	public static final int			IN_RIGHT_BLUE		= 9;
	
	public static final int[]		INPUTS				= {
			IN_RANDOM,
			IN_LEFT_RED,IN_LEFT_GREEN,IN_LEFT_BLUE,
			IN_FRONT_RED,IN_FRONT_GREEN,IN_FRONT_BLUE,
			IN_RIGHT_RED,IN_RIGHT_GREEN,IN_RIGHT_BLUE,
	};

	public static final int			OUT_LEFT_ACTUATOR	= 0;
	public static final int			OUT_FRONT_MOUTH		= 1;
	public static final int			OUT_BACK_ACTUATOR	= 2;
	public static final int			OUT_RIGHT_ACTUATOR	= 3;

	public static final int[]		OUTPUTS				= {
			OUT_LEFT_ACTUATOR,
			OUT_FRONT_MOUTH,
			OUT_BACK_ACTUATOR,
			OUT_RIGHT_ACTUATOR
	};

	public static final float[]		INTENSITIES			= {1.00F,0.75F,0.50F,0.25F};

	public static final int			MAX_LAYERS			= 3;
	public static final int			MAX_NEURONS			= 12;
	
	public static final float[]		COLOR_BLACK			= {0.00F,0.00F,0.00F};
	public static final float[]		COLOR_RED			= {1.00F,0.00F,0.00F};
	public static final float[]		COLOR_GREEN			= {0.00F,1.00F,0.00F};
	public static final float[]		COLOR_BLUE			= {0.00F,0.00F,1.00F};
	public static final float[]		COLOR_GREY			= {0.75F,0.75F,0.75F};
}
