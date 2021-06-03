package nl.zeesoft.zdk.test.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsonTransient;

public class MockObject {
	public String				st		= "string";
	public StringBuilder		sb		= new StringBuilder("string builder");
	public Integer				in		= 1;
	public Long					ln		= 1L;
	public Float				fl		= 0.1F;
	public float				pf		= 0.2F;
	public Double				db		= 0.1D;
	public Boolean				bl		= false;
	public Byte					bt		= 1;
	public Short				sh		= 1;
	public MockObject			ob		= null;
	public List<MockObject>		ls		= new ArrayList<MockObject>();
	
	@JsonTransient
	public Integer 				trans	= 123;
	public MockTransientObject	mto		= new MockTransientObject();
	
	public void change() {
		change(2);
		ob = new MockObject();
		ob.change(3);
		MockObject mo = new MockObject();
		mo.change(4);
		ls.add(mo);
		mo = new MockObject();
		mo.change(5);
		ls.add(mo);
	}
	
	public void change(int num) {
		st = "changed " + num;
		sb.append(" appended " + num);
		in = num;
		ln = (long)num;
		fl = (float)num;
		pf = (float)(num + 1);
		db = (double)num;
		bl = num % 2 == 0;
		bt = (byte)num;
		sh = (short)num;
	}
}
