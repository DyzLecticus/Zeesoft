package nl.zeesoft.zdk.htm.sdr;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;

public class SDRSet {
	private int			width	= 0;
	private List<SDR>	set		= new ArrayList<SDR>();
	
	public SDRSet(int width) {
		if (width < 10) {
			width = 10;
		}
		this.width = width;
	}
	
	public SDRSet copy() {
		SDRSet r = new SDRSet(width);
		for (SDR sdr: set) {
			r.add(sdr.copy());
		}
		return r;
	}
	
	public void subsample(int keep) {
		for (SDR sdr: set) {
			sdr.subsample(keep);
		}
	}

	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("" + width);
		for (SDR sdr: set) {
			r.append("|");
			int added = 0;
			for (Integer onBit: sdr.getOnBits()) {
				if (added>0) {
					r.append(",");
				}
				r.append("" + onBit);
				added++;
			}
		}
		return r;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		width = 0;
		set.clear();
		List<ZStringBuilder> elems = str.split("|");
		if (elems.size()>=1) {
			width = Integer.parseInt(elems.get(0).toString());
			for (int i = 1; i<elems.size(); i++) {
				SDR sdr = new SDR(width);
				List<ZStringBuilder> onBits = elems.get(i).split(",");
				for (ZStringBuilder onBit: onBits) {
					sdr.setBit(Integer.parseInt(onBit.toString()),true);
				}
				set.add(sdr);
			}
		}
	}
	
	public int size() {
		return set.size();
	}
	
	public void add(SDR sdr) {
		if (sdr.size()==width) {
			set.add(sdr);
		}
	}
	
	public SDR get(int index) {
		return set.get(index);
	}
	
	public SDR remove(int index) {
		return set.remove(index);
	}
	
	public boolean remove(SDR sdr) {
		return set.remove(sdr);
	}
	
	public void clear() {
		set.clear();
	}
	
	public boolean contains(SDR sdr) {
		return set.contains(sdr);
	}

	public List<SDR> getMatches(SDR sdr,int theta) {
		List<SDR> r = new ArrayList<SDR>();
		if (sdr.size()==width) {
			for (SDR sdrC: set) {
				if (sdrC.matches(sdr, theta)) {
					r.add(sdrC);
				}
			}
		}
		return r;
	}
	
	public SortedMap<Integer,List<SDR>> getMatches(SDR sdr) {
		SortedMap<Integer,List<SDR>> r = new TreeMap<Integer,List<SDR>>();
		if (sdr.size()==width) {
			for (SDR sdrC: set) {
				int overlap = sdrC.getOverlapScore(sdr);
				if (overlap>0) {
					List<SDR> list = r.get(overlap);
					if (list==null) {
						list = new ArrayList<SDR>();
						r.put(overlap,list);
					}
					list.add(sdrC);
				}
			}
		}
		return r;
	}

	public SDR getUnion() {
		SDR r = new SDR(width);
		for (SDR sdr: set) {
			r = r.or(sdr);
		}
		return r;
	}
}
