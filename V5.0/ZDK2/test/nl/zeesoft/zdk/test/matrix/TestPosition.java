package nl.zeesoft.zdk.test.matrix;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;

public class TestPosition {
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Position position1 = new Position(1,2);
		assert position1.x == 1;
		assert position1.y == 2;
		assert position1.z == 0;
		assert position1.toString().equals("1,2,0");

		Position position2 = new Position(1,2,3);
		assert position2.x == 1;
		assert position2.y == 2;
		assert position2.z == 3;

		assert position2.equals(null) == false;
		assert position2.equals(new String()) == false;
		assert position2.equals(position1) == false;
		assert position2.equals(new Position(0,2,3)) == false;
		assert position2.equals(new Position(1,0,3)) == false;
		assert position2.equals(new Position(1,2,3)) == true;
		
		assert position1.getDistance(position1) == 0;
		assert position1.getDistance(position2) == 3;
		
		Position position = new Position(2,2,2);
		assert position.getDistance(new Position(2,2,0)) == 2;
		assert position.getDistance(new Position(5,2,2)) == 3;
		assert position.getDistance(new Position(2,6,2)) == 4;
		assert position.getDistance(new Position(5,6,2)) == 5;
		assert position.getDistance(new Position(5,6,14)) == 13;
	}
}
