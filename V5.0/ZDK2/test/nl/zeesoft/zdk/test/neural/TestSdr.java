package nl.zeesoft.zdk.test.neural;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;

public class TestSdr {
	private static TestSdr	self	= new TestSdr();
	
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Sdr sdr = new Sdr();
		assert sdr.length == 1;
		sdr.setBit(0,true);
		sdr.setBit(10,true);
		assert sdr.onBits.size() == 1;
		assert sdr.onBits.contains(0);
		assert sdr.toString().equals("1,0");
		assert sdr.copy().toString().equals("1,0");
		
		sdr.length = 9;
		sdr.setBit(3,true);
		sdr.setBit(3,true);
		assert sdr.onBits.size() == 2;
		assert sdr.onBits.contains(3);
		assert sdr.toString().equals("9,0,3");
		
		List<Position> positions = sdr.getOnPositions(new Size(3,3));
		assert positions.size() == 2;
		assert positions.get(0).equals(new Position(0,0));
		assert positions.get(1).equals(new Position(0,1));
		
		sdr.setBit(0,false);
		sdr.setBit(0,false);
		assert sdr.onBits.size() == 1;
		assert sdr.onBits.get(0) == 3;
		assert sdr.toString().equals("9,3");
		
		Sdr sdr2 = new Sdr(0);
		assert sdr2.length == 1;
		sdr2 = new Sdr(1);
		assert sdr2.equals(null) == false;
		assert sdr2.equals(self) == false;
		assert sdr2.equals(sdr) == false;
		sdr2.length = 9;
		assert sdr2.equals(sdr) == false;
		sdr2.setBit(3, true);
		assert sdr2.equals(sdr) == true;
		sdr2.setBit(6, true);
		assert sdr2.equals(sdr) == false;
		sdr.setBit(0, true);
		assert sdr2.equals(sdr) == false;
		
		sdr2 = new Sdr(9);
		sdr2.setOnPositions(new Size(3,3), positions);
		assert sdr2.toString().equals("9,0,3");		
	}
}
