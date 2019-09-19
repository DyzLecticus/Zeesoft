package nl.zeesoft.zdk.htm.sdr;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.functions.ZRandomize;

public class SDRMap {
	private int												width				= 0;
	private int												bits				= 0;
	
	private List<SDRMapElement>								elements			= new ArrayList<SDRMapElement>();
	
	public SDRMap(int width,int bits) {
		if (width < 10) {
			width = 10;
		}
		if (width < 1) {
			width = 1;
		}
		this.width = width;
		this.bits = bits;
	}
	
	public void addSDR(SDR sdr,Object value) {
		if (sdr.size()==width && sdr.onBits()<=bits && value!=null) {
			SDRMapElement element = new SDRMapElement();
			element.key = sdr;
			element.value = value;
			elements.add(element);
		}
	}

	public int size() {
		return elements.size();
	}
	
	public int width() {
		return width;
	}
	
	public int bits() {
		return bits;
	}

	public SDRMapElement get(int index) {
		SDRMapElement r = null;
		if (index>=0 && index<elements.size()) {
			r = elements.get(index);
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
	
	public Object remove(int index) {
		Object r = null;
		if (index>=0 && index<elements.size()) {
			SDRMapElement elem = elements.remove(index);
			if (elem!=null) {
				r = elem.value;
			}
		}
		return r;
	}

	public SDRMapElement getRandomClosestMatch(SDR sdr) {
		return getRandomClosestMatch(sdr,bits / 4);
	}
	
	public SDRMapElement getRandomClosestMatch(SDR sdr,int minOverlap) {
		SDRMapElement r = null;
		SortedMap<Integer,List<SDRMapElement>> matches = getMatches(sdr,minOverlap);
		if (matches.size()>0) {
			List<SDRMapElement> elements = matches.get(matches.lastKey());
			if (elements.size()==1) {
				r = elements.get(0);
			} else {
				r = elements.get(ZRandomize.getRandomInt(0,elements.size() - 1));
			}
		}
		return r;
	}
	
	public SortedMap<Integer,List<SDRMapElement>> getMatches(SDR sdr, int minOverlap) {
		SortedMap<Integer,List<SDRMapElement>> r = new TreeMap<Integer,List<SDRMapElement>>();
		if (minOverlap	< 1) {
			minOverlap = 1;
		} else if (minOverlap >= bits) {
			minOverlap = bits - 1;
		}
		if (sdr.onBits()>=minOverlap && sdr.size()==width) {
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
		return r;
	}
}
