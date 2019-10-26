package nl.zeesoft.zdk.htm.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZRandomize;

/**
 * Sparse distributed representations are used to code signals in HTM systems.
 */
public class SDR {
	private int				length 		= 0;
	private Set<Integer>	onBits		= new HashSet<Integer>();
	
	public SDR(int length) {
		if (length < 2) {
			length = 2;
		}
		this.length = length;
	}
	
	/**
	 * Returns a copy of this SDR.
	 * 
	 * @return A copy of this SDR
	 */
	public SDR copy() {
		SDR r = new SDR(length);
		for (Integer onBit: onBits) {
			r.onBits.add(new Integer(onBit));
		}
		return r;
	}
	
	/**
	 * Returns a list of on bits.
	 * 
	 * @return A list of on bits
	 */
	public List<Integer> getOnBits() {
		List<Integer> r = new ArrayList<Integer>();
		for (Integer onBit: onBits) {
			r.add(new Integer(onBit));
		}
		return r;
	}
	
	/**
	 * Sets a specific bit.
	 * 
	 * @param index The bit index
	 * @param on Indicates the bit should be set to 1
	 * @return True of the bit was set to the specified value
	 */
	public boolean setBit(int index,boolean on) {
		boolean r = false;
		if (index>=0 && index<length) {
			Integer onBit = (Integer) index;
			if (on) {
				if (!onBits.contains(onBit)) {
					onBits.add(onBit);
					r = true;
				}
			} else {
				r = onBits.remove(onBit);
			}
		}
		return r;
	}
	
	/**
	 * Turns of a random bit
	 * 
	 * @return The index of the bit that was turned off
	 */
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
	
	/**
	 * Returns the value of a specific bit
	 * 
	 * @param index The bit index
	 * @return The value of the bit
	 */
	public boolean getBit(int index) {
		return onBits.contains((Integer)index);
	}
	
	/**
	 * Returns the length of the SDR.
	 * 
	 * @return The length of the SDR.
	 */
	public int length() {
		return length;
	}
	
	/**
	 * Returns the number of on bits.
	 * 
	 * @return The number of on bits
	 */
	public int onBits() {
		return onBits.size();
	}

	/**
	 * Turns off bits until a specific number of on bits remains.
	 * 
	 * @param keep The number of on bits to keep
	 */
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
	
	/**
	 * Initializes the SDR with a specified number of random bits turned on
	 * 
	 * @param set The number of bits to turn on
	 * @return A list of bit indexes of bits that were turned on
	 */
	public List<Integer> randomize(int set) {
		onBits.clear();
		if (set>0) {
			for (int i = 0; i<set; i++) {
				boolean added = false;
				while (!added) {
					Integer onBit = ZRandomize.getRandomInt(0,length - 1);
					if (!onBits.contains(onBit)) {
						onBits.add(onBit);
						added = true;
					}
				}
			}
		}
		return getOnBits();
	}
	
	/**
	 * Converts the SDR to a string builder.
	 * 
	 * @return The string builder
	 */
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("" + length);
		for (Integer onBit: onBits) {
			r.append(",");
			r.append("" + onBit);
		}
		return r;
	}

	/**
	 * Initializes the SDR from a string builder.
	 * 
	 * @param str The string builder
	 */
	public void fromStringBuilder(ZStringBuilder str) {
		length = 0;
		onBits.clear();
		List<ZStringBuilder> elems = str.split(",");
		if (elems.size()>1) {
			length = Integer.parseInt(elems.get(0).toString());
			for (int i = 1; i<elems.size(); i++) {
				onBits.add(Integer.parseInt(elems.get(i).toString()));
			}
		}
	}

	/**
	 * Converts the SDR to a complete binary string builder representation
	 * 
	 * @return The string builder
	 */
	public ZStringBuilder toBitString() {
		ZStringBuilder r = new ZStringBuilder();
		for (int i = 0; i < length; i++) {
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
			if (length!=c.length ||	onBits.size()!=c.onBits.size()) {
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
	
	/**
	 * Returns true if the specified SDR matches this SDR with a certain minimal number of overlapping bits.
	 * 
	 * @param c The SDR to compare this SDR to
	 * @param theta The minimal number of overlapping bits
	 * @return True if the specified SDR matches
	 */
	public boolean matches(SDR c,int theta) {
		boolean r = false;
		if (theta<=0 || getOverlapScore(c)>=theta) {
			r = true;
		}
		return r;
	}

	/**
	 * Returns the number of overlapping bits between this SDR and another SDR.
	 * 
	 * @param c The SDR to compare this SDR to
	 * @return The number of overlapping bits
	 */
	public int getOverlapScore(SDR c) {
		int r = 0;
		for (Integer onBit: onBits) {
			if (c.onBits.contains(onBit)) {
				r++;
			}
		}
		return r;
	}

	/**
	 * Returns a new SDR that combines two SDRs using the AND operator.
	 * 
	 * @param a SDR A
	 * @param b SDR B
	 * @return The new SDR
	 */
	public static SDR and(SDR a,SDR b) {
		return a.and(b);
	}

	/**
	 * Returns a new SDR that combines two SDRs using the OR operator.
	 * 
	 * @param a SDR A
	 * @param b SDR B
	 * @return The new SDR
	 */
	public static SDR or(SDR a,SDR b) {
		return a.or(b);
	}

	/**
	 * Returns a new SDR that combines two SDRs using the XOR operator.
	 * 
	 * @param a SDR A
	 * @param b SDR B
	 * @return The new SDR
	 */
	public static SDR xor(SDR a,SDR b) {
		return a.xor(b);
	}

	/**
	 * Returns a new SDR that is an inversion of the input SDR. 
	 * 
	 * @param a The input SDR
	 * @return The new SDR
	 */
	public static SDR not(SDR a) {
		return a.not();
	}

	/**
	 * Returns a new SDR that is a concatenation of two SDRs.
	 * 
	 * @param a SDR A
	 * @param b SDR B
	 * @return The new SDR
	 */
	public static SDR concat(SDR a,SDR b) {
		return a.concat(b);
	}

	/**
	 * Returns the capacity (number of possible unique SDRs) for an a certain SDR length and on bits
	 * 
	 * @param length The length of the SDR
	 * @param onBits The number of on bits for the SDR
	 * @return The capacity
	 */
	public static BigInteger capacity(int length,int onBits) {
		BigInteger r = null;
		if (length==128 && onBits==2) {
			r = new BigInteger("8128");
		} else if (length==256 && onBits==5) {
			r = new BigInteger("8809549056");
		} else if (length==1024 && onBits==20) {
			r = new BigInteger("547994376586478624307777948483062748869376");
		} else if (length==2048 && onBits==41) {
			r = new BigInteger("116159298814240952148294423915105693715223390099660465104363841507980771546258614700032");
		} else {
			BigInteger lengthF = StaticFunctions.factorial(length);
			BigInteger onBitsF = StaticFunctions.factorial(onBits);
			BigInteger offBitsF = StaticFunctions.factorial(length - onBits);
			BigInteger div = onBitsF.multiply(offBitsF);
			r = lengthF.divide(div);
		}
		return r;
	}

	private SDR and(SDR c) {
		SDR r = null;
		if (c.length == length) {
			r = new SDR(length);
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
		if (c.length == length) {
			r = new SDR(length);
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
		if (c.length == length) {
			r = new SDR(length);
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
		SDR r = new SDR(length);
		for (int i = 0; i < length; i++) {
			Integer onBit = (Integer) i;
			if (!onBits.contains(onBit)) {
				r.onBits.add(onBit);
			}
		}
		return r;
	}
	
	private SDR concat(SDR c) {
		SDR r = copy();
		r.length = r.length + c.length;
		for (Integer onBit: c.onBits) {
			r.onBits.add(new Integer(onBit + length));
		}
		return r;
	}
}
