package nl.zeesoft.zdk.neural;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;

public class KeyValueSDR extends SDR {
	public static final String				DEFAULT_VALUE_KEY	= "value";
	
	protected SortedMap<String,Object>		keyValues			= new TreeMap<String,Object>();

	public KeyValueSDR() {
		
	}
	
	public KeyValueSDR(int sizeX, int sizeY) {
		super(sizeX, sizeY);
	}
	
	public KeyValueSDR(SDR sdr) {
		super(sdr);
		if (sdr instanceof KeyValueSDR) {
			keyValues.clear();
			KeyValueSDR kvSDR = (KeyValueSDR) sdr;
			for (Entry<String,Object> entry: kvSDR.keyValues.entrySet()) {
				keyValues.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public void setValue(Object value) {
		keyValues.put(DEFAULT_VALUE_KEY, value);
	}

	public Object getValue() {
		return keyValues.get(DEFAULT_VALUE_KEY);
	}

	public void put(String key, Object value) {
		keyValues.put(key, value);
	}
	
	public Object get(String key) {
		return keyValues.get(key);
	}
	
	public Object remove(String key) {
		return keyValues.remove(key);
	}
	
	public void clear() {
		keyValues.clear();
	}
	
	public int size() {
		return keyValues.size();
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append("SDR");
		r.sb().append("##");
		r.sb().append(super.toStr().sb());
		for (Entry<String,Object> entry: keyValues.entrySet()) {
			r.sb().append("@");
			r.sb().append(entry.getKey());
			r.sb().append("#");
			r.sb().append(entry.getValue().getClass().getName());
			r.sb().append("#");
			if (entry.getValue() instanceof StrAble) {
				StrAble sv = (StrAble) entry.getValue();
				r.sb().append(sv.toStr().sb());
			} else if (entry.getValue() instanceof Str) {
				Str sv = (Str) entry.getValue();
				r.sb().append(sv.sb());
			} else {
				r.sb().append(entry.getValue());
			}
		}
		return r;
	}
	
	@Override
	public void fromStr(Str str) {
		keyValues.clear();
		List<Str> elems = str.split("@");
		int i = 0;
		for (Str elem: elems) {
			List<Str> kcv = elem.split("#");
			String key = kcv.get(0).toString();
			String className = kcv.get(1).toString();
			if (i==0 && key.equals("SDR")) {
				super.fromStr(kcv.get(2));
			} else {
				Object value = null;
				if (className.equals(Str.class.getName())) {
					value = kcv.get(2);
				} else if (className.equals(String.class.getName())) {
					value = kcv.get(2).toString();
				} else if (className.equals(Long.class.getName())) {
					value = Long.parseLong(kcv.get(2).toString());
				} else if (className.equals(Integer.class.getName())) {
					value = Integer.parseInt(kcv.get(2).toString());
				} else if (className.equals(Integer.class.getName())) {
					value = Float.parseFloat(kcv.get(2).toString());
				} else if (className.equals(Double.class.getName())) {
					value = Double.parseDouble(kcv.get(2).toString());
				} else if (className.equals(Boolean.class.getName())) {
					value = Boolean.parseBoolean(kcv.get(2).toString());
				} else {
					value = Instantiator.getNewClassInstanceForName(className);
					if (value instanceof StrAble) {
						((StrAble) value).fromStr(kcv.get(2));
					}
				}
				if (value!=null) {
					put(key, value);
				}
			}
			i++;
		}
	}
}
