package nl.zeesoft.zmmt.gui;

import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import nl.zeesoft.zmmt.syntesizer.Drum;
import nl.zeesoft.zmmt.syntesizer.Instrument;

public class PanelInstruments extends PanelObject implements ItemListener {
	private JComboBox<String>		instrument					= null;
	private JPanel					cardPanel					= null;
	private JFormattedTextField[]	instrumentMidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseVelocity		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentAccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	drumNoteNum					= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumBaseVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	drumAccentVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	
	@Override
	public void initialize(Controller controller,KeyListener keyListener) {
		getPanel().addKeyListener(keyListener);
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;

		instrument = controller.getNewInstrumentSelector();
		instrument.addItemListener(this);
		
		addComponent(getPanel(),row,0.01,instrument,false);
		
		row++;
		cardPanel = getCardPanel();
		addComponent(getPanel(),row,0.01,cardPanel);
		
		row++;
		addFiller(getPanel(),row);
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
	    cl.show(cardPanel,(String) evt.getItem());
	}

	protected JPanel getCardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new CardLayout());
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			panel.add(getInstrumentPanel(i),Instrument.INSTRUMENTS[i]);
		}
		return panel;
	}
	
	protected JPanel getInstrumentPanel(int instrumentNum) {
		String name = Instrument.INSTRUMENTS[instrumentNum];

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		//panel.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		int row = 0;

		instrumentMidiNum[instrumentNum] = getNewNumberTextField(3);
		addLabel(panel,row,"MIDI instrument number");
		addProperty(panel,row,instrumentMidiNum[instrumentNum]);

		if (!name.equals(Instrument.DRUMS)) {
			row++;
			instrumentBaseOctave[instrumentNum] = getNewNumberTextField(1);
			addLabel(panel,row,"Base octave");
			addProperty(panel,row,instrumentBaseOctave[instrumentNum]);

			row++;
			instrumentBaseVelocity[instrumentNum] = getNewNumberTextField(3);
			addLabel(panel,row,"Base velocity");
			addProperty(panel,row,instrumentBaseVelocity[instrumentNum]);
	
			row++;
			instrumentAccentVelocity[instrumentNum] = getNewNumberTextField(3);
			addLabel(panel,row,"Accent velocity");
			addProperty(panel,row,instrumentAccentVelocity[instrumentNum]);
		}

		if (name.equals(Instrument.DRUMS)) {
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				int drow = 0;
				JPanel drumPanel = new JPanel();
				drumPanel.setLayout(new GridBagLayout());
				drumPanel.setBorder(BorderFactory.createTitledBorder(Drum.DRUMS[d]));

				drumNoteNum[d] = getNewNumberTextField(3);
				addLabel(drumPanel,drow,"Midi note number");
				addProperty(drumPanel,drow,drumNoteNum[d]);
				
				drow++;
				drumBaseVelocity[d] = getNewNumberTextField(3);
				addLabel(drumPanel,drow,"Base velocity");
				addProperty(drumPanel,drow,drumBaseVelocity[d]);
		
				drow++;
				drumAccentVelocity[d] = getNewNumberTextField(3);
				addLabel(drumPanel,drow,"Accent velocity");
				addProperty(drumPanel,drow,drumAccentVelocity[d]);
				
				row++;
				addComponent(panel,row,0.01,drumPanel);
			}
		}
		
		row++;
		addFiller(panel,row);
		
		return panel;
	}
}
