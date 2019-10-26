package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * ScalarEncoders are used to encode scalar values into SDRs.
 */
public class ScalarEncoder extends EncoderObject {
	private float	minValue	= 0;
	private float	maxValue	= 0;
	private boolean periodic	= false;
	
	public ScalarEncoder(int length,int bits,float minValue,float maxValue) {
		super(length,bits);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	/**
	 * Sets the periodic indicator of this SDR.
	 * Periodic SDRs can be used to represent cyclic values like dates and times.
	 *  
	 * @param periodic Indicates this encoder should generate periodic SDRs
	 */
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
	
	/**
	 * Returns the number of possible buckets in this SDR.
	 * 
	 * @return The number of possible buckets in this SDR
	 */
	public int getBuckets() {
		return periodic ? length : length - bits;
	}

	/**
	 * Iterates through all possible values of this encoder to determine if the SDR values have a certain minimal and maximal overlap.
	 * 
	 * @param minOverlap The minimal overlap between two sequential values
	 * @param maxOverlap The maximal overlap between two sequential values
	 * @return An empty string builder or a string builder containing an error message
	 */
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
