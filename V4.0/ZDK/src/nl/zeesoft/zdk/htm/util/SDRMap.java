package nl.zeesoft.zdk.htm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;

public class SDRMap {
	private int												length				= 0;
	private int												bits				= 0;
	private boolean											useIndex			= true;
	
	private List<SDRMapElement>								elements			= new ArrayList<SDRMapElement>();
	
	private SortedMap<Integer,List<SDRMapElement>>			elementsByOnBits	= new TreeMap<Integer,List<SDRMapElement>>();
	
	public SDRMap(int length) {
		initialize(length,length,useIndex);
	}
	
	public SDRMap(int length,int bits) {
		initialize(length,bits,useIndex);
	}
	
	public SDRMap(int length,boolean useIndex) {
		initialize(length,length,useIndex);
	}
	
	public SDRMap(int length,int bits,boolean useIndex) {
		initialize(length,bits,useIndex);
	}

	public SDRMapElement add(SDR sdr) {
		return add(sdr,null);
	}

	public SDRMapElement add(SDR sdr,Object value) {
		SDRMapElement r = null;
		if (sdr.onBits()>bits) {
			sdr.subsample(bits);
		}
		if (sdr.length()==length) {
			SDRMapElement element = new SDRMapElement();
			element.key = sdr;
			element.value = value;
			elements.add(element);
			addToIndex(element);
		}
		return r;
	}

	public int size() {
		return elements.size();
	}
	
	public int length() {
		return length;
	}
	
	public int bits() {
		return bits;
	}

	public int indexOf(SDRMapElement element) {
		return elements.indexOf(element);
	}

	public void toLast(SDRMapElement element) {
		int index = elements.indexOf(element);
		if (index>=0 && index<elements.size() - 1) {
			elements.remove(index);
			elements.add(element);
		}
	}

	public SDRMapElement get(int index) {
		SDRMapElement r = null;
		if (index>=0 && index<elements.size()) {
			r = elements.get(index);
		}
		return r;
	}

	public SDR getSDR(int index) {
		SDR r = null;
		if (index>=0 && index<elements.size()) {
			r = elements.get(index).key;
		}
		return r;
	}

	public DateTimeSDR getDateTimeSDR(int index) {
		DateTimeSDR r = null;
		if (index>=0 && index<elements.size()) {
			SDR	sdr = elements.get(index).key;
			if (sdr instanceof DateTimeSDR) {
				r = (DateTimeSDR) sdr;
			}
		}
		return r;
	}
	
	public Object getValue(int index) {
		Object r = null;
		if (index>=0 && index<elements.size()) {
			r = elements.get(index).value;
		}
		return r;
	}
	
	public void setValue(int index,Object value) {
		if (index>=0 && index<elements.size()) {
			elements.get(index).value = value;
		}
	}
	
	public void setBit(int index,int bitIndex,boolean on) {
		if (index>=0 && index<elements.size()) {
			SDRMapElement element = elements.get(index);
			boolean flipped = element.key.setBit(bitIndex,on);
			if (flipped && useIndex) {
				List<SDRMapElement> list = elementsByOnBits.get(bitIndex);
				if (list==null) {
					list = new ArrayList<SDRMapElement>();
					elementsByOnBits.put(bitIndex,list);
				}
				if (on) {
					if (!list.contains(element)) {
						list.add(element);
					}
				} else {
					list.remove(element);
				}
			}
		}
	}
	
	public SDRMapElement remove(int index) {
		SDRMapElement r = null;
		if (index>=0 && index<elements.size()) {
			r = elements.remove(index);
			if (r!=null) {
				removeFromIndex(r);
			}
		}
		return r;
	}

	public void clear() {
		elements.clear();
		elementsByOnBits.clear();
	}

	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("" + length);
		r.append(",");
		r.append("" + bits);
		for (SDRMapElement element: elements) {
			r.append("|");
			int added = 0;
			for (Integer onBit: element.key.getOnBits()) {
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
		clear();
		List<ZStringBuilder> elems = str.split("|");
		if (elems.size()>=1) {
			List<ZStringBuilder> vals = elems.get(0).split(",");
			length = Integer.parseInt(vals.get(0).toString());
			bits = Integer.parseInt(vals.get(1).toString());
			for (int i = 1; i<elems.size(); i++) {
				SDR sdr = new SDR(length);
				List<ZStringBuilder> onBits = elems.get(i).split(",");
				for (ZStringBuilder onBit: onBits) {
					sdr.setBit(Integer.parseInt(onBit.toString()),true);
				}
				add(sdr,null);
			}
		}
	}
	
	public SDRMapElement getRandomClosestMatch(SDR sdr) {
		return getRandomClosestMatch(sdr,bits / 4);
	}
	
	public SDRMapElement getRandomClosestMatch(SDR sdr,int minOverlap) {
		SDRMapElement r = null;
		List<SDRMapElement> elements = getClosestMatches(sdr,minOverlap,1);
		if (elements.size()>0) {
			r = elements.get(0);
		}
		return r;
	}
	
	public List<SDRMapElement> getClosestMatches(SDR sdr,int matchDepth) {
		return getClosestMatches(sdr,bits / 4,matchDepth);
	}
	
	public List<SDRMapElement> getClosestMatches(SDR sdr,int minOverlap,int matchDepth) {
		List<SDRMapElement> r = new ArrayList<SDRMapElement>();
		if (matchDepth < 1) {
			matchDepth = 1;
		}
		SDRMapElement first = null;
		SortedMap<Integer,List<SDRMapElement>> matches = getMatches(sdr,minOverlap);
		if (matches.size()>0) {
			for (int i = 0; i < matchDepth; i++) {
				if (matches.size()>0) {
					List<SDRMapElement> list = matches.remove(matches.lastKey());
					if (first==null) {
						if (list.size()==1) {
							first = list.remove(0);
						} else {
							first = list.remove(ZRandomize.getRandomInt(0,list.size() - 1));
						}
					}
					for (SDRMapElement elem: list) {
						r.add(elem);
					}
				} else {
					break;
				}
			}
		}
		if (first!=null) {
			r.add(0,first);
		}
		return r;
	}

	public SortedMap<Integer,List<SDRMapElement>> getMatches(SDR sdr) {
		return getMatches(sdr,bits / 4);
	}
	
	public SortedMap<Integer,List<SDRMapElement>> getMatches(SDR sdr, int minOverlap) {
		SortedMap<Integer,List<SDRMapElement>> r = new TreeMap<Integer,List<SDRMapElement>>();
		if (useIndex) {
			r = getMatchesUseIndex(sdr,minOverlap);
		} else {
			r = new TreeMap<Integer,List<SDRMapElement>>();
			if (minOverlap	< 1) {
				minOverlap = 1;
			} else if (minOverlap >= bits) {
				minOverlap = bits - 1;
			}
			if (sdr.onBits()>=minOverlap && sdr.length()==length && checkUnionOverlap(sdr,minOverlap)) {
				for (SDRMapElement element: elements) {
					int overlap = 0;
					if (sdr.onBits()>=element.key.onBits()) {
						overlap = element.key.getOverlapScore(sdr);
					} else {
						overlap = sdr.getOverlapScore(element.key);
					}
					if (overlap>=minOverlap) {
						List<SDRMapElement> list = r.get(overlap);
						if (list==null) {
							list = new ArrayList<SDRMapElement>();
							r.put(overlap,list);
						}
						list.add(element);
					}
				}
			}
		}
		return r;
	}
	
	public SDR getUnion() {
		SDR r = new SDR(length);
		if (useIndex) {
			for (Integer onBit: elementsByOnBits.keySet()) {
				r.setBit(onBit,true);
			}
		} else {
			for (SDRMapElement element: elements) {
				r = SDR.or(r,element.key);
			}
		}
		return r;
	}
	
	private boolean checkUnionOverlap(SDR sdr,int minOverlap) {
		return sdr.getOverlapScore(getUnion()) >= minOverlap;
	}
	
	private SortedMap<Integer,List<SDRMapElement>> getMatchesUseIndex(SDR sdr, int minOverlap) {
		SortedMap<Integer,List<SDRMapElement>> r = new TreeMap<Integer,List<SDRMapElement>>();
		if (minOverlap	< 1) {
			minOverlap = 1;
		} else if (minOverlap >= bits) {
			minOverlap = bits - 1;
		}
		if (useIndex && sdr.onBits()>=minOverlap && sdr.length()==length && checkUnionOverlap(sdr,minOverlap)) {
			Set<SDRMapElement> done = new HashSet<SDRMapElement>(); 
			for (Integer onBit: sdr.getOnBits()) {
				List<SDRMapElement> bitList = elementsByOnBits.get(onBit);
				if (bitList!=null) {
					for (SDRMapElement element: bitList) {
						if (!done.contains(element)) {
							done.add(element);
							int overlap = 0;
							if (sdr.onBits()>=element.key.onBits()) {
								overlap = element.key.getOverlapScore(sdr);
							} else {
								overlap = sdr.getOverlapScore(element.key);
							}
							if (overlap>=minOverlap) {
								List<SDRMapElement> list = r.get(overlap);
								if (list==null) {
									list = new ArrayList<SDRMapElement>();
									r.put(overlap,list);
								}
								list.add(element);
							}
						}
					}
				}
			}
		}
		return r;
	}
	
	private void addToIndex(SDRMapElement element) {
		if (useIndex) {
			for (Integer onBit: element.key.getOnBits()) {
				List<SDRMapElement> list = elementsByOnBits.get(onBit);
				if (list==null) {
					list = new ArrayList<SDRMapElement>();
					elementsByOnBits.put(onBit, list);
				}
				list.add(element);
			}
		}
	}
	
	private void removeFromIndex(SDRMapElement element) {
		if (useIndex) {
			for (Integer onBit: element.key.getOnBits()) {
				List<SDRMapElement> list = elementsByOnBits.get(onBit);
				if (list!=null) {
					list.remove(element);
					if (list.size()==0) {
						elementsByOnBits.remove(onBit);
					}
				}
			}
		}
	}
	
	private void initialize(int length,int bits, boolean useIndex) {
		if (length < 10) {
			length = 10;
		}
		if (bits < 1) {
			bits = 1;
		} else if (bits > length) {
			bits = length;
		}
		this.length = length;
		this.bits = bits;
		this.useIndex = useIndex;
	}
}
