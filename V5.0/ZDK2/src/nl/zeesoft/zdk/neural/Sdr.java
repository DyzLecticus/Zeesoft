package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class Sdr {
	public int				length	= 1;
	public List<Integer>	onBits	= new ArrayList<Integer>();
	
	public Sdr() {
		
	}
	
	public Sdr(int length) {
		if (length>0) {
			this.length = length;
		}
	}
	
	public Sdr copy() {
		Sdr r = new Sdr();
		r.copyFrom(this);
		return r;
	}
	
	public void setBit(Integer bit, boolean value) {
		if (value) {
			if (!onBits.contains(bit) && bit < length) {
				onBits.add(bit);
			}
		} else {
			if (onBits.contains(bit)) {
				onBits.remove(bit);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(length);
		for (Integer onBit: onBits) {
			sb.append(",");
			sb.append(onBit);
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof Sdr) {
			Sdr sdr = (Sdr) other;
			if (sdr.length == length) {
				if (sdr.onBits.size()==onBits.size() && getOverlap(sdr)==onBits.size()) {
					r = true;
				}
			}
		}
		return r;
	}
	
	public int getOverlap(Sdr sdr) {
		int r = 0;
		for (Integer onBit: onBits) {
			if (sdr.onBits.contains(onBit)) {
				r++;
			}
		}
		return r;
	}
	
	public List<Position> getOnPositions(Size size) {
		List<Position> r = new ArrayList<Position>();
		for (Integer onBit: onBits) {
			r.add(size.getPositionForIndex(onBit));
		}
		return r;
	}
	
	public void setOnPositions(Size size, List<Position> positions) {
		for (Position position: positions) {
			setBit(size.getIndexForPosition(position),true);
		}
	}
	
	public void or(Sdr sdr) {
		for (Integer onBit: sdr.onBits) {
			setBit(onBit,true);
		}
	}
	
	public void concat(Sdr sdr,int offset) {
		for (Integer onBit: sdr.onBits) {
			setBit(offset + onBit,true);
		}
	}
	
	public void subsample(int maxOnBits) {
		Rand.subsampleList(onBits, maxOnBits);
	}
	
	protected void copyFrom(Sdr other) {
		length = other.length;
		for (Integer onBit: other.onBits) {
			onBits.add(onBit);
		}
	}
}
