package nl.zeesoft.zeetracker.test;

import java.awt.Color;

import javax.swing.JFrame;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zeetracker.gui.panel.MixerStrip;

public class TestMixerStrip extends TestObject {
	public TestMixerStrip(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMixerStrip(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test displays a *MixerStrip*.");
	}

	@Override
	protected void test(String[] args) {
		JFrame f = new JFrame();
		f.setSize(300,300);
		MixerStrip strip = new MixerStrip(Color.WHITE);
		f.setContentPane(strip);
		
		f.setVisible(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strip.setValue(15);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strip.setValue(30);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strip.setValue(100);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strip.setValue(127);
		
	}
}
