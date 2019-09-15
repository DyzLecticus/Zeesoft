package nl.zeesoft.zdk.htm.enc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class RDScalarEncoder extends StateEncoderObject {
	private SortedMap<Float,SDR>	sdrsByValue			= new TreeMap<Float,SDR>();
	private Set<ZStringBuilder>		sdrsByStringBuilder	= new HashSet<ZStringBuilder>();
	private List<Integer>			freeBits			= new ArrayList<Integer>();
	private BigInteger				capacity			= null;
	
	public RDScalarEncoder(int size,int bits) {
		super(size,bits);
		for (int i = 0; i < size; i++) {
			freeBits.add(i);
		}
		capacity = SDR.capacity(size,bits);
	}
	
	@Override
	public SDR getSDRForValue(float value) {
		if (value % resolution > 0) {
			value = value - (value % resolution);
		}
		
		SDR r = sdrsByValue.get(value);
		if (r==null) {
			if (sdrsByValue.size()==0) {
				r = new SDR(size);
				List<Integer> onBits = r.randomize(bits);
				for (Integer onBit: onBits) {
					freeBits.remove(onBit);
				}
				sdrsByValue.put(value,r);
				sdrsByStringBuilder.add(r.toStringBuilder());
			} else {
				if (capacity.compareTo(BigInteger.valueOf(sdrsByValue.size()))>0) {
					if (value > sdrsByValue.lastKey()) {
						int num = (int) ((value - sdrsByValue.lastKey()) / resolution);
						float val = sdrsByValue.lastKey() + resolution;
						r = sdrsByValue.get(sdrsByValue.lastKey());
						for (int i = 0; i < num; i++) {
							r = addNewUniqueRandomVariation(val,r);
							if (r==null) {
								break;
							} else {
								val = val + resolution;
							}
						}
					} else if (value < sdrsByValue.firstKey()) {
						int num = (int) ((sdrsByValue.firstKey() - value) / resolution);
						float val = sdrsByValue.firstKey() - resolution;
						r = sdrsByValue.get(sdrsByValue.firstKey());
						for (int i = 0; i < num; i++) {
							r = addNewUniqueRandomVariation(val,r);
							if (r==null) {
								break;
							} else {
								val = val - resolution;
							}
						}
					}
				}
			}
		}
		return r;
	}
	
	public int getBuckets() {
		return sdrsByValue.size();
	}
	
	public BigInteger getCapacity() {
		return capacity;
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (Entry<Float,SDR> entry: sdrsByValue.entrySet()) {
			if (r.length()>0) {
				r.append(";");
			}
			r.append("" + entry.getKey());
			for (Integer onBit: entry.getValue().getOnBits()) {
				r.append(",");
				r.append("" + onBit);
			}
		}
		return r;
	}
	
	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		if (str.length()>0) {
			sdrsByValue.clear();
			sdrsByStringBuilder.clear();
			freeBits.clear();
			SDR fBits = new SDR(size);
			List<ZStringBuilder> elems = str.split(";");
			for (ZStringBuilder elem: elems) {
				List<ZStringBuilder> onBits = elem.split(",");
				if (onBits.size() == bits + 1) {
					Float value = Float.parseFloat(onBits.get(0).toString());
					SDR sdr = new SDR(size);
					for (int b = 1; b<onBits.size(); b++) {
						sdr.setBit(Integer.parseInt(onBits.get(b).toString()),true);
					}
					sdrsByValue.put(value,sdr);
					sdrsByStringBuilder.add(sdr.toStringBuilder());
					fBits = SDR.or(fBits,sdr);
				}
			}
			freeBits = SDR.not(fBits).getOnBits();
		}
	}
	
	private SDR addNewUniqueRandomVariation(float value,SDR sdr) {
		SDR r = null;
		if (capacity.compareTo(BigInteger.valueOf(sdrsByValue.size()))>0) {
			r = sdr.copy();
			if (freeBits.size()>0) {
				r.turnOffRandomBit();
				int select = ZRandomize.getRandomInt(0,freeBits.size() - 1);
				Integer on = freeBits.remove(select);
				r.setBit(on,true);
			} else {
				r = generateNewUniqueRandomVariation(r);
			}
			sdrsByValue.put(value,r);
			sdrsByStringBuilder.add(r.toStringBuilder());
		}
		return r;
	}
	
	private SDR generateNewUniqueRandomVariation(SDR sdr) {
		SDR r = null;
		List<Integer> availableBits = SDR.not(sdr).getOnBits();
		sdr.turnOffRandomBit();
		while (r==null) {
			r = sdr.copy();
			int select = ZRandomize.getRandomInt(0,availableBits.size() - 1);
			Integer on = availableBits.remove(select);
			r.setBit(on,true);
			if (sdrsByStringBuilder.contains(r.toStringBuilder())) {
				r = null;
			}
		}
		return r;
	}
}
