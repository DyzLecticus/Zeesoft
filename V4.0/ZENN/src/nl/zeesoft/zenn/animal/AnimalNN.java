package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.network.NN;

public class AnimalNN extends NN {
	public static final int			LEFT_RED		= 0;
	public static final int			LEFT_GREEN		= 1;
	public static final int			LEFT_BLUE		= 2;
	public static final int			LEFT_GREY		= 3;
	
	public static final int			FRONT_RED		= 4;
	public static final int			FRONT_GREEN		= 5;
	public static final int			FRONT_BLACK		= 6;
	public static final int			FRONT_BLUE		= 7;
	public static final int			FRONT_GREY		= 8;
	
	public static final int			RIGHT_RED		= 9;
	public static final int			RIGHT_GREEN		= 10;
	public static final int			RIGHT_BLUE		= 11;
	public static final int			RIGHT_GREY		= 12;
	
	public static final int			OUT_LEFT		= 0;
	public static final int			OUT_FRONT		= 1;
	public static final int			OUT_BACK		= 2;
	public static final int			OUT_RIGHT		= 3;
	
	public static final float[]		INTENSITIES		= {1.00F,0.75F,0.50F,0.25F};

	private static final int		MIN_LAYERS		= 1;
	private static final int		MAX_LAYERS		= 5;
	private static final int		INPUT_NEURONS	= RIGHT_GREY + 1;
	private static final int		OUTPUT_NEURONS	= OUT_RIGHT + 1;

	public ZStringBuilder initialize() {
		getCode().generate(10000);
		return initialize(INPUT_NEURONS,OUTPUT_NEURONS,MIN_LAYERS,MAX_LAYERS);
	}

	public static AnimalNN getTrainableAnimalNN(boolean herbivore, int minSuccesses, int timeOutMs) {
		AnimalNN r = null;
		long started = System.currentTimeMillis();
		while (r==null) {
			AnimalNN nn = new AnimalNN();
			ZStringBuilder err = nn.initialize();
			if (err.length()>0) {
				break;
			} else {
				AnimalTestCycleSet tcs = new AnimalTestCycleSet();
				tcs.initialize(nn,herbivore);
				nn.runTestCycleSet(tcs);
				if (tcs.successes>=minSuccesses) {
					r = nn;
				}
			}
			if (r==null && (System.currentTimeMillis() - started) >= timeOutMs) {
				break;
			}
		}
		return r;
	}
	
	@Override
	protected NN getCopyNN() {
		return new AnimalNN();
	}
}
