package nl.zeesoft.zso.composition;

import java.util.ArrayList;
import java.util.List;

public class Bar {
	private List<BarPosition> positions = new ArrayList<BarPosition>();

	public BarPosition addPosition(String positionName) {
		BarPosition pos = new BarPosition();
		pos.setPositionName(positionName);
		positions.add(pos);
		return pos;
	}
	
	public List<BarPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<BarPosition> positions) {
		this.positions = positions;
	}
}
