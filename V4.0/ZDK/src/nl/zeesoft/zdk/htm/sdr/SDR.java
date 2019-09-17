package nl.zeesoft.zdk.htm.sdr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZRandomize;

public class SDR {
	private int				size 		= 0;
	private Set<Integer>	onBits		= new HashSet<Integer>();
	
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
	
	public Integer turnOffRandomBit() {
		Integer r = null;
		if (onBits.size()>0) {
			if (onBits.size()==1) {
				for (Integer onBit: onBits) {
					r = onBit;
				}
				onBits.clear();
			} else {
				int remove = ZRandomize.getRandomInt(0,onBits.size() - 1);
				int i = 0;
				for (Integer onBit: onBits) {
					if (i==remove) {
						r = onBit;
						break;
					}
					i++;
				}
				onBits.remove((Integer)r);
			}
		}
		return r;
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
		if (keep>0 && keep < onBits.size()) {
			int remove = (onBits.size() - keep);
			if (remove>0) {
				List<Integer> list = new ArrayList<Integer>(onBits);
				for (int i = 0; i<remove; i++) {
					onBits.remove(list.remove(ZRandomize.getRandomInt(0,list.size() - 1)));
				}
			}
		}
	}
	
	public List<Integer> randomize(int set) {
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
		return getOnBits();
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

	public static SDR and(SDR a,SDR b) {
		return a.and(b);
	}

	public static SDR or(SDR a,SDR b) {
		return a.or(b);
	}

	public static SDR xor(SDR a,SDR b) {
		return a.xor(b);
	}

	public static SDR not(SDR a) {
		return a.not();
	}

	public static SDR concat(SDR a,SDR b) {
		return a.concat(b);
	}

	public static BigInteger capacity(int size,int onBits) {
		BigInteger r = null;
		if (size==128 && onBits==2) {
			r = new BigInteger("8128");
		} else if (size==256 && onBits==5) {
			r = new BigInteger("8809549056");
		} else if (size==1024 && onBits==20) {
			r = new BigInteger("547994376586478624307777948483062748869376");
		} else if (size==2048 && onBits==41) {
			r = new BigInteger("116159298814240952148294423915105693715223390099660465104363841507980771546258614700032");
		} else {
			BigInteger sizeF = StaticFunctions.factorial(size);
			BigInteger onBitsF = StaticFunctions.factorial(onBits);
			BigInteger offBitsF = StaticFunctions.factorial(size - onBits);
			BigInteger div = onBitsF.multiply(offBitsF);
			r = sizeF.divide(div);
		}
		return r;
	}

	private SDR and(SDR c) {
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
	
	private SDR or(SDR c) {
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
	
	private SDR xor(SDR c) {
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
	
	private SDR not() {
		SDR r = new SDR(size);
		for (int i = 0; i < size; i++) {
			Integer onBit = (Integer) i;
			if (!onBits.contains(onBit)) {
				r.onBits.add(onBit);
			}
		}
		return r;
	}
	
	private SDR concat(SDR c) {
		SDR r = copy();
		r.size = r.size + c.size;
		for (Integer onBit: c.onBits) {
			r.onBits.add(new Integer(onBit + size));
		}
		return r;
	}
}
