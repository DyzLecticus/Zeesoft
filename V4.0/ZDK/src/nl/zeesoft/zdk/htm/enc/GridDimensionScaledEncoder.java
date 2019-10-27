package nl.zeesoft.zdk.htm.enc;

/**
 * A GridDimensionScaledEncoder can be used to encode coordinates within a single dimension (0 - getCapacity()).
 * It is deterministic and has a relatively large capacity while maintaining uniqueness and relative overlaps.
 * The capacity is less than that of a GridDimensionEncoder but it represents value changes more accurately.
 */
public class GridDimensionScaledEncoder extends GridDimensionEncoder {
	public GridDimensionScaledEncoder(int length) {
		super(length,calculateBits(length));
		initializeModules();
	}
	
	@Override
	public int getCapacity() {
		int mods = modules.getEncoders().size();
		return (int) (((ScalarEncoder)modules.getEncoders().get(mods - 1)).getMaxValue() / resolution);
	}
		
	@Override
	protected void initializeModules() {
		modules.initialize();

		int bts = 2;
		int totalLength = 0;
		int mult = 32;
		if (length % 3 == 0) {
			mult = 24;
		}
		for (int m = 1; m <= 100; m++) {
			int len = m * mult;
			
			float res = resolution;
			if (m > 1) {
				res = (bts * resolution * (m * m)) - resolution;
			}
			
			if (totalLength + len > length) {
				len = length - totalLength;
			}

			ScalarEncoder module = new ScalarEncoder(len,bts,0,(len * res));
			module.setPeriodic(true);
			module.setResolution(res);
			modules.addEncoder(String.format("%03d",m),module);

			totalLength += len;
			if (totalLength >= length) {
				break;
			}

			bts = bts + 2;
		}
	}
	
	private static int calculateBits(int length) {
		int r = 0;
		int bts = 2;
		int totalLength = 0;
		int mult = 32;
		if (length % 3 == 0) {
			mult = 24;
		}
		for (int m = 1; m <= 100; m++) {
			int len = m * mult;
			r += bts;

			totalLength += len;
			if (totalLength >= length) {
				break;
			}
			
			bts = bts + 2;
		}
		return r;
	}
}
