package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.htm.util.SDR;

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
	
	public int getCapacity() {
		int r = 0;
		int mods = modules.getEncoders().size();
		float unit = (((ScalarEncoder)modules.getEncoders().get(mods - 1)).getMaxValue() / resolution);
		int mult = (mods / 2);
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
