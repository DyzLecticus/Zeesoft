package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.enc.ScaledGridDimensionEncoder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestScaledGridEncoder extends TestObject {
	public TestScaledGridEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestScaledGridEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe and document
		System.out.println("This test shows how to use a *ScalarEncoder* to convert a range of scalar values into sparse distributed representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("ScalarEncoder enc = new ScalarEncoder(52,2,0,50);");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGridEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(ScalarEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows two scalar encoders and the SDRs they generate for several values.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ScaledGridDimensionEncoder enc = new ScaledGridDimensionEncoder(64);
		enc.setResolution(0.1F);

		SDR sdr1 = null;
		SDR sdr = null;
		SDR sdrC = null;
		
		System.out.println("Scaled grid dimension encoder bits: " + enc.bits());
		SDRMap sdrMap = new SDRMap(64,enc.bits());
		for (int i = 0; i < 1000; i++) {
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

			System.out.println(sdr.toBitString() + " (" + overlap + ")");

			sdrC = sdr;
		}
		System.out.println("SDRs: " + sdrMap.size() + ", overlap last with first: " + sdrC.getOverlapScore(sdr1));
		//testGridEncoder(128,16,20,80,false);
	}
	
	/*
	private void testGridEncoder(int length, int bits, int sizeX, int sizeY, boolean silent) {
		GridEncoder enc = GridEncoder.getNew2DGridEncoder(length,bits,sizeX,sizeY);
		if (!silent) {
			System.out.println("Grid encoder bits: " + enc.bits());
		}
		
		SDRMap sdrMap = new SDRMap(length,enc.bits());
		boolean err = false;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				SDR sdr = enc.getSDRForPosition(x,y);
				String pos = String.format("%02d",x) + "," + String.format("%02d",y);
				assertEqual(sdr.onBits(),enc.bits(),"SDR on bits does not match expectation");
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
	*/
}
