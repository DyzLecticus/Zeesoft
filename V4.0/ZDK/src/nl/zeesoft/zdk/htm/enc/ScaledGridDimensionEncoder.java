package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.htm.util.SDR;

public class ScaledGridDimensionEncoder extends EncoderObject {
	protected CombinedEncoder 	modules		= new CombinedEncoder();

	public ScaledGridDimensionEncoder(int length) {
		super(length,calculateBits(length));
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
				r = SDR.concat(r, sdr, length);
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

		float res = resolution;
		float resP = 0;

		int bts = 2;
		int totalLength = 0;
		for (int m = 1; m <= 100; m++) {
			int len = bts * 8;
			
			if (resP==0) {
				res = resolution;
			} else {
				res = bts * resolution;
			}
			System.out.println("Length: " + len + ", bits: " + bts + ", resolution: " + res);

			ScalarEncoder module = new ScalarEncoder(len,bts,0,(len * res));
			module.setPeriodic(true);
			module.setResolution(res);
			modules.addEncoder(String.format("%03d",m),module);

			totalLength += len;
			if (totalLength >= length) {
				break;
			}

			bts = bts * 2;
			resP = res;
		}
	}
	
	private static int calculateBits(int length) {
		int r = 0;
		int bts = 2;
		int totalLength = 0;
		for (int m = 1; m <= 100; m++) {
			int len = bts * 8;
			r += bts;

			totalLength += len;
			if (totalLength >= length) {
				break;
			}
			
			bts = bts * 2;
		}
		return r;
	}
}
