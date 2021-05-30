package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

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
		return ((SdrStringConvertor) ObjectStringConvertors.getConvertor(this.getClass())).toStringBuilder(this).toString();
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
	
	public void concat(List<Sdr> sdrs) {
		int offset = 0;
		for (Sdr sdr: sdrs) {
			concat(sdr, offset);
			offset += sdr.length;
			if (offset>=length) {
				break;
			}
		}
	}
	
	public void subsample(int maxOnBits) {
		Rand.subsampleList(onBits, maxOnBits);
	}

	public void invert() {
		for (int onBit = 0; onBit < length; onBit++) {
			setBit(onBit, !onBits.contains(onBit));
		}
	}

	public void distort(float distortion) {
		if (distortion > 1F) {
			distortion = 1F;
		}
		if (distortion>0.0F) {
			int remove = (int) ((float) onBits.size() * distortion);
			if (remove>0) {
				Sdr inverted = copy();
				inverted.invert();
				subsample(onBits.size() - remove);
				for (int i = 0; i < remove; i++) {
					Integer onBit = (Integer) Rand.selectRandomFromList(inverted.onBits);
					setBit(onBit, true);
				}
			}
		}
	}
	
	protected void copyFrom(Sdr other) {
		length = other.length;
		for (Integer onBit: other.onBits) {
			onBits.add(onBit);
		}
	}
}
