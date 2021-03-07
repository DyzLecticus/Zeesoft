package nl.zeesoft.zdk.test.matrix;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class TestSize {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Size size = new Size(0,0);
		assert size.x == 1;
		assert size.y == 1;
		assert size.z == 1;

		size = new Size(2,3);
		assert size.x == 2;
		assert size.y == 3;
		assert size.z == 1;
		
		size = new Size(0,0,0);
		assert size.x == 1;
		assert size.y == 1;
		assert size.z == 1;

		size = new Size(2,3,4);
		assert size.x == 2;
		assert size.y == 3;
		assert size.z == 4;

		size = new Size(2,3);
		assert size.getIndexForPosition(new Position(0,0)) == 0;
		assert size.getIndexForPosition(new Position(1,0)) == 1;
		assert size.getIndexForPosition(new Position(0,1)) == 2;
		assert size.getIndexForPosition(new Position(1,1)) == 3;
		assert size.getIndexForPosition(new Position(0,2)) == 4;
		assert size.getIndexForPosition(new Position(1,2)) == 5;
		
		assert size.getPositionForIndex(0).equals(new Position(0,0));
		assert size.getPositionForIndex(1).equals(new Position(1,0));
		assert size.getPositionForIndex(2).equals(new Position(0,1));
		assert size.getPositionForIndex(3).equals(new Position(1,1));
		assert size.getPositionForIndex(4).equals(new Position(0,2));
		assert size.getPositionForIndex(5).equals(new Position(1,2));

		size = new Size(2,3,4);
		assert size.getIndexForPosition(new Position(0,0,0)) == 0;
		assert size.getIndexForPosition(new Position(0,0,1)) == 1;
		assert size.getIndexForPosition(new Position(0,0,2)) == 2;
		assert size.getIndexForPosition(new Position(0,0,3)) == 3;
		assert size.getIndexForPosition(new Position(1,0,0)) == 4;
		assert size.getIndexForPosition(new Position(1,0,1)) == 5;
		assert size.getIndexForPosition(new Position(1,0,2)) == 6;
		assert size.getIndexForPosition(new Position(1,0,3)) == 7;
		assert size.getIndexForPosition(new Position(0,1,0)) == 8;
		assert size.getIndexForPosition(new Position(0,1,1)) == 9;
		assert size.getIndexForPosition(new Position(0,1,2)) == 10;
		assert size.getIndexForPosition(new Position(0,1,3)) == 11;
		assert size.getIndexForPosition(new Position(1,1,0)) == 12;

		assert size.getIndexForPosition(new Position(1,2,3)) == 23;
		assert size.getIndexForPosition(new Position(9,9,9)) == 117;

		assert size.getPositionForIndex(0).equals(new Position(0,0,0));
		assert size.getPositionForIndex(1).equals(new Position(0,0,1));
		assert size.getPositionForIndex(2).equals(new Position(0,0,2));
		assert size.getPositionForIndex(3).equals(new Position(0,0,3));
		assert size.getPositionForIndex(4).equals(new Position(1,0,0));
		assert size.getPositionForIndex(5).equals(new Position(1,0,1));
		assert size.getPositionForIndex(6).equals(new Position(1,0,2));
		assert size.getPositionForIndex(7).equals(new Position(1,0,3));
		assert size.getPositionForIndex(8).equals(new Position(0,1,0));
		assert size.getPositionForIndex(9).equals(new Position(0,1,1));
		assert size.getPositionForIndex(10).equals(new Position(0,1,2));
		assert size.getPositionForIndex(11).equals(new Position(0,1,3));
		assert size.getPositionForIndex(12).equals(new Position(1,1,0));
		
		assert size.getPositionForIndex(23).equals(new Position(1,2,3));
		assert size.getPositionForIndex(999).equals(new Position(1,124,3));
	}
}
