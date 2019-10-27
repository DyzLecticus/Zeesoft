package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.enc.GridDimensionEncoder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
import nl.zeesoft.zdk.htm.enc.GridDimensionScaledEncoder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestGridEncoder extends TestObject {
	public TestGridEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGridEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *GridEncoder* to convert a range of multidimensional positions into sparse distributed representations.");
		System.out.println("The *GridEncoder* class provides static helper methods to create different type of 2D or 3D encoders.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("GridEncoder enc = GridEncoder.getNew2DGridEncoder(length,bits,sizeX,sizeY);");
		System.out.println("// Obtain the SDR for a certain position");
		System.out.println("SDR sdr = enc.getSDRForPosition(0,0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGridEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(GridEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows different grid encoders and the SDRs they generate for several positions.");
	}
	
	@Override
	protected void test(String[] args) {
		testGridDimensionEncoder(32,4,224,true,true);
		testGridDimensionEncoder(48,6,1663,true,true);
		testGridDimensionEncoder(64,8,6480,true,false);
		testGridDimensionEncoder(128,16,76832,true,false);
		testGridEncoder(128,16,20,60,false,false);
		
		testGridDimensionScaledEncoder(48,true);
		testGridDimensionScaledEncoder(64,true);
		testGridDimensionScaledEncoder(96,true);
		testGridDimensionScaledEncoder(128,true);

		System.out.println();
		testGridEncoder(128,16,20,60,true,false);

		System.out.println();
		testGridEncoder(512,64,20,60,true,false);
	}

	private void testGridDimensionEncoder(int length, int bits, int expectedCapacity, boolean silent, boolean checkUnique) {
		GridDimensionEncoder enc = new GridDimensionEncoder(length,bits);
		enc.setResolution(0.1F);
		int capacity = enc.getCapacity();
		if (!silent) {
			System.out.println("Grid dimension encoder capacity: " + capacity);
		}
		assertEqual(capacity,expectedCapacity,"Grid encoder capacity does not match expectation");

		SDR sdr = null;
		SDR sdrC = null;
		
		SDRMap sdrMap = new SDRMap(enc.length(),enc.bits());
		for (int i = 0; i < enc.getCapacity(); i++) {
			float v = (float)i * 0.1F;
			sdr = enc.getSDRForValue(v);
			if (!silent && i < 1000) {
				System.out.println(sdr.toBitString());
			}
			
			if (!assertEqual(sdr.onBits(),enc.bits(),"SDR on bits does not match expectation")) {
				break;
			}
			
			if (checkUnique) {
				boolean unique = sdrMap.getMatches(sdr,enc.bits()).size()==0;
				if (!assertEqual(unique,true,"SDR for value " + v + " is not unique")) {
					break;
				}
				sdrMap.add(sdr);
			}
			
			if (sdrC!=null) {
				int overlap = sdr.getOverlapScore(sdrC);
				int minOverlap = (enc.bits() / 8 * 5);
				if (!assertEqual(overlap<enc.bits(),true,"SDR overlap score does not match expectation")) {
					System.err.println("Bucket: " + i + ", overlap: " + overlap);
					break;
				} else if (!assertEqual(overlap>=minOverlap,true,"SDR overlap score does not match expectation")) {
					System.err.println("Bucket: " + i + ", overlap: " + overlap + " (min: " + minOverlap + ")");
					break;
				}
			}
			sdrC = sdr;
		}
	}

	private void testGridEncoder(int length, int bits, int sizeX, int sizeY,boolean scaled, boolean silent) {
		GridEncoder enc = null;
		if (scaled) {
			enc = GridEncoder.getNewScaled2DGridEncoder(length,sizeX,sizeY);
		} else {
			enc = GridEncoder.getNew2DGridEncoder(length,bits,sizeX,sizeY);
		}
		if (!silent) {
			System.out.println(enc.getDescription());
			System.out.println();
		}
		
		SDRMap sdrMap = new SDRMap(enc.length(),enc.bits());
		boolean err = false;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				SDR sdr = enc.getSDRForPosition(x,y);
				String pos = String.format("%02d",x) + "," + String.format("%02d",y);
				
				if (!assertEqual(sdr.onBits(),enc.bits(),"SDR on bits does not match expectation")) {
					break;
				}
				
				boolean unique = sdrMap.getMatches(sdr,enc.bits()).size()==0;
				if (!assertEqual(unique,true,"SDR for position " + pos + " is not unique")) {
					err = true;
					break;
				}
				sdrMap.add(sdr);
				
				if (!silent && x==y) {
					System.out.println("SDR for position " + pos + ": " + sdr.toBitString());
				}
			}
			if (err) {
				break;
			}
		}
	}
	
	private void testGridDimensionScaledEncoder(int length,boolean silent) {
		GridDimensionScaledEncoder enc = new GridDimensionScaledEncoder(length);
		enc.setResolution(0.1F);

		SDR sdr1 = null;
		SDR sdr = null;
		SDR sdrC = null;
		
		if (!silent) {
			System.out.println(enc.getDescription());
			System.out.println();
		}
		SDRMap sdrMap = new SDRMap(enc.length(),enc.bits());
		for (int i = 0; i < enc.getCapacity(); i++) {
			float v = (float)i * 0.1F;
			sdr = enc.getSDRForValue(v);
			if (sdr1 == null) {
				sdr1 = sdr;
			}
			
			boolean unique = sdrMap.getMatches(sdr,enc.bits()).size()==0;
			if (!assertEqual(unique,true,"SDR for value " + v + " is not unique")) {
				break;
			}
			sdrMap.add(sdr);
			
			int overlap = 0;
			if (sdrC!=null) {
				overlap = sdr.getOverlapScore(sdrC);
				if (!assertEqual(overlap<enc.bits(),true,"SDR overlap score does not match expectation")) {
					break;
				}
			}

			if (!assertEqual(sdr.onBits(),enc.bits(),"SDR on bits does not match expectation")) {
				break;
			}

			if (!silent) {
				System.out.println(sdr.toBitString() + " (Overlap: " + overlap + ")");
			}

			sdrC = sdr;
		}
		if (!silent) {
			System.out.println("SDRs: " + sdrMap.size() + ", overlap last with first: " + sdrC.getOverlapScore(sdr1));
		}
	}
}
