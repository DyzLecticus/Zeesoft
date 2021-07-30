package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrPanel;

public class TestSdrPanel {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new SdrPanel() != null;
		
		Sdr sdr = new Sdr(9);
		sdr.setBit(1, true);
		sdr.setBit(2, true);
		sdr.setBit(3, true);
		sdr.setBit(4, true);
		
		SdrPanel panel = new SdrPanel(sdr);
		panel.scale = 100;
		panel.border = 10;
		assert panel.getPanel() == null;
		assert panel.getByteArray(false) == null;
		panel.render();
		assert panel.getPanel() != null;
		assert panel.getByteArray(false).length == 1673;
		assert panel.getPanel().getPreferredSize().width == 340;
		assert panel.getPanel().getPreferredSize().height == 340;
	}
}
