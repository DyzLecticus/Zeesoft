package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

public class SDRHistory {
	protected int			sizeX	= 1;
	protected int			sizeY	= 1;
	protected int			sizeZ	= 1000;
	protected int[]			totals	= null;
	protected List<SDR>		history = new ArrayList<SDR>();
	
	public SDRHistory() {
		
	}
	
	public SDRHistory(int sizeX, int sizeY) {
		setDimensions(sizeX, sizeY, sizeZ);
	}
	
	public SDRHistory(int sizeX, int sizeY, int sizeZ) {
		setDimensions(sizeX, sizeY, sizeZ);
	}

	public void setDimensions(int sizeX, int sizeY, int sizeZ) {
		if (sizeX < 1) {
			sizeX = 1;
		}
		if (sizeY < 1) {
			sizeY = 1;
		}
		if (sizeZ < 1) {
			sizeZ = 1;
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.totals = new int[sizeX * sizeY];
		for (int i = 0; i < sizeX * sizeY; i++) {
			totals[i] = 0;
		}
	}
	
	public void addSDR(SDR sdr) {
		if (sdr.sizeX==sizeX && sdr.sizeY==sizeY) {
			history.add(sdr);
			for (Integer onBit: sdr.onBits) {
				totals[onBit]++;
			}
			if (history.size()>sizeZ) {
				int r = history.size() - sizeZ;
				for (int i = 0; i < r; i++) {
					SDR removed = history.remove(history.size() - 1); 
					for (Integer onBit: removed.onBits) {
						totals[onBit]--;
					}
				}
			}
		} else {
			Str msg = new Str("SDR dimensions do not match expectation: ");
			msg.sb().append(sdr.sizeX);
			msg.sb().append("*");
			msg.sb().append(sdr.sizeY);
			msg.sb().append(" <> ");
			msg.sb().append(sizeX);
			msg.sb().append("*");
			msg.sb().append(sizeY);
			Logger.err(this, msg);
		}
	}
	
	public float getAverage(Integer bit) {
		float r = 0;
		if (history.size()>0) {
			r = totals[bit] / (float) history.size();
		}
		return r;
	}
	
	public float getTotalAverage() {
		float r = 0;
		if (history.size()>0) {
			for (int i = 0; i < totals.length; i++) {
				r += totals[i];
			}
			if (r>0) {
				r = r / (float) (totals.length * history.size());
			}
		}
		return r;
	}
	
	public void clear() {
		history.clear();
		for (int i = 0; i < sizeX * sizeY; i++) {
			totals[i] = 0;
		}
	}
}
