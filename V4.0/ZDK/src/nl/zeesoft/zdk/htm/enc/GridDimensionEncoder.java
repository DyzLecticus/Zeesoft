package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A GridDimensionEncoder can be used to encode coordinates within a single dimension (0 - getCapacity()).
 * It is deterministic and has a relatively large capacity while maintaining uniqueness and relative overlaps.
 */
public class GridDimensionEncoder extends EncoderObject {
	protected CombinedEncoder 	modules		= new CombinedEncoder();

	public GridDimensionEncoder(int length,int bits) {
		super(length,bits);
		initializeModules();
	}

	@Override
	public void setResolution(float resolution) {
		super.setResolution(resolution);
		initializeModules();
	}
	
	/**
	 * Returns the capacity of this encoder.
	 * 
	 * @return The capacity
	 */
	public int getCapacity() {
		int r = 0;
		int mods = modules.getEncoders().size();
		float unit = (((ScalarEncoder)modules.getEncoders().get(mods - 1)).getMaxValue() / resolution);
		int mult = (mods - 1);
		if (mult == 0) {
			mult = 1;
		}
		r = (int)(unit * mult);
		return r;
	}
	
	@Override
	public SDR getSDRForValue(float value) {
		SDR r = null;
		for (EncoderObject encoder: modules.getEncoders()) {
			SDR sdr = encoder.getSDRForValue(value);
			if (r==null) {
				r = sdr;
			} else {
				r = SDR.concat(r, sdr, length);
			}
		}
		if (r.length()<length) {
			SDR sdr = new SDR(length - r.length());
			r = SDR.concat(r, sdr);
		}
		return r;
	}
	
	@Override
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		r.append(", capacity: ");
		r.append("" + getCapacity());
		for (String name: modules.getEncoderNames()) {
			EncoderObject encoder = modules.getEncoder(name);
			r.append("\n- ");
			r.append(name);
			r.append(" ");
			r.append(encoder.getDescription());
		}
		return r;
	}
	
	/**
	 * Iterates through all possible values of this encoder to determine if the SDR values have a certain minimal and maximal overlap.
	 * 
	 * @param minOverlap The minimal overlap between two sequential values
	 * @param maxOverlap The maximal overlap between two sequential values
	 * @return An empty string builder or a string builder containing an error message
	 */
	public ZStringBuilder testScalarOverlap(int minOverlap, int maxOverlap) {
		return testScalarOverlap(0,getCapacity() * resolution,minOverlap,maxOverlap);
	}
	
	protected void initializeModules() {
		modules.initialize();
		int mods = (bits / 2);
		int lengthPerModule = length / mods;
		float res = resolution;
		for (int m = 1; m <= mods; m++) {
			ScalarEncoder module = new ScalarEncoder(lengthPerModule,2,0,(lengthPerModule * res));
			module.setPeriodic(true);
			module.setResolution(res);
			modules.addEncoder(String.format("%03d",m),module);
			
			float unit = resolution * lengthPerModule;
			unit = unit - (resolution * ((m % 3) + 1));
			int mult = m * m;
			res = unit * mult;
		}
	}
}
