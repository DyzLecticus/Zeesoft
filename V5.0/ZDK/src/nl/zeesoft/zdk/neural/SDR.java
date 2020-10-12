package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.Position;

public class SDR implements StrAble {
	protected int			sizeX	= 1;
	protected int			sizeY	= 1;
	protected List<Integer>	onBits	= new ArrayList<Integer>();
	
	public SDR() {
		
	}
	
	public SDR(int sizeX, int sizeY) {
		setDimensions(sizeX, sizeY);
	}
	
	public void setDimensions(int sizeX, int sizeY) {
		if (sizeX < 1) {
			sizeX = 1;
		}
		if (sizeY < 1) {
			sizeY = 1;
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public void flatten() {
		sizeX = sizeX * sizeY;
		sizeY = 1;
	}
	
	public void square() {
		int oriSizeX = sizeX;
		if (sizeY==1) {
			sizeX = (int) Math.sqrt(sizeX);
			sizeY = sizeX;
			if (sizeX * sizeY < oriSizeX) {
				sizeX++;
			}
			if (sizeX * sizeY < oriSizeX) {
				sizeY++;
			}
		}
	}

	public int sizeX() {
		return sizeX;
	}

	public int sizeY() {
		return sizeY;
	}
	
	public boolean getBit(Integer bit) {
		return onBits.contains(bit);
	}
	
	public boolean getBit(int posX, int posY) {
		return getBit(getBitIndexForPosition(posX, posY));
	}
	
	public void setBit(Integer bit, boolean value) {
		if (value) {
			if (!onBits.contains(bit)) {
				onBits.add(bit);
			}
		} else {
			if (onBits.contains(bit)) {
				onBits.remove(bit);
			}
		}
	}
	
	public void setBit(int posX, int posY, boolean value) {
		setBit(getBitIndexForPosition(posX, posY), value);
	}
	
	public List<Integer> getOnBits() {
		return new ArrayList<Integer>(onBits);
	}
	
	public int onBits() {
		return onBits.size();
	}
	
	public void clear() {
		onBits.clear();
	}
	
	@Override
	public String toString() {
		return toStr().toString();
	}
	
	@Override
	public Str toStr() {
		Str r = new Str();
		for (Integer onBit: onBits) {
			if (r.length()>0) {
				r.sb().append(",");
			}
			r.sb().append(onBit);
		}
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeY);
		r.sb().insert(0,";");
		r.sb().insert(0,"" + sizeX);
		return r;
	}
	
	@Override
	public void fromStr(Str str) {
		onBits.clear();
		List<Str> split = str.split(";");
		if (split.size()>2) {
			sizeX = Integer.parseInt(split.get(0).toString());
			sizeY = Integer.parseInt(split.get(1).toString());
			split = split.get(2).split(",");
			for (Str ob: split) {
				onBits.add(Integer.parseInt(ob.toString()));
			}
		}
	}

	public List<Position> toPositions() {
		List<Position> r = new ArrayList<Position>();
		if (sizeY==1) {
			for (Integer onBit: onBits) {
				r.add(new Position(onBit, 0, 0));
			}
		} else {
			Integer onBit = 0;
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					if (onBits.contains(onBit)) {
						r.add(new Position(x, y, 0));
					}
					onBit++;
				}
			}
		}
		return r;
	}
	
	public void fromPositions(List<Position> positions) {
		onBits.clear();
		for (Position pos: positions) {
			setBit(pos.x, pos.y, true);
		}
	}
	
	public int getOverlap(SDR sdr) {
		int r = 0;
		for (Integer onBit: onBits) {
			if (sdr.onBits.contains(onBit)) {
				r++;
			}
		}
		return r;
	}
	
	public Str toVisualStr() {
		Str r = new Str();
		Integer onBit = 0;
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				if (onBits.contains(onBit)) {
					r.sb().append("X");
				} else {
					r.sb().append("_");
				}
				onBit++;
			}
			r.sb().append("\n");
		}
		return r;
	}
	
	protected int getBitIndexForPosition(int posX, int posY) {
		int r = posX;
		if (posY > 0) {
			r += posY * sizeX;
		}
		return r;
	}
}
