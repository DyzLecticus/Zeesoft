package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
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
		
		Sdr sdr3 = new Sdr(9);
		sdr3.concat(sdr, 0);
		assert sdr3.equals(sdr);
		sdr3.concat(sdr, 9);
		assert sdr3.equals(sdr);
		
		sdr3.or(sdr2);
		assert sdr3.onBits.size() == 2;
		
		Sdr sdr4 = new Sdr(18);
		List<Sdr> sdrs = new ArrayList<Sdr>();
		sdr4.concat(sdrs);
		assert sdr4.onBits.size() == 0;
		sdrs.add(sdr2);
		sdrs.add(sdr3);
		sdrs.add(sdr);
		sdr4.concat(sdrs);
		assert sdr4.onBits.size() == 4;

		sdr3.subsample(1);
		assert sdr3.onBits.size() == 1;
		sdr3.subsample(0);
		assert sdr3.onBits.size() == 0;
		
		sdr3 = sdr2.copy();
		sdr3.distort(0F);
		assert sdr3.equals(sdr3);
		sdr3.distort(0.01F);
		assert sdr3.equals(sdr3);
		sdr3.distort(0.5F);
		assert sdr3.getOverlap(sdr2) == 1;
		sdr3 = sdr2.copy();
		sdr3.distort(2F);
		assert sdr3.getOverlap(sdr2) == 0;
	}
}
