package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.image.ImageIcon;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZeesoftIcon extends TestObject {
	public TestZeesoftIcon(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZeesoftIcon(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test displays the Zeesoft icon and writes it to a PNG file.");
	}

	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		
		ImageIcon ico = factory.getZeesoftIcon32();
		//ico.setVisible(true);
		ico.writePngFile("C:\\Zeesoft\\Zeesoft.png");

		ico = factory.getZeesoftIcon48();
		//ico.setVisible(true);
		ico.writePngFile("C:\\Zeesoft\\Zeesoft48.png");

		ico = factory.getZeesoftIcon64();
		ico.setVisible(true);
		ico.writePngFile("C:\\Zeesoft\\Zeesoft64.png");
	}
}
