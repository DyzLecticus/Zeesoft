package nl.zeesoft.zdk.test.matrix;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class TestSize {
	private static TestSize	self = new TestSize();
	
	@SuppressWarnings("unlikely-arg-type")
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
		assert size.equals(new Size(2,3));
		assert !size.equals(new Size(1,3));
		assert !size.equals(new Size(2,2));
		assert !size.equals(new Size(2,3,2));
		assert !size.equals(null);
		assert !size.equals(self);
		
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
		
		size = new Size(5,5,5);
		Size size2 = new Size(3,3,3);
		assert size.projectPositionOn(new Position(0,0,0), size2).equals(new Position(0,0,0));
		assert size.projectPositionOn(new Position(2,2,2), size2).equals(new Position(1,1,1));
		assert size.projectPositionOn(new Position(4,4,4), size2).equals(new Position(2,2,2));
		assert size2.projectPositionOn(new Position(0,0,0), size).equals(new Position(0,0,0));
		assert size2.projectPositionOn(new Position(1,1,1), size).equals(new Position(2,2,2));
		assert size2.projectPositionOn(new Position(2,2,2), size).equals(new Position(4,4,4));
		
		size2 = new Size(15,15,15);
		assert size.projectPositionOn(new Position(0,0,0), size2).equals(new Position(0,0,0));
		assert size.projectPositionOn(new Position(1,1,1), size2).equals(new Position(4,4,4));
		assert size.projectPositionOn(new Position(2,2,2), size2).equals(new Position(7,7,7));
		assert size.projectPositionOn(new Position(4,4,4), size2).equals(new Position(14,14,14));
		assert size2.projectPositionOn(new Position(0,0,0), size).equals(new Position(0,0,0));
		assert size2.projectPositionOn(new Position(7,7,7), size).equals(new Position(2,2,2));
		assert size2.projectPositionOn(new Position(14,14,14), size).equals(new Position(4,4,4));

		size2 = new Size(150,150,150);
		assert size.projectPositionOn(new Position(2,2,2), size2).equals(new Position(75,75,75));
		assert size.projectPositionOn(new Position(4,4,4), size2).equals(new Position(149,149,149));
		
		size = new Size(0);
		assert size.equals(new Size(1,1));
		size = new Size(1);
		assert size.equals(new Size(1,1));
		size = new Size(2);
		assert size.equals(new Size(2,1));
		size = new Size(3);
		assert size.equals(new Size(2,2));
		size = new Size(4);
		assert size.equals(new Size(2,2));
		assert size.copy().equals(new Size(2,2));
		assert size.volume() == 4;
	}
}
