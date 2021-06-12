package nl.zeesoft.zdk.test.json;

import java.util.ArrayList;
import java.util.List;

public class MockListObject {
	public List<StringBuilder>	sbs		= new ArrayList<StringBuilder>();
	public List<String>			strs	= new ArrayList<String>();
	public List<Integer>		ints	= new ArrayList<Integer>();
	public List<Long>			lngs	= new ArrayList<Long>();
	public List<Float>			flts	= new ArrayList<Float>();
	public List<Double>			dbls	= new ArrayList<Double>();
	public List<Boolean>		blns	= new ArrayList<Boolean>();
	public List<Byte>			bts		= new ArrayList<Byte>();
	public List<Short>			srts	= new ArrayList<Short>();
	
	public void change() {
		sbs.add(new StringBuilder("sb1"));
		sbs.add(new StringBuilder("sb2"));
		sbs.add(null);
		strs.add("A");
		strs.add("B");
		ints.add(1);
		ints.add(2);
		lngs.add(3L);
		lngs.add(4L);
		flts.add(1.0F);
		flts.add(2.0F);
		dbls.add(3.0D);
		dbls.add(4.0D);
		blns.add(true);
		blns.add(false);
		bts.add((new Integer(5)).byteValue());
		bts.add((new Integer(6)).byteValue());
		srts.add((new Integer(7)).shortValue());
		srts.add((new Integer(8)).shortValue());
	}
}
