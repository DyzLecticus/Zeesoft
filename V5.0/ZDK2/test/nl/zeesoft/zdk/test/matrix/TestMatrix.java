package nl.zeesoft.zdk.test.matrix;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class TestMatrix {
	private static TestMatrix	self	= new TestMatrix();
	
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Size size = new Size(2,3,4);
		Position position1 = new Position(1,2);
		Position position2 = new Position(1,2,3);
		
		Matrix matrix = new Matrix();
		assert matrix.equals(matrix);
		
		// Test not initialized
		assert matrix.volume() == 0;
		matrix.setValue(0,self);
		
		// Test happy flow
		matrix.initialize(size);
		assert matrix.equals(matrix);
		assert matrix.volume() == 24;
		matrix.setValue(self,0);
		assert matrix.getValue(position1) != null;
		assert matrix.getValue(position1) instanceof Integer;
		assert (int)matrix.getValue(position1) == 0;

		// Test custom function
		matrix.setValue(position2,1);
		Function function = new Function() {
			@Override
			protected Object exec() {
				assert param1 != null;
				assert param1 instanceof Position;
				int value = (int) param2;
				return value + 1;
			}
		};
		matrix.applyFunction(self,function);
		assert matrix.getValue(position1) != null;
		assert matrix.getValue(position1) instanceof Integer;
		assert (int)matrix.getValue(position1) == 1;
		assert matrix.getValue(position2) != null;
		assert matrix.getValue(position2) instanceof Integer;
		assert (int)matrix.getValue(position2) == 2;
		
		matrix.data[0][1][1] = null;
		matrix.data[1][1][1] = position2;
		List<Position> positions = matrix.getPositionsForValue(self,null);
		assert positions.size() == 1;
		assert positions.get(0).equals(new Position(0,1,1));
		positions = matrix.getPositionsForValue(self,position2);
		assert positions.size() == 1;
		assert positions.get(0).equals(new Position(1,1,1));
		positions = matrix.getPositionsForValue(self,1);
		assert positions.size() == 21;
		positions = matrix.getPositionsForValue(self,2);
		assert positions.size() == 1;
		assert positions.get(0).equals(position2);

		Matrix matrix2 = (new Matrix()).copy(self);
		assert matrix2.size == null;
		assert matrix2.equals(new Matrix());
		assert !matrix2.equals(matrix);
		assert !matrix2.equals(null);
		assert !matrix2.equals(self);
		matrix2.copyDataFrom(self, new Matrix());
		matrix2.copyDataFrom(self, matrix);
		
		matrix2.initialize(new Size(1,1));
		matrix2.copyDataFrom(self, new Matrix());
		matrix2.copyDataFrom(self, matrix);
		assert matrix2.data[0][0][0] == null;
		assert !matrix2.equals(new Matrix());
		assert !matrix2.equals(matrix);

		matrix2.initialize(matrix.size);
		assert !matrix2.equals(matrix);
		matrix2.copyDataFrom(self, matrix);
		assert matrix2.data[0][0][0] != null;
		assert matrix2.data[0][0][0] == matrix.data[0][0][0];
		assert matrix2.data[1][0][0] == matrix.data[1][0][0];
		assert matrix2.equals(matrix);
		matrix2.data[1][0][0] = null;
		assert !matrix2.equals(matrix);
		
		matrix2 = matrix.copy(self);
		assert matrix2.equals(matrix);
	}
}
