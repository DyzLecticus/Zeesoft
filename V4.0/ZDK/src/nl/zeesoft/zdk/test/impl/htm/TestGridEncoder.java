package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.enc.GridDimensionEncoder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
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
		GridDimensionEncoder enc2 = new GridDimensionEncoder(64,8);
		enc2.setResolution(0.1F);

		SDR sdr = null;
		SDR sdrC = null;
		
		SDRMap sdrMap = new SDRMap(64,8);
		for (int i = 0; i < 1000; i++) {
			float v = (float)i * 0.1F;
			sdr = enc2.getSDRForValue(v);
			boolean unique = sdrMap.getMatches(sdr,enc2.bits()).size()==0;
			if (!assertEqual(unique,true,"SDR for value " + v + " is not unique")) {
				break;
			}
			sdrMap.add(sdr);
			
			if (sdrC!=null) {
				if (!assertEqual(sdr.getOverlapScore(sdrC)<8,true,"SDR overlap score does not match expectation")) {
					break;
				}
			}
			sdrC = sdr;
		}
		testGridEncoder(128,16,20,80,false);
	}
	
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
}
