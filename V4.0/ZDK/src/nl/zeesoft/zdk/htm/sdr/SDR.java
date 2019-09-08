package nl.zeesoft.zdk.htm.sdr;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;

public class SDR {
	private int				size 	= 0;
	private List<Integer>	onBits	= new ArrayList<Integer>();
	
	public SDR(int size) {
		if (size < 10) {
			size = 10;
		}
		this.size = size;
	}
	
	public SDR copy() {
		SDR r = new SDR(size);
		for (Integer onBit: onBits) {
			r.onBits.add(new Integer(onBit));
		}
		return r;
	}
	
	public List<Integer> getOnBits() {
		List<Integer> r = new ArrayList<Integer>();
		for (Integer onBit: onBits) {
			r.add(new Integer(onBit));
		}
		return r;
	}
	
	public void setBit(int index,boolean on) {
		if (index>=0 && index<size) {
			Integer onBit = (Integer) index;
			if (on) {
				if (!onBits.contains(onBit)) {
					onBits.add(onBit);
				}
			} else {
				onBits.remove(onBit);
			}
		}
	}
	
	public boolean getBit(int index) {
		return onBits.contains((Integer)index);
	}
	
	public int size() {
		return size;
	}
	
	public int onBits() {
		return onBits.size();
	}

	public void subsample(int keep) {
		if (keep>0 && keep<onBits.size()) {
			int remove = (onBits.size() - keep);
			for (int i = 0; i<remove; i++) {
				onBits.remove(ZRandomize.getRandomInt(0,onBits.size() - 1));
			}
		}
	}
	
	public void randomize(int set) {
		onBits.clear();
		if (set>0) {
			for (int i = 0; i<set; i++) {
				boolean added = false;
				while (!added) {
					Integer onBit = ZRandomize.getRandomInt(0,size - 1);
					if (!onBits.contains(onBit)) {
						onBits.add(onBit);
						added = true;
					}
				}
			}
		}
	}
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("" + size);
		for (Integer onBit: onBits) {
			r.append(",");
			r.append("" + onBit);
		}
		return r;
	}

	public void fromStringBuilder(ZStringBuilder str) {
		size = 0;
		onBits.clear();
		List<ZStringBuilder> elems = str.split(",");
		if (elems.size()>1) {
			size = Integer.parseInt(elems.get(0).toString());
			for (int i = 1; i<elems.size(); i++) {
				onBits.add(Integer.parseInt(elems.get(i).toString()));
			}
		}
	}

	public ZStringBuilder toBitString() {
		ZStringBuilder r = new ZStringBuilder();
		for (int i = 0; i < size; i++) {
			if (getBit(i)) {
				r.append("1");
			} else {
				r.append("0");
			}
		}
		return r;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean r = true;
		if (o==null || !(o instanceof SDR)) {
			r = false;
		} else {
			SDR c = (SDR) o;
			if (size!=c.size ||	onBits.size()!=c.onBits.size()) {
				r = false;
			} else if (onBits.size()>0) {
				for (Integer onBit: onBits) {
					if (!c.onBits.contains(onBit)) {
						r = false;
						break;
					}
				}
			}
		}
		return r;
	}
	
	public boolean matches(SDR c,int theta) {
		boolean r = false;
		if (theta<=0 || getOverlapScore(c)>=theta) {
			r = true;
		}
		return r;
	}

	public int getOverlapScore(SDR c) {
		int r = 0;
		for (Integer onBit: onBits) {
			if (c.onBits.contains(onBit)) {
				r++;
			}
		}
		return r;
	}

	public SDR and(SDR c) {
		SDR r = null;
		if (c.size == size) {
			r = new SDR(size);
			for (Integer onBit: onBits) {
				if (c.onBits.contains(onBit)) {
					r.onBits.add(onBit);
				}
			}
		}
		return r;
	}
	
	public SDR or(SDR c) {
		SDR r = null;
		if (c.size == size) {
			r = new SDR(size);
			for (Integer onBit: onBits) {
				r.onBits.add(onBit);
			}
			for (Integer onBit: c.onBits) {
				if (!r.onBits.contains(onBit)) {
					r.onBits.add(onBit);
				}
			}
		}
		return r;
	}
	
	public SDR xor(SDR c) {
		SDR r = null;
		if (c.size == size) {
			r = new SDR(size);
			for (Integer onBit: onBits) {
				if (!c.onBits.contains(onBit)) {
					r.onBits.add(onBit);
				}
			}
			for (Integer onBit: c.onBits) {
				if (!onBits.contains(onBit)) {
					r.onBits.add(onBit);
				}
			}
		}
		return r;
	}
	
	public SDR not() {
		SDR r = new SDR(size);
		for (int i = 0; i < size; i++) {
			Integer onBit = (Integer) i;
			if (!onBits.contains(onBit)) {
				r.onBits.add(onBit);
			}
		}
		return r;
	}

	public static SDR and(SDR a,SDR b) {
		return a.and(b);
	}

	public static SDR or(SDR a,SDR b) {
		return a.or(b);
	}

	public static SDR xor(SDR a,SDR b) {
		return a.xor(b);
	}
}
