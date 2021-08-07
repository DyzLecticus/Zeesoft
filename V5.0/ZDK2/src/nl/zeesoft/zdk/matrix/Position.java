package nl.zeesoft.zdk.matrix;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.MathUtil;

public class Position extends XYZ implements Comparable<Position> {
	public Position() {
		
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public List<Position> selectPositionsLimitDistance(float maxDistance, List<Position> positions) {
		List<Position> r = new ArrayList<Position>();
		float dist = 0;
		for (Position pos: positions) {
			dist = getDistance(pos);
			if (dist<=maxDistance) {
				r.add(pos);
			}
		}
		return r;
	}

	public boolean columnContains(List<Position> positions) {
		boolean r = false;
		for (Position position: positions) {
			if (position.x==x && position.y==y) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	/**
	 * Assumes all coordinates are >= 0
	 */
	public float getDistance(Position other) {
		float r = 0;
		float len1 = getAbsoluteDistance(other.x, x);
		float len2 = getAbsoluteDistance(other.y, y);
		if (len1==0) {
			r = len2;
		} else if (len2==0) {
			r = len1;
		} else {
			r = MathUtil.getHypotenuse(len1, len2);
		}
		if (other.z!=z) {
			r = addDepthDistance(r, other);
		}
		return r;
	}
	
	public static List<Position> squashTo2D(List<Position> positions) {
		List<Position> r = new ArrayList<Position>();
		for (Position position: positions) {
			Position add = new Position(position.x, position.y);
			if (!r.contains(add)) {
				r.add(add);
			}
		}
		return r;
	}
	
	public static int getAbsoluteDistance(int a, int b) {
		int r = 0;
		if (a > b) {
			r = a - b;
		} else {
			r = b - a;
		}
		return r;
	}
	
	protected float addDepthDistance(float xyDist, Position other) {
		float r = 0;
		float len1 = xyDist;
		float len2 = getAbsoluteDistance(other.z, z);
		if (len1==0) {
			r = len2;
		} else {
			r = MathUtil.getHypotenuse(len1, len2);
		}
		return r;
	}

	@Override
	public int compareTo(Position other) {
		int r = 0;
		if (other.x < this.x) {
			r = 1;
		} else if (other.x > this.x) {
			r = -1;
		} else if (other.y < this.y) {
			r = 1;
		} else if (other.y > this.y) {
			r = -1;
		} else if (other.z < this.z) {
			r = 1;
		} else if (other.z > this.z) {
			r = -1;
		}
		return r;
	}
}
