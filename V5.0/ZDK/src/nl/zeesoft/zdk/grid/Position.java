package nl.zeesoft.zdk.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;

public class Position {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public Position() {
		
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof Position) {
			Position pos = (Position) other;
			r = pos.x == x && pos.y == y && pos.z == z;
		}
		return r;
	}
	
	public static List<Position> getColumnPositions(int x, int y, int sizeZ) {
		List<Position> r = new ArrayList<Position>();
		for (int z = 0; z < sizeZ; z++) {
			r.add(new Position(x, y, z));
		}
		return r;
	}
	
	public static boolean posXIsInList(int x, List<Position> positions) {
		boolean r = false;
		for (Position pos: positions) {
			if (pos.x==x) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public static boolean posYIsInList(int y, List<Position> positions) {
		boolean r = false;
		for (Position pos: positions) {
			if (pos.y==y) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public static boolean posXYIsInList(int x, int y, List<Position> positions) {
		boolean r = false;
		for (Position pos: positions) {
			if (pos.x==x && pos.y==y) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public static boolean posIsInList(int x, int y, int z, List<Position> positions) {
		boolean r = false;
		for (Position pos: positions) {
			if (pos.x==x && pos.y==y && pos.z==z) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public static void randomizeList(List<Position> positions) {
		List<Position> temp = new ArrayList<Position>(positions);
		int size = positions.size();
		positions.clear();
		for (int p = 0; p < size; p++) {
			positions.add(temp.remove(Rand.getRandomInt(0, temp.size() - 1)));
		}
	}
}
