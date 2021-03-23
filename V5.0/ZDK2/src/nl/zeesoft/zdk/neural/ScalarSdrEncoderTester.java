package nl.zeesoft.zdk.neural;

public class ScalarSdrEncoderTester {
	public StringBuilder testOnBits(ScalarSdrEncoder encoder) {
		StringBuilder r = new StringBuilder();
		for (float val = encoder.minValue; val <= encoder.maxValue; val+=encoder.resolution) {
			Sdr sdr = encoder.getEncodedValue(val);
			r = checkOnBits(sdr, encoder.onBits, val);
		}
		return r;
	}
	
	public StringBuilder testMinimalOverlap(ScalarSdrEncoder encoder) {
		return testOverlap(encoder, 1, (encoder.onBits - 1));
	}
	
	public StringBuilder testNoOverlap(ScalarSdrEncoder encoder) {
		return testOverlap(encoder, 0, 0);
	}
	
	public StringBuilder testOverlap(ScalarSdrEncoder encoder, int minOverlap, int maxOverlap) {
		StringBuilder r = new StringBuilder();
		Sdr curr = encoder.getEncodedValue(encoder.minValue);
		for (float val = (encoder.minValue + encoder.resolution); val <= encoder.maxValue; val+=encoder.resolution) {
			Sdr next = encoder.getEncodedValue(val);
			int overlap = curr.getOverlap(next);
			if (minOverlap > 0 && overlap < minOverlap) {
				r.append(getMinOverlapError(val, overlap, minOverlap));
				break;
			} else if (maxOverlap >= 0 && overlap > maxOverlap) {
				r.append(getMaxOverlapError(val, overlap, maxOverlap));
				break;
			}
			curr = next;
		}
		return r;
	}
	
	public static StringBuilder checkOnBits(Sdr sdr, int onBits, Object value) {
		StringBuilder r = new StringBuilder();
		if (sdr.onBits.size()!=onBits) {
			r.append("Invalid on bits for value: ");
			r.append(value);
			r.append(", on bits: ");
			r.append(sdr.onBits.size());
			r.append(", required: ");
			r.append(onBits);
		}
		return r;
	}
	
	protected StringBuilder getMinOverlapError(float val, int overlap, int minOverlap) {
		StringBuilder r = getOverlapError(val, overlap);
		r.append(", minimum: ");
		r.append(minOverlap);
		return r;
	}
	
	protected StringBuilder getMaxOverlapError(float val, int overlap, int maxOverlap) {
		StringBuilder r = getOverlapError(val, overlap);
		r.append(", maximum: ");
		r.append(maxOverlap);
		return r;
	}
	
	protected StringBuilder getOverlapError(float val, int overlap) {
		StringBuilder r = new StringBuilder();
		r.append("Invalid bucket value overlap for value: ");
		r.append(val);
		r.append(", overlap: ");
		r.append(overlap);
		return r;
	}
}
