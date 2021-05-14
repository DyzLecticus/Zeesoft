package nl.zeesoft.zdk.test.json;

public class MockArrayObject {
	public String[]		strs	= {"A", "B"};
	public int[]		ints	= {1, 2};
	public long[]		lngs	= {1L, 2L};
	public float[]		flts	= {0.1F, 0.2F};
	public double[]		dbls	= {0.1D, 0.2D};
	public boolean[]	blns	= {true, false};
	public byte[]		bts		= {1, 2};
	public short[]		srts	= {1, 2};
	
	public void change() {
		strs[0] = "Q";
		ints[0] = 3;
		lngs[0] = 3L;
		flts[0] = 0.3F;
		dbls[0] = 0.3D;
		blns[0] = false;
		bts[0] = 3;
		srts[0] = 3;
	}
}
