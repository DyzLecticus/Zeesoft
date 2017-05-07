package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.CompositionUpdater;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;

public class PanelPatterns extends PanelObject implements ItemListener, ListSelectionListener, CompositionUpdater {
	private int						selectedPattern					= 0;
	
	private JComboBox<String>		instrument						= null;

	private JList<String>			patternSelect					= null;
	private JTable					grid							= null;
	
	private Pattern					pattern							= null;
	
	public PanelPatterns(Controller controller) {
		super(controller);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;

		instrument = getController().getNewInstrumentSelector();
		instrument.addItemListener(this);
		addComponent(getPanel(),row,0.01,instrument,false);
		
		row++;
		addComponent(getPanel(),row,0.99,getPatternPanel(),true);		
	}

	@Override
	public void requestFocus() {
		patternSelect.requestFocus();
	}

	@Override
	public void handleValidChange() {
		getController().changedComposition(FrameMain.PATTERNS);
	}

	@Override
	public void updatedComposition(String tab,Composition comp) {
		setValidate(false);
		// TODO: Implement
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		// TODO: Implement
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==instrument) {
			// TODO: Implement
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		if (evt.getSource()==patternSelect) {
			if (setSelectedPattern(patternSelect.getSelectedIndex())) {
				getController().selectPattern(selectedPattern,evt.getSource());
			}
		}
	}

	public int getSelectedPattern() {
		return selectedPattern;
	}
	
	public void setSelectedPattern(Pattern pattern) {
		if (pattern!=null) {
			setSelectedPattern(pattern.getNumber());
			this.pattern = pattern;
			// TODO: Save and display pattern
			if (this.pattern!=null) {
				
			}
		}
	}

	protected boolean setSelectedPattern(int selectedPattern) {
		boolean changed = false;
		if (this.selectedPattern!=selectedPattern) {
			this.selectedPattern = selectedPattern;
			changed = true;
			if (patternSelect.getSelectedIndex()!=selectedPattern) {
				patternSelect.setSelectedIndex(selectedPattern);
			}
		}
		return changed;
	}
	
	protected JPanel getPatternPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BorderLayout());

		String[] patternNumbers = new String[100];
		for (int p = 0; p <= 99; p++) {
			String pat = "" + p;
			if (p<10) {
				pat = "0" + pat;
			}
			patternNumbers[p] = pat;
		}
		patternSelect = new JList<String>();
		patternSelect.setListData(patternNumbers);
		patternSelect.setSelectedIndex(selectedPattern);
		patternSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		patternSelect.setLayoutOrientation(JList.VERTICAL);
		patternSelect.addListSelectionListener(this);
		patternSelect.setFocusable(true);
		for (int l = 0; l < patternSelect.getKeyListeners().length; l++) {
			patternSelect.removeKeyListener(patternSelect.getKeyListeners()[l]);
		}
		patternSelect.addKeyListener(getController().getPlayerKeyListener());
		
		JScrollPane scroller = new JScrollPane(patternSelect);
		scroller.getVerticalScrollBar().setUnitIncrement(20);
		r.add(scroller,BorderLayout.LINE_START);
		
		grid = new JTable();
		grid.addKeyListener(getController().getPlayerKeyListener());
		scroller = new JScrollPane(grid);
		scroller.getVerticalScrollBar().setUnitIncrement(20);
		r.add(scroller,BorderLayout.CENTER);
		
		return r;
	}
}
