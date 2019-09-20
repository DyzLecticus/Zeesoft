package nl.zeesoft.zdk.htm.enc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class CombinedEncoder {
	private int									length		= 0;
	private int									bits		= 0;
	private SortedMap<String,EncoderObject>		encoders	= new TreeMap<String,EncoderObject>();
	
	public CombinedEncoder() {
		initialize();
	}
	
	public int length() {
		return length;
	}
	
	public int bits() {
		return bits;
	}

	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (Entry<String,EncoderObject> entry: encoders.entrySet()) {
			if (entry.getValue() instanceof StateEncoderObject) {
				StateEncoderObject enc = (StateEncoderObject) entry.getValue();
				if (r.length()>0) {
					r.append("|");
				}
				r.append("" + entry.getKey());
				r.append("=");
				r.append(enc.toStringBuilder());
			}
		}
		return r;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		if (str.length()>0) {
			List<ZStringBuilder> elems = str.split("|");
			for (ZStringBuilder elem: elems) {
				List<ZStringBuilder> nameState = elem.split("=");
				if (nameState.size()>1) {
					EncoderObject e = encoders.get(nameState.get(0).toString());
					if (e!=null && e instanceof StateEncoderObject) {
						StateEncoderObject enc = (StateEncoderObject) e;
						enc.fromStringBuilder(nameState.get(1));
					}
				}
			}
		}
	}
	
	protected void initialize() {
		length = 0;
		bits = 0;
		encoders.clear();
	}
	
	protected void addEncoder(String name,EncoderObject encoder) {
		length = length + encoder.length;
		bits = bits + encoder.bits;
		encoders.put(name,encoder);
	}
	
	protected SDR getSDRForValues(SortedMap<String,Float> values) {
		SDR r = null;
		for (Entry<String,EncoderObject> entry: encoders.entrySet()) {
			SDR add = null;
			Float value = values.get(entry.getKey());
			if (value==null) {
				add = new SDR(entry.getValue().length);
			} else {
				add = entry.getValue().getSDRForValue(value);
			}
			if (r==null) {
				r = add;
			} else {
				r = SDR.concat(r,add);
			}
		}
		return r;
	}
	
	protected List<EncoderObject> getEncoders() {
		List<EncoderObject> r = new ArrayList<EncoderObject>();
		for (EncoderObject encoder: encoders.values()) {
			r.add(encoder);
		}
		return r;
	}
	
	protected List<String> getEncoderNames() {
		List<String> r = new ArrayList<String>();
		for (String name: encoders.keySet()) {
			r.add(name);
		}
		return r;
	}
	
	protected EncoderObject getEncoder(String name) {
		return encoders.get(name);
	}
	
	protected ScalarEncoder getScalarEncoder(String name) {
		ScalarEncoder r = null;
		EncoderObject encoder = encoders.get(name);
		if (encoder instanceof ScalarEncoder) {
			r = (ScalarEncoder) encoder;
		}
		return r;
	}
}
