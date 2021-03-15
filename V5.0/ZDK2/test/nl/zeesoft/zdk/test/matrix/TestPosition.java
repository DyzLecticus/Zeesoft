package nl.zeesoft.zdk.test.matrix;

import java.util.ArrayList;
import java.util.List;

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
		
		List<Position> positions = new ArrayList<Position>();
		positions.add(position);
		assert positions.contains(new Position(2,2,2));
		
		assert position2.selectPositionsLimitDistance(10, positions).size() == 1;
		assert position2.selectPositionsLimitDistance(1, positions).size() == 0;
		
		assert position.columnContains(positions);
		assert !position2.columnContains(positions);
		assert !(new Position(1,2)).columnContains(positions);
		assert !(new Position(2,1)).columnContains(positions);
		assert (new Position(2,2)).columnContains(positions);
		
		positions.add(position1);
		positions.add(position2);
		List<Position> squashed = Position.squashTo2D(positions);
		assert squashed.size() == 2;
		assert squashed.get(0).equals(new Position(2,2,0));
		assert squashed.get(1).equals(position1);
	}
}
