package nl.zeesoft.zdk.grid;

import java.util.ArrayList;
import java.util.List;

public class Position {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public Position() {
		
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = y;
	}
	
	public static List<Position> getColumnPositions(int x, int y, int sizeZ) {
		List<Position> r = new ArrayList<Position>();
		for (int z = 0; z < sizeZ; z++) {
			r.add(new Position(x, y, z));
		}
		return r;
	}
}
