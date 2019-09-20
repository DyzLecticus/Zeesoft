package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class ScalarEncoder extends EncoderObject {
	private int		minValue	= 0;
	private int		maxValue	= 0;
	private boolean periodic	= false;
	
	public ScalarEncoder(int length,int bits,int minValue,int maxValue) {
		super(length,bits);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}

	@Override
	public SDR getSDRForValue(float value) {
		SDR r = new SDR(length);

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
			if (set>=length) {
				set = set % length;
			}
			r.setBit(set,true);
		}
		return r;
	}
	
	public int getBuckets() {
		return periodic ? length : length - bits;
	}

	public ZStringBuilder testScalarOverlap(int minOverlap, int maxOverlap) {
		if (maxOverlap==0) {
			maxOverlap = bits; 
		}
		ZStringBuilder r = new ZStringBuilder();
		float pv = minValue - resolution;
		SDR prev = null;
		for (float v = minValue; v < maxValue; v += resolution) {
			SDR test = getSDRForValue(v);
			if (prev!=null) {
				int overlap = test.getOverlapScore(prev);
				if (overlap<minOverlap) {
					r.append("Overlap between " + pv + " and " + v + " is less than " + minOverlap);
					break;
				} else if (overlap>maxOverlap) {
					r.append("Overlap between " + pv + " and " + v + " is greater than " + maxOverlap);
					break;
				}
			}
			pv = v;
			prev = test;
		}
		return r;
	}
}
