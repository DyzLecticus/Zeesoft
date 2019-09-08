package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class ScalarEncoder extends EncoderObject {
	private int		minValue	= 0;
	private int		maxValue	= 0;
	private boolean periodic	= false;
	
	public ScalarEncoder(int size,int bits,int minValue,int maxValue) {
		super(size,bits);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}

	@Override
	public SDR getSDRForValue(float value) {
		SDR r = new SDR(size);

		int bucket = 0;

		float range = (maxValue - minValue) / resolution;
		float normalizedValue = (value - minValue) / resolution;;
		
		bucket = (int) ((normalizedValue / range) * getBuckets());
		
		if (periodic) {
			bucket = bucket % getBuckets();
			if (bucket < 0) {
				bucket = getBuckets() - (bucket * -1);
			}
		} else {
			if (bucket<0) {
				bucket = 0;
			} else if (bucket>getBuckets()) {
				bucket = getBuckets();
			}
		}
		
		int end = bucket + bits;
		for (int i = bucket; i < end; i++) {
			int set = i;
			if (set>=size) {
				set = set % size;
			}
			r.setBit(set,true);
		}
		return r;
	}
	
	public int getBuckets() {
		return periodic ? size : size - bits;
	}
}
