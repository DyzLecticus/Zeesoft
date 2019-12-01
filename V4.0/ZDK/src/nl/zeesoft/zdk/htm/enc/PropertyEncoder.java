package nl.zeesoft.zdk.htm.enc;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A PropertyEncoder can be used to encode a set of non-semantically overlapping properties.
 */
public class PropertyEncoder extends StateEncoderObject {
	private long					uid					= 0;
	private SortedMap<String,Long>	propertyIds			= new TreeMap<String,Long>();
	private SortedMap<Long,String>	propertyById		= new TreeMap<Long,String>();
	private ScalarEncoder			encoder				= null;
	
	public PropertyEncoder(int length,int bits) {
		super(length,bits);
		encoder = new ScalarEncoder(length,bits,1,length / bits);
	}
	
	@Override
	public SDR getSDRForValue(float value) {
		SDR r = null;
		if (value>0 && value % 1 == 0) {
			long id = (long) value;
			if (id>0 && id <=getBuckets()) {
				r = encoder.getSDRForValue(value);
			}
		}
		if (r==null) {
			r = new SDR(length);
		}
		return r;
	}

	/**
	 * Returns the SDR for a certain value.
	 * Will return an empty SDR if the property is unknown and there is no more room in the encoder for new properties.
	 * 
	 * @param property The property
	 * @return The SDR
	 */
	public SDR getSDRForProperty(String property) {
		SDR r = null;
		float value = getValueForProperty(property);
		if (value>0) {
			r = getSDRForValue(value);
		}
		if (r==null) {
			r = new SDR(length);
		}
		return r;
	}

	/**
	 * Returns the value for certain property.
	 * Will return 0 if the property is unknown and there is no more room in the encoder for new properties.
	 * 
	 * @param property The property
	 * @return The value
	 */
	public float getValueForProperty(String property) {
		float r = 0;
		if (property.length()>0) {
			if (propertyIds.containsKey(property)) {
				r = propertyIds.get(property);
			} else if (uid < getBuckets()) {
				uid++;
				propertyById.put(uid,property);
				propertyIds.put(property,uid);
				r = uid;
			}
		}
		return r;
	}

	/**
	 * Returns the number of used buckets in this SDR.
	 * 
	 * @return The number of used buckets in this SDR
	 */
	public int getBuckets() {
		return length / bits;
	}
	
	@Override
	public ZStringBuilder getDescription() {
		return encoder.getDescription();
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (Entry<Long,String> entry: propertyById.entrySet()) {
			if (r.length()>0) {
				r.append(";");
			}
			r.append("" + entry.getKey());
			r.append(",");
			r.append(entry.getValue());
		}
		return r;
	}
	
	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		if (str.length()>0) {
			uid = 0;
			propertyIds.clear();
			propertyById.clear();
			List<ZStringBuilder> elems = str.split(";");
			for (ZStringBuilder elem: elems) {
				List<ZStringBuilder> idProp = elem.split(",");
				if (idProp.size()==2) {
					Long id = Long.parseLong(idProp.get(0).toString());
					String property = idProp.get(1).toString();
					if (id > 0 && id < getBuckets() && property.length()>0) {
						if (id>uid) {
							uid = id;
						}
						propertyById.put(id,property);
						propertyIds.put(property,id);
					}
				}
			}
		}
	}
}
