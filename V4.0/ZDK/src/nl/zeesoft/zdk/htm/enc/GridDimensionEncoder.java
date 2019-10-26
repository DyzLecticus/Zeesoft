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
	
	@Override
	public SDR getSDRForValue(float value) {
		SDR r = null;
		for (EncoderObject encoder: modules.getEncoders()) {
			SDR sdr = encoder.getSDRForValue(value);
			if (r==null) {
				r = sdr;
			} else {
				r = SDR.concat(r, sdr);
			}
		}
		if (r.length()<length) {
			SDR sdr = new SDR(length - r.length());
			r = SDR.concat(r, sdr);
		}
		return r;
	}
	
	private void initializeModules() {
		modules.initialize();
		int mods = (bits / 2);
		int lengthPerModule = length / mods;
		float res = resolution;
		float resP = 0;
		for (int m = 1; m <= mods; m++) {
			if (resP==0) {
				res = resolution;
			} else if (resP==resolution) {
				res = resolution * lengthPerModule;
			} else {
				res = (lengthPerModule * resP);
			}
				
			ScalarEncoder module = new ScalarEncoder(lengthPerModule,2,0,(lengthPerModule * res));
			module.setPeriodic(true);
			module.setResolution(res);
			modules.addEncoder(String.format("%03d",m),module);
			
			resP = res;
		}
	}
}
